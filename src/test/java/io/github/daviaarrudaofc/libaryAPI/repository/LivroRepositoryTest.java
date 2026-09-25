package io.github.daviaarrudaofc.libaryAPI.repository;

import io.github.daviaarrudaofc.libaryAPI.model.Autor;
import io.github.daviaarrudaofc.libaryAPI.model.GeneroLivro;
import io.github.daviaarrudaofc.libaryAPI.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class LivroRepositoryTest {
    @Autowired
    LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;

    // forma sem ser cascata
    @Test
    public void salvarTest(){
        Livro livro = new Livro();
        livro.setIsbn("90887-84874");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Outro livro");
        livro.setDataPublicacao(LocalDate.of(1980,1,2));

        Autor autor = autorRepository
                .findById(UUID.fromString("0fce1d0b-e1ed-43f7-af04-f76e037cba15"))
                .orElse(null);


        livro.setAutor(autor);

        livroRepository.save(livro);
    }
    @Test
    public void salvaAutorELivroTest(){
        Livro livro = new Livro();
        livro.setIsbn("90887-84874");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Terceiro livro");
        livro.setDataPublicacao(LocalDate.of(1980,1,2));

        Autor autor = new Autor();
        autor.setNome("Jose");
        autor.setNacionalidade("Brasilerio");
        autor.setDataNascimento(LocalDate.of(1951,1,31));

        autorRepository.save(autor);

        livro.setAutor(autor);

        livroRepository.save(livro);
    }


    @Test
    public void salvarCascadeTest(){
        Livro livro = new Livro();
        livro.setIsbn("90887-84874");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Outro livro");
        livro.setDataPublicacao(LocalDate.of(1980,1,2));

        Autor autor = new Autor();
        autor.setNome("Joao");
        autor.setNacionalidade("Brasilerio");
        autor.setDataNascimento(LocalDate.of(1951,1,31));

        livro.setAutor(autor);

        livroRepository.save(livro);
    }

    @Test
    public void atualizarAutorDoLivro(){
        UUID id = UUID.fromString("c2319c3b-4d51-43c2-bde4-44b2cc315961");
        var livroParaAtualizar = livroRepository.findById(id)
                .orElse(null);

        UUID idAutor = UUID.fromString("0fce1d0b-e1ed-43f7-af04-f76e037cba15");
        Autor maria = autorRepository.findById(idAutor).orElse(null);

        livroParaAtualizar.setAutor(maria);

        livroRepository.save(livroParaAtualizar);
    }
    @Test
    public void deletar(){
        UUID id = UUID.fromString("c2319c3b-4d51-43c2-bde4-44b2cc315961");
        livroRepository.deleteById(id);
    }


    @Test
    public void deletarCascade(){
        UUID id = UUID.fromString("eace85d9-9cc9-44f5-a05a-4941fd04c1df");
        livroRepository.deleteById(id);
    }

    @Test
    @Transactional // a do spring,abriu uma janela para fazer operações do banco e so vai fechar a execu do method
    public void  buscarLivroTest(){
        UUID id = UUID.fromString("74034080-f07f-4f0a-a2ba-87c3a72baef9");
        Livro livro = livroRepository.findById(id).orElse(null);
        System.out.println("Livro: ");
        System.out.println(livro.getTitulo());

        System.out.println("Autor: ");
        System.out.println(livro.getAutor().getNome());
    }

    @Test
    void perquisarPorTituloTest(){
        List<Livro> lista = livroRepository.findByTitulo("O roubo da casa assombrada");
        lista.forEach(System.out::println);

    }

    @Test
    void pesquisarPorISBNTest(){
        List<Livro> listaIsbn = livroRepository.findByIsbn("20847-84874");
        //System.out.println("Livro achado de acordo com ISBN: "+ listaIsbn); - se fosse por objeto,mas vou fazer por lista
        listaIsbn.forEach(System.out::println);
    }

    @Test
    void pesquisarPorTituloEPreco(){
        String titulo = "O roubo da casa assombrada";
        BigDecimal preco = BigDecimal.valueOf(650);

        List<Livro> listaIsbn = livroRepository.findByTituloAndPreco(titulo, preco);
        listaIsbn.forEach(System.out::println);
    }
    @Test
    void listarLivrosComQueryJPQL(){
        var resultado = livroRepository.listarTodosOrdenadoPorTituloAndPreco();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarAutoresDoLivroComQueryJPQL(){
        var resultado = livroRepository.listarAutoresDosLivros();
        resultado.forEach(System.out::println);
    }
    @Test
    void listarTitulosNaoRepetidosComQueryJPQL(){
        var resultado = livroRepository.listarNomesDiferentesLivros();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarGenerosDeLivrosAutoresBrasileirosComQueryJPQL(){
        var resultado = livroRepository.listarGenerosAutoresBrasileiros();
        resultado.forEach(System.out::println);
    }


}
