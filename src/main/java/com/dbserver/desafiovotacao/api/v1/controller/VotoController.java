package com.dbserver.desafiovotacao.api.v1.controller;

import com.dbserver.desafiovotacao.api.v1.assembler.VotoAssembler;
import com.dbserver.desafiovotacao.api.v1.assembler.VotoDisassembler;
import com.dbserver.desafiovotacao.api.v1.model.VotoDto;
import com.dbserver.desafiovotacao.api.v1.model.input.VotoInput;
import com.dbserver.desafiovotacao.domain.exceptions.AssociadoNaoEncontradoException;
import com.dbserver.desafiovotacao.domain.exceptions.PautaNaoEncontradaException;
import com.dbserver.desafiovotacao.domain.model.Voto;
import com.dbserver.desafiovotacao.domain.repository.VotoRepository;
import com.dbserver.desafiovotacao.domain.service.VotoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VotoDto> listarVotos() {
        List<Voto> votos = votoRepository.findAll();
        return votoAssembler.toListDtoObject(votos);
    }

    @GetMapping("/{votoId}")
    public ResponseEntity<VotoDto> buscarVotoPeloId(@PathVariable final Long votoId) {
        Optional<Voto> votoOptional = votoRepository.findById(votoId);
        if (votoOptional.isPresent()) {
            VotoDto votoDto = votoAssembler.toDtoObject(votoOptional.get());
            return ResponseEntity.ok(votoDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> salvarVoto(@RequestBody @Valid final VotoInput votoInput) {
        try {
            Voto voto = votoService.salvarVoto(votoInput);
            VotoDto votoDto = votoAssembler.toDtoObject(voto);
            return ResponseEntity.status(HttpStatus.CREATED).body(votoDto);
        } catch (AssociadoNaoEncontradoException | PautaNaoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
