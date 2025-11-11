package com.padaria.dao;

import com.padaria.model.Produto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ProdutoDao {

    private final JdbcTemplate jdbcTemplate;

    public ProdutoDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Produto> produtoRowMapper = (rs, rowNum) -> {
        Produto produto = new Produto();
        produto.setId(rs.getLong("id"));
        produto.setNome(rs.getString("nome"));
        produto.setDescricao(rs.getString("descricao"));
        produto.setPreco(rs.getBigDecimal("preco"));
        produto.setCategoria(rs.getString("categoria"));
        produto.setUnidadeMedida(rs.getString("unidade_medida"));
        produto.setAtivo(rs.getBoolean("ativo"));
        produto.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        produto.setDataAtualizacao(rs.getTimestamp("data_atualizacao").toLocalDateTime());
        return produto;
    };

    public List<Produto> findAll() {
        String sql = "SELECT * FROM produtos ORDER BY nome";
        return jdbcTemplate.query(sql, produtoRowMapper);
    }

    public List<Produto> findAllAtivos() {
        String sql = "SELECT * FROM produtos WHERE ativo = true ORDER BY nome";
        return jdbcTemplate.query(sql, produtoRowMapper);
    }

    public Produto findById(Long id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        List<Produto> produtos = jdbcTemplate.query(sql, produtoRowMapper, id);
        return produtos.isEmpty() ? null : produtos.get(0);
    }

    public List<Produto> findByCategoria(String categoria) {
        String sql = "SELECT * FROM produtos WHERE categoria = ? AND ativo = true ORDER BY nome";
        return jdbcTemplate.query(sql, produtoRowMapper, categoria);
    }

    public Long save(Produto produto) {
        String sql = "INSERT INTO produtos (nome, descricao, preco, categoria, unidade_medida, ativo) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, produto.getNome());
            ps.setString(2, produto.getDescricao());
            ps.setBigDecimal(3, produto.getPreco());
            ps.setString(4, produto.getCategoria());
            ps.setString(5, produto.getUnidadeMedida());
            ps.setBoolean(6, produto.getAtivo() != null ? produto.getAtivo() : true);
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }

    public void update(Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, descricao = ?, preco = ?, " +
                     "categoria = ?, unidade_medida = ?, ativo = ? WHERE id = ?";
        
        jdbcTemplate.update(sql, 
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getCategoria(),
            produto.getUnidadeMedida(),
            produto.getAtivo(),
            produto.getId()
        );
    }

    public void delete(Long id) {
        String sql = "DELETE FROM produtos WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void inativar(Long id) {
        String sql = "UPDATE produtos SET ativo = false WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
