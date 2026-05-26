package br.com.centralit.condominio.entity;

import br.com.centralit.condominio.enums.AreaComum;
import br.com.centralit.condominio.enums.StatusReserva;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "solicitante_id", nullable = false)
    private Morador solicitante;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AreaComum area;

    @NotNull
    private LocalDateTime dataHoraInicio;

    @NotNull
    private LocalDateTime dataHoraFim;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusReserva status;

}
