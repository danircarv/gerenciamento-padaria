package com.padaria.service;

import com.padaria.dao.FornecedorDao;
import com.padaria.model.Fornecedor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FornecedorService {

    private final FornecedorDao fornecedorDao;

    public FornecedorService(FornecedorDao fornecedorDao) {
        this.fornecedorDao = fornecedorDao;
    }

    public List<Fornecedor> listarTodos() {
        return fornecedorDao.findAll();
    }

    public List<Fornecedor> listarAtivos() {
        return fornecedorDao.findAllAtivos();
    }

    public Fornecedor buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Fornecedor fornecedor = fornecedorDao.findById(id);
        if (fornecedor == null) {
            throw new IllegalArgumentException("Fornecedor não encontrado com ID: " + id);
        }
        return fornecedor;
    }

    public Fornecedor buscarPorCnpj(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ não pode ser vazio");
        }
        return fornecedorDao.findByCnpj(cnpj);
    }

    public Fornecedor salvar(Fornecedor fornecedor) {
        validarFornecedor(fornecedor);
        
        // Check if CNPJ already exists
        if (fornecedor.getCnpj() != null && !fornecedor.getCnpj().trim().isEmpty()) {
            Fornecedor fornecedorExistente = fornecedorDao.findByCnpj(fornecedor.getCnpj());
            if (fornecedorExistente != null) {
                throw new IllegalArgumentException("CNPJ já cadastrado: " + fornecedor.getCnpj());
            }
        }
        
        Long id = fornecedorDao.save(fornecedor);
        fornecedor.setId(id);
        return fornecedor;
    }

    public Fornecedor atualizar(Fornecedor fornecedor) {
        if (fornecedor.getId() == null) {
            throw new IllegalArgumentException("ID do fornecedor não pode ser nulo para atualização");
        }
        
        validarFornecedor(fornecedor);
        
        Fornecedor fornecedorExistente = fornecedorDao.findById(fornecedor.getId());
        if (fornecedorExistente == null) {
            throw new IllegalArgumentException("Fornecedor não encontrado com ID: " + fornecedor.getId());
        }
        
        // Check if CNPJ belongs to another supplier
        if (fornecedor.getCnpj() != null && !fornecedor.getCnpj().trim().isEmpty()) {
            Fornecedor fornecedorPorCnpj = fornecedorDao.findByCnpj(fornecedor.getCnpj());
            if (fornecedorPorCnpj != null && !fornecedorPorCnpj.getId().equals(fornecedor.getId())) {
                throw new IllegalArgumentException("CNPJ já cadastrado para outro fornecedor: " + fornecedor.getCnpj());
            }
        }
        
        fornecedorDao.update(fornecedor);
        return fornecedor;
    }

    public void excluir(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        
        Fornecedor fornecedor = fornecedorDao.findById(id);
        if (fornecedor == null) {
            throw new IllegalArgumentException("Fornecedor não encontrado com ID: " + id);
        }
        
        fornecedorDao.delete(id);
    }

    public void inativar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        
        Fornecedor fornecedor = fornecedorDao.findById(id);
        if (fornecedor == null) {
            throw new IllegalArgumentException("Fornecedor não encontrado com ID: " + id);
        }
        
        fornecedorDao.inativar(id);
    }

    private void validarFornecedor(Fornecedor fornecedor) {
        if (fornecedor == null) {
            throw new IllegalArgumentException("Fornecedor não pode ser nulo");
        }
        
        if (fornecedor.getNome() == null || fornecedor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do fornecedor é obrigatório");
        }
    }
}
