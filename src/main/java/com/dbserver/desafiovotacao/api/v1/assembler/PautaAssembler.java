package com.dbserver.desafiovotacao.api.v1.assembler;

import com.dbserver.desafiovotacao.api.v1.model.PautaDto;
import com.dbserver.desafiovotacao.domain.model.Pauta;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PautaAssembler implements MapperToDtoObject<Pauta, PautaDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PautaDto toDtoObject(Pauta pauta) {
        return modelMapper.map(pauta, PautaDto.class);
    }

    @Override
    public List<PautaDto> toListDtoObject(List<Pauta> pautas) {
        return pautas.stream().map(pauta -> modelMapper.map(pauta, PautaDto.class)).toList();
    }
}
