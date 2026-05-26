package br.com.centralit.condominio.repository;

import br.com.centralit.condominio.entity.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {

    Optional<Unidade> findByBlocoAndNumero(String bloco, String numero);

}
