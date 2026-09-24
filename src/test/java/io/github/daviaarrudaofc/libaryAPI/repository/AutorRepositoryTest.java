package io.github.daviaarrudaofc.libaryAPI.repository;

import io.github.daviaarrudaofc.libaryAPI.model.Autor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {

    @Autowired
    AutorRepository repository;


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
}
