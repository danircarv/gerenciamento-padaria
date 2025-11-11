package com.padaria.gestao.controller;

import com.padaria.gestao.model.Produto;
import com.padaria.gestao.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoRestController {
    
    private final ProdutoService produtoService;
    
    public ProdutoRestController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }
    
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<List<Produto>> listarAtivos() {
        return ResponseEntity.ok(produtoService.listarAtivos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(produto);
    }
    
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Produto>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(produtoService.listarPorCategoria(categoria));
    }
    
    @GetMapping("/categorias")
    public ResponseEntity<List<String>> listarCategorias() {
        return ResponseEntity.ok(produtoService.listarCategorias());
    }
    
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Produto produto) {
        try {
            Long id = produtoService.salvar(produto);
            produto.setId(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(produto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        try {
            produto.setId(id);
            produtoService.atualizar(produto);
            return ResponseEntity.ok(produto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            produtoService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            produtoService.inativar(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
