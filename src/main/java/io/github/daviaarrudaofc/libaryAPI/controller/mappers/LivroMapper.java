package io.github.daviaarrudaofc.libaryAPI.controller.mappers;

import io.github.daviaarrudaofc.libaryAPI.controller.dto.CadastroLivroDTO;
import io.github.daviaarrudaofc.libaryAPI.model.Livro;
import io.github.daviaarrudaofc.libaryAPI.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class LivroMapper {
    @Autowired
    AutorRepository autorRepository;

    @Mapping(target = "autor", expression = "java( autorRepository.findById(dto.idAutor()).orElse(null) )")
    public abstract Livro toEntity(CadastroLivroDTO dto);


}
