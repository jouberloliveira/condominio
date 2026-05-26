package br.com.centralit.condominio.controller;

import br.com.centralit.condominio.service.MoradorService;
import br.com.centralit.condominio.service.OcorrenciaService;
import br.com.centralit.condominio.service.ReservaService;
import br.com.centralit.condominio.service.UnidadeService;
import br.com.centralit.condominio.service.VisitanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UnidadeService unidadeService;
    private final MoradorService moradorService;
    private final VisitanteService visitanteService;
    private final ReservaService reservaService;
    private final OcorrenciaService ocorrenciaService;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalUnidades",    unidadeService.findAll().size());
        model.addAttribute("totalMoradores",   moradorService.findAll().size());
        model.addAttribute("totalVisitantes",  visitanteService.findAll().size());
        model.addAttribute("totalReservas",    reservaService.findAll().size());
        model.addAttribute("totalOcorrencias", ocorrenciaService.findAll().size());
        model.addAttribute("ultimasOcorrencias", ocorrenciaService.findAll().stream().limit(5).toList());
        model.addAttribute("proximasReservas", reservaService.findAll().stream().limit(5).toList());
        return "index";
    }
}
