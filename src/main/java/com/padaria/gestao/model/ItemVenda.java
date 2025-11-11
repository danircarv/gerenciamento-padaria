package com.padaria.gestao.model;

import java.math.BigDecimal;

public class ItemVenda {
    
    private Long id;
    private Long vendaId;
    private Long produtoId;
    private BigDecimal quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
    
    // Para facilitar exibição
    private String nomeProduto;
    
    public ItemVenda() {
    }
    
    public ItemVenda(Long produtoId, BigDecimal quantidade, BigDecimal precoUnitario) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = quantidade.multiply(precoUnitario);
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getVendaId() {
        return vendaId;
    }
    
    public void setVendaId(Long vendaId) {
        this.vendaId = vendaId;
    }
    
    public Long getProdutoId() {
        return produtoId;
    }
    
    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }
    
    public BigDecimal getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }
    
    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }
    
    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public String getNomeProduto() {
        return nomeProduto;
    }
    
    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }
}
