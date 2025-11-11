package com.padaria.dao;

import com.padaria.model.Fornecedor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class FornecedorDao {

    private final JdbcTemplate jdbcTemplate;

    public FornecedorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Fornecedor> fornecedorRowMapper = (rs, rowNum) -> {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(rs.getLong("id"));
        fornecedor.setNome(rs.getString("nome"));
        fornecedor.setCnpj(rs.getString("cnpj"));
        fornecedor.setTelefone(rs.getString("telefone"));
        fornecedor.setEmail(rs.getString("email"));
        fornecedor.setEndereco(rs.getString("endereco"));
        fornecedor.setContato(rs.getString("contato"));
        fornecedor.setAtivo(rs.getBoolean("ativo"));
        fornecedor.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        fornecedor.setDataAtualizacao(rs.getTimestamp("data_atualizacao").toLocalDateTime());
        return fornecedor;
    };

    public List<Fornecedor> findAll() {
        String sql = "SELECT * FROM fornecedores ORDER BY nome";
        return jdbcTemplate.query(sql, fornecedorRowMapper);
    }

    public List<Fornecedor> findAllAtivos() {
        String sql = "SELECT * FROM fornecedores WHERE ativo = true ORDER BY nome";
        return jdbcTemplate.query(sql, fornecedorRowMapper);
    }

    public Fornecedor findById(Long id) {
        String sql = "SELECT * FROM fornecedores WHERE id = ?";
        List<Fornecedor> fornecedores = jdbcTemplate.query(sql, fornecedorRowMapper, id);
        return fornecedores.isEmpty() ? null : fornecedores.get(0);
    }

    public Fornecedor findByCnpj(String cnpj) {
        String sql = "SELECT * FROM fornecedores WHERE cnpj = ?";
        List<Fornecedor> fornecedores = jdbcTemplate.query(sql, fornecedorRowMapper, cnpj);
        return fornecedores.isEmpty() ? null : fornecedores.get(0);
    }

    public Long save(Fornecedor fornecedor) {
        String sql = "INSERT INTO fornecedores (nome, cnpj, telefone, email, endereco, contato, ativo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, fornecedor.getNome());
            ps.setString(2, fornecedor.getCnpj());
            ps.setString(3, fornecedor.getTelefone());
            ps.setString(4, fornecedor.getEmail());
            ps.setString(5, fornecedor.getEndereco());
            ps.setString(6, fornecedor.getContato());
            ps.setBoolean(7, fornecedor.getAtivo() != null ? fornecedor.getAtivo() : true);
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }

    public void update(Fornecedor fornecedor) {
        String sql = "UPDATE fornecedores SET nome = ?, cnpj = ?, telefone = ?, " +
                     "email = ?, endereco = ?, contato = ?, ativo = ? WHERE id = ?";
        
        jdbcTemplate.update(sql, 
            fornecedor.getNome(),
            fornecedor.getCnpj(),
            fornecedor.getTelefone(),
            fornecedor.getEmail(),
            fornecedor.getEndereco(),
            fornecedor.getContato(),
            fornecedor.getAtivo(),
            fornecedor.getId()
        );
    }

    public void delete(Long id) {
        String sql = "DELETE FROM fornecedores WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void inativar(Long id) {
        String sql = "UPDATE fornecedores SET ativo = false WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
