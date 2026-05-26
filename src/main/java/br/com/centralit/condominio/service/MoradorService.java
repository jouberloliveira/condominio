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
        if (!isCpfValido(morador.getCpf())) {
            throw new IllegalArgumentException("CPF inválido: " + morador.getCpf());
        }

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

    private boolean isCpfValido(String cpf) {
        if (cpf == null) return false;
        String digits = cpf.replaceAll("[^0-9]", "");
        if (digits.length() != 11) return false;
        if (digits.chars().distinct().count() == 1) return false;

        int sum1 = 0;
        for (int i = 0; i < 9; i++) sum1 += (digits.charAt(i) - '0') * (10 - i);
        int rem1 = (sum1 * 10) % 11;
        if (rem1 == 10) rem1 = 0;
        if (rem1 != (digits.charAt(9) - '0')) return false;

        int sum2 = 0;
        for (int i = 0; i < 10; i++) sum2 += (digits.charAt(i) - '0') * (11 - i);
        int rem2 = (sum2 * 10) % 11;
        if (rem2 == 10) rem2 = 0;
        return rem2 == (digits.charAt(10) - '0');
    }

}
