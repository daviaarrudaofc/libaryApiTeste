package io.github.daviaarrudaofc.libaryAPI.controller;

import io.github.daviaarrudaofc.libaryAPI.controller.dto.AutorDTO;
import io.github.daviaarrudaofc.libaryAPI.controller.dto.ErroResposta;
import io.github.daviaarrudaofc.libaryAPI.controller.mappers.AutorMapper;
import io.github.daviaarrudaofc.libaryAPI.exceptions.OperacaoNaoPermitidaException;
import io.github.daviaarrudaofc.libaryAPI.exceptions.RegistroDuplicadoException;
import io.github.daviaarrudaofc.libaryAPI.model.Autor;
import io.github.daviaarrudaofc.libaryAPI.model.Usuario;
import io.github.daviaarrudaofc.libaryAPI.security.SecurityService;
import io.github.daviaarrudaofc.libaryAPI.service.AutorService;
import io.github.daviaarrudaofc.libaryAPI.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
//  http://host:8080/autores
@Tag(name = "Autores")
public class AutorController implements GenericController {

    private final AutorMapper autorMapper;

    @Autowired
    AutorService autorService;

    //ResponseEntity, ele representa todos os dados que se pode retornar da Resposta!
    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Salvar", description = "Cadastrar novo autor")
    @ApiResponses({
            @ApiResponse(responseCode = "201",description = "cadastrado com sucesso"),
            @ApiResponse(responseCode = "433",description = "erro de validaçãp"),
            @ApiResponse(responseCode = "409",description = "autor já cadastrado")
    })
    public ResponseEntity<Void> salvar(@RequestBody @Valid AutorDTO autorDTO){
        //antes
        //       var autorEntidade = autor.mapearParaAutor();
        //    autorService.salvar(autorEntidade);

        //UserDetails usuarioLogado = (UserDetails) authentication.getPrincipal(); --antes
        //Usuario usuario = usuarioService.obterPorLogin(usuarioLogado.getUsername());

        Autor autor = autorMapper.toEntity(autorDTO);
        autorService.salvar(autor);

        // http://host:8080/autores/ewiebweib(id)
        URI location = gerarHeaderLocation(autor.getId());

        return ResponseEntity.created(location).build();

    }


    @PreAuthorize("hasAnyRole('GERENTE','OPERADOR')")
    @GetMapping("{id}")
    @Operation(summary = "Obter Detalhes", description = "Retorna os dados do autor pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Autor encontrado"),
            @ApiResponse(responseCode = "404",description = "Autor não Encontrado   ")
    })
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id){
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorID(idAutor);

        return autorService
                .obterPorID(idAutor)
                .map(autor -> {
                    AutorDTO dto = autorMapper.toDTO(autor);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());

//        if(autorOptional.isPresent()){
//            Autor autor = autorOptional.get();
//            AutorDTO dto = autorMapper.toDTO(autor); //new AutorDTO(
//                    //autor.getId(),
//                    //autor.getNome(),
//                    //autor.getDataNascimento(),
//                    //autor.getNacionalidade());
//            return ResponseEntity.ok(dto);// n precisa do body
//        }
//        return ResponseEntity.notFound().build();
    }

    // indempotente
    @PreAuthorize("hasRole('GERENTE')")
    @DeleteMapping("{id}")
    @Operation(summary = "Deletar", description = "Deleta um autor existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "deletado com sucesso"),
            @ApiResponse(responseCode = "404",description = "autor não encontrado"),
            @ApiResponse(responseCode = "400",description = "autor possui livro cadastrado")
    })
    public ResponseEntity<Void> deletar(@PathVariable("id") String id){
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorID(idAutor);
        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        autorService.deletar(autorOptional.get());
        return ResponseEntity.noContent().build();

    }

    @PreAuthorize("hasAnyRole('GERENTE','OPERADOR')")
    @GetMapping
    @Operation(summary = "Pesquisar", description = "Realiza pesuisa de autores por parametros")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "sucesso")
    })
    public ResponseEntity<List<AutorDTO>> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade){
        List<Autor> resultado = autorService.pesquisaByExample(nome, nacionalidade);
        List<AutorDTO> lista = resultado
                .stream()
                .map(autorMapper::toDTO
                ).collect(Collectors.toList());
        return ResponseEntity.ok(lista);

                // versao antiga
//                .map(autor -> new AutorDTO(
//                        autor.getId(),
//                        autor.getNome(),
//                        autor.getDataNascimento(),
//                        autor.getNacionalidade())
//                ).collect(Collectors.toList());
        //  return ResponseEntity.ok(lista);
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PutMapping("{id}")
    @Operation(summary = "Atualizar", description = "Atualiza um autor existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "atualizado com sucesso"),
            @ApiResponse(responseCode = "404",description = "autor não encontrado"),
            @ApiResponse(responseCode = "409",description = "autor já cadastrado")
    })
    public ResponseEntity<Void> atualizar(
            @PathVariable("id") String id,  @Valid @RequestBody AutorDTO dto){
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


    }




}
