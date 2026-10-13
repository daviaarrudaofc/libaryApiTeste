package io.github.daviaarrudaofc.libaryAPI.controller.mappers;

import io.github.daviaarrudaofc.libaryAPI.controller.dto.UsuarioDTO;
import io.github.daviaarrudaofc.libaryAPI.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO dto);
}
