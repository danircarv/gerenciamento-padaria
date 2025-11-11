package com.padaria.gestao.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Produto {
    
    private Long id;
    private String nome;
    private String descricao;
    private String categoria;
    private BigDecimal precoVenda;
    private BigDecimal precoCusto;
    private String unidadeMedida;
    private Boolean ativo;
    private LocalDateTime dataCadastro;
    
    public Produto() {
        this.ativo = true;
        this.unidadeMedida = "UN";
    }
    
    public Produto(String nome, String descricao, String categoria, BigDecimal precoVenda, BigDecimal precoCusto) {
        this();
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.precoVenda = precoVenda;
        this.precoCusto = precoCusto;
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }
    
    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }
    
    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }
    
    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }
    
    public String getUnidadeMedida() {
        return unidadeMedida;
    }
    
    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
    
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
