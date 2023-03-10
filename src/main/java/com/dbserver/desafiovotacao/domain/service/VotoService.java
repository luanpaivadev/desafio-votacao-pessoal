package com.dbserver.desafiovotacao.domain.service;

import com.dbserver.desafiovotacao.api.v1.model.input.VotoInput;
import com.dbserver.desafiovotacao.domain.exceptions.AssociadoNaoEncontradoException;
import com.dbserver.desafiovotacao.domain.exceptions.PautaNaoEncontradaException;
import com.dbserver.desafiovotacao.domain.model.Associado;
import com.dbserver.desafiovotacao.domain.model.Pauta;
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
    public Voto salvarVoto(final VotoInput votoInput) throws AssociadoNaoEncontradoException, PautaNaoEncontradaException {

        Voto voto = new Voto();
        Associado associado = associadoService.buscarAssociadoPeloId(votoInput.getAssociadoId());
        Pauta pauta = pautaService.buscarPautaPeloId(votoInput.getPautaId());

        voto.setAssociado(associado);
        voto.setPauta(pauta);
        voto.setVoto(votoInput.getVoto());
        voto = votoRepository.save(voto);

        pauta.getVotos().add(voto);
        pautaService.save(pauta);

        return voto;
    }
}
