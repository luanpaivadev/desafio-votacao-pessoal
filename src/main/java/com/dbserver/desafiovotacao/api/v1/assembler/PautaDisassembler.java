package com.dbserver.desafiovotacao.api.v1.assembler;

import com.dbserver.desafiovotacao.api.v1.model.input.PautaInput;
import com.dbserver.desafiovotacao.domain.model.Pauta;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PautaDisassembler implements MapperToDomainObject<PautaInput, Pauta> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Pauta toDomainObject(PautaInput pautaInput) {
        return modelMapper.map(pautaInput, Pauta.class);
    }

    @Override
    public List<Pauta> toListDomainObject(List<PautaInput> pautaInputList) {
        return pautaInputList.stream().map(pautaInput -> modelMapper.map(pautaInput, Pauta.class)).toList();
    }
}
