package br.com.centralit.condominio.controller;

import br.com.centralit.condominio.entity.Morador;
import br.com.centralit.condominio.service.MoradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/moradores")
@RequiredArgsConstructor
public class MoradorController {

    private final MoradorService service;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("moradores", service.findAll());
        return "moradores/lista";
    }

    @GetMapping("/novo")
    public String formNovo(Model model) {
        model.addAttribute("morador", new Morador());
        return "moradores/form";
    }

    @GetMapping("/{id}/editar")
    public String formEditar(@PathVariable Long id, Model model) {
        return service.findById(id)
            .map(morador -> {
                model.addAttribute("morador", morador);
                return "moradores/form";
            })
            .orElse("redirect:/moradores");
    }

    @PostMapping
    public String criar(@ModelAttribute Morador morador) {
        service.save(morador);
        return "redirect:/moradores";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Morador morador) {
        morador.setId(id);
        service.save(morador);
        return "redirect:/moradores";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/moradores";
    }

}
