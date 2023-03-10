package com.dbserver.desafiovotacao.api.v1.controller;

import com.dbserver.desafiovotacao.api.v1.assembler.VotoAssembler;
import com.dbserver.desafiovotacao.api.v1.assembler.VotoDisassembler;
import com.dbserver.desafiovotacao.api.v1.model.input.VotoDto;
import com.dbserver.desafiovotacao.api.v1.model.input.VotoInput;
import com.dbserver.desafiovotacao.domain.exceptions.AssociadoNaoEncontradoException;
import com.dbserver.desafiovotacao.domain.exceptions.PautaNaoEncontradaException;
import com.dbserver.desafiovotacao.domain.model.Voto;
import com.dbserver.desafiovotacao.domain.repository.VotoRepository;
import com.dbserver.desafiovotacao.domain.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/votos")
public class VotoController {

    @Autowired
    private VotoService votoService;
    @Autowired
    private VotoRepository votoRepository;
    @Autowired
    private VotoAssembler votoAssembler;
    @Autowired
    private VotoDisassembler votoDisassembler;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody final VotoInput votoInput) {
        try {
            Voto voto = votoDisassembler.toDomainObject(votoInput);
            voto = votoService.save(voto);
            VotoDto votoDto = votoAssembler.toDtoObject(voto);
            return ResponseEntity.status(HttpStatus.CREATED).body(votoDto);
        } catch (AssociadoNaoEncontradoException | PautaNaoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
