package com.padaria.gestao.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venda {
    
    private Long id;
    private Long clienteId;
    private LocalDateTime dataVenda;
    private BigDecimal valorTotal;
    private BigDecimal desconto;
    private BigDecimal valorFinal;
    private String formaPagamento;
    private String status;
    private String observacoes;
    
    // Para facilitar exibição
    private String nomeCliente;
    private List<ItemVenda> itens;
    
    public Venda() {
        this.desconto = BigDecimal.ZERO;
        this.status = "CONCLUIDA";
        this.itens = new ArrayList<>();
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    
    public LocalDateTime getDataVenda() {
        return dataVenda;
    }
    
    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }
    
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    public BigDecimal getDesconto() {
        return desconto;
    }
    
    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }
    
    public BigDecimal getValorFinal() {
        return valorFinal;
    }
    
    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }
    
    public String getFormaPagamento() {
        return formaPagamento;
    }
    
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    public String getNomeCliente() {
        return nomeCliente;
    }
    
    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
    
    public List<ItemVenda> getItens() {
        return itens;
    }
    
    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }
}
