package com.padaria.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Encomenda {
    private Long id;
    private Long clienteId;
    private LocalDateTime dataPedido;
    private LocalDate dataEntrega;
    private String status;
    private BigDecimal valorTotal;
    private BigDecimal valorEntrada;
    private String observacoes;
    
    // Transient fields
    private String nomeCliente;
    private List<ItemEncomenda> itens;

    public Encomenda() {
        this.status = "PENDENTE";
        this.valorEntrada = BigDecimal.ZERO;
        this.itens = new ArrayList<>();
    }

    public Encomenda(Long id, Long clienteId, LocalDate dataEntrega, BigDecimal valorTotal) {
        this.id = id;
        this.clienteId = clienteId;
        this.dataEntrega = dataEntrega;
        this.valorTotal = valorTotal;
        this.status = "PENDENTE";
        this.valorEntrada = BigDecimal.ZERO;
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

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorEntrada() {
        return valorEntrada;
    }

    public void setValorEntrada(BigDecimal valorEntrada) {
        this.valorEntrada = valorEntrada;
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

    public List<ItemEncomenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemEncomenda> itens) {
        this.itens = itens;
    }
}
