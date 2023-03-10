package com.dbserver.desafiovotacao.api.v1.controller;

import com.dbserver.desafiovotacao.api.v1.assembler.AssociadoAssembler;
import com.dbserver.desafiovotacao.api.v1.assembler.AssociadoDisassembler;
import com.dbserver.desafiovotacao.api.v1.model.AssociadoDto;
import com.dbserver.desafiovotacao.api.v1.model.input.AssociadoInput;
import com.dbserver.desafiovotacao.domain.service.AssociadoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/associados")
public class AssociadoController {

    public static final String CPF_INVALIDO = "CPF inválido.";
    @Autowired
    private AssociadoService associadoService;
    @Autowired
    private AssociadoAssembler associadoAssembler;
    @Autowired
    private AssociadoDisassembler associadoDisassembler;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid final AssociadoInput associadoInput) {
        if (!associadoService.validarCPF(associadoInput.getCpf())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CPF_INVALIDO);
        }
        final var associado = associadoDisassembler.toDomainObject(associadoInput);
        final var associadoDto = associadoAssembler.toDtoObject(associadoService.save(associado));
        return ResponseEntity.status(HttpStatus.CREATED).body(associadoDto);
    }
}
