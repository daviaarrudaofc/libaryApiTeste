package io.github.daviaarrudaofc.libaryAPI.controller;

import io.github.daviaarrudaofc.libaryAPI.controller.dto.CadastroLivroDTO;
import io.github.daviaarrudaofc.libaryAPI.controller.dto.ErroResposta;
import io.github.daviaarrudaofc.libaryAPI.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.daviaarrudaofc.libaryAPI.controller.mappers.LivroMapper;
import io.github.daviaarrudaofc.libaryAPI.exceptions.RegistroDuplicadoException;
import io.github.daviaarrudaofc.libaryAPI.model.GeneroLivro;
import io.github.daviaarrudaofc.libaryAPI.model.Livro;
import io.github.daviaarrudaofc.libaryAPI.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("livros")
//@PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')") aq tb vai, mas para toda a classe
@RequiredArgsConstructor
@Tag(name = "Livros")
public class LivroController implements GenericController{

    private final LivroService livroService;
    private final LivroMapper livroMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Salvar", description = "Cadastrar novo Livro")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "cadastrado com sucesso"),
            @ApiResponse(responseCode = "409",description = "Livro já cadastrado")
    })
    public ResponseEntity<Void> criar(@RequestBody @Valid CadastroLivroDTO dto){ // void agr, pois nao retornamos nd agr
        //mapear dto para entidade
        Livro livro = livroMapper.toEntity(dto);
        //enviar entidade para o service validar e salvar na base
        livroService.salvar(livro);
        // criar url para acesso dos dados do livro
        var url = gerarHeaderLocation(livro.getId());
        //retornar codigo created com header location
        return ResponseEntity.created(url).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Obter Detalhes", description = "Obter detalhes Livro")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Livro encontrado"),
            @ApiResponse(responseCode = "404",description = "Livro não encntrado")
    })
    public  ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(@PathVariable("id")String id){
            return livroService.obterPorId(UUID.fromString(id))
                    .map(livro -> {
                        var dto = livroMapper.toDTO(livro);
                        return  ResponseEntity.ok(dto);
                    }).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Deletar", description = "Deletar Livro")
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "deletado com sucesso"),
            @ApiResponse(responseCode = "404",description = "Livro não encntrado")
    })
    public ResponseEntity<Object> deletar(@PathVariable("id") String id){
        return livroService.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    livroService.deletar(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Pesquisar", description = "Pesquisar Livro")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Livro encontrado"),
    })
    public ResponseEntity<Page<ResultadoPesquisaLivroDTO>> pesquisa(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "titulo", required = false)
            String titulo,
            @RequestParam(value = "nome-autor", required = false)
            String nomeAutor,
            @RequestParam(value = "genero", required = false)
            GeneroLivro genero,
            @RequestParam(value = "ano-publicacao", required = false)
            Integer anoPublicacao,
            @RequestParam(value = "pagina", defaultValue = "0")
            Integer pagina,
            @RequestParam(value = "tamanho-pagina", defaultValue = "10")
            Integer tamanhoPagina
    ){
        Page<Livro> paginaResultado = livroService.pesquisa(isbn, titulo, nomeAutor, genero, anoPublicacao,pagina,tamanhoPagina);

        Page<ResultadoPesquisaLivroDTO> resultado = paginaResultado.map(livroMapper::toDTO);

//        var lista = resultado
//                .stream()
//                .map(livroMapper::toDTO)
//                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);

    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Atualizar", description = "Atualizar Livro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "atualizado com sucesso"),
            @ApiResponse(responseCode = "404",description = "livro não encontrado"),
            @ApiResponse(responseCode = "409",description = "livro já cadastrado")
    })
    public ResponseEntity<Object> atualizar(
            @PathVariable("id") String id, @RequestBody @Valid  CadastroLivroDTO dto){
        return livroService.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    Livro entidadeAux = livroMapper.toEntity(dto);

                    livro.setDataPublicacao(entidadeAux.getDataPublicacao());
                    livro.setIsbn(entidadeAux.getIsbn());
                    livro.setPreco(entidadeAux.getPreco());
                    livro.setGenero(entidadeAux.getGenero());
                    livro.setTitulo(entidadeAux.getTitulo());
                    livro.setAutor(entidadeAux.getAutor());

                    livroService.atualizar(livro);
                    return  ResponseEntity.noContent().build();

                }).orElseGet(() -> ResponseEntity.notFound().build());

    }

}
