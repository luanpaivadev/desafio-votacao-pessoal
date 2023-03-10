package com.dbserver.desafiovotacao.domain.service;

import com.dbserver.desafiovotacao.domain.exceptions.AssociadoNaoEncontradoException;
import com.dbserver.desafiovotacao.domain.exceptions.PautaNaoEncontradaException;
import com.dbserver.desafiovotacao.domain.model.Voto;
import com.dbserver.desafiovotacao.domain.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VotoService {

    @Autowired
    private VotoRepository votoRepository;
    @Autowired
    private AssociadoService associadoService;
    @Autowired
    private PautaService pautaService;

    @Transactional
    public Voto save(final Voto voto) throws AssociadoNaoEncontradoException, PautaNaoEncontradaException {
        associadoService.buscarAssociadoPeloId(voto.getAssociadoId());
        pautaService.buscarPautaPeloId(voto.getPautaId());
        return votoRepository.save(voto);
    }
}
