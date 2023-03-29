package com.dbserver.desafiovotacao.api.v1.model.input;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AssociadoInput {

    @NotBlank
    private String nome;
    @NotBlank
    private String cpf;
}
