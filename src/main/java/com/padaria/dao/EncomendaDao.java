package com.padaria.dao;

import com.padaria.model.Encomenda;
import com.padaria.model.ItemEncomenda;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class EncomendaDao {

    private final JdbcTemplate jdbcTemplate;

    public EncomendaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Encomenda> encomendaRowMapper = (rs, rowNum) -> {
        Encomenda encomenda = new Encomenda();
        encomenda.setId(rs.getLong("id"));
        encomenda.setClienteId(rs.getLong("cliente_id"));
        encomenda.setDataPedido(rs.getTimestamp("data_pedido").toLocalDateTime());
        encomenda.setDataEntrega(rs.getDate("data_entrega").toLocalDate());
        encomenda.setStatus(rs.getString("status"));
        encomenda.setValorTotal(rs.getBigDecimal("valor_total"));
        encomenda.setValorEntrada(rs.getBigDecimal("valor_entrada"));
        encomenda.setObservacoes(rs.getString("observacoes"));
        
        try {
            encomenda.setNomeCliente(rs.getString("nome_cliente"));
        } catch (Exception e) {
            // Column not available
        }
        
        return encomenda;
    };

    private final RowMapper<ItemEncomenda> itemEncomendaRowMapper = (rs, rowNum) -> {
        ItemEncomenda item = new ItemEncomenda();
        item.setId(rs.getLong("id"));
        item.setEncomendaId(rs.getLong("encomenda_id"));
        item.setProdutoId(rs.getLong("produto_id"));
        item.setQuantidade(rs.getBigDecimal("quantidade"));
        item.setPrecoUnitario(rs.getBigDecimal("preco_unitario"));
        item.setSubtotal(rs.getBigDecimal("subtotal"));
        item.setObservacoes(rs.getString("observacoes"));
        
        try {
            item.setNomeProduto(rs.getString("nome_produto"));
        } catch (Exception e) {
            // Column not available
        }
        
        return item;
    };

    public List<Encomenda> findAll() {
        String sql = "SELECT e.*, c.nome as nome_cliente " +
                     "FROM encomendas e " +
                     "INNER JOIN clientes c ON e.cliente_id = c.id " +
                     "ORDER BY e.data_entrega";
        return jdbcTemplate.query(sql, encomendaRowMapper);
    }

    public Encomenda findById(Long id) {
        String sql = "SELECT e.*, c.nome as nome_cliente " +
                     "FROM encomendas e " +
                     "INNER JOIN clientes c ON e.cliente_id = c.id " +
                     "WHERE e.id = ?";
        List<Encomenda> encomendas = jdbcTemplate.query(sql, encomendaRowMapper, id);
        return encomendas.isEmpty() ? null : encomendas.get(0);
    }

    public List<Encomenda> findByStatus(String status) {
        String sql = "SELECT e.*, c.nome as nome_cliente " +
                     "FROM encomendas e " +
                     "INNER JOIN clientes c ON e.cliente_id = c.id " +
                     "WHERE e.status = ? " +
                     "ORDER BY e.data_entrega";
        return jdbcTemplate.query(sql, encomendaRowMapper, status);
    }

    public List<Encomenda> findByDataEntrega(LocalDate data) {
        String sql = "SELECT e.*, c.nome as nome_cliente " +
                     "FROM encomendas e " +
                     "INNER JOIN clientes c ON e.cliente_id = c.id " +
                     "WHERE e.data_entrega = ? " +
                     "ORDER BY e.data_pedido";
        return jdbcTemplate.query(sql, encomendaRowMapper, Date.valueOf(data));
    }

    public Long save(Encomenda encomenda) {
        String sql = "INSERT INTO encomendas (cliente_id, data_entrega, status, valor_total, valor_entrada, observacoes) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, encomenda.getClienteId());
            ps.setDate(2, Date.valueOf(encomenda.getDataEntrega()));
            ps.setString(3, encomenda.getStatus());
            ps.setBigDecimal(4, encomenda.getValorTotal());
            ps.setBigDecimal(5, encomenda.getValorEntrada());
            ps.setString(6, encomenda.getObservacoes());
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }

    public void update(Encomenda encomenda) {
        String sql = "UPDATE encomendas SET cliente_id = ?, data_entrega = ?, status = ?, " +
                     "valor_total = ?, valor_entrada = ?, observacoes = ? WHERE id = ?";
        
        jdbcTemplate.update(sql, 
            encomenda.getClienteId(),
            Date.valueOf(encomenda.getDataEntrega()),
            encomenda.getStatus(),
            encomenda.getValorTotal(),
            encomenda.getValorEntrada(),
            encomenda.getObservacoes(),
            encomenda.getId()
        );
    }

    public void updateStatus(Long id, String status) {
        String sql = "UPDATE encomendas SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, id);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM encomendas WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<ItemEncomenda> findItensByEncomendaId(Long encomendaId) {
        String sql = "SELECT i.*, p.nome as nome_produto " +
                     "FROM itens_encomenda i " +
                     "INNER JOIN produtos p ON i.produto_id = p.id " +
                     "WHERE i.encomenda_id = ?";
        return jdbcTemplate.query(sql, itemEncomendaRowMapper, encomendaId);
    }

    public Long saveItem(ItemEncomenda item) {
        String sql = "INSERT INTO itens_encomenda (encomenda_id, produto_id, quantidade, preco_unitario, subtotal, observacoes) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, item.getEncomendaId());
            ps.setLong(2, item.getProdutoId());
            ps.setBigDecimal(3, item.getQuantidade());
            ps.setBigDecimal(4, item.getPrecoUnitario());
            ps.setBigDecimal(5, item.getSubtotal());
            ps.setString(6, item.getObservacoes());
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }
}
