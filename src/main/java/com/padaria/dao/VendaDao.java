package com.padaria.dao;

import com.padaria.model.Venda;
import com.padaria.model.ItemVenda;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class VendaDao {

    private final JdbcTemplate jdbcTemplate;

    public VendaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Venda> vendaRowMapper = (rs, rowNum) -> {
        Venda venda = new Venda();
        venda.setId(rs.getLong("id"));
        venda.setClienteId(rs.getObject("cliente_id", Long.class));
        venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
        venda.setValorTotal(rs.getBigDecimal("valor_total"));
        venda.setFormaPagamento(rs.getString("forma_pagamento"));
        venda.setStatus(rs.getString("status"));
        venda.setObservacoes(rs.getString("observacoes"));
        
        try {
            venda.setNomeCliente(rs.getString("nome_cliente"));
        } catch (Exception e) {
            // Column not available
        }
        
        return venda;
    };

    private final RowMapper<ItemVenda> itemVendaRowMapper = (rs, rowNum) -> {
        ItemVenda item = new ItemVenda();
        item.setId(rs.getLong("id"));
        item.setVendaId(rs.getLong("venda_id"));
        item.setProdutoId(rs.getLong("produto_id"));
        item.setQuantidade(rs.getBigDecimal("quantidade"));
        item.setPrecoUnitario(rs.getBigDecimal("preco_unitario"));
        item.setSubtotal(rs.getBigDecimal("subtotal"));
        
        try {
            item.setNomeProduto(rs.getString("nome_produto"));
        } catch (Exception e) {
            // Column not available
        }
        
        return item;
    };

    public List<Venda> findAll() {
        String sql = "SELECT v.*, c.nome as nome_cliente " +
                     "FROM vendas v " +
                     "LEFT JOIN clientes c ON v.cliente_id = c.id " +
                     "ORDER BY v.data_venda DESC";
        return jdbcTemplate.query(sql, vendaRowMapper);
    }

    public Venda findById(Long id) {
        String sql = "SELECT v.*, c.nome as nome_cliente " +
                     "FROM vendas v " +
                     "LEFT JOIN clientes c ON v.cliente_id = c.id " +
                     "WHERE v.id = ?";
        List<Venda> vendas = jdbcTemplate.query(sql, vendaRowMapper, id);
        return vendas.isEmpty() ? null : vendas.get(0);
    }

    public List<Venda> findByPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        String sql = "SELECT v.*, c.nome as nome_cliente " +
                     "FROM vendas v " +
                     "LEFT JOIN clientes c ON v.cliente_id = c.id " +
                     "WHERE v.data_venda BETWEEN ? AND ? " +
                     "ORDER BY v.data_venda DESC";
        return jdbcTemplate.query(sql, vendaRowMapper, inicio, fim);
    }

    public Long save(Venda venda) {
        String sql = "INSERT INTO vendas (cliente_id, valor_total, forma_pagamento, status, observacoes) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (venda.getClienteId() != null) {
                ps.setLong(1, venda.getClienteId());
            } else {
                ps.setNull(1, java.sql.Types.BIGINT);
            }
            ps.setBigDecimal(2, venda.getValorTotal());
            ps.setString(3, venda.getFormaPagamento());
            ps.setString(4, venda.getStatus());
            ps.setString(5, venda.getObservacoes());
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }

    public void update(Venda venda) {
        String sql = "UPDATE vendas SET cliente_id = ?, valor_total = ?, forma_pagamento = ?, " +
                     "status = ?, observacoes = ? WHERE id = ?";
        
        jdbcTemplate.update(sql, 
            venda.getClienteId(),
            venda.getValorTotal(),
            venda.getFormaPagamento(),
            venda.getStatus(),
            venda.getObservacoes(),
            venda.getId()
        );
    }

    public void delete(Long id) {
        String sql = "DELETE FROM vendas WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<ItemVenda> findItensByVendaId(Long vendaId) {
        String sql = "SELECT i.*, p.nome as nome_produto " +
                     "FROM itens_venda i " +
                     "INNER JOIN produtos p ON i.produto_id = p.id " +
                     "WHERE i.venda_id = ?";
        return jdbcTemplate.query(sql, itemVendaRowMapper, vendaId);
    }

    public Long saveItem(ItemVenda item) {
        String sql = "INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario, subtotal) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, item.getVendaId());
            ps.setLong(2, item.getProdutoId());
            ps.setBigDecimal(3, item.getQuantidade());
            ps.setBigDecimal(4, item.getPrecoUnitario());
            ps.setBigDecimal(5, item.getSubtotal());
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }

    public BigDecimal getTotalVendasPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        String sql = "SELECT COALESCE(SUM(valor_total), 0) FROM vendas " +
                     "WHERE data_venda BETWEEN ? AND ? AND status = 'FINALIZADA'";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, inicio, fim);
    }
}
