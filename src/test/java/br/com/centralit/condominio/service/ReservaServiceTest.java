package br.com.centralit.condominio.service;

import br.com.centralit.condominio.entity.Morador;
import br.com.centralit.condominio.entity.Reserva;
import br.com.centralit.condominio.entity.Unidade;
import br.com.centralit.condominio.enums.*;
import br.com.centralit.condominio.repository.MoradorRepository;
import br.com.centralit.condominio.repository.ReservaRepository;
import br.com.centralit.condominio.repository.UnidadeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReservaServiceTest {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private MoradorRepository moradorRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Test
    void deveLancarExcecaoQuandoFimAnteriorInicio() {
        Unidade unidade = new Unidade();
        unidade.setBloco("A");
        unidade.setNumero("101");
        unidade.setSituacao(SituacaoUnidade.OCUPADA);
        unidade = unidadeRepository.save(unidade);

        Morador morador = new Morador();
        morador.setNome("Carlos");
        morador.setCpf("33333333333");
        morador.setTipo(TipoMorador.PROPRIETARIO);
        morador.setUnidade(unidade);
        morador = moradorRepository.save(morador);

        Reserva reserva = new Reserva();
        reserva.setUnidade(unidade);
        reserva.setSolicitante(morador);
        reserva.setArea(AreaComum.CHURRASQUEIRA);
        reserva.setDataHoraInicio(LocalDateTime.now().plusDays(1));
        reserva.setDataHoraFim(LocalDateTime.now());
        reserva.setStatus(StatusReserva.SOLICITADA);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.save(reserva);
        });

        assertTrue(exception.getMessage().contains("fim deve ser posterior"));
    }

    @Test
    void deveLancarExcecaoQuandoExisteConflitoDeReserva() {
        Unidade unidade = new Unidade();
        unidade.setBloco("B");
        unidade.setNumero("202");
        unidade.setSituacao(SituacaoUnidade.OCUPADA);
        unidade = unidadeRepository.save(unidade);

        Morador morador = new Morador();
        morador.setNome("Lucas");
        morador.setCpf("44444444444");
        morador.setTipo(TipoMorador.PROPRIETARIO);
        morador.setUnidade(unidade);
        morador = moradorRepository.save(morador);

        LocalDateTime inicio = LocalDateTime.now().plusDays(2);
        LocalDateTime fim = inicio.plusHours(4);

        Reserva reserva1 = new Reserva();
        reserva1.setUnidade(unidade);
        reserva1.setSolicitante(morador);
        reserva1.setArea(AreaComum.SALAO_FESTAS);
        reserva1.setDataHoraInicio(inicio);
        reserva1.setDataHoraFim(fim);
        reserva1.setStatus(StatusReserva.APROVADA);
        reservaRepository.save(reserva1);

        Reserva reserva2 = new Reserva();
        reserva2.setUnidade(unidade);
        reserva2.setSolicitante(morador);
        reserva2.setArea(AreaComum.SALAO_FESTAS);
        reserva2.setDataHoraInicio(inicio.plusHours(2));
        reserva2.setDataHoraFim(fim.plusHours(2));
        reserva2.setStatus(StatusReserva.APROVADA);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.save(reserva2);
        });

        assertTrue(exception.getMessage().contains("conflito") ||
                   exception.getMessage().toLowerCase().contains("já existe reserva"));
    }

    @Test
    void deveLancarExcecaoQuandoSolicitanteNaoPertenceAUnidade() {
        Unidade unidadeA = new Unidade();
        unidadeA.setBloco("C");
        unidadeA.setNumero("301");
        unidadeA.setSituacao(SituacaoUnidade.OCUPADA);
        unidadeA = unidadeRepository.save(unidadeA);

        Unidade unidadeB = new Unidade();
        unidadeB.setBloco("C");
        unidadeB.setNumero("302");
        unidadeB.setSituacao(SituacaoUnidade.OCUPADA);
        unidadeB = unidadeRepository.save(unidadeB);

        Morador moradorDeA = new Morador();
        moradorDeA.setNome("Fernanda");
        moradorDeA.setCpf("55555555555");
        moradorDeA.setTipo(TipoMorador.PROPRIETARIO);
        moradorDeA.setUnidade(unidadeA);
        moradorDeA = moradorRepository.save(moradorDeA);

        Reserva reserva = new Reserva();
        reserva.setUnidade(unidadeB);
        reserva.setSolicitante(moradorDeA);
        reserva.setArea(AreaComum.CHURRASQUEIRA);
        reserva.setDataHoraInicio(LocalDateTime.now().plusDays(1));
        reserva.setDataHoraFim(LocalDateTime.now().plusDays(1).plusHours(2));
        reserva.setStatus(StatusReserva.SOLICITADA);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.save(reserva);
        });

        assertTrue(exception.getMessage().contains("não é morador da unidade"));
    }

}
