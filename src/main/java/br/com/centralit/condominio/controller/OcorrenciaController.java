package br.com.centralit.condominio.controller;

import br.com.centralit.condominio.entity.Ocorrencia;
import br.com.centralit.condominio.enums.PrioridadeOcorrencia;
import br.com.centralit.condominio.enums.StatusOcorrencia;
import br.com.centralit.condominio.enums.TipoOcorrencia;
import br.com.centralit.condominio.service.MoradorService;
import br.com.centralit.condominio.service.OcorrenciaService;
import br.com.centralit.condominio.service.UnidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ocorrencias")
@RequiredArgsConstructor
public class OcorrenciaController {

    private final OcorrenciaService service;
    private final UnidadeService unidadeService;
    private final MoradorService moradorService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("ocorrencias", service.findAll());
        return "ocorrencias/lista";
    }

    @GetMapping("/novo")
    public String formNovo(Model model) {
        model.addAttribute("ocorrencia", new Ocorrencia());
        addFormModel(model);
        return "ocorrencias/form";
    }

    @GetMapping("/{id}/editar")
    public String formEditar(@PathVariable Long id, Model model) {
        return service.findById(id)
            .map(ocorrencia -> {
                model.addAttribute("ocorrencia", ocorrencia);
                addFormModel(model);
                return "ocorrencias/form";
            })
            .orElse("redirect:/ocorrencias");
    }

    @PostMapping
    public String criar(@ModelAttribute Ocorrencia ocorrencia) {
        service.save(ocorrencia);
        return "redirect:/ocorrencias";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Ocorrencia ocorrencia) {
        ocorrencia.setId(id);
        service.save(ocorrencia);
        return "redirect:/ocorrencias";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/ocorrencias";
    }

    private void addFormModel(Model model) {
        model.addAttribute("unidades", unidadeService.findAll());
        model.addAttribute("moradores", moradorService.findAll());
        model.addAttribute("tipos", TipoOcorrencia.values());
        model.addAttribute("prioridades", PrioridadeOcorrencia.values());
        model.addAttribute("statusValues", StatusOcorrencia.values());
    }
}
