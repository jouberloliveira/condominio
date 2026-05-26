package br.com.centralit.condominio.entity;

import br.com.centralit.condominio.enums.SimNao;
import br.com.centralit.condominio.enums.TipoMorador;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Data
public class Morador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @CPF
    @NotBlank
    @Column(unique = true)
    private String cpf;

    private String telefone;

    @Email
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoMorador tipo;

    @Enumerated(EnumType.STRING)
    private SimNao responsavel;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

}
