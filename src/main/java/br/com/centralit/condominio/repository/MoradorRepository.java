package br.com.centralit.condominio.repository;

import br.com.centralit.condominio.entity.Morador;
import br.com.centralit.condominio.entity.Unidade;
import br.com.centralit.condominio.enums.SimNao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoradorRepository extends JpaRepository<Morador, Long> {

    Optional<Morador> findByCpf(String cpf);

    long countByUnidadeAndResponsavel(Unidade unidade, SimNao responsavel);

}
