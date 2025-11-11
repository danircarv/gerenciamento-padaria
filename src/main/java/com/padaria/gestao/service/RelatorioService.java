package com.padaria.gestao.service;

import com.padaria.gestao.dao.VendaDao;
import com.padaria.gestao.model.Venda;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {
    
    private final VendaDao vendaDao;
    private final JdbcTemplate jdbcTemplate;
    
    public RelatorioService(VendaDao vendaDao, JdbcTemplate jdbcTemplate) {
        this.vendaDao = vendaDao;
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public Map<String, Object> gerarRelatorioVendasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        List<Venda> vendas = vendaDao.findByPeriodo(inicio, fim);
        
        BigDecimal totalVendas = BigDecimal.ZERO;
        BigDecimal totalDescontos = BigDecimal.ZERO;
        int quantidadeVendas = vendas.size();
        
        for (Venda venda : vendas) {
            totalVendas = totalVendas.add(venda.getValorFinal());
            totalDescontos = totalDescontos.add(venda.getDesconto());
        }
        
        BigDecimal ticketMedio = quantidadeVendas > 0 ? 
                totalVendas.divide(new BigDecimal(quantidadeVendas), 2, BigDecimal.ROUND_HALF_UP) : 
                BigDecimal.ZERO;
        
        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("inicio", inicio);
        relatorio.put("fim", fim);
        relatorio.put("vendas", vendas);
        relatorio.put("quantidadeVendas", quantidadeVendas);
        relatorio.put("totalVendas", totalVendas);
        relatorio.put("totalDescontos", totalDescontos);
        relatorio.put("ticketMedio", ticketMedio);
        
        return relatorio;
    }
    
    public List<Map<String, Object>> gerarRelatorioProdutosMaisVendidos(LocalDateTime inicio, LocalDateTime fim) {
        String sql = "SELECT p.id, p.nome, SUM(iv.quantidade) as quantidade_vendida, " +
                     "SUM(iv.subtotal) as total_vendido " +
                     "FROM itens_venda iv " +
                     "JOIN produtos p ON iv.produto_id = p.id " +
                     "JOIN vendas v ON iv.venda_id = v.id " +
                     "WHERE v.data_venda BETWEEN ? AND ? " +
                     "GROUP BY p.id, p.nome " +
                     "ORDER BY quantidade_vendida DESC " +
                     "LIMIT 10";
        
        return jdbcTemplate.queryForList(sql, inicio, fim);
    }
    
    public List<Map<String, Object>> gerarRelatorioVendasPorFormaPagamento(LocalDateTime inicio, LocalDateTime fim) {
        String sql = "SELECT forma_pagamento, COUNT(*) as quantidade, SUM(valor_final) as total " +
                     "FROM vendas " +
                     "WHERE data_venda BETWEEN ? AND ? " +
                     "GROUP BY forma_pagamento " +
                     "ORDER BY total DESC";
        
        return jdbcTemplate.queryForList(sql, inicio, fim);
    }
    
    public List<Map<String, Object>> gerarRelatorioEstoqueBaixo() {
        String sql = "SELECT p.id, p.nome, p.categoria, e.quantidade, e.quantidade_minima, e.localizacao " +
                     "FROM estoque e " +
                     "JOIN produtos p ON e.produto_id = p.id " +
                     "WHERE e.quantidade < e.quantidade_minima " +
                     "ORDER BY (e.quantidade_minima - e.quantidade) DESC";
        
        return jdbcTemplate.queryForList(sql);
    }
    
    public List<Map<String, Object>> gerarRelatorioEncomendasPorStatus() {
        String sql = "SELECT status, COUNT(*) as quantidade, SUM(valor_total) as total " +
                     "FROM encomendas " +
                     "GROUP BY status " +
                     "ORDER BY quantidade DESC";
        
        return jdbcTemplate.queryForList(sql);
    }
    
    public List<Map<String, Object>> gerarRelatorioClientesAtivos() {
        String sql = "SELECT c.id, c.nome, c.telefone, COUNT(v.id) as total_compras, " +
                     "SUM(v.valor_final) as valor_total " +
                     "FROM clientes c " +
                     "LEFT JOIN vendas v ON c.id = v.cliente_id " +
                     "WHERE c.ativo = true " +
                     "GROUP BY c.id, c.nome, c.telefone " +
                     "ORDER BY total_compras DESC " +
                     "LIMIT 20";
        
        return jdbcTemplate.queryForList(sql);
    }
}
