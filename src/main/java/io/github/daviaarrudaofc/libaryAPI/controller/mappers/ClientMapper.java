package io.github.daviaarrudaofc.libaryAPI.controller.mappers;

import io.github.daviaarrudaofc.libaryAPI.controller.dto.AutorDTO;
import io.github.daviaarrudaofc.libaryAPI.controller.dto.ClientDTO;
import io.github.daviaarrudaofc.libaryAPI.controller.dto.UsuarioDTO;
import io.github.daviaarrudaofc.libaryAPI.model.Autor;
import io.github.daviaarrudaofc.libaryAPI.model.Client;
import io.github.daviaarrudaofc.libaryAPI.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientDTO dto);

    ClientDTO toDTO(Client autor);
}
