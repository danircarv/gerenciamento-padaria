package com.padaria.service;

import com.padaria.dao.ProdutoDao;
import com.padaria.model.Produto;
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
        return produtoDao.findAllAtivos();
    }

    public Produto buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Produto produto = produtoDao.findById(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado com ID: " + id);
        }
        return produto;
    }

    public List<Produto> buscarPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria não pode ser vazia");
        }
        return produtoDao.findByCategoria(categoria);
    }

    public Produto salvar(Produto produto) {
        validarProduto(produto);
        
        Long id = produtoDao.save(produto);
        produto.setId(id);
        return produto;
    }

    public Produto atualizar(Produto produto) {
        if (produto.getId() == null) {
            throw new IllegalArgumentException("ID do produto não pode ser nulo para atualização");
        }
        
        validarProduto(produto);
        
        Produto produtoExistente = produtoDao.findById(produto.getId());
        if (produtoExistente == null) {
            throw new IllegalArgumentException("Produto não encontrado com ID: " + produto.getId());
        }
        
        produtoDao.update(produto);
        return produto;
    }

    public void excluir(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        
        Produto produto = produtoDao.findById(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado com ID: " + id);
        }
        
        produtoDao.delete(id);
    }

    public void inativar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        
        Produto produto = produtoDao.findById(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado com ID: " + id);
        }
        
        produtoDao.inativar(id);
    }

    private void validarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        
        if (produto.getPreco() == null) {
            throw new IllegalArgumentException("Preço do produto é obrigatório");
        }
        
        if (produto.getPreco().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço do produto não pode ser negativo");
        }
        
        if (produto.getUnidadeMedida() == null || produto.getUnidadeMedida().trim().isEmpty()) {
            throw new IllegalArgumentException("Unidade de medida é obrigatória");
        }
    }
}
