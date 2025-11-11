package com.padaria.gestao.controller;

import com.padaria.gestao.model.Estoque;
import com.padaria.gestao.service.EstoqueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueRestController {
    
    private final EstoqueService estoqueService;
    
    public EstoqueRestController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }
    
    @GetMapping
    public ResponseEntity<List<Estoque>> listarTodos() {
        return ResponseEntity.ok(estoqueService.listarTodos());
    }
    
    @GetMapping("/alertas")
    public ResponseEntity<List<Estoque>> listarAlertasEstoqueBaixo() {
        return ResponseEntity.ok(estoqueService.listarAbaixoDoMinimo());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Estoque> buscarPorId(@PathVariable Long id) {
        Estoque estoque = estoqueService.buscarPorId(id);
        if (estoque == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estoque);
    }
    
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<Estoque> buscarPorProdutoId(@PathVariable Long produtoId) {
        Estoque estoque = estoqueService.buscarPorProdutoId(produtoId);
        if (estoque == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estoque);
    }
    
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Estoque estoque) {
        try {
            Long id = estoqueService.salvar(estoque);
            estoque.setId(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(estoque);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Estoque estoque) {
        try {
            estoque.setId(id);
            estoqueService.atualizar(estoque);
            return ResponseEntity.ok(estoque);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PatchMapping("/produto/{produtoId}/adicionar")
    public ResponseEntity<?> adicionarQuantidade(@PathVariable Long produtoId, 
                                                  @RequestBody Map<String, BigDecimal> body) {
        try {
            BigDecimal quantidade = body.get("quantidade");
            estoqueService.adicionarQuantidade(produtoId, quantidade);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PatchMapping("/produto/{produtoId}/remover")
    public ResponseEntity<?> removerQuantidade(@PathVariable Long produtoId, 
                                                @RequestBody Map<String, BigDecimal> body) {
        try {
            BigDecimal quantidade = body.get("quantidade");
            estoqueService.subtrairQuantidade(produtoId, quantidade);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            estoqueService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
