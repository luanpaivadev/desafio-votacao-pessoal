package com.dbserver.desafiovotacao.api.v1.assembler;

import com.dbserver.desafiovotacao.api.v1.model.input.VotoInput;
import com.dbserver.desafiovotacao.domain.model.Voto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VotoDisassembler implements MapperToDomainObject<VotoInput, Voto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Voto toDomainObject(VotoInput votoInput) {
        return modelMapper.map(votoInput, Voto.class);
    }

    @Override
    public List<Voto> toListDomainObject(List<VotoInput> votoInputs) {
        return votoInputs.stream().map(votoInput -> modelMapper.map(votoInput, Voto.class)).toList();
    }
}
