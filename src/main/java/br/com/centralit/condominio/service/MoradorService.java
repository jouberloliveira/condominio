package br.com.centralit.condominio.service;

import br.com.centralit.condominio.entity.Morador;
import br.com.centralit.condominio.enums.SimNao;
import br.com.centralit.condominio.repository.MoradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoradorService {

    private final MoradorRepository repository;

    public List<Morador> findAll() {
        return repository.findAll();
    }

    public Optional<Morador> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Morador save(Morador morador) {
        // Garantir CPF único global
        Optional<Morador> existente = repository.findByCpf(morador.getCpf());
        if (existente.isPresent() && !existente.get().getId().equals(morador.getId())) {
            throw new IllegalArgumentException("CPF já cadastrado: " + morador.getCpf());
        }

        // Max 1 responsável por unidade
        if (SimNao.SIM.equals(morador.getResponsavel())) {
            long count = repository.countByUnidadeAndResponsavel(
                morador.getUnidade(),
                SimNao.SIM
            );

            if (count > 0) {
                // Verificar se o responsável existente não é o próprio morador sendo editado
                Optional<Morador> responsavelAtual = repository.findAll().stream()
                    .filter(m -> m.getUnidade().equals(morador.getUnidade()) &&
                                SimNao.SIM.equals(m.getResponsavel()))
                    .findFirst();

                if (responsavelAtual.isPresent() &&
                    !responsavelAtual.get().getId().equals(morador.getId())) {
                    throw new IllegalArgumentException(
                        "Unidade já possui um responsável: " + responsavelAtual.get().getNome()
                    );
                }
            }
        }

        return repository.save(morador);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
