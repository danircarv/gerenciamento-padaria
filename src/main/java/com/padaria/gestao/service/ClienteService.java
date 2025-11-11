package com.padaria.gestao.service;

import com.padaria.gestao.dao.ClienteDao;
import com.padaria.gestao.model.Cliente;
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
    
    public List<Cliente> listarAtivos() {
        return clienteDao.findAtivos();
    }
    
    public Cliente buscarPorId(Long id) {
        return clienteDao.findById(id);
    }
    
    public Cliente buscarPorCpf(String cpf) {
        return clienteDao.findByCpf(cpf);
    }
    
    public Long salvar(Cliente cliente) {
        validarCliente(cliente);
        
        // Verificar se CPF já existe
        if (cliente.getCpf() != null && !cliente.getCpf().trim().isEmpty()) {
            Cliente existente = clienteDao.findByCpf(cliente.getCpf());
            if (existente != null) {
                throw new IllegalArgumentException("Já existe um cliente cadastrado com este CPF");
            }
        }
        
        return clienteDao.save(cliente);
    }
    
    public void atualizar(Cliente cliente) {
        validarCliente(cliente);
        Cliente existente = clienteDao.findById(cliente.getId());
        if (existente == null) {
            throw new IllegalArgumentException("Cliente não encontrado com ID: " + cliente.getId());
        }
        
        // Verificar se CPF já existe para outro cliente
        if (cliente.getCpf() != null && !cliente.getCpf().trim().isEmpty()) {
            Cliente clienteComMesmoCpf = clienteDao.findByCpf(cliente.getCpf());
            if (clienteComMesmoCpf != null && !clienteComMesmoCpf.getId().equals(cliente.getId())) {
                throw new IllegalArgumentException("Já existe outro cliente cadastrado com este CPF");
            }
        }
        
        clienteDao.update(cliente);
    }
    
    public void excluir(Long id) {
        Cliente cliente = clienteDao.findById(id);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado com ID: " + id);
        }
        clienteDao.delete(id);
    }
    
    public void inativar(Long id) {
        Cliente cliente = clienteDao.findById(id);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado com ID: " + id);
        }
        clienteDao.inativar(id);
    }
    
    private void validarCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        }
        
        if (cliente.getCpf() != null && !cliente.getCpf().trim().isEmpty()) {
            String cpfLimpo = cliente.getCpf().replaceAll("[^0-9]", "");
            if (cpfLimpo.length() != 11) {
                throw new IllegalArgumentException("CPF inválido");
            }
        }
    }
}
