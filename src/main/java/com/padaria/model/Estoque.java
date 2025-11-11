package com.padaria.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Estoque {
    private Long id;
    private Long produtoId;
    private BigDecimal quantidade;
    private BigDecimal quantidadeMinima;
    private BigDecimal quantidadeMaxima;
    private LocalDateTime dataAtualizacao;
    
    // Transient fields for display
    private String nomeProduto;

    public Estoque() {
    }

    public Estoque(Long id, Long produtoId, BigDecimal quantidade, 
                   BigDecimal quantidadeMinima, BigDecimal quantidadeMaxima) {
        this.id = id;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.quantidadeMinima = quantidadeMinima;
        this.quantidadeMaxima = quantidadeMaxima;
    }

    public boolean isAbaixoDoMinimo() {
        return quantidade.compareTo(quantidadeMinima) < 0;
    }

    public boolean isAcimaDoMaximo() {
        if (quantidadeMaxima == null) return false;
        return quantidade.compareTo(quantidadeMaxima) > 0;
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

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }
}
