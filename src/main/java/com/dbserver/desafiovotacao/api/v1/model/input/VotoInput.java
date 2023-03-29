package com.dbserver.desafiovotacao.api.v1.model.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotoInput {

    @NotNull
    private Long associadoId;
    @NotBlank
    private String voto;
    @NotNull
    private Long pautaId;

}
