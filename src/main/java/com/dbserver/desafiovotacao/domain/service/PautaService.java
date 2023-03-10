package com.dbserver.desafiovotacao.domain.service;

import com.dbserver.desafiovotacao.domain.exceptions.PautaException;
import com.dbserver.desafiovotacao.domain.exceptions.PautaNaoEncontradaException;
import com.dbserver.desafiovotacao.domain.model.Pauta;
import com.dbserver.desafiovotacao.domain.model.Situacao;
import com.dbserver.desafiovotacao.domain.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.text.MessageFormat.format;

@Service
public class PautaService {

    public static final String NAO_FOI_POSSIVEL_LOCALIZAR_UMA_PAUTA_COM_ID = "Não foi possível localizar uma pauta com id: {0}.";
    @Autowired
    private PautaRepository pautaRepository;
    @Transactional
    public Pauta save(final Pauta pauta) {
        return pautaRepository.save(pauta);
    }

    public Pauta buscarPautaPeloId(final Long pautaId) throws PautaNaoEncontradaException {
        return pautaRepository.findById(pautaId)
                .orElseThrow(() -> new PautaNaoEncontradaException(format(NAO_FOI_POSSIVEL_LOCALIZAR_UMA_PAUTA_COM_ID, pautaId)));
    }

    @Transactional
    public Pauta abrirSessao(final Long pautaId, final LocalDateTime dataHoraFim) throws PautaNaoEncontradaException, PautaException {

        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new PautaNaoEncontradaException(format(NAO_FOI_POSSIVEL_LOCALIZAR_UMA_PAUTA_COM_ID, pautaId)));

        if (Objects.equals(pauta.getSituacao(), Situacao.VOTACAO_ABERTA)) {
            throw new PautaException(format("A pauta com id: [{0}], já está aberta para votação.", pautaId));
        }

        pauta.setSituacao(Situacao.VOTACAO_ABERTA);
        pauta.setDataHoraInicio(LocalDateTime.now());
        pauta.setDataHoraFim(Objects.nonNull(dataHoraFim) ? dataHoraFim : pauta.getDataHoraInicio().plusMinutes(1));
        return save(pauta);
    }
}
