package com.padaria.controller.api;

import com.padaria.service.RelatorioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioRestController {

    private final RelatorioService relatorioService;

    public RelatorioRestController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/vendas/diario")
    public ResponseEntity<Map<String, Object>> getRelatorioVendasDiario(@RequestParam LocalDate data) {
        Map<String, Object> relatorio = relatorioService.getRelatorioVendasDiario(data);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/vendas/mensal")
    public ResponseEntity<Map<String, Object>> getRelatorioVendasMensal(
            @RequestParam int ano,
            @RequestParam int mes) {
        Map<String, Object> relatorio = relatorioService.getRelatorioVendasMensal(ano, mes);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/estoque")
    public ResponseEntity<Map<String, Object>> getRelatorioEstoque() {
        Map<String, Object> relatorio = relatorioService.getRelatorioEstoque();
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/encomendas")
    public ResponseEntity<Map<String, Object>> getRelatorioEncomendas() {
        Map<String, Object> relatorio = relatorioService.getRelatorioEncomendas();
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/encomendas/dia")
    public ResponseEntity<Map<String, Object>> getRelatorioEncomendasDia(@RequestParam LocalDate data) {
        Map<String, Object> relatorio = relatorioService.getRelatorioEncomendasDia(data);
        return ResponseEntity.ok(relatorio);
    }
}
