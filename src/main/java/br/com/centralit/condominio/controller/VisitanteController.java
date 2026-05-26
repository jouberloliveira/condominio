package br.com.centralit.condominio.controller;

import br.com.centralit.condominio.entity.Visitante;
import br.com.centralit.condominio.enums.SimNao;
import br.com.centralit.condominio.enums.TipoVisitante;
import br.com.centralit.condominio.service.MoradorService;
import br.com.centralit.condominio.service.UnidadeService;
import br.com.centralit.condominio.service.VisitanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/visitantes")
@RequiredArgsConstructor
public class VisitanteController {

    private final VisitanteService service;
    private final UnidadeService unidadeService;
    private final MoradorService moradorService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("visitantes", service.findAll());
        return "visitantes/lista";
    }

    @GetMapping("/novo")
    public String formNovo(Model model) {
        model.addAttribute("visitante", new Visitante());
        addFormModel(model);
        return "visitantes/form";
    }

    @GetMapping("/{id}/editar")
    public String formEditar(@PathVariable Long id, Model model) {
        return service.findById(id)
            .map(visitante -> {
                model.addAttribute("visitante", visitante);
                addFormModel(model);
                return "visitantes/form";
            })
            .orElse("redirect:/visitantes");
    }

    @PostMapping
    public String criar(@ModelAttribute Visitante visitante) {
        service.save(visitante);
        return "redirect:/visitantes";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Visitante visitante) {
        visitante.setId(id);
        service.save(visitante);
        return "redirect:/visitantes";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/visitantes";
    }

    private void addFormModel(Model model) {
        model.addAttribute("unidades", unidadeService.findAll());
        model.addAttribute("moradores", moradorService.findAll());
        model.addAttribute("tipos", TipoVisitante.values());
        model.addAttribute("simNaoValues", SimNao.values());
    }
}
