package br.com.centralit.condominio.service;

import br.com.centralit.condominio.entity.Ocorrencia;
import br.com.centralit.condominio.enums.PrioridadeOcorrencia;
import br.com.centralit.condominio.enums.StatusOcorrencia;
import br.com.centralit.condominio.enums.TipoOcorrencia;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OcorrenciaServiceTest {

    @Autowired
    private OcorrenciaService ocorrenciaService;

    @Test
    void deveLancarExcecaoQuandoResolvidaSemDataFechamento() {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setTipo(TipoOcorrencia.MANUTENCAO);
        ocorrencia.setPrioridade(PrioridadeOcorrencia.ALTA);
        ocorrencia.setStatus(StatusOcorrencia.RESOLVIDA);
        ocorrencia.setTitulo("Problema no elevador");
        ocorrencia.setDataHoraAbertura(LocalDateTime.now());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ocorrenciaService.save(ocorrencia);
        });

        assertTrue(exception.getMessage().contains("fechamento é obrigatória"));
    }

    @Test
    void deveLancarExcecaoQuandoFechamentoAnteriorAbertura() {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setTipo(TipoOcorrencia.RECLAMACAO);
        ocorrencia.setPrioridade(PrioridadeOcorrencia.MEDIA);
        ocorrencia.setStatus(StatusOcorrencia.RESOLVIDA);
        ocorrencia.setTitulo("Barulho");
        ocorrencia.setDataHoraAbertura(LocalDateTime.now());
        ocorrencia.setDataHoraFechamento(LocalDateTime.now().minusHours(1));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ocorrenciaService.save(ocorrencia);
        });

        assertTrue(exception.getMessage().contains("posterior ou igual"));
    }

}
