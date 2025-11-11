package com.padaria.gestao.controller;

import com.padaria.gestao.model.ItemVenda;
import com.padaria.gestao.model.Venda;
import com.padaria.gestao.service.VendaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendas")
public class VendaRestController {
    
    private final VendaService vendaService;
    
    public VendaRestController(VendaService vendaService) {
        this.vendaService = vendaService;
    }
    
    @GetMapping
    public ResponseEntity<List<Venda>> listarTodas() {
        return ResponseEntity.ok(vendaService.listarTodas());
    }
    
    @GetMapping("/periodo")
    public ResponseEntity<List<Venda>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(vendaService.listarPorPeriodo(inicio, fim));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarPorId(@PathVariable Long id) {
        Venda venda = vendaService.buscarPorId(id);
        if (venda == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(venda);
    }
    
    @PostMapping
    public ResponseEntity<?> realizarVenda(@RequestBody Map<String, Object> request) {
        try {
            Venda venda = new Venda();
            
            if (request.containsKey("clienteId")) {
                Object clienteIdObj = request.get("clienteId");
                if (clienteIdObj != null) {
                    venda.setClienteId(Long.valueOf(clienteIdObj.toString()));
                }
            }
            
            venda.setFormaPagamento((String) request.get("formaPagamento"));
            venda.setObservacoes((String) request.get("observacoes"));
            
            if (request.containsKey("desconto")) {
                venda.setDesconto(new java.math.BigDecimal(request.get("desconto").toString()));
            }
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> itensData = (List<Map<String, Object>>) request.get("itens");
            List<ItemVenda> itens = itensData.stream().map(item -> {
                ItemVenda itemVenda = new ItemVenda();
                itemVenda.setProdutoId(Long.valueOf(item.get("produtoId").toString()));
                itemVenda.setQuantidade(new java.math.BigDecimal(item.get("quantidade").toString()));
                itemVenda.setPrecoUnitario(new java.math.BigDecimal(item.get("precoUnitario").toString()));
                return itemVenda;
            }).toList();
            
            Long id = vendaService.realizarVenda(venda, itens);
            venda.setId(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(venda);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            vendaService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
