package io.github.daviaarrudaofc.libaryAPI.controller.mappers;

import io.github.daviaarrudaofc.libaryAPI.controller.dto.AutorDTO;
import io.github.daviaarrudaofc.libaryAPI.model.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutorMapper {
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "dataNascimento", target = "dataNascimento")
    @Mapping(source = "nacionalidade", target = "nacionalidade")


    Autor toEntity(AutorDTO dto);

    AutorDTO toDTO(Autor autor);
}
