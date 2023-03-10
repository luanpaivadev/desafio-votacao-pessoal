package com.dbserver.desafiovotacao.api.v1.assembler;

import com.dbserver.desafiovotacao.api.v1.model.AssociadoDto;
import com.dbserver.desafiovotacao.domain.model.Associado;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssociadoAssembler implements MapperToDtoObject<Associado, AssociadoDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AssociadoDto toDtoObject(Associado associado) {
        return modelMapper.map(associado, AssociadoDto.class);
    }

    @Override
    public List<AssociadoDto> toListDtoObject(List<Associado> associados) {
        return associados.stream().map(associado -> modelMapper.map(associado, AssociadoDto.class)).toList();
    }
}
