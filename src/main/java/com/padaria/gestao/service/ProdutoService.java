package com.padaria.gestao.service;

import com.padaria.gestao.dao.ProdutoDao;
import com.padaria.gestao.model.Produto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProdutoService {
    
    private final ProdutoDao produtoDao;
    
    public ProdutoService(ProdutoDao produtoDao) {
        this.produtoDao = produtoDao;
    }
    
    public List<Produto> listarTodos() {
        return produtoDao.findAll();
    }
    
    public List<Produto> listarAtivos() {
        return produtoDao.findAtivos();
    }
    
    public List<Produto> listarPorCategoria(String categoria) {
        return produtoDao.findByCategoria(categoria);
    }
    
    public Produto buscarPorId(Long id) {
        return produtoDao.findById(id);
    }
    
    public Long salvar(Produto produto) {
        validarProduto(produto);
        return produtoDao.save(produto);
    }
    
    public void atualizar(Produto produto) {
        validarProduto(produto);
        Produto existente = produtoDao.findById(produto.getId());
        if (existente == null) {
            throw new IllegalArgumentException("Produto não encontrado com ID: " + produto.getId());
        }
        produtoDao.update(produto);
    }
    
    public void excluir(Long id) {
        Produto produto = produtoDao.findById(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado com ID: " + id);
        }
        produtoDao.delete(id);
    }
    
    public void inativar(Long id) {
        Produto produto = produtoDao.findById(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado com ID: " + id);
        }
        produtoDao.inativar(id);
    }
    
    public List<String> listarCategorias() {
        return produtoDao.findAllCategorias();
    }
    
    private void validarProduto(Produto produto) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        
        if (produto.getPrecoVenda() == null || produto.getPrecoVenda().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço de venda deve ser maior que zero");
        }
        
        if (produto.getPrecoCusto() != null && produto.getPrecoCusto().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço de custo não pode ser negativo");
        }
        
        if (produto.getPrecoCusto() != null && produto.getPrecoVenda() != null 
                && produto.getPrecoCusto().compareTo(produto.getPrecoVenda()) > 0) {
            throw new IllegalArgumentException("Preço de custo não pode ser maior que o preço de venda");
        }
    }
}
