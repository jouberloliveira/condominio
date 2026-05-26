package br.com.centralit.condominio.service;

import br.com.centralit.condominio.entity.Unidade;
import br.com.centralit.condominio.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnidadeService {

    private final UnidadeRepository repository;

    public List<Unidade> findAll() {
        return repository.findAll();
    }

    public Optional<Unidade> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Unidade save(Unidade unidade) {
        // Garantir unicidade (bloco+numero)
        Optional<Unidade> existente = repository.findByBlocoAndNumero(
            unidade.getBloco(),
            unidade.getNumero()
        );

        if (existente.isPresent() && !existente.get().getId().equals(unidade.getId())) {
            throw new IllegalArgumentException(
                "Já existe unidade com bloco " + unidade.getBloco() +
                " e número " + unidade.getNumero()
            );
        }

        return repository.save(unidade);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
