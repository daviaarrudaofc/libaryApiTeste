package io.github.daviaarrudaofc.libaryAPI.controller;

import io.github.daviaarrudaofc.libaryAPI.controller.dto.ClientDTO;
import io.github.daviaarrudaofc.libaryAPI.controller.mappers.ClientMapper;
import io.github.daviaarrudaofc.libaryAPI.model.Client;
import io.github.daviaarrudaofc.libaryAPI.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    private final ClientMapper clientMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('GERENTE')")
    public void salvar(@RequestBody ClientDTO dto){
        var client = clientMapper.toEntity(dto);
        clientService.salvar(client);
    }
}
