package br.com.centralit.condominio.service;

import br.com.centralit.condominio.entity.Morador;
import br.com.centralit.condominio.entity.Unidade;
import br.com.centralit.condominio.enums.SimNao;
import br.com.centralit.condominio.enums.SituacaoUnidade;
import br.com.centralit.condominio.enums.TipoMorador;
import br.com.centralit.condominio.repository.MoradorRepository;
import br.com.centralit.condominio.repository.UnidadeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MoradorServiceTest {

    @Autowired
    private MoradorService moradorService;

    @Autowired
    private UnidadeService unidadeService;

    @Autowired
    private MoradorRepository moradorRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Test
    void deveLancarExcecaoQuandoCpfJaExiste() {
        Unidade unidade = new Unidade();
        unidade.setBloco("A");
        unidade.setNumero("101");
        unidade.setSituacao(SituacaoUnidade.OCUPADA);
        unidade = unidadeRepository.save(unidade);

        Morador morador1 = new Morador();
        morador1.setNome("João");
        morador1.setCpf("12345678901");
        morador1.setTipo(TipoMorador.PROPRIETARIO);
        morador1.setUnidade(unidade);
        moradorService.save(morador1);

        Morador morador2 = new Morador();
        morador2.setNome("Maria");
        morador2.setCpf("12345678901");
        morador2.setTipo(TipoMorador.INQUILINO);
        morador2.setUnidade(unidade);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            moradorService.save(morador2);
        });

        assertTrue(exception.getMessage().contains("CPF já cadastrado"));
    }

    @Test
    void deveLancarExcecaoQuandoUnidadeJaPossuiResponsavel() {
        Unidade unidade = new Unidade();
        unidade.setBloco("B");
        unidade.setNumero("202");
        unidade.setSituacao(SituacaoUnidade.OCUPADA);
        unidade = unidadeRepository.save(unidade);

        Morador responsavel1 = new Morador();
        responsavel1.setNome("Pedro");
        responsavel1.setCpf("11111111111");
        responsavel1.setTipo(TipoMorador.PROPRIETARIO);
        responsavel1.setResponsavel(SimNao.SIM);
        responsavel1.setUnidade(unidade);
        moradorService.save(responsavel1);

        Morador responsavel2 = new Morador();
        responsavel2.setNome("Ana");
        responsavel2.setCpf("22222222222");
        responsavel2.setTipo(TipoMorador.PROPRIETARIO);
        responsavel2.setResponsavel(SimNao.SIM);
        responsavel2.setUnidade(unidade);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            moradorService.save(responsavel2);
        });

        assertTrue(exception.getMessage().contains("já possui um responsável"));
    }

}
