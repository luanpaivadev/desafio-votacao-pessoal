package com.dbserver.desafiovotacao.api.v1.controller;

import com.dbserver.desafiovotacao.api.v1.assembler.PautaAssembler;
import com.dbserver.desafiovotacao.api.v1.assembler.PautaDisassembler;
import com.dbserver.desafiovotacao.api.v1.model.PautaDto;
import com.dbserver.desafiovotacao.api.v1.model.input.PautaInput;
import com.dbserver.desafiovotacao.domain.exceptions.PautaException;
import com.dbserver.desafiovotacao.domain.exceptions.PautaNaoEncontradaException;
import com.dbserver.desafiovotacao.domain.model.Pauta;
import com.dbserver.desafiovotacao.domain.repository.PautaRepository;
import com.dbserver.desafiovotacao.domain.service.PautaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/pautas")
public class PautaController {

    @Autowired
    private PautaService pautaService;
    @Autowired
    private PautaRepository pautaRepository;
    @Autowired
    private PautaAssembler pautaAssembler;
    @Autowired
    private PautaDisassembler pautaDisassembler;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PautaDto> listarPautas() {
        List<Pauta> pautas = pautaRepository.findAll();
        return pautaAssembler.toListDtoObject(pautas);
    }

    @GetMapping("/{pautaId}")
    public ResponseEntity<PautaDto> buscarPautaPeloId(@PathVariable final Long pautaId) {
        Optional<Pauta> pautaOptional = pautaRepository.findById(pautaId);
        if (pautaOptional.isPresent()) {
            PautaDto pautaDto = pautaAssembler.toDtoObject(pautaOptional.get());
            return ResponseEntity.ok(pautaDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PautaDto> salvarPauta(@RequestBody @Valid final PautaInput pautaInput) {
        Pauta pauta = pautaDisassembler.toDomainObject(pautaInput);
        pautaService.save(pauta);
        PautaDto pautaDto = pautaAssembler.toDtoObject(pauta);
        return ResponseEntity.status(HttpStatus.CREATED).body(pautaDto);
    }

    @PutMapping("/abrir-sessao")
    public ResponseEntity<?> abrirSessao(@RequestParam final Long pautaId, @RequestParam(required = false) final LocalDateTime dataHoraFim) {
        try {
            Pauta pauta = pautaService.abrirSessao(pautaId, dataHoraFim);
            PautaDto pautaDto = pautaAssembler.toDtoObject(pauta);
            return ResponseEntity.ok(pautaDto);
        } catch (PautaNaoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (PautaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
