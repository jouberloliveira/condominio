package br.com.centralit.condominio.entity;

import br.com.centralit.condominio.enums.PrioridadeOcorrencia;
import br.com.centralit.condominio.enums.StatusOcorrencia;
import br.com.centralit.condominio.enums.TipoOcorrencia;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;

    @ManyToOne
    @JoinColumn(name = "aberto_por_id")
    private Morador abertoPor;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoOcorrencia tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PrioridadeOcorrencia prioridade;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusOcorrencia status;

    @NotBlank
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @NotNull
    private LocalDateTime dataHoraAbertura;

    private LocalDateTime dataHoraFechamento;

}
