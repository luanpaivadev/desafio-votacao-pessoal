package com.dbserver.desafiovotacao.api.v1.model.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VotoInput {

    @NotNull
    private Long associadoId;
    @NotBlank
    private String voto;
    @NotNull
    private Long pautaId;

}
