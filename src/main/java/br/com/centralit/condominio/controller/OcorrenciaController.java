package br.com.centralit.condominio.controller;

import br.com.centralit.condominio.entity.Ocorrencia;
import br.com.centralit.condominio.service.OcorrenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ocorrencias")
@RequiredArgsConstructor
public class OcorrenciaController {

    private final OcorrenciaService service;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("ocorrencias", service.findAll());
        return "ocorrencias/lista";
    }

    @GetMapping("/novo")
    public String formNovo(Model model) {
        model.addAttribute("ocorrencia", new Ocorrencia());
        return "ocorrencias/form";
    }

    @GetMapping("/{id}/editar")
    public String formEditar(@PathVariable Long id, Model model) {
        return service.findById(id)
            .map(ocorrencia -> {
                model.addAttribute("ocorrencia", ocorrencia);
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

}
