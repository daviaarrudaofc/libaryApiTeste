package io.github.daviaarrudaofc.libaryAPI.controller.dto;

import io.github.daviaarrudaofc.libaryAPI.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(
        UUID id,
        @NotBlank(message = "campo obrigatório") // especialmente para String, falando para nao vim nula e nem vazia
        String nome,
        @NotNull(message = "campo obrigatório") // é para campos que podem vim nulos
        LocalDate dataNascimento,
        @NotBlank(message = "campo obrigatório")
        String nacionalidade) {

    public Autor mapearParaAutor(){
        Autor autor = new Autor();
        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNacionalidade(this.nacionalidade);
        return autor;

    }
}
