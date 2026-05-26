package br.com.centralit.condominio.entity;

import br.com.centralit.condominio.enums.SimNao;
import br.com.centralit.condominio.enums.SituacaoUnidade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Formula;

@Entity
@Data
@EqualsAndHashCode(of = "id")
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String bloco;

    @NotBlank
    private String numero;

    private String andar;

    @Enumerated(EnumType.STRING)
    private SimNao vagaGaragem;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SituacaoUnidade situacao;

    @Formula("CONCAT(numero, '-', bloco)")
    private String identificacao;

}
