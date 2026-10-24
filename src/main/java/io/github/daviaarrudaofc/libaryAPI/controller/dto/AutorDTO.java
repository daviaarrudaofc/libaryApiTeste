package io.github.daviaarrudaofc.libaryAPI.controller.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "Autor")
public record AutorDTO(
        UUID id,
        @NotBlank(message = "campo obrigatório") // especialmente para String, falando para nao vim nula e nem vazia
        @Size(min = 2 ,max = 100, message = "campo fora do tamanho padrão")
        @Schema(name = "nome")
        String nome,
        @NotNull(message = "campo obrigatório") // é para campos que podem vim nulos
        @Past(message = "Não pode ser uma data futura")
        @Schema(name = "dataNascimento")
        LocalDate dataNascimento,
        @NotBlank(message = "campo obrigatório")
        @Size(max = 50, min = 2, message = "campo fora do tamanho padrão")
        @Schema(name = "nacionalidade")
        String nacionalidade) {

//    public Autor mapearParaAutor(){
//        Autor autor = new Autor();
//        autor.setNome(this.nome);
//        autor.setDataNascimento(this.dataNascimento);
//        autor.setNacionalidade(this.nacionalidade);
//        return autor;
//
//    }
}
