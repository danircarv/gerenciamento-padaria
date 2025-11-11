package com.padaria.controller.api;

import com.padaria.model.Estoque;
import com.padaria.service.EstoqueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
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
        List<Estoque> estoques = estoqueService.listarTodos();
        return ResponseEntity.ok(estoques);
    }

    @GetMapping("/alertas")
    public ResponseEntity<List<Estoque>> listarAbaixoDoMinimo() {
        List<Estoque> estoques = estoqueService.listarAbaixoDoMinimo();
        return ResponseEntity.ok(estoques);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Estoque estoque = estoqueService.buscarPorId(id);
            return ResponseEntity.ok(estoque);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<?> buscarPorProdutoId(@PathVariable Long produtoId) {
        try {
            Estoque estoque = estoqueService.buscarPorProdutoId(produtoId);
            if (estoque == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Estoque não encontrado para o produto"));
            }
            return ResponseEntity.ok(estoque);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Estoque estoque) {
        try {
            Estoque estoqueSalvo = estoqueService.salvar(estoque);
            return ResponseEntity.status(HttpStatus.CREATED).body(estoqueSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Estoque estoque) {
        try {
            estoque.setId(id);
            Estoque estoqueAtualizado = estoqueService.atualizar(estoque);
            return ResponseEntity.ok(estoqueAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @PatchMapping("/produto/{produtoId}/adicionar")
    public ResponseEntity<?> adicionarQuantidade(
            @PathVariable Long produtoId,
            @RequestParam BigDecimal quantidade) {
        try {
            estoqueService.adicionarQuantidade(produtoId, quantidade);
            return ResponseEntity.ok(createSuccessResponse("Quantidade adicionada com sucesso"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @PatchMapping("/produto/{produtoId}/remover")
    public ResponseEntity<?> removerQuantidade(
            @PathVariable Long produtoId,
            @RequestParam BigDecimal quantidade) {
        try {
            estoqueService.removerQuantidade(produtoId, quantidade);
            return ResponseEntity.ok(createSuccessResponse("Quantidade removida com sucesso"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            estoqueService.excluir(id);
            return ResponseEntity.ok(createSuccessResponse("Estoque excluído com sucesso"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("error", message);
        return response;
    }

    private Map<String, String> createSuccessResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }
}
