package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.api.v1.model.ResultadoVotacao;
import com.dbserver.desafiovotacao.domain.exceptions.PautaException;
import com.dbserver.desafiovotacao.domain.exceptions.PautaNaoEncontradaException;
import com.dbserver.desafiovotacao.domain.model.Pauta;
import com.dbserver.desafiovotacao.domain.model.Situacao;
import com.dbserver.desafiovotacao.domain.model.Voto;
import com.dbserver.desafiovotacao.domain.repository.PautaRepository;
import com.dbserver.desafiovotacao.domain.service.PautaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.dbserver.desafiovotacao.domain.service.PautaService.DATA_FINALIZACAO_INVALIDA;
import static com.dbserver.desafiovotacao.domain.service.PautaService.NAO_FOI_POSSIVEL_LOCALIZAR_UMA_PAUTA_COM_ID;
import static com.dbserver.desafiovotacao.domain.service.PautaService.SESSAO_FINALIZADA;
import static com.dbserver.desafiovotacao.domain.service.PautaService.SESSAO_JA_ABERTA_PARA_VOTACAO;
import static java.text.MessageFormat.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PautaServiceTest {

    public static final String VOTO_SIM = "Sim";
    public static final String VOTO_NAO = "Não";
    @InjectMocks
    private PautaService pautaService;
    @Mock
    private PautaRepository pautaRepository;

    @Test
    void deveSalvarUmaPautaComSucesso_QuandoForPassadoUmaDataHoraFimNula() throws PautaException, PautaNaoEncontradaException {

        Pauta pauta = new Pauta();
        pauta.setId(1L);
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));
        pautaService.abrirSessao(pauta.getId(), null);

        verify(pautaRepository).save(pauta);
    }

    @Test
    void deveSalvarUmaPautaComSucesso_QuandoForPassadoUmaDataHoraFimValida() throws PautaException, PautaNaoEncontradaException {

        Pauta pauta = new Pauta();
        pauta.setId(1L);

        LocalDateTime dataHoraFim = LocalDateTime.now().plusMinutes(10);

        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));
        pautaService.abrirSessao(pauta.getId(), dataHoraFim);

        verify(pautaRepository).save(pauta);
    }

    @Test
    void deveLancarUmaException_QuandoForPassadoUmaDataHoraFimInvalida() throws PautaNaoEncontradaException {

        Pauta pauta = new Pauta();
        pauta.setId(1L);

        LocalDateTime dataHoraFim = LocalDateTime.now().minusMinutes(10);

        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));
        try {
            pautaService.abrirSessao(pauta.getId(), dataHoraFim);
            fail();
        } catch (PautaException e) {
            assertEquals(format(DATA_FINALIZACAO_INVALIDA, pauta.getId()), e.getMessage());
        }
    }

    @Test
    void deveLancarUmaException_QuandoAPautaJaEstiverAberta() throws PautaNaoEncontradaException {

        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setSituacao(Situacao.VOTACAO_ABERTA);

        LocalDateTime dataHoraFim = LocalDateTime.now().minusMinutes(10);

        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));
        try {
            pautaService.abrirSessao(pauta.getId(), dataHoraFim);
            fail();
        } catch (PautaException e) {
            assertEquals(format(SESSAO_JA_ABERTA_PARA_VOTACAO, pauta.getId()), e.getMessage());
        }
    }

    @Test
    void deveLancarUmaException_QuandoTentarAbrirUmaSessaoParaUmaPautaJaFinalizada() throws PautaNaoEncontradaException {

        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setSituacao(Situacao.VOTACAO_FECHADA);
        pauta.setDataHoraInicio(LocalDateTime.now());

        LocalDateTime dataHoraFim = LocalDateTime.now().minusMinutes(10);

        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));
        try {
            pautaService.abrirSessao(pauta.getId(), dataHoraFim);
            fail();
        } catch (PautaException e) {
            assertEquals(format(SESSAO_FINALIZADA, pauta.getId()), e.getMessage());
        }
    }

    @Test
    void deveFecharUmaVotacaoComSucesso_QuandoADataHoraFimForMenorQueADataHoraAtual() {

        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setSituacao(Situacao.VOTACAO_ABERTA);
        pauta.setDataHoraInicio(LocalDateTime.now());
        pauta.setDataHoraFim(pauta.getDataHoraInicio().minusMinutes(1));

        when(pautaRepository.findBySituacao(Situacao.VOTACAO_ABERTA)).thenReturn(List.of(pauta));

        pautaService.fecharVotacao();

        verify(pautaRepository).save(pauta);
    }

    @Test
    void deveLancarUmaException_CasoNaoExistaUmaPautaComIdInformado() {

        long pautaId = 1L;
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.empty());

        try {
            pautaService.buscarPautaPeloId(pautaId);
            fail();
        } catch (PautaNaoEncontradaException e) {
            assertEquals(format(NAO_FOI_POSSIVEL_LOCALIZAR_UMA_PAUTA_COM_ID, pautaId), e.getMessage());
        }
    }

    @Test
    void deveRetornarOResultadoDaVotacao() throws PautaNaoEncontradaException {

        Pauta pauta = new Pauta();
        pauta.setId(1L);

        Voto votoSim = new Voto();
        votoSim.setVoto(VOTO_SIM);

        Voto votoNao1 = new Voto();
        votoNao1.setVoto(VOTO_NAO);

        Voto votoNao2 = new Voto();
        votoNao2.setVoto(VOTO_NAO);

        pauta.setVotos(Arrays.asList(votoSim, votoNao1, votoNao2));

        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        ResultadoVotacao resultadoVotacao = pautaService.resultadoVotacao(pauta.getId());
        assertEquals(1, resultadoVotacao.getQuantidadeVotosSim());
        assertEquals(2, resultadoVotacao.getQuantidadeVotosNao());
    }
}
