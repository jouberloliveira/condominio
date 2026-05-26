package br.com.centralit.condominio.entity;

import br.com.centralit.condominio.enums.SimNao;
import br.com.centralit.condominio.enums.TipoVisitante;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Visitante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String documento;

    private String telefone;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoVisitante tipo;

    @Enumerated(EnumType.STRING)
    private SimNao ativo;

    @ManyToOne
    @JoinColumn(name = "autorizado_por_id")
    private Morador autorizadoPor;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

}
