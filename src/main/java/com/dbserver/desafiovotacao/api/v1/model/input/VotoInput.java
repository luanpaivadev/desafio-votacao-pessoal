package com.dbserver.desafiovotacao.api.v1.model.input;

import lombok.Data;

@Data
public class VotoInput {

    private Long associadoId;
    private String voto;
    private Long pautaId;

}
