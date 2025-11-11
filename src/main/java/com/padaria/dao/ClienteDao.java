package com.padaria.dao;

import com.padaria.model.Cliente;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ClienteDao {

    private final JdbcTemplate jdbcTemplate;

    public ClienteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Cliente> clienteRowMapper = (rs, rowNum) -> {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setEmail(rs.getString("email"));
        cliente.setEndereco(rs.getString("endereco"));
        cliente.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        cliente.setDataAtualizacao(rs.getTimestamp("data_atualizacao").toLocalDateTime());
        return cliente;
    };

    public List<Cliente> findAll() {
        String sql = "SELECT * FROM clientes ORDER BY nome";
        return jdbcTemplate.query(sql, clienteRowMapper);
    }

    public Cliente findById(Long id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        List<Cliente> clientes = jdbcTemplate.query(sql, clienteRowMapper, id);
        return clientes.isEmpty() ? null : clientes.get(0);
    }

    public Cliente findByCpf(String cpf) {
        String sql = "SELECT * FROM clientes WHERE cpf = ?";
        List<Cliente> clientes = jdbcTemplate.query(sql, clienteRowMapper, cpf);
        return clientes.isEmpty() ? null : clientes.get(0);
    }

    public List<Cliente> findByNome(String nome) {
        String sql = "SELECT * FROM clientes WHERE nome LIKE ? ORDER BY nome";
        return jdbcTemplate.query(sql, clienteRowMapper, "%" + nome + "%");
    }

    public Long save(Cliente cliente) {
        String sql = "INSERT INTO clientes (nome, cpf, telefone, email, endereco) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.setString(3, cliente.getTelefone());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getEndereco());
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }

    public void update(Cliente cliente) {
        String sql = "UPDATE clientes SET nome = ?, cpf = ?, telefone = ?, " +
                     "email = ?, endereco = ? WHERE id = ?";
        
        jdbcTemplate.update(sql, 
            cliente.getNome(),
            cliente.getCpf(),
            cliente.getTelefone(),
            cliente.getEmail(),
            cliente.getEndereco(),
            cliente.getId()
        );
    }

    public void delete(Long id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
