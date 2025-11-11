package com.padaria.gestao.controller;

import com.padaria.gestao.model.Fornecedor;
import com.padaria.gestao.service.FornecedorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorRestController {
    
    private final FornecedorService fornecedorService;
    
    public FornecedorRestController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }
    
    @GetMapping
    public ResponseEntity<List<Fornecedor>> listarTodos() {
        return ResponseEntity.ok(fornecedorService.listarTodos());
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<List<Fornecedor>> listarAtivos() {
        return ResponseEntity.ok(fornecedorService.listarAtivos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> buscarPorId(@PathVariable Long id) {
        Fornecedor fornecedor = fornecedorService.buscarPorId(id);
        if (fornecedor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fornecedor);
    }
    
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Fornecedor fornecedor) {
        try {
            Long id = fornecedorService.salvar(fornecedor);
            fornecedor.setId(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(fornecedor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Fornecedor fornecedor) {
        try {
            fornecedor.setId(id);
            fornecedorService.atualizar(fornecedor);
            return ResponseEntity.ok(fornecedor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            fornecedorService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            fornecedorService.inativar(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
