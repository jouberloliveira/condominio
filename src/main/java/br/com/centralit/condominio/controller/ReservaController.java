package br.com.centralit.condominio.controller;

import br.com.centralit.condominio.entity.Reserva;
import br.com.centralit.condominio.enums.AreaComum;
import br.com.centralit.condominio.enums.StatusReserva;
import br.com.centralit.condominio.service.MoradorService;
import br.com.centralit.condominio.service.ReservaService;
import br.com.centralit.condominio.service.UnidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService service;
    private final UnidadeService unidadeService;
    private final MoradorService moradorService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("reservas", service.findAll());
        return "reservas/lista";
    }

    @GetMapping("/novo")
    public String formNovo(Model model) {
        model.addAttribute("reserva", new Reserva());
        addFormModel(model);
        return "reservas/form";
    }

    @GetMapping("/{id}/editar")
    public String formEditar(@PathVariable Long id, Model model) {
        return service.findById(id)
            .map(reserva -> {
                model.addAttribute("reserva", reserva);
                addFormModel(model);
                return "reservas/form";
            })
            .orElse("redirect:/reservas");
    }

    @PostMapping
    public String criar(@ModelAttribute Reserva reserva) {
        service.save(reserva);
        return "redirect:/reservas";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Reserva reserva) {
        reserva.setId(id);
        service.save(reserva);
        return "redirect:/reservas";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/reservas";
    }

    private void addFormModel(Model model) {
        model.addAttribute("unidades", unidadeService.findAll());
        model.addAttribute("moradores", moradorService.findAll());
        model.addAttribute("areas", AreaComum.values());
        model.addAttribute("statusValues", StatusReserva.values());
    }
}
