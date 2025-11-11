package com.padaria.service;

import com.padaria.dao.EncomendaDao;
import com.padaria.model.Encomenda;
import com.padaria.model.ItemEncomenda;
import com.padaria.model.Produto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EncomendaService {

    private final EncomendaDao encomendaDao;
    private final ProdutoService produtoService;
    private final ClienteService clienteService;

    public EncomendaService(EncomendaDao encomendaDao, ProdutoService produtoService, ClienteService clienteService) {
        this.encomendaDao = encomendaDao;
        this.produtoService = produtoService;
        this.clienteService = clienteService;
    }

    public List<Encomenda> listarTodas() {
        return encomendaDao.findAll();
    }

    public Encomenda buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Encomenda encomenda = encomendaDao.findById(id);
        if (encomenda == null) {
            throw new IllegalArgumentException("Encomenda não encontrada com ID: " + id);
        }
        
        // Load items
        List<ItemEncomenda> itens = encomendaDao.findItensByEncomendaId(id);
        encomenda.setItens(itens);
        
        return encomenda;
    }

    public List<Encomenda> buscarPorStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status não pode ser vazio");
        }
        return encomendaDao.findByStatus(status);
    }

    public List<Encomenda> buscarPorDataEntrega(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Data de entrega não pode ser nula");
        }
        return encomendaDao.findByDataEntrega(data);
    }

    public Encomenda salvar(Encomenda encomenda) {
        validarEncomenda(encomenda);
        
        // Validate client exists
        clienteService.buscarPorId(encomenda.getClienteId());
        
        // Calculate total
        BigDecimal total = BigDecimal.ZERO;
        for (ItemEncomenda item : encomenda.getItens()) {
            Produto produto = produtoService.buscarPorId(item.getProdutoId());
            
            // Set unit price from product if not set
            if (item.getPrecoUnitario() == null) {
                item.setPrecoUnitario(produto.getPreco());
            }
            
            // Calculate subtotal
            BigDecimal subtotal = item.getQuantidade().multiply(item.getPrecoUnitario());
            item.setSubtotal(subtotal);
            
            total = total.add(subtotal);
        }
        
        encomenda.setValorTotal(total);
        
        // Validate down payment
        if (encomenda.getValorEntrada().compareTo(encomenda.getValorTotal()) > 0) {
            throw new IllegalArgumentException("Valor de entrada não pode ser maior que o valor total");
        }
        
        // Save order
        Long id = encomendaDao.save(encomenda);
        encomenda.setId(id);
        
        // Save items
        for (ItemEncomenda item : encomenda.getItens()) {
            item.setEncomendaId(id);
            encomendaDao.saveItem(item);
        }
        
        return encomenda;
    }

    public Encomenda atualizar(Encomenda encomenda) {
        if (encomenda.getId() == null) {
            throw new IllegalArgumentException("ID da encomenda não pode ser nulo para atualização");
        }
        
        validarEncomenda(encomenda);
        
        Encomenda encomendaExistente = encomendaDao.findById(encomenda.getId());
        if (encomendaExistente == null) {
            throw new IllegalArgumentException("Encomenda não encontrada com ID: " + encomenda.getId());
        }
        
        // Validate client exists
        clienteService.buscarPorId(encomenda.getClienteId());
        
        // Validate down payment
        if (encomenda.getValorEntrada().compareTo(encomenda.getValorTotal()) > 0) {
            throw new IllegalArgumentException("Valor de entrada não pode ser maior que o valor total");
        }
        
        encomendaDao.update(encomenda);
        return encomenda;
    }

    public void atualizarStatus(Long id, String status) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status não pode ser vazio");
        }
        
        Encomenda encomenda = encomendaDao.findById(id);
        if (encomenda == null) {
            throw new IllegalArgumentException("Encomenda não encontrada com ID: " + id);
        }
        
        encomendaDao.updateStatus(id, status);
    }

    public void excluir(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        
        Encomenda encomenda = encomendaDao.findById(id);
        if (encomenda == null) {
            throw new IllegalArgumentException("Encomenda não encontrada com ID: " + id);
        }
        
        encomendaDao.delete(id);
    }

    private void validarEncomenda(Encomenda encomenda) {
        if (encomenda == null) {
            throw new IllegalArgumentException("Encomenda não pode ser nula");
        }
        
        if (encomenda.getClienteId() == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }
        
        if (encomenda.getDataEntrega() == null) {
            throw new IllegalArgumentException("Data de entrega é obrigatória");
        }
        
        if (encomenda.getDataEntrega().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de entrega não pode ser anterior à data atual");
        }
        
        if (encomenda.getItens() == null || encomenda.getItens().isEmpty()) {
            throw new IllegalArgumentException("Encomenda deve conter pelo menos um item");
        }
        
        for (ItemEncomenda item : encomenda.getItens()) {
            if (item.getProdutoId() == null) {
                throw new IllegalArgumentException("ID do produto é obrigatório para cada item");
            }
            
            if (item.getQuantidade() == null || item.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser maior que zero");
            }
        }
    }
}
