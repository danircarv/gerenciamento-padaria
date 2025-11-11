package com.padaria.gestao.dao;

import com.padaria.gestao.model.Produto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class ProdutoDao {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Produto> produtoRowMapper = (rs, rowNum) -> {
        Produto produto = new Produto();
        produto.setId(rs.getLong("id"));
        produto.setNome(rs.getString("nome"));
        produto.setDescricao(rs.getString("descricao"));
        produto.setCategoria(rs.getString("categoria"));
        produto.setPrecoVenda(rs.getBigDecimal("preco_venda"));
        produto.setPrecoCusto(rs.getBigDecimal("preco_custo"));
        produto.setUnidadeMedida(rs.getString("unidade_medida"));
        produto.setAtivo(rs.getBoolean("ativo"));
        Timestamp timestamp = rs.getTimestamp("data_cadastro");
        if (timestamp != null) {
            produto.setDataCadastro(timestamp.toLocalDateTime());
        }
        return produto;
    };
    
    public ProdutoDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<Produto> findAll() {
        String sql = "SELECT * FROM produtos ORDER BY nome";
        return jdbcTemplate.query(sql, produtoRowMapper);
    }
    
    public List<Produto> findAtivos() {
        String sql = "SELECT * FROM produtos WHERE ativo = true ORDER BY nome";
        return jdbcTemplate.query(sql, produtoRowMapper);
    }
    
    public List<Produto> findByCategoria(String categoria) {
        String sql = "SELECT * FROM produtos WHERE categoria = ? AND ativo = true ORDER BY nome";
        return jdbcTemplate.query(sql, produtoRowMapper, categoria);
    }
    
    public Produto findById(Long id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        List<Produto> produtos = jdbcTemplate.query(sql, produtoRowMapper, id);
        return produtos.isEmpty() ? null : produtos.get(0);
    }
    
    public Long save(Produto produto) {
        String sql = "INSERT INTO produtos (nome, descricao, categoria, preco_venda, preco_custo, unidade_medida, ativo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, produto.getNome());
            ps.setString(2, produto.getDescricao());
            ps.setString(3, produto.getCategoria());
            ps.setBigDecimal(4, produto.getPrecoVenda());
            ps.setBigDecimal(5, produto.getPrecoCusto());
            ps.setString(6, produto.getUnidadeMedida());
            ps.setBoolean(7, produto.getAtivo());
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }
    
    public void update(Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, descricao = ?, categoria = ?, preco_venda = ?, " +
                     "preco_custo = ?, unidade_medida = ?, ativo = ? WHERE id = ?";
        
        jdbcTemplate.update(sql,
                produto.getNome(),
                produto.getDescricao(),
                produto.getCategoria(),
                produto.getPrecoVenda(),
                produto.getPrecoCusto(),
                produto.getUnidadeMedida(),
                produto.getAtivo(),
                produto.getId());
    }
    
    public void delete(Long id) {
        String sql = "DELETE FROM produtos WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    public void inativar(Long id) {
        String sql = "UPDATE produtos SET ativo = false WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    public List<String> findAllCategorias() {
        String sql = "SELECT DISTINCT categoria FROM produtos WHERE categoria IS NOT NULL ORDER BY categoria";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}
