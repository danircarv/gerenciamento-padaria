package com.padaria.gestao.controller;

import com.padaria.gestao.model.Produto;
import com.padaria.gestao.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {
    
    private final ProdutoService produtoService;
    
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }
    
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", produtoService.listarTodos());
        model.addAttribute("categorias", produtoService.listarCategorias());
        return "produtos/lista";
    }
    
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("produto", new Produto());
        return "produtos/form";
    }
    
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto == null) {
            return "redirect:/produtos";
        }
        model.addAttribute("produto", produto);
        return "produtos/form";
    }
    
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Produto produto, RedirectAttributes redirectAttributes) {
        try {
            if (produto.getId() == null) {
                produtoService.salvar(produto);
                redirectAttributes.addFlashAttribute("mensagem", "Produto cadastrado com sucesso!");
            } else {
                produtoService.atualizar(produto);
                redirectAttributes.addFlashAttribute("mensagem", "Produto atualizado com sucesso!");
            }
            return "redirect:/produtos";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/produtos/novo";
        }
    }
    
    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            produtoService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagem", "Produto excluído com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/produtos";
    }
}
