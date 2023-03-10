package com.dbserver.desafiovotacao.api.v1.model;

import com.dbserver.desafiovotacao.domain.model.Associado;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VotoDto {

    private Associado associado;
    private LocalDateTime dataHoraVoto;
    private String voto;
    private PautaDto pauta;
}
