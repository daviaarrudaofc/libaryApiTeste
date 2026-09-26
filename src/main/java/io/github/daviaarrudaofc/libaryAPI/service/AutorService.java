package io.github.daviaarrudaofc.libaryAPI.service;

import io.github.daviaarrudaofc.libaryAPI.model.Autor;
import io.github.daviaarrudaofc.libaryAPI.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorService {

    @Autowired
    AutorRepository autorRepository;

    public Autor salvar(Autor autor){
        return  autorRepository.save(autor);
    }
}
