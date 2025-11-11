package com.padaria.gestao.controller;

import com.padaria.gestao.model.Encomenda;
import com.padaria.gestao.model.ItemEncomenda;
import com.padaria.gestao.service.EncomendaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        return ResponseEntity.ok(encomendaService.listarTodas());
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Encomenda>> listarPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(encomendaService.listarPorStatus(status));
    }
    
    @GetMapping("/data-entrega")
    public ResponseEntity<List<Encomenda>> listarPorDataEntrega(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(encomendaService.listarPorDataEntrega(data));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Encomenda> buscarPorId(@PathVariable Long id) {
        Encomenda encomenda = encomendaService.buscarPorId(id);
        if (encomenda == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(encomenda);
    }
    
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Map<String, Object> request) {
        try {
            Encomenda encomenda = new Encomenda();
            encomenda.setClienteId(Long.valueOf(request.get("clienteId").toString()));
            encomenda.setDataEntrega(LocalDate.parse(request.get("dataEntrega").toString()));
            encomenda.setObservacoes((String) request.get("observacoes"));
            
            if (request.containsKey("valorEntrada")) {
                encomenda.setValorEntrada(new java.math.BigDecimal(request.get("valorEntrada").toString()));
            }
            
            if (request.containsKey("status")) {
                encomenda.setStatus((String) request.get("status"));
            }
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> itensData = (List<Map<String, Object>>) request.get("itens");
            List<ItemEncomenda> itens = itensData.stream().map(item -> {
                ItemEncomenda itemEncomenda = new ItemEncomenda();
                itemEncomenda.setProdutoId(Long.valueOf(item.get("produtoId").toString()));
                itemEncomenda.setQuantidade(new java.math.BigDecimal(item.get("quantidade").toString()));
                itemEncomenda.setPrecoUnitario(new java.math.BigDecimal(item.get("precoUnitario").toString()));
                return itemEncomenda;
            }).toList();
            
            Long id = encomendaService.criarEncomenda(encomenda, itens);
            encomenda.setId(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(encomenda);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String novoStatus = body.get("status");
            encomendaService.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            encomendaService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
