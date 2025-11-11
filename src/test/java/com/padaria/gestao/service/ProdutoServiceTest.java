package com.padaria.gestao.service;

import com.padaria.gestao.dao.ProdutoDao;
import com.padaria.gestao.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoDao produtoDao;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setNome("Pão Francês");
        produto.setCategoria("PAES");
        produto.setPrecoVenda(new BigDecimal("0.60"));
        produto.setPrecoCusto(new BigDecimal("0.30"));
        produto.setUnidadeMedida("UN");
        produto.setAtivo(true);
    }

    @Test
    void deveSalvarProdutoComSucesso() {
        when(produtoDao.save(any(Produto.class))).thenReturn(1L);

        Long id = produtoService.salvar(produto);

        assertNotNull(id);
        assertEquals(1L, id);
        verify(produtoDao, times(1)).save(produto);
    }

    @Test
    void naoDeveSalvarProdutoSemNome() {
        produto.setNome(null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> produtoService.salvar(produto)
        );

        assertEquals("Nome do produto é obrigatório", exception.getMessage());
        verify(produtoDao, never()).save(any());
    }

    @Test
    void naoDeveSalvarProdutoComPrecoVendaZero() {
        produto.setPrecoVenda(BigDecimal.ZERO);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> produtoService.salvar(produto)
        );

        assertEquals("Preço de venda deve ser maior que zero", exception.getMessage());
        verify(produtoDao, never()).save(any());
    }

    @Test
    void naoDeveSalvarProdutoComPrecoCustoNegativo() {
        produto.setPrecoCusto(new BigDecimal("-10"));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> produtoService.salvar(produto)
        );

        assertEquals("Preço de custo não pode ser negativo", exception.getMessage());
        verify(produtoDao, never()).save(any());
    }

    @Test
    void naoDeveSalvarProdutoComPrecoCustoMaiorQuePrecoVenda() {
        produto.setPrecoVenda(new BigDecimal("0.30"));
        produto.setPrecoCusto(new BigDecimal("0.60"));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> produtoService.salvar(produto)
        );

        assertEquals("Preço de custo não pode ser maior que o preço de venda", exception.getMessage());
        verify(produtoDao, never()).save(any());
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        produto.setId(1L);
        when(produtoDao.findById(1L)).thenReturn(produto);

        assertDoesNotThrow(() -> produtoService.atualizar(produto));

        verify(produtoDao, times(1)).update(produto);
    }

    @Test
    void naoDeveAtualizarProdutoInexistente() {
        produto.setId(999L);
        when(produtoDao.findById(999L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> produtoService.atualizar(produto)
        );

        assertEquals("Produto não encontrado com ID: 999", exception.getMessage());
        verify(produtoDao, never()).update(any());
    }
}
