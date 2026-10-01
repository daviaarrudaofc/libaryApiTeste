package io.github.daviaarrudaofc.libaryAPI.controller;

import io.github.daviaarrudaofc.libaryAPI.controller.dto.CadastroLivroDTO;
import io.github.daviaarrudaofc.libaryAPI.controller.dto.ErroResposta;
import io.github.daviaarrudaofc.libaryAPI.controller.mappers.LivroMapper;
import io.github.daviaarrudaofc.libaryAPI.exceptions.RegistroDuplicadoException;
import io.github.daviaarrudaofc.libaryAPI.model.Livro;
import io.github.daviaarrudaofc.libaryAPI.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
public class LivroController implements GenericController{

    private final LivroService livroService;
    private final LivroMapper livroMapper;

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody @Valid CadastroLivroDTO dto){
        try{
            //mapear dto para entidade
            Livro livro = livroMapper.toEntity(dto);
            //enviar entidade para o service validar e salvar na base
            livroService.salvar(livro);
            // criar url para acesso dos dados do livro
            var url =gerarHeaderLocation(livro.getId());
            //retornar codigo created com header location
            return ResponseEntity.created(url).build();

        }catch (RegistroDuplicadoException e){
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }
}
