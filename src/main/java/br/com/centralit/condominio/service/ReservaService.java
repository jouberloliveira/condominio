package br.com.centralit.condominio.service;

import br.com.centralit.condominio.entity.Morador;
import br.com.centralit.condominio.entity.Reserva;
import br.com.centralit.condominio.enums.StatusReserva;
import br.com.centralit.condominio.repository.MoradorRepository;
import br.com.centralit.condominio.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository repository;
    private final MoradorRepository moradorRepository;

    public List<Reserva> findAll() {
        return repository.findAll();
    }

    public Optional<Reserva> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Reserva save(Reserva reserva) {
        // fim > início
        if (!reserva.getDataHoraFim().isAfter(reserva.getDataHoraInicio())) {
            throw new IllegalArgumentException(
                "Data/hora fim deve ser posterior à data/hora início"
            );
        }

        // Solicitante deve pertencer à unidade da reserva
        Morador solicitante = moradorRepository.findById(reserva.getSolicitante().getId())
            .orElseThrow(() -> new IllegalArgumentException("Solicitante não encontrado"));
        if (!solicitante.getUnidade().getId().equals(reserva.getUnidade().getId())) {
            throw new IllegalArgumentException(
                "Solicitante não é morador da unidade informada"
            );
        }

        // Sem conflito de área para status APROVADA
        if (StatusReserva.APROVADA.equals(reserva.getStatus())) {
            List<Reserva> conflitos = repository.findConflitos(
                reserva.getArea(),
                StatusReserva.APROVADA,
                reserva.getDataHoraInicio(),
                reserva.getDataHoraFim()
            );

            // Excluir a própria reserva sendo editada
            conflitos.removeIf(c -> c.getId().equals(reserva.getId()));

            if (!conflitos.isEmpty()) {
                throw new IllegalArgumentException(
                    "Já existe reserva aprovada para esta área no período informado"
                );
            }
        }

        return repository.save(reserva);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
