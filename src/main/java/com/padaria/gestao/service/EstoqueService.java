package com.padaria.gestao.service;

import com.padaria.gestao.dao.EstoqueDao;
import com.padaria.gestao.dao.ProdutoDao;
import com.padaria.gestao.model.Estoque;
import com.padaria.gestao.model.Produto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class EstoqueService {
    
    private final EstoqueDao estoqueDao;
    private final ProdutoDao produtoDao;
    
    public EstoqueService(EstoqueDao estoqueDao, ProdutoDao produtoDao) {
        this.estoqueDao = estoqueDao;
        this.produtoDao = produtoDao;
    }
    
    public List<Estoque> listarTodos() {
        return estoqueDao.findAll();
    }
    
    public List<Estoque> listarAbaixoDoMinimo() {
        return estoqueDao.findAbaixoDoMinimo();
    }
    
    public Estoque buscarPorId(Long id) {
        return estoqueDao.findById(id);
    }
    
    public Estoque buscarPorProdutoId(Long produtoId) {
        return estoqueDao.findByProdutoId(produtoId);
    }
    
    public Long salvar(Estoque estoque) {
        validarEstoque(estoque);
        
        // Verificar se já existe estoque para este produto
        Estoque existente = estoqueDao.findByProdutoId(estoque.getProdutoId());
        if (existente != null) {
            throw new IllegalArgumentException("Já existe estoque cadastrado para este produto");
        }
        
        return estoqueDao.save(estoque);
    }
    
    public void atualizar(Estoque estoque) {
        validarEstoque(estoque);
        Estoque existente = estoqueDao.findById(estoque.getId());
        if (existente == null) {
            throw new IllegalArgumentException("Estoque não encontrado com ID: " + estoque.getId());
        }
        estoqueDao.update(estoque);
    }
    
    public void atualizarQuantidade(Long produtoId, BigDecimal novaQuantidade) {
        if (novaQuantidade.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        
        Estoque estoque = estoqueDao.findByProdutoId(produtoId);
        if (estoque == null) {
            throw new IllegalArgumentException("Estoque não encontrado para o produto ID: " + produtoId);
        }
        
        estoqueDao.atualizarQuantidade(produtoId, novaQuantidade);
    }
    
    public void adicionarQuantidade(Long produtoId, BigDecimal quantidade) {
        if (quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        
        Estoque estoque = estoqueDao.findByProdutoId(produtoId);
        if (estoque == null) {
            throw new IllegalArgumentException("Estoque não encontrado para o produto ID: " + produtoId);
        }
        
        estoqueDao.adicionarQuantidade(produtoId, quantidade);
    }
    
    public void subtrairQuantidade(Long produtoId, BigDecimal quantidade) {
        if (quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        
        Estoque estoque = estoqueDao.findByProdutoId(produtoId);
        if (estoque == null) {
            throw new IllegalArgumentException("Estoque não encontrado para o produto ID: " + produtoId);
        }
        
        BigDecimal novaQuantidade = estoque.getQuantidade().subtract(quantidade);
        if (novaQuantidade.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantidade insuficiente em estoque. Disponível: " + estoque.getQuantidade());
        }
        
        estoqueDao.subtrairQuantidade(produtoId, quantidade);
    }
    
    public boolean verificarDisponibilidade(Long produtoId, BigDecimal quantidadeNecessaria) {
        Estoque estoque = estoqueDao.findByProdutoId(produtoId);
        if (estoque == null) {
            return false;
        }
        return estoque.getQuantidade().compareTo(quantidadeNecessaria) >= 0;
    }
    
    public void excluir(Long id) {
        Estoque estoque = estoqueDao.findById(id);
        if (estoque == null) {
            throw new IllegalArgumentException("Estoque não encontrado com ID: " + id);
        }
        estoqueDao.delete(id);
    }
    
    private void validarEstoque(Estoque estoque) {
        if (estoque.getProdutoId() == null) {
            throw new IllegalArgumentException("Produto é obrigatório");
        }
        
        Produto produto = produtoDao.findById(estoque.getProdutoId());
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado com ID: " + estoque.getProdutoId());
        }
        
        if (estoque.getQuantidade() == null || estoque.getQuantidade().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        
        if (estoque.getQuantidadeMinima() == null || estoque.getQuantidadeMinima().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantidade mínima não pode ser negativa");
        }
        
        if (estoque.getQuantidadeMaxima() != null && estoque.getQuantidadeMaxima().compareTo(estoque.getQuantidadeMinima()) < 0) {
            throw new IllegalArgumentException("Quantidade máxima não pode ser menor que a quantidade mínima");
        }
    }
}
