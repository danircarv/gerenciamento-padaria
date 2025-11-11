package com.padaria.gestao.dao;

import com.padaria.gestao.model.Estoque;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class EstoqueDao {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Estoque> estoqueRowMapper = (rs, rowNum) -> {
        Estoque estoque = new Estoque();
        estoque.setId(rs.getLong("id"));
        estoque.setProdutoId(rs.getLong("produto_id"));
        estoque.setQuantidade(rs.getBigDecimal("quantidade"));
        estoque.setQuantidadeMinima(rs.getBigDecimal("quantidade_minima"));
        estoque.setQuantidadeMaxima(rs.getBigDecimal("quantidade_maxima"));
        estoque.setLocalizacao(rs.getString("localizacao"));
        Timestamp timestamp = rs.getTimestamp("ultima_atualizacao");
        if (timestamp != null) {
            estoque.setUltimaAtualizacao(timestamp.toLocalDateTime());
        }
        // Se houver join com produtos
        try {
            estoque.setNomeProduto(rs.getString("nome_produto"));
        } catch (Exception e) {
            // Campo não existe no result set
        }
        return estoque;
    };
    
    public EstoqueDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<Estoque> findAll() {
        String sql = "SELECT e.*, p.nome as nome_produto FROM estoque e " +
                     "JOIN produtos p ON e.produto_id = p.id ORDER BY p.nome";
        return jdbcTemplate.query(sql, estoqueRowMapper);
    }
    
    public List<Estoque> findAbaixoDoMinimo() {
        String sql = "SELECT e.*, p.nome as nome_produto FROM estoque e " +
                     "JOIN produtos p ON e.produto_id = p.id " +
                     "WHERE e.quantidade < e.quantidade_minima ORDER BY p.nome";
        return jdbcTemplate.query(sql, estoqueRowMapper);
    }
    
    public Estoque findById(Long id) {
        String sql = "SELECT e.*, p.nome as nome_produto FROM estoque e " +
                     "JOIN produtos p ON e.produto_id = p.id WHERE e.id = ?";
        List<Estoque> estoques = jdbcTemplate.query(sql, estoqueRowMapper, id);
        return estoques.isEmpty() ? null : estoques.get(0);
    }
    
    public Estoque findByProdutoId(Long produtoId) {
        String sql = "SELECT e.*, p.nome as nome_produto FROM estoque e " +
                     "JOIN produtos p ON e.produto_id = p.id WHERE e.produto_id = ?";
        List<Estoque> estoques = jdbcTemplate.query(sql, estoqueRowMapper, produtoId);
        return estoques.isEmpty() ? null : estoques.get(0);
    }
    
    public Long save(Estoque estoque) {
        String sql = "INSERT INTO estoque (produto_id, quantidade, quantidade_minima, quantidade_maxima, localizacao) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, estoque.getProdutoId());
            ps.setBigDecimal(2, estoque.getQuantidade());
            ps.setBigDecimal(3, estoque.getQuantidadeMinima());
            ps.setBigDecimal(4, estoque.getQuantidadeMaxima());
            ps.setString(5, estoque.getLocalizacao());
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }
    
    public void update(Estoque estoque) {
        String sql = "UPDATE estoque SET produto_id = ?, quantidade = ?, quantidade_minima = ?, " +
                     "quantidade_maxima = ?, localizacao = ? WHERE id = ?";
        
        jdbcTemplate.update(sql,
                estoque.getProdutoId(),
                estoque.getQuantidade(),
                estoque.getQuantidadeMinima(),
                estoque.getQuantidadeMaxima(),
                estoque.getLocalizacao(),
                estoque.getId());
    }
    
    public void atualizarQuantidade(Long produtoId, BigDecimal novaQuantidade) {
        String sql = "UPDATE estoque SET quantidade = ? WHERE produto_id = ?";
        jdbcTemplate.update(sql, novaQuantidade, produtoId);
    }
    
    public void adicionarQuantidade(Long produtoId, BigDecimal quantidade) {
        String sql = "UPDATE estoque SET quantidade = quantidade + ? WHERE produto_id = ?";
        jdbcTemplate.update(sql, quantidade, produtoId);
    }
    
    public void subtrairQuantidade(Long produtoId, BigDecimal quantidade) {
        String sql = "UPDATE estoque SET quantidade = quantidade - ? WHERE produto_id = ?";
        jdbcTemplate.update(sql, quantidade, produtoId);
    }
    
    public void delete(Long id) {
        String sql = "DELETE FROM estoque WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
