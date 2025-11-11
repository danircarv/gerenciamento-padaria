package com.padaria.gestao.controller;

import com.padaria.gestao.model.Cliente;
import com.padaria.gestao.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {
    
    private final ClienteService clienteService;
    
    public ClienteRestController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<List<Cliente>> listarAtivos() {
        return ResponseEntity.ok(clienteService.listarAtivos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }
    
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Cliente cliente) {
        try {
            Long id = clienteService.salvar(cliente);
            cliente.setId(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            cliente.setId(id);
            clienteService.atualizar(cliente);
            return ResponseEntity.ok(cliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            clienteService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            clienteService.inativar(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
