package com.padaria.controller.web;

import com.padaria.model.Estoque;
import com.padaria.service.EstoqueService;
import com.padaria.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/estoque")
public class EstoqueWebController {

    private final EstoqueService estoqueService;
    private final ProdutoService produtoService;

    public EstoqueWebController(EstoqueService estoqueService, ProdutoService produtoService) {
        this.estoqueService = estoqueService;
        this.produtoService = produtoService;
    }

    @GetMapping
    public String listar(Model model) {
        List<Estoque> estoques = estoqueService.listarTodos();
        model.addAttribute("estoques", estoques);
        return "estoque/lista";
    }

    @GetMapping("/alertas")
    public String alertas(Model model) {
        List<Estoque> estoques = estoqueService.listarAbaixoDoMinimo();
        model.addAttribute("estoques", estoques);
        model.addAttribute("alertas", true);
        return "estoque/alertas";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Estoque estoque = estoqueService.buscarPorId(id);
            model.addAttribute("estoque", estoque);
            return "estoque/form";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/estoque";
        }
    }

    @PostMapping
    public String salvar(@ModelAttribute Estoque estoque, RedirectAttributes redirectAttributes) {
        try {
            if (estoque.getId() == null) {
                estoqueService.salvar(estoque);
                redirectAttributes.addFlashAttribute("sucesso", "Estoque cadastrado com sucesso!");
            } else {
                estoqueService.atualizar(estoque);
                redirectAttributes.addFlashAttribute("sucesso", "Estoque atualizado com sucesso!");
            }
            return "redirect:/estoque";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/estoque";
        }
    }
}
