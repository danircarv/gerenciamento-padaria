package com.padaria.dao;

import com.padaria.model.Estoque;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class EstoqueDao {

    private final JdbcTemplate jdbcTemplate;

    public EstoqueDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Estoque> estoqueRowMapper = (rs, rowNum) -> {
        Estoque estoque = new Estoque();
        estoque.setId(rs.getLong("id"));
        estoque.setProdutoId(rs.getLong("produto_id"));
        estoque.setQuantidade(rs.getBigDecimal("quantidade"));
        estoque.setQuantidadeMinima(rs.getBigDecimal("quantidade_minima"));
        estoque.setQuantidadeMaxima(rs.getBigDecimal("quantidade_maxima"));
        estoque.setDataAtualizacao(rs.getTimestamp("data_atualizacao").toLocalDateTime());
        
        // Try to get product name if available
        try {
            estoque.setNomeProduto(rs.getString("nome_produto"));
        } catch (Exception e) {
            // Column not available in this query
        }
        
        return estoque;
    };

    public List<Estoque> findAll() {
        String sql = "SELECT e.*, p.nome as nome_produto " +
                     "FROM estoque e " +
                     "INNER JOIN produtos p ON e.produto_id = p.id " +
                     "ORDER BY p.nome";
        return jdbcTemplate.query(sql, estoqueRowMapper);
    }

    public Estoque findById(Long id) {
        String sql = "SELECT e.*, p.nome as nome_produto " +
                     "FROM estoque e " +
                     "INNER JOIN produtos p ON e.produto_id = p.id " +
                     "WHERE e.id = ?";
        List<Estoque> estoques = jdbcTemplate.query(sql, estoqueRowMapper, id);
        return estoques.isEmpty() ? null : estoques.get(0);
    }

    public Estoque findByProdutoId(Long produtoId) {
        String sql = "SELECT e.*, p.nome as nome_produto " +
                     "FROM estoque e " +
                     "INNER JOIN produtos p ON e.produto_id = p.id " +
                     "WHERE e.produto_id = ?";
        List<Estoque> estoques = jdbcTemplate.query(sql, estoqueRowMapper, produtoId);
        return estoques.isEmpty() ? null : estoques.get(0);
    }

    public List<Estoque> findAbaixoDoMinimo() {
        String sql = "SELECT e.*, p.nome as nome_produto " +
                     "FROM estoque e " +
                     "INNER JOIN produtos p ON e.produto_id = p.id " +
                     "WHERE e.quantidade < e.quantidade_minima " +
                     "ORDER BY p.nome";
        return jdbcTemplate.query(sql, estoqueRowMapper);
    }

    public Long save(Estoque estoque) {
        String sql = "INSERT INTO estoque (produto_id, quantidade, quantidade_minima, quantidade_maxima) " +
                     "VALUES (?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, estoque.getProdutoId());
            ps.setBigDecimal(2, estoque.getQuantidade());
            ps.setBigDecimal(3, estoque.getQuantidadeMinima());
            ps.setBigDecimal(4, estoque.getQuantidadeMaxima());
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }

    public void update(Estoque estoque) {
        String sql = "UPDATE estoque SET quantidade = ?, quantidade_minima = ?, " +
                     "quantidade_maxima = ? WHERE id = ?";
        
        jdbcTemplate.update(sql, 
            estoque.getQuantidade(),
            estoque.getQuantidadeMinima(),
            estoque.getQuantidadeMaxima(),
            estoque.getId()
        );
    }

    public void updateQuantidade(Long produtoId, BigDecimal quantidade) {
        String sql = "UPDATE estoque SET quantidade = quantidade + ? WHERE produto_id = ?";
        jdbcTemplate.update(sql, quantidade, produtoId);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM estoque WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
