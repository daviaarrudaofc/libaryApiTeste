package io.github.daviaarrudaofc.libaryAPI.repository.specs;

import io.github.daviaarrudaofc.libaryAPI.model.GeneroLivro;
import io.github.daviaarrudaofc.libaryAPI.model.Livro;
import org.springframework.data.jpa.domain.Specification;

public class LivroSpecs {

    public static Specification<Livro> isbnEqual(String isbn){
        return (root, query, cb) -> cb.equal(root.get("isbn"),isbn);
    }

    public static Specification<Livro> tituloLike(String titulo){
        //upper(livro.titulo) like (%:param%)
        return (root, query, cb)
                -> cb.like(cb.upper(root.get("titulo")) ,"%"+ titulo.toUpperCase()+"%");
    }

    public static Specification<Livro> generoEqual(GeneroLivro genero){
        return (root, query, cb) -> cb.equal(root.get("genero"), genero);
    }

    public static Specification<Livro> anoPublicacaoEqual(Integer anoPublicacao){
        // and to_char(data-publicacao, 'YYYY') = anoPublicacao
        return (root, query, cb) ->
                cb.equal(cb.function("to_char", String.class,
                        root.get("dataPublicacao"), cb.literal("YYYY"))  , anoPublicacao.toString());
    }
}
