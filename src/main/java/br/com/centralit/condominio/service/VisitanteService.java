package br.com.centralit.condominio.service;

import br.com.centralit.condominio.entity.Visitante;
import br.com.centralit.condominio.repository.VisitanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VisitanteService {

    private final VisitanteRepository repository;

    public List<Visitante> findAll() {
        return repository.findAll();
    }

    public Optional<Visitante> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Visitante save(Visitante visitante) {
        // autorizadoPor deve ser morador da mesma unidade
        if (visitante.getAutorizadoPor() != null) {
            if (!visitante.getAutorizadoPor().getUnidade().equals(visitante.getUnidade())) {
                throw new IllegalArgumentException(
                    "Morador autorizador deve pertencer à mesma unidade do visitante"
                );
            }
        }

        return repository.save(visitante);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
