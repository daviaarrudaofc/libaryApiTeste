package io.github.daviaarrudaofc.libaryAPI.controller;

import io.github.daviaarrudaofc.libaryAPI.controller.dto.UsuarioDTO;
import io.github.daviaarrudaofc.libaryAPI.controller.mappers.UsuarioMapper;
import io.github.daviaarrudaofc.libaryAPI.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuário")
public class UsuarioController {

    private  final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Salvar", description = "Cadastrar Usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201",description = "cadastrado com sucesso"),
            @ApiResponse(responseCode = "433",description = "usuário não encontrado"),
            @ApiResponse(responseCode = "409",description = "usuário já cadastrado")
    })
    public void salvar(@RequestBody @Valid UsuarioDTO dto){
        var usuario = usuarioMapper.toEntity(dto);
        usuarioService.salvar(usuario);
    }
}
