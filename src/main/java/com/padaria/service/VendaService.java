package com.padaria.service;

import com.padaria.dao.VendaDao;
import com.padaria.model.ItemVenda;
import com.padaria.model.Produto;
import com.padaria.model.Venda;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VendaService {

    private final VendaDao vendaDao;
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;

    public VendaService(VendaDao vendaDao, ProdutoService produtoService, EstoqueService estoqueService) {
        this.vendaDao = vendaDao;
        this.produtoService = produtoService;
        this.estoqueService = estoqueService;
    }

    public List<Venda> listarTodas() {
        return vendaDao.findAll();
    }

    public Venda buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Venda venda = vendaDao.findById(id);
        if (venda == null) {
            throw new IllegalArgumentException("Venda não encontrada com ID: " + id);
        }
        
        // Load items
        List<ItemVenda> itens = vendaDao.findItensByVendaId(id);
        venda.setItens(itens);
        
        return venda;
    }

    public List<Venda> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("Período inicial e final são obrigatórios");
        }
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data inicial não pode ser posterior à data final");
        }
        return vendaDao.findByPeriodo(inicio, fim);
    }

    public Venda salvar(Venda venda) {
        validarVenda(venda);
        
        // Calculate total
        BigDecimal total = BigDecimal.ZERO;
        for (ItemVenda item : venda.getItens()) {
            Produto produto = produtoService.buscarPorId(item.getProdutoId());
            
            // Set unit price from product
            item.setPrecoUnitario(produto.getPreco());
            
            // Calculate subtotal
            BigDecimal subtotal = item.getQuantidade().multiply(item.getPrecoUnitario());
            item.setSubtotal(subtotal);
            
            total = total.add(subtotal);
            
            // Validate stock availability
            estoqueService.removerQuantidade(item.getProdutoId(), item.getQuantidade());
        }
        
        venda.setValorTotal(total);
        
        // Save sale
        Long id = vendaDao.save(venda);
        venda.setId(id);
        
        // Save items
        for (ItemVenda item : venda.getItens()) {
            item.setVendaId(id);
            vendaDao.saveItem(item);
        }
        
        return venda;
    }

    public void excluir(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        
        Venda venda = vendaDao.findById(id);
        if (venda == null) {
            throw new IllegalArgumentException("Venda não encontrada com ID: " + id);
        }
        
        // Return items to stock
        List<ItemVenda> itens = vendaDao.findItensByVendaId(id);
        for (ItemVenda item : itens) {
            estoqueService.adicionarQuantidade(item.getProdutoId(), item.getQuantidade());
        }
        
        vendaDao.delete(id);
    }

    public BigDecimal getTotalVendasPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("Período inicial e final são obrigatórios");
        }
        return vendaDao.getTotalVendasPeriodo(inicio, fim);
    }

    private void validarVenda(Venda venda) {
        if (venda == null) {
            throw new IllegalArgumentException("Venda não pode ser nula");
        }
        
        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            throw new IllegalArgumentException("Venda deve conter pelo menos um item");
        }
        
        if (venda.getFormaPagamento() == null || venda.getFormaPagamento().trim().isEmpty()) {
            throw new IllegalArgumentException("Forma de pagamento é obrigatória");
        }
        
        for (ItemVenda item : venda.getItens()) {
            if (item.getProdutoId() == null) {
                throw new IllegalArgumentException("ID do produto é obrigatório para cada item");
            }
            
            if (item.getQuantidade() == null || item.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser maior que zero");
            }
        }
    }
}
