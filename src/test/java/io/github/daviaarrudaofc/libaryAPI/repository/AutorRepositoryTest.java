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
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {

    @Autowired
    AutorRepository repository;

    @Autowired
    LivroRepository livroRepository;


    @Test
    public void salvarTest(){
        Autor autor = new Autor();
        autor.setNome("Maria");
        autor.setNacionalidade("Brasilerio");
        autor.setDataNascimento(LocalDate.of(1951,1,30));

        var autorSalvo = repository.save(autor);
        System.out.println("Autor Salvo: "+ autorSalvo);
    }
    @Test
    public void atualizarTest(){
        var id = UUID.fromString("97e267eb-f7e5-4345-b3c7-4f7b4ecff568");
       Optional<Autor> possivelAutor = repository.findById(id);

       if(possivelAutor.isPresent()){

           Autor autorEncontrado= possivelAutor.get();
           System.out.println("Dados autor: ");
           System.out.println(autorEncontrado);

           autorEncontrado.setDataNascimento(LocalDate.of(1960,1,30));

           repository.save(autorEncontrado);
       }
    }
    @Test
    public void listarTeste(){
        List<Autor> lista = repository.findAll();
        lista.forEach(System.out::println);
    }
    @Test
    public void countTest(){
        System.out.println("Contagem de autores: "+ repository.count());
    }

    @Test
    public void deletePorIdTest(){
        var id = UUID.fromString("97e267eb-f7e5-4345-b3c7-4f7b4ecff568");
        repository.deleteById(id);
    }

    @Test
    public void deleteTest(){
        var id = UUID.fromString("7473ad94-258f-4adb-8931-74b4d4a27aa7");
        var maria = repository.findById(id).get();
        repository.delete(maria);
    }
    @Test
    void salvarAutorComLivroTest(){
        Autor autor = new Autor();
        autor.setNome("Antonio");
        autor.setNacionalidade("Americano");
        autor.setDataNascimento(LocalDate.of(1970,8,5));

        Livro livro = new Livro();
        livro.setIsbn("20847-84874");
        livro.setPreco(BigDecimal.valueOf(204));
        livro.setGenero(GeneroLivro.MISTERIO);
        livro.setTitulo("O roubo da casa assombrada");
        livro.setDataPublicacao(LocalDate.of(1999,1,2));
        livro.setAutor(autor);

        Livro livro2 = new Livro();
        livro2.setIsbn("99999-84874");
        livro2.setPreco(BigDecimal.valueOf(650));
        livro2.setGenero(GeneroLivro.MISTERIO);
        livro2.setTitulo("O roubo da casa assombrada");

    }
    @Test
    void listarLivrosAutor(){
        var id = UUID.fromString("5e8213d5-fd96-4c5f-86ed-a1633863d0db");
        Autor autor = repository.findById(id).get();
        //buscar os livros do autir
        List<Livro> livrosLista = livroRepository.findByAutor(autor);
        autor.setLivros(livrosLista);


        autor.getLivros().forEach(System.out::println);

    }
}
