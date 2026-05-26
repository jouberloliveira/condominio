package br.com.centralit.condominio.service;

import br.com.centralit.condominio.entity.Ocorrencia;
import br.com.centralit.condominio.enums.StatusOcorrencia;
import br.com.centralit.condominio.repository.OcorrenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OcorrenciaService {

    private final OcorrenciaRepository repository;

    public List<Ocorrencia> findAll() {
        return repository.findAll();
    }

    public Optional<Ocorrencia> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Ocorrencia save(Ocorrencia ocorrencia) {
        // Fechamento obrigatório se RESOLVIDA/CANCELADA
        if (StatusOcorrencia.RESOLVIDA.equals(ocorrencia.getStatus()) ||
            StatusOcorrencia.CANCELADA.equals(ocorrencia.getStatus())) {

            if (ocorrencia.getDataHoraFechamento() == null) {
                throw new IllegalArgumentException(
                    "Data/hora de fechamento é obrigatória para ocorrências resolvidas ou canceladas"
                );
            }

            // fechamento >= abertura
            if (ocorrencia.getDataHoraFechamento().isBefore(ocorrencia.getDataHoraAbertura())) {
                throw new IllegalArgumentException(
                    "Data/hora de fechamento deve ser posterior ou igual à data/hora de abertura"
                );
            }
        }

        return repository.save(ocorrencia);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
