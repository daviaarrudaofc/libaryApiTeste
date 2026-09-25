package io.github.daviaarrudaofc.libaryAPI.repository;

import io.github.daviaarrudaofc.libaryAPI.model.Autor;
import io.github.daviaarrudaofc.libaryAPI.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
/**
 * @see LivroRepositoryTest
 */

public interface LivroRepository extends JpaRepository<Livro, UUID> {
    //QUERY METHOD
    //select * from livro where id_autor = id
    List<Livro> findByAutor(Autor autor);
    //select * from livro where titulo = titulo_passado_parametro
    List<Livro> findByTitulo(String titulo);
    // fiz aq por objeto porque pode ser unico o isbn e nao varios: Livro findByIsbn(String isbn);
    List<Livro> findByIsbn(String isbn);

    List<Livro> findByTituloAndPreco(String titulo, BigDecimal preco);

    List<Livro> findByTituloOrIsbn(String titulo, String isbn);

    // select * from livro where data_publicacao between ? and ?
    List<Livro> findByDataPublicacaoBetween(LocalDate inicio, LocalDate fim);


    //JPQL -> referencia as entidades e as propriedades
    @Query(" select l from Livro as l order by l.titulo, l.preco ")
    List<Livro> listarTodosOrdenadoPorTituloAndPreco();


    /**
     *  select a.*
     *  from livro as l
     *  join autor as a on a.id = l.id-autor
     */
    @Query(" select a from Livro as l join l.autor a ")
    List<Autor> listarAutoresDosLivros();

    //select distinct l.* from livro as l
    @Query(" select distinct l.titulo from Livro as l ")
    List<String> listarNomesDiferentesLivros();

    @Query("""
        select l.genero
        from Livro as l
        join l.autor a
        where a.nacionalidade = 'Brasilerio'
        order by l.genero
        """)
    List<String> listarGenerosAutoresBrasileiros();

}
