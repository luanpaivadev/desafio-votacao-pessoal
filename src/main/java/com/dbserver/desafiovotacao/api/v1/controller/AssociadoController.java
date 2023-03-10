package com.dbserver.desafiovotacao.api.v1.controller;

import com.dbserver.desafiovotacao.api.v1.assembler.AssociadoAssembler;
import com.dbserver.desafiovotacao.api.v1.assembler.AssociadoDisassembler;
import com.dbserver.desafiovotacao.api.v1.model.AssociadoDto;
import com.dbserver.desafiovotacao.api.v1.model.input.AssociadoInput;
import com.dbserver.desafiovotacao.domain.model.Associado;
import com.dbserver.desafiovotacao.domain.repository.AssociadoRepository;
import com.dbserver.desafiovotacao.domain.service.AssociadoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/v1/associados")
public class AssociadoController {

    public static final String CPF_INVALIDO = "CPF inválido.";
    @Autowired
    private AssociadoService associadoService;
    @Autowired
    private AssociadoRepository associadoRepository;
    @Autowired
    private AssociadoAssembler associadoAssembler;
    @Autowired
    private AssociadoDisassembler associadoDisassembler;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AssociadoDto> listarAssociados() {
        List<Associado> associados = associadoRepository.findAll();
        return associadoAssembler.toListDtoObject(associados);
    }

    @GetMapping("/{associadoId}")
    public ResponseEntity<AssociadoDto> buscarAssociadoPeloId(@PathVariable final Long associadoId) {
        Optional<Associado> associadoOptional = associadoRepository.findById(associadoId);
        if (associadoOptional.isPresent()) {
            AssociadoDto associadoDto = associadoAssembler.toDtoObject(associadoOptional.get());
            return ResponseEntity.ok(associadoDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> salvarAssociado(@RequestBody @Valid final AssociadoInput associadoInput) {
        if (!associadoService.validarCPF(associadoInput.getCpf())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CPF_INVALIDO);
        }
        Associado associado = associadoDisassembler.toDomainObject(associadoInput);
        AssociadoDto associadoDto = associadoAssembler.toDtoObject(associadoService.save(associado));
        return ResponseEntity.status(HttpStatus.CREATED).body(associadoDto);
    }
}
