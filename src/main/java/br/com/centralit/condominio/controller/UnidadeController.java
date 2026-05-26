package br.com.centralit.condominio.controller;

import br.com.centralit.condominio.entity.Unidade;
import br.com.centralit.condominio.service.UnidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/unidades")
@RequiredArgsConstructor
public class UnidadeController {

    private final UnidadeService service;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("unidades", service.findAll());
        return "unidades/lista";
    }

    @GetMapping("/novo")
    public String formNovo(Model model) {
        model.addAttribute("unidade", new Unidade());
        return "unidades/form";
    }

    @GetMapping("/{id}/editar")
    public String formEditar(@PathVariable Long id, Model model) {
        return service.findById(id)
            .map(unidade -> {
                model.addAttribute("unidade", unidade);
                return "unidades/form";
            })
            .orElse("redirect:/unidades");
    }

    @PostMapping
    public String criar(@ModelAttribute Unidade unidade) {
        service.save(unidade);
        return "redirect:/unidades";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Unidade unidade) {
        unidade.setId(id);
        service.save(unidade);
        return "redirect:/unidades";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/unidades";
    }

}
