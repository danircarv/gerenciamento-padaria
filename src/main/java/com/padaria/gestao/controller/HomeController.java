package com.padaria.gestao.controller;

import com.padaria.gestao.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;
    private final VendaService vendaService;
    private final EncomendaService encomendaService;
    
    public HomeController(ProdutoService produtoService, EstoqueService estoqueService,
                          VendaService vendaService, EncomendaService encomendaService) {
        this.produtoService = produtoService;
        this.estoqueService = estoqueService;
        this.vendaService = vendaService;
        this.encomendaService = encomendaService;
    }
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalProdutos", produtoService.listarAtivos().size());
        model.addAttribute("alertasEstoque", estoqueService.listarAbaixoDoMinimo().size());
        model.addAttribute("encomendasPendentes", encomendaService.listarPorStatus("PENDENTE").size());
        return "index";
    }
}
