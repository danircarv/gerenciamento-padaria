package com.padaria.gestao.service;

import com.padaria.gestao.dao.FornecedorDao;
import com.padaria.gestao.model.Fornecedor;
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
        return fornecedorDao.findAtivos();
    }
    
    public Fornecedor buscarPorId(Long id) {
        return fornecedorDao.findById(id);
    }
    
    public Fornecedor buscarPorCnpj(String cnpj) {
        return fornecedorDao.findByCnpj(cnpj);
    }
    
    public Long salvar(Fornecedor fornecedor) {
        validarFornecedor(fornecedor);
        
        // Verificar se CNPJ já existe
        if (fornecedor.getCnpj() != null && !fornecedor.getCnpj().trim().isEmpty()) {
            Fornecedor existente = fornecedorDao.findByCnpj(fornecedor.getCnpj());
            if (existente != null) {
                throw new IllegalArgumentException("Já existe um fornecedor cadastrado com este CNPJ");
            }
        }
        
        return fornecedorDao.save(fornecedor);
    }
    
    public void atualizar(Fornecedor fornecedor) {
        validarFornecedor(fornecedor);
        Fornecedor existente = fornecedorDao.findById(fornecedor.getId());
        if (existente == null) {
            throw new IllegalArgumentException("Fornecedor não encontrado com ID: " + fornecedor.getId());
        }
        
        // Verificar se CNPJ já existe para outro fornecedor
        if (fornecedor.getCnpj() != null && !fornecedor.getCnpj().trim().isEmpty()) {
            Fornecedor fornecedorComMesmoCnpj = fornecedorDao.findByCnpj(fornecedor.getCnpj());
            if (fornecedorComMesmoCnpj != null && !fornecedorComMesmoCnpj.getId().equals(fornecedor.getId())) {
                throw new IllegalArgumentException("Já existe outro fornecedor cadastrado com este CNPJ");
            }
        }
        
        fornecedorDao.update(fornecedor);
    }
    
    public void excluir(Long id) {
        Fornecedor fornecedor = fornecedorDao.findById(id);
        if (fornecedor == null) {
            throw new IllegalArgumentException("Fornecedor não encontrado com ID: " + id);
        }
        fornecedorDao.delete(id);
    }
    
    public void inativar(Long id) {
        Fornecedor fornecedor = fornecedorDao.findById(id);
        if (fornecedor == null) {
            throw new IllegalArgumentException("Fornecedor não encontrado com ID: " + id);
        }
        fornecedorDao.inativar(id);
    }
    
    private void validarFornecedor(Fornecedor fornecedor) {
        if (fornecedor.getNome() == null || fornecedor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do fornecedor é obrigatório");
        }
        
        if (fornecedor.getCnpj() != null && !fornecedor.getCnpj().trim().isEmpty()) {
            String cnpjLimpo = fornecedor.getCnpj().replaceAll("[^0-9]", "");
            if (cnpjLimpo.length() != 14) {
                throw new IllegalArgumentException("CNPJ inválido");
            }
        }
    }
}
