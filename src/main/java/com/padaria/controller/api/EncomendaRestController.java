package com.padaria.controller.api;

import com.padaria.model.Encomenda;
import com.padaria.service.EncomendaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/encomendas")
public class EncomendaRestController {

    private final EncomendaService encomendaService;

    public EncomendaRestController(EncomendaService encomendaService) {
        this.encomendaService = encomendaService;
    }

    @GetMapping
    public ResponseEntity<List<Encomenda>> listarTodas() {
        List<Encomenda> encomendas = encomendaService.listarTodas();
        return ResponseEntity.ok(encomendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Encomenda encomenda = encomendaService.buscarPorId(id);
            return ResponseEntity.ok(encomenda);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Encomenda>> buscarPorStatus(@PathVariable String status) {
        List<Encomenda> encomendas = encomendaService.buscarPorStatus(status);
        return ResponseEntity.ok(encomendas);
    }

    @GetMapping("/entrega/{data}")
    public ResponseEntity<List<Encomenda>> buscarPorDataEntrega(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<Encomenda> encomendas = encomendaService.buscarPorDataEntrega(data);
        return ResponseEntity.ok(encomendas);
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Encomenda encomenda) {
        try {
            Encomenda encomendaSalva = encomendaService.salvar(encomenda);
            return ResponseEntity.status(HttpStatus.CREATED).body(encomendaSalva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Encomenda encomenda) {
        try {
            encomenda.setId(id);
            Encomenda encomendaAtualizada = encomendaService.atualizar(encomenda);
            return ResponseEntity.ok(encomendaAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            encomendaService.atualizarStatus(id, status);
            return ResponseEntity.ok(createSuccessResponse("Status atualizado com sucesso"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            encomendaService.excluir(id);
            return ResponseEntity.ok(createSuccessResponse("Encomenda excluída com sucesso"));
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
