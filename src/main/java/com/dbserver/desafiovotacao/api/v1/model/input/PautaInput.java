package com.dbserver.desafiovotacao.api.v1.model.input;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PautaInput {

    @NotBlank
    private String descricao;
}
