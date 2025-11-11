package com.padaria.gestao.dao;

import com.padaria.gestao.model.Encomenda;
import com.padaria.gestao.model.ItemEncomenda;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
public class EncomendaDao {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Encomenda> encomendaRowMapper = (rs, rowNum) -> {
        Encomenda encomenda = new Encomenda();
        encomenda.setId(rs.getLong("id"));
        encomenda.setClienteId(rs.getLong("cliente_id"));
        Timestamp timestamp = rs.getTimestamp("data_encomenda");
        if (timestamp != null) {
            encomenda.setDataEncomenda(timestamp.toLocalDateTime());
        }
        Date dataEntrega = rs.getDate("data_entrega");
        if (dataEntrega != null) {
            encomenda.setDataEntrega(dataEntrega.toLocalDate());
        }
        encomenda.setValorTotal(rs.getBigDecimal("valor_total"));
        encomenda.setValorEntrada(rs.getBigDecimal("valor_entrada"));
        encomenda.setStatus(rs.getString("status"));
        encomenda.setObservacoes(rs.getString("observacoes"));
        try {
            encomenda.setNomeCliente(rs.getString("nome_cliente"));
        } catch (Exception e) {
            // Campo não existe no result set
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
        try {
            item.setNomeProduto(rs.getString("nome_produto"));
        } catch (Exception e) {
            // Campo não existe no result set
        }
        return item;
    };
    
    public EncomendaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<Encomenda> findAll() {
        String sql = "SELECT e.*, c.nome as nome_cliente FROM encomendas e " +
                     "JOIN clientes c ON e.cliente_id = c.id ORDER BY e.data_entrega";
        return jdbcTemplate.query(sql, encomendaRowMapper);
    }
    
    public List<Encomenda> findByStatus(String status) {
        String sql = "SELECT e.*, c.nome as nome_cliente FROM encomendas e " +
                     "JOIN clientes c ON e.cliente_id = c.id " +
                     "WHERE e.status = ? ORDER BY e.data_entrega";
        return jdbcTemplate.query(sql, encomendaRowMapper, status);
    }
    
    public List<Encomenda> findByDataEntrega(LocalDate data) {
        String sql = "SELECT e.*, c.nome as nome_cliente FROM encomendas e " +
                     "JOIN clientes c ON e.cliente_id = c.id " +
                     "WHERE e.data_entrega = ? ORDER BY e.data_entrega";
        return jdbcTemplate.query(sql, encomendaRowMapper, data);
    }
    
    public Encomenda findById(Long id) {
        String sql = "SELECT e.*, c.nome as nome_cliente FROM encomendas e " +
                     "JOIN clientes c ON e.cliente_id = c.id WHERE e.id = ?";
        List<Encomenda> encomendas = jdbcTemplate.query(sql, encomendaRowMapper, id);
        return encomendas.isEmpty() ? null : encomendas.get(0);
    }
    
    public List<ItemEncomenda> findItensByEncomendaId(Long encomendaId) {
        String sql = "SELECT ie.*, p.nome as nome_produto FROM itens_encomenda ie " +
                     "JOIN produtos p ON ie.produto_id = p.id WHERE ie.encomenda_id = ?";
        return jdbcTemplate.query(sql, itemEncomendaRowMapper, encomendaId);
    }
    
    public Long save(Encomenda encomenda) {
        String sql = "INSERT INTO encomendas (cliente_id, data_entrega, valor_total, valor_entrada, status, observacoes) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, encomenda.getClienteId());
            ps.setDate(2, Date.valueOf(encomenda.getDataEntrega()));
            ps.setBigDecimal(3, encomenda.getValorTotal());
            ps.setBigDecimal(4, encomenda.getValorEntrada());
            ps.setString(5, encomenda.getStatus());
            ps.setString(6, encomenda.getObservacoes());
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }
    
    public Long saveItem(ItemEncomenda item) {
        String sql = "INSERT INTO itens_encomenda (encomenda_id, produto_id, quantidade, preco_unitario, subtotal) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, item.getEncomendaId());
            ps.setLong(2, item.getProdutoId());
            ps.setBigDecimal(3, item.getQuantidade());
            ps.setBigDecimal(4, item.getPrecoUnitario());
            ps.setBigDecimal(5, item.getSubtotal());
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }
    
    public void updateStatus(Long id, String status) {
        String sql = "UPDATE encomendas SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, id);
    }
    
    public void delete(Long id) {
        String sql = "DELETE FROM encomendas WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
