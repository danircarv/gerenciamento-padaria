package com.padaria.gestao.service;

import com.padaria.gestao.dao.VendaDao;
import com.padaria.gestao.model.ItemVenda;
import com.padaria.gestao.model.Venda;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VendaService {
    
    private final VendaDao vendaDao;
    private final EstoqueService estoqueService;
    
    public VendaService(VendaDao vendaDao, EstoqueService estoqueService) {
        this.vendaDao = vendaDao;
        this.estoqueService = estoqueService;
    }
    
    public List<Venda> listarTodas() {
        return vendaDao.findAll();
    }
    
    public List<Venda> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return vendaDao.findByPeriodo(inicio, fim);
    }
    
    public Venda buscarPorId(Long id) {
        Venda venda = vendaDao.findById(id);
        if (venda != null) {
            venda.setItens(vendaDao.findItensByVendaId(id));
        }
        return venda;
    }
    
    public Long realizarVenda(Venda venda, List<ItemVenda> itens) {
        validarVenda(venda, itens);
        
        // Verificar disponibilidade de estoque para todos os itens
        for (ItemVenda item : itens) {
            if (!estoqueService.verificarDisponibilidade(item.getProdutoId(), item.getQuantidade())) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto ID: " + item.getProdutoId());
            }
        }
        
        // Calcular valores
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ItemVenda item : itens) {
            BigDecimal subtotal = item.getQuantidade().multiply(item.getPrecoUnitario());
            item.setSubtotal(subtotal);
            valorTotal = valorTotal.add(subtotal);
        }
        
        venda.setValorTotal(valorTotal);
        
        // Aplicar desconto
        if (venda.getDesconto() == null) {
            venda.setDesconto(BigDecimal.ZERO);
        }
        venda.setValorFinal(valorTotal.subtract(venda.getDesconto()));
        
        if (venda.getValorFinal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor final não pode ser negativo");
        }
        
        // Salvar venda
        if (venda.getDataVenda() == null) {
            venda.setDataVenda(LocalDateTime.now());
        }
        
        Long vendaId = vendaDao.save(venda);
        
        // Salvar itens e atualizar estoque
        for (ItemVenda item : itens) {
            item.setVendaId(vendaId);
            vendaDao.saveItem(item);
            
            // Subtrair do estoque
            estoqueService.subtrairQuantidade(item.getProdutoId(), item.getQuantidade());
        }
        
        return vendaId;
    }
    
    public void excluir(Long id) {
        Venda venda = vendaDao.findById(id);
        if (venda == null) {
            throw new IllegalArgumentException("Venda não encontrada com ID: " + id);
        }
        
        // Devolver itens ao estoque antes de excluir
        List<ItemVenda> itens = vendaDao.findItensByVendaId(id);
        for (ItemVenda item : itens) {
            estoqueService.adicionarQuantidade(item.getProdutoId(), item.getQuantidade());
        }
        
        vendaDao.delete(id);
    }
    
    private void validarVenda(Venda venda, List<ItemVenda> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("A venda deve ter pelo menos um item");
        }
        
        if (venda.getFormaPagamento() == null || venda.getFormaPagamento().trim().isEmpty()) {
            throw new IllegalArgumentException("Forma de pagamento é obrigatória");
        }
        
        for (ItemVenda item : itens) {
            if (item.getProdutoId() == null) {
                throw new IllegalArgumentException("Produto é obrigatório para cada item");
            }
            
            if (item.getQuantidade() == null || item.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser maior que zero");
            }
            
            if (item.getPrecoUnitario() == null || item.getPrecoUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Preço unitário deve ser maior que zero");
            }
        }
    }
}
