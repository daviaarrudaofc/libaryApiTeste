package io.github.daviaarrudaofc.libaryAPI.service;

import io.github.daviaarrudaofc.libaryAPI.exceptions.OperacaoNaoPermitidaException;
import io.github.daviaarrudaofc.libaryAPI.model.Autor;
import io.github.daviaarrudaofc.libaryAPI.repository.AutorRepository;
import io.github.daviaarrudaofc.libaryAPI.repository.LivroRepository;
import io.github.daviaarrudaofc.libaryAPI.validator.AutorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutorService {

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;

    @Autowired
    AutorValidator autorValidator;

    public Autor salvar(Autor autor){
        autorValidator.validar(autor);
        return  autorRepository.save(autor);
    }

    public void atualizar(Autor autor){
        if(autor.getId() == null){
            throw new IllegalArgumentException("Para Atualizar, é necessário que o Autor esteja salvo na base");
        }
        autorValidator.validar(autor);
        autorRepository.save(autor);
    }

    public Optional<Autor> obterPorID(UUID id){
         return autorRepository.findById(id);
    }

    public void deletar(Autor autor){
        if(possuiLivro(autor)){
            throw  new OperacaoNaoPermitidaException("" +
                    "Não é permitido excluir um Autor que  possui Livros cadastrados");
        }
        autorRepository.delete(autor);
    }

    public List<Autor> pesquisa(String nome, String nacionalidade){
        if(nome != null && nacionalidade != null){
            return autorRepository.findByNomeAndNacionalidade(nome, nacionalidade);
        }
        if(nome!= null){
            return autorRepository.findByNome(nome);
        }
        if(nacionalidade != null){
            return autorRepository.findByNacionalidade(nacionalidade);
        }
        return autorRepository.findAll();
    }

    public boolean possuiLivro(Autor autor){
        return livroRepository.existsByAutor(autor);
    }
}
