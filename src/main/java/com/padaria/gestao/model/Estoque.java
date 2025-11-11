package com.padaria.gestao.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Estoque {
    
    private Long id;
    private Long produtoId;
    private BigDecimal quantidade;
    private BigDecimal quantidadeMinima;
    private BigDecimal quantidadeMaxima;
    private String localizacao;
    private LocalDateTime ultimaAtualizacao;
    
    // Para facilitar exibição
    private String nomeProduto;
    
    public Estoque() {
        this.quantidade = BigDecimal.ZERO;
        this.quantidadeMinima = BigDecimal.TEN;
        this.quantidadeMaxima = new BigDecimal("100");
    }
    
    public Estoque(Long produtoId, BigDecimal quantidade, BigDecimal quantidadeMinima, BigDecimal quantidadeMaxima) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.quantidadeMinima = quantidadeMinima;
        this.quantidadeMaxima = quantidadeMaxima;
    }
    
    // Método auxiliar para verificar se está abaixo do estoque mínimo
    public boolean isAbaixoDoMinimo() {
        if (quantidade == null || quantidadeMinima == null) {
            return false;
        }
        return quantidade.compareTo(quantidadeMinima) < 0;
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public BigDecimal getQuantidadeMinima() {
        return quantidadeMinima;
    }
    
    public void setQuantidadeMinima(BigDecimal quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }
    
    public BigDecimal getQuantidadeMaxima() {
        return quantidadeMaxima;
    }
    
    public void setQuantidadeMaxima(BigDecimal quantidadeMaxima) {
        this.quantidadeMaxima = quantidadeMaxima;
    }
    
    public String getLocalizacao() {
        return localizacao;
    }
    
    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
    
    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }
    
    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }
    
    public String getNomeProduto() {
        return nomeProduto;
    }
    
    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }
}
