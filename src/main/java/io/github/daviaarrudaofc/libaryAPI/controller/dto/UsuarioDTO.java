package io.github.daviaarrudaofc.libaryAPI.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UsuarioDTO(
         @NotBlank(message = "campo Obrigatório")
         String login,
         @NotBlank(message = "campo Obrigatório")
         String senha,
         @Email(message = "E-mail inválido")
         @NotBlank(message = "campo Obrigatório")
         String email,
         List<String> roles) {
}
