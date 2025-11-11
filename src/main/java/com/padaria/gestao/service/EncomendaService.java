package com.padaria.gestao.service;

import com.padaria.gestao.dao.EncomendaDao;
import com.padaria.gestao.model.Encomenda;
import com.padaria.gestao.model.ItemEncomenda;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EncomendaService {
    
    private final EncomendaDao encomendaDao;
    
    public EncomendaService(EncomendaDao encomendaDao) {
        this.encomendaDao = encomendaDao;
    }
    
    public List<Encomenda> listarTodas() {
        return encomendaDao.findAll();
    }
    
    public List<Encomenda> listarPorStatus(String status) {
        return encomendaDao.findByStatus(status);
    }
    
    public List<Encomenda> listarPorDataEntrega(LocalDate data) {
        return encomendaDao.findByDataEntrega(data);
    }
    
    public Encomenda buscarPorId(Long id) {
        Encomenda encomenda = encomendaDao.findById(id);
        if (encomenda != null) {
            encomenda.setItens(encomendaDao.findItensByEncomendaId(id));
        }
        return encomenda;
    }
    
    public Long criarEncomenda(Encomenda encomenda, List<ItemEncomenda> itens) {
        validarEncomenda(encomenda, itens);
        
        // Calcular valor total
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ItemEncomenda item : itens) {
            BigDecimal subtotal = item.getQuantidade().multiply(item.getPrecoUnitario());
            item.setSubtotal(subtotal);
            valorTotal = valorTotal.add(subtotal);
        }
        
        encomenda.setValorTotal(valorTotal);
        
        // Validar valor de entrada
        if (encomenda.getValorEntrada() == null) {
            encomenda.setValorEntrada(BigDecimal.ZERO);
        }
        
        if (encomenda.getValorEntrada().compareTo(encomenda.getValorTotal()) > 0) {
            throw new IllegalArgumentException("Valor de entrada não pode ser maior que o valor total");
        }
        
        // Salvar encomenda
        Long encomendaId = encomendaDao.save(encomenda);
        
        // Salvar itens
        for (ItemEncomenda item : itens) {
            item.setEncomendaId(encomendaId);
            encomendaDao.saveItem(item);
        }
        
        return encomendaId;
    }
    
    public void atualizarStatus(Long id, String novoStatus) {
        Encomenda encomenda = encomendaDao.findById(id);
        if (encomenda == null) {
            throw new IllegalArgumentException("Encomenda não encontrada com ID: " + id);
        }
        
        List<String> statusValidos = List.of("PENDENTE", "CONFIRMADA", "EM_PRODUCAO", "PRONTA", "ENTREGUE", "CANCELADA");
        if (!statusValidos.contains(novoStatus)) {
            throw new IllegalArgumentException("Status inválido: " + novoStatus);
        }
        
        encomendaDao.updateStatus(id, novoStatus);
    }
    
    public void excluir(Long id) {
        Encomenda encomenda = encomendaDao.findById(id);
        if (encomenda == null) {
            throw new IllegalArgumentException("Encomenda não encontrada com ID: " + id);
        }
        
        if ("ENTREGUE".equals(encomenda.getStatus())) {
            throw new IllegalArgumentException("Não é possível excluir encomendas já entregues");
        }
        
        encomendaDao.delete(id);
    }
    
    private void validarEncomenda(Encomenda encomenda, List<ItemEncomenda> itens) {
        if (encomenda.getClienteId() == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }
        
        if (encomenda.getDataEntrega() == null) {
            throw new IllegalArgumentException("Data de entrega é obrigatória");
        }
        
        if (encomenda.getDataEntrega().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de entrega não pode ser anterior à data atual");
        }
        
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("A encomenda deve ter pelo menos um item");
        }
        
        for (ItemEncomenda item : itens) {
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
