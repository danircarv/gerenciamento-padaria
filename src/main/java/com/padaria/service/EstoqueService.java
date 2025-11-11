package com.padaria.service;

import com.padaria.dao.EstoqueDao;
import com.padaria.dao.ProdutoDao;
import com.padaria.model.Estoque;
import com.padaria.model.Produto;
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

    public Estoque buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Estoque estoque = estoqueDao.findById(id);
        if (estoque == null) {
            throw new IllegalArgumentException("Estoque não encontrado com ID: " + id);
        }
        return estoque;
    }

    public Estoque buscarPorProdutoId(Long produtoId) {
        if (produtoId == null) {
            throw new IllegalArgumentException("ID do produto não pode ser nulo");
        }
        return estoqueDao.findByProdutoId(produtoId);
    }

    public List<Estoque> listarAbaixoDoMinimo() {
        return estoqueDao.findAbaixoDoMinimo();
    }

    public Estoque salvar(Estoque estoque) {
        validarEstoque(estoque);
        
        // Check if product exists
        Produto produto = produtoDao.findById(estoque.getProdutoId());
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado com ID: " + estoque.getProdutoId());
        }
        
        // Check if stock already exists for this product
        Estoque estoqueExistente = estoqueDao.findByProdutoId(estoque.getProdutoId());
        if (estoqueExistente != null) {
            throw new IllegalArgumentException("Já existe estoque para este produto");
        }
        
        Long id = estoqueDao.save(estoque);
        estoque.setId(id);
        return estoque;
    }

    public Estoque atualizar(Estoque estoque) {
        if (estoque.getId() == null) {
            throw new IllegalArgumentException("ID do estoque não pode ser nulo para atualização");
        }
        
        validarEstoque(estoque);
        
        Estoque estoqueExistente = estoqueDao.findById(estoque.getId());
        if (estoqueExistente == null) {
            throw new IllegalArgumentException("Estoque não encontrado com ID: " + estoque.getId());
        }
        
        estoqueDao.update(estoque);
        return estoque;
    }

    public void adicionarQuantidade(Long produtoId, BigDecimal quantidade) {
        if (produtoId == null) {
            throw new IllegalArgumentException("ID do produto não pode ser nulo");
        }
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        
        Estoque estoque = estoqueDao.findByProdutoId(produtoId);
        if (estoque == null) {
            throw new IllegalArgumentException("Estoque não encontrado para o produto ID: " + produtoId);
        }
        
        estoqueDao.updateQuantidade(produtoId, quantidade);
    }

    public void removerQuantidade(Long produtoId, BigDecimal quantidade) {
        if (produtoId == null) {
            throw new IllegalArgumentException("ID do produto não pode ser nulo");
        }
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        
        Estoque estoque = estoqueDao.findByProdutoId(produtoId);
        if (estoque == null) {
            throw new IllegalArgumentException("Estoque não encontrado para o produto ID: " + produtoId);
        }
        
        BigDecimal novaQuantidade = estoque.getQuantidade().subtract(quantidade);
        if (novaQuantidade.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantidade em estoque insuficiente");
        }
        
        estoqueDao.updateQuantidade(produtoId, quantidade.negate());
    }

    public void excluir(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        
        Estoque estoque = estoqueDao.findById(id);
        if (estoque == null) {
            throw new IllegalArgumentException("Estoque não encontrado com ID: " + id);
        }
        
        estoqueDao.delete(id);
    }

    private void validarEstoque(Estoque estoque) {
        if (estoque == null) {
            throw new IllegalArgumentException("Estoque não pode ser nulo");
        }
        
        if (estoque.getProdutoId() == null) {
            throw new IllegalArgumentException("ID do produto é obrigatório");
        }
        
        if (estoque.getQuantidade() == null) {
            throw new IllegalArgumentException("Quantidade é obrigatória");
        }
        
        if (estoque.getQuantidade().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        
        if (estoque.getQuantidadeMinima() == null) {
            throw new IllegalArgumentException("Quantidade mínima é obrigatória");
        }
        
        if (estoque.getQuantidadeMinima().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantidade mínima não pode ser negativa");
        }
        
        if (estoque.getQuantidadeMaxima() != null && 
            estoque.getQuantidadeMaxima().compareTo(estoque.getQuantidadeMinima()) < 0) {
            throw new IllegalArgumentException("Quantidade máxima não pode ser menor que a mínima");
        }
    }
}
