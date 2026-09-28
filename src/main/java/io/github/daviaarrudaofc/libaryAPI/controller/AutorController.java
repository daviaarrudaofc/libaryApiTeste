package io.github.daviaarrudaofc.libaryAPI.controller;

import io.github.daviaarrudaofc.libaryAPI.controller.dto.AutorDTO;
import io.github.daviaarrudaofc.libaryAPI.controller.dto.ErroResposta;
import io.github.daviaarrudaofc.libaryAPI.exceptions.OperacaoNaoPermitidaException;
import io.github.daviaarrudaofc.libaryAPI.exceptions.RegistroDuplicadoException;
import io.github.daviaarrudaofc.libaryAPI.model.Autor;
import io.github.daviaarrudaofc.libaryAPI.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/autores")
//  http://host:8080/autores
public class AutorController {

    @Autowired
    AutorService autorService;

    //ResponseEntity, ele representa todos os dados que se pode retornar da Resposta!
    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody AutorDTO autor){
        try {
            var autorEntidade = autor.mapearParaAutor();
            autorService.salvar(autorEntidade);

            // http://host:8080/autores/ewiebweib(id)
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(autorEntidade.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        }catch (RegistroDuplicadoException e){
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return  ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id){
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorID(idAutor);
        if(autorOptional.isPresent()){
            Autor autor = autorOptional.get();
            AutorDTO dto = new AutorDTO(
                    autor.getId(),
                    autor.getNome(),
                    autor.getDataNascimento(),
                    autor.getNacionalidade());
            return ResponseEntity.ok(dto);// n precisa do body
        }
        return ResponseEntity.notFound().build();
    }

    // indempotente
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") String id){
        try {


            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = autorService.obterPorID(idAutor);
            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            autorService.deletar(autorOptional.get());
            return ResponseEntity.noContent().build();
        }catch (OperacaoNaoPermitidaException e){
            var erroResposta = ErroResposta.respostaPadrao(e.getMessage());
           return ResponseEntity.status(erroResposta.status()).body(erroResposta);
        }
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade){
        List<Autor> resultado = autorService.pesquisa(nome, nacionalidade);
        List<AutorDTO> lista = resultado
                .stream()
                .map(autor -> new AutorDTO(
                        autor.getId(),
                        autor.getNome(),
                        autor.getDataNascimento(),
                        autor.getNacionalidade())
                ).collect(Collectors.toList());
        return ResponseEntity.ok(lista);

    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizar(
            @PathVariable("id") String id, @RequestBody AutorDTO dto){
        try {


            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = autorService.obterPorID(idAutor);
            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var autorDto = autorOptional.get();
            autorDto.setNome(dto.nome());
            autorDto.setNacionalidade(dto.nacionalidade());
            autorDto.setDataNascimento(dto.dataNascimento());
            autorService.atualizar(autorDto);

            return ResponseEntity.noContent().build();
        }catch (RegistroDuplicadoException e){
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return  ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }

    }




}
