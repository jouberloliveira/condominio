package br.com.centralit.condominio.controller;

import br.com.centralit.condominio.entity.Morador;
import br.com.centralit.condominio.enums.SimNao;
import br.com.centralit.condominio.enums.TipoMorador;
import br.com.centralit.condominio.service.MoradorService;
import br.com.centralit.condominio.service.UnidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/moradores")
@RequiredArgsConstructor
public class MoradorController {

    private final MoradorService service;
    private final UnidadeService unidadeService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("moradores", service.findAll());
        return "moradores/lista";
    }

    @GetMapping("/novo")
    public String formNovo(Model model) {
        model.addAttribute("morador", new Morador());
        addFormModel(model);
        return "moradores/form";
    }

    @GetMapping("/{id}/editar")
    public String formEditar(@PathVariable Long id, Model model) {
        return service.findById(id)
            .map(morador -> {
                model.addAttribute("morador", morador);
                addFormModel(model);
                return "moradores/form";
            })
            .orElse("redirect:/moradores");
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute Morador morador, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addFormModel(model);
            return "moradores/form";
        }
        service.save(morador);
        return "redirect:/moradores";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute Morador morador,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            addFormModel(model);
            return "moradores/form";
        }
        morador.setId(id);
        service.save(morador);
        return "redirect:/moradores";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/moradores";
    }

    private void addFormModel(Model model) {
        model.addAttribute("unidades", unidadeService.findAll());
        model.addAttribute("tipos", TipoMorador.values());
        model.addAttribute("simNaoValues", SimNao.values());
    }
}
