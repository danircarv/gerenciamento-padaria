package com.padaria.service;

import com.padaria.dao.EncomendaDao;
import com.padaria.dao.VendaDao;
import com.padaria.model.Encomenda;
import com.padaria.model.Estoque;
import com.padaria.model.Venda;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    private final VendaDao vendaDao;
    private final EncomendaDao encomendaDao;
    private final EstoqueService estoqueService;

    public RelatorioService(VendaDao vendaDao, EncomendaDao encomendaDao, EstoqueService estoqueService) {
        this.vendaDao = vendaDao;
        this.encomendaDao = encomendaDao;
        this.estoqueService = estoqueService;
    }

    public Map<String, Object> getRelatorioVendasDiario(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(LocalTime.MAX);
        
        List<Venda> vendas = vendaDao.findByPeriodo(inicio, fim);
        BigDecimal totalVendas = vendaDao.getTotalVendasPeriodo(inicio, fim);
        
        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("data", data);
        relatorio.put("vendas", vendas);
        relatorio.put("totalVendas", totalVendas);
        relatorio.put("quantidadeVendas", vendas.size());
        
        return relatorio;
    }

    public Map<String, Object> getRelatorioVendasMensal(int ano, int mes) {
        LocalDate primeiroDia = LocalDate.of(ano, mes, 1);
        LocalDate ultimoDia = primeiroDia.plusMonths(1).minusDays(1);
        
        LocalDateTime inicio = primeiroDia.atStartOfDay();
        LocalDateTime fim = ultimoDia.atTime(LocalTime.MAX);
        
        List<Venda> vendas = vendaDao.findByPeriodo(inicio, fim);
        BigDecimal totalVendas = vendaDao.getTotalVendasPeriodo(inicio, fim);
        
        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("ano", ano);
        relatorio.put("mes", mes);
        relatorio.put("vendas", vendas);
        relatorio.put("totalVendas", totalVendas);
        relatorio.put("quantidadeVendas", vendas.size());
        
        return relatorio;
    }

    public Map<String, Object> getRelatorioEstoque() {
        List<Estoque> todosEstoques = estoqueService.listarTodos();
        List<Estoque> estoqueBaixo = estoqueService.listarAbaixoDoMinimo();
        
        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("todosEstoques", todosEstoques);
        relatorio.put("estoqueBaixo", estoqueBaixo);
        relatorio.put("alertas", estoqueBaixo.size());
        
        return relatorio;
    }

    public Map<String, Object> getRelatorioEncomendas() {
        List<Encomenda> todasEncomendas = encomendaDao.findAll();
        List<Encomenda> pendentes = encomendaDao.findByStatus("PENDENTE");
        List<Encomenda> emProducao = encomendaDao.findByStatus("EM_PRODUCAO");
        List<Encomenda> prontas = encomendaDao.findByStatus("PRONTA");
        List<Encomenda> entregues = encomendaDao.findByStatus("ENTREGUE");
        
        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("todas", todasEncomendas);
        relatorio.put("pendentes", pendentes);
        relatorio.put("emProducao", emProducao);
        relatorio.put("prontas", prontas);
        relatorio.put("entregues", entregues);
        
        return relatorio;
    }

    public Map<String, Object> getRelatorioEncomendasDia(LocalDate data) {
        List<Encomenda> encomendas = encomendaDao.findByDataEntrega(data);
        
        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("data", data);
        relatorio.put("encomendas", encomendas);
        relatorio.put("quantidade", encomendas.size());
        
        return relatorio;
    }
}
