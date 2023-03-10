package com.dbserver.desafiovotacao.domain.service;

import com.dbserver.desafiovotacao.api.v1.model.input.VotoInput;
import com.dbserver.desafiovotacao.domain.exceptions.AssociadoNaoEncontradoException;
import com.dbserver.desafiovotacao.domain.exceptions.PautaException;
import com.dbserver.desafiovotacao.domain.exceptions.PautaNaoEncontradaException;
import com.dbserver.desafiovotacao.domain.model.Associado;
import com.dbserver.desafiovotacao.domain.model.Pauta;
import com.dbserver.desafiovotacao.domain.model.Situacao;
import com.dbserver.desafiovotacao.domain.model.Voto;
import com.dbserver.desafiovotacao.domain.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class VotoService {

    public static final String SESSAO_NAO_ABERTA = "A sessão para esta pauta ainda não aberta";
    public static final String SESSAO_FINALIZADA = "A sessão para esta pauta já foi finalizada.";
    @Autowired
    private VotoRepository votoRepository;
    @Autowired
    private AssociadoService associadoService;
    @Autowired
    private PautaService pautaService;

    @Transactional
    public Voto salvarVoto(final VotoInput votoInput)
            throws AssociadoNaoEncontradoException, PautaNaoEncontradaException, PautaException {

        Voto voto = new Voto();
        Pauta pauta = pautaService.buscarPautaPeloId(votoInput.getPautaId());

        validarSituacaoPauta(pauta);

        Associado associado = associadoService.buscarAssociadoPeloId(votoInput.getAssociadoId());

        voto.setAssociado(associado);
        voto.setPauta(pauta);
        voto.setVoto(votoInput.getVoto());
        voto = votoRepository.save(voto);

        pauta.getVotos().add(voto);
        pautaService.save(pauta);

        return voto;
    }

    private static void validarSituacaoPauta(Pauta pauta) throws PautaException {
        if (Objects.equals(pauta.getSituacao(), Situacao.VOTACAO_FECHADA) &&
                Objects.isNull(pauta.getDataHoraInicio())) {
            throw new PautaException(SESSAO_NAO_ABERTA);
        }

        if (Objects.equals(pauta.getSituacao(), Situacao.VOTACAO_FECHADA) &&
                Objects.nonNull(pauta.getDataHoraInicio())) {
            throw new PautaException(SESSAO_FINALIZADA);
        }
    }
}
