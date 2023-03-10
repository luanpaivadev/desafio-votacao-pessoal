package com.dbserver.desafiovotacao.api.v1.controller;

import com.dbserver.desafiovotacao.domain.exceptions.PautaException;
import com.dbserver.desafiovotacao.domain.exceptions.PautaNaoEncontradaException;
import com.dbserver.desafiovotacao.domain.model.Pauta;
import com.dbserver.desafiovotacao.domain.repository.PautaRepository;
import com.dbserver.desafiovotacao.domain.service.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/pautas")
public class PautaController {

    @Autowired
    private PautaService pautaService;

    @Autowired
    private PautaRepository pautaRepository;

    @GetMapping
    public ResponseEntity<List<Pauta>> findAll() {
        return ResponseEntity.ok(pautaRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Pauta> save(@RequestBody final Pauta pauta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pautaService.save(pauta));
    }

    @PutMapping("/abrir-sessao")
    public ResponseEntity<?> update(@RequestParam final Long pautaId, @RequestParam(required = false) final LocalDateTime dataHoraFim) {
        try {
            return ResponseEntity.ok(pautaService.abrirSessao(pautaId, dataHoraFim));
        } catch (PautaNaoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (PautaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
