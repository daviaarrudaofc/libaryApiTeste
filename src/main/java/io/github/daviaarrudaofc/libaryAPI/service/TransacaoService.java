package io.github.daviaarrudaofc.libaryAPI.service;

import io.github.daviaarrudaofc.libaryAPI.model.Autor;
import io.github.daviaarrudaofc.libaryAPI.model.GeneroLivro;
import io.github.daviaarrudaofc.libaryAPI.model.Livro;
import io.github.daviaarrudaofc.libaryAPI.repository.AutorRepository;
import io.github.daviaarrudaofc.libaryAPI.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransacaoService {

    @Autowired
    AutorRepository autorRepository;
    @Autowired
    LivroRepository livroRepository;

    @Transactional
    public void salvarLivroComFoto(){

    }



    @Transactional
    public void atualizacaoSemAtualizar(){
        var livro = livroRepository
                .findById(UUID.fromString("74034080-f07f-4f0a-a2ba-87c3a72baef9"))
                .orElse(null);
        livro.setDataPublicacao(LocalDate.of(2024,6,1));

        livroRepository.save(livro);// n precisa disso pelo fato de estar usando O @Transactional
    }



    @Transactional
    public void executar(){
        // salavr o autor
        Autor autor = new Autor();
        autor.setNome("Test Francisco");
        autor.setNacionalidade("Brasileiro");
        autor.setDataNascimento(LocalDate.of(1951,1,31));
        autorRepository.save(autor);


        //salva o livro
        Livro livro = new Livro();
        livro.setIsbn("90887-84874");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Test Livro Do Francisco");
        livro.setDataPublicacao(LocalDate.of(1980,1,2));


        livro.setAutor(autor);

        livroRepository.save(livro);

        if(autor.getNome().equals("Test Francisco")){
            throw new RuntimeException("Rollback!");
        }
    }
}
