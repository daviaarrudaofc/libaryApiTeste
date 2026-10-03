package io.github.daviaarrudaofc.libaryAPI.service;

import io.github.daviaarrudaofc.libaryAPI.model.GeneroLivro;
import io.github.daviaarrudaofc.libaryAPI.model.Livro;
import io.github.daviaarrudaofc.libaryAPI.repository.LivroRepository;
import io.github.daviaarrudaofc.libaryAPI.repository.specs.LivroSpecs;
import io.github.daviaarrudaofc.libaryAPI.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import static io.github.daviaarrudaofc.libaryAPI.repository.specs.LivroSpecs.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository  livroRepository;
    private LivroValidator livroValidator;

    public Livro salvar(Livro livro) {
         livroValidator.validar(livro);
        return livroRepository.save(livro);
    }

    public Optional<Livro> obterPorId(UUID id){
        return livroRepository.findById(id);
    }

    public void deletar(Livro livro){
        livroRepository.delete(livro);
    }

    //isbn,titulo, nome autor, genero, ano publicacao
    public List<Livro> pesquisa(String isbn,String titulo, String nomeAutor, GeneroLivro genero, Integer anoPublicacao){

        //select * from livro where isbn = :isbn and nomeAutor = :
//        Specification<Livro> specs = Specification
//                .where(LivroSpecs.isbnEqual(isbn))
//                .and(LivroSpecs.tituloLike(titulo))
//                .and(LivroSpecs.generoEqual(genero));

        //select * from livro  where 0 = 0
        Specification<Livro> specs = Specification.where((root, query, cb) -> cb.conjunction());
        if(isbn != null){
            // query = query and isbn = :isbn
            specs = specs.and(LivroSpecs.isbnEqual(isbn));
        }
        if(titulo != null){
            specs = specs .and(tituloLike(titulo));
        }
        if(genero != null){
            specs = specs .and(generoEqual(genero));
        }if(anoPublicacao != null){
            specs = specs.and(anoPublicacaoEqual(anoPublicacao));
        }if(nomeAutor != null){
            specs= specs.and(nomeAutorLike(nomeAutor));
        }



        return livroRepository.findAll(specs);


    }

    public void atualizar(Livro livro) {
        if(livro.getId() == null){
            throw new IllegalArgumentException("Para Atualizar, é necessário que o Livro esteja salvo na base");
        }
        livroValidator.validar(livro);
        livroRepository.save(livro);
    }
}
