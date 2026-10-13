package io.github.daviaarrudaofc.libaryAPI.controller.dto;

import java.util.List;

public record UsuarioDTO(String login, String senha, List<String> roles) {
}
