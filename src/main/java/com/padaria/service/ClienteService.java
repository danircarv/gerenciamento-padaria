package com.padaria.service;

import com.padaria.dao.ClienteDao;
import com.padaria.model.Cliente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    private final ClienteDao clienteDao;

    public ClienteService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public List<Cliente> listarTodos() {
        return clienteDao.findAll();
    }

    public Cliente buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Cliente cliente = clienteDao.findById(id);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado com ID: " + id);
        }
        return cliente;
    }

    public Cliente buscarPorCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser vazio");
        }
        return clienteDao.findByCpf(cpf);
    }

    public List<Cliente> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        return clienteDao.findByNome(nome);
    }

    public Cliente salvar(Cliente cliente) {
        validarCliente(cliente);
        
        // Check if CPF already exists
        if (cliente.getCpf() != null && !cliente.getCpf().trim().isEmpty()) {
            Cliente clienteExistente = clienteDao.findByCpf(cliente.getCpf());
            if (clienteExistente != null) {
                throw new IllegalArgumentException("CPF já cadastrado: " + cliente.getCpf());
            }
        }
        
        Long id = clienteDao.save(cliente);
        cliente.setId(id);
        return cliente;
    }

    public Cliente atualizar(Cliente cliente) {
        if (cliente.getId() == null) {
            throw new IllegalArgumentException("ID do cliente não pode ser nulo para atualização");
        }
        
        validarCliente(cliente);
        
        Cliente clienteExistente = clienteDao.findById(cliente.getId());
        if (clienteExistente == null) {
            throw new IllegalArgumentException("Cliente não encontrado com ID: " + cliente.getId());
        }
        
        // Check if CPF belongs to another client
        if (cliente.getCpf() != null && !cliente.getCpf().trim().isEmpty()) {
            Cliente clientePorCpf = clienteDao.findByCpf(cliente.getCpf());
            if (clientePorCpf != null && !clientePorCpf.getId().equals(cliente.getId())) {
                throw new IllegalArgumentException("CPF já cadastrado para outro cliente: " + cliente.getCpf());
            }
        }
        
        clienteDao.update(cliente);
        return cliente;
    }

    public void excluir(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        
        Cliente cliente = clienteDao.findById(id);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado com ID: " + id);
        }
        
        clienteDao.delete(id);
    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        }
    }
}
