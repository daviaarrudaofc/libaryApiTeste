package io.github.daviaarrudaofc.libaryAPI.controller;

import io.github.daviaarrudaofc.libaryAPI.controller.dto.AutorDTO;
import io.github.daviaarrudaofc.libaryAPI.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/autores")
//  http://host:8080/autores
public class AutorController {

    @Autowired
    AutorService autorService;

    //ResponseEntity, ele representa todos os dados que se pode retornar da Resposta!
    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody AutorDTO autor){
        var autorEntidade = autor.mapearParaAutor();
        autorService.salvar(autorEntidade);

        // http://host:8080/autores/ewiebweib(id)
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(autorEntidade.getId())
                .toUri();

        return ResponseEntity.created(location).build();

    }
}
