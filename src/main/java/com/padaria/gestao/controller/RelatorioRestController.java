package com.padaria.gestao.controller;

import com.padaria.gestao.service.RelatorioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioRestController {
    
    private final RelatorioService relatorioService;
    
    public RelatorioRestController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }
    
    @GetMapping("/vendas/periodo")
    public ResponseEntity<Map<String, Object>> relatorioVendasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(relatorioService.gerarRelatorioVendasPorPeriodo(inicio, fim));
    }
    
    @GetMapping("/produtos/mais-vendidos")
    public ResponseEntity<List<Map<String, Object>>> relatorioProdutosMaisVendidos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(relatorioService.gerarRelatorioProdutosMaisVendidos(inicio, fim));
    }
    
    @GetMapping("/vendas/forma-pagamento")
    public ResponseEntity<List<Map<String, Object>>> relatorioVendasPorFormaPagamento(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(relatorioService.gerarRelatorioVendasPorFormaPagamento(inicio, fim));
    }
    
    @GetMapping("/estoque/baixo")
    public ResponseEntity<List<Map<String, Object>>> relatorioEstoqueBaixo() {
        return ResponseEntity.ok(relatorioService.gerarRelatorioEstoqueBaixo());
    }
    
    @GetMapping("/encomendas/status")
    public ResponseEntity<List<Map<String, Object>>> relatorioEncomendasPorStatus() {
        return ResponseEntity.ok(relatorioService.gerarRelatorioEncomendasPorStatus());
    }
    
    @GetMapping("/clientes/ativos")
    public ResponseEntity<List<Map<String, Object>>> relatorioClientesAtivos() {
        return ResponseEntity.ok(relatorioService.gerarRelatorioClientesAtivos());
    }
}
