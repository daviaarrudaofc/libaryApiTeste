package io.github.daviaarrudaofc.libaryAPI.validator;

import io.github.daviaarrudaofc.libaryAPI.exceptions.CampoInvalidoException;
import io.github.daviaarrudaofc.libaryAPI.exceptions.RegistroDuplicadoException;
import io.github.daviaarrudaofc.libaryAPI.model.Livro;
import io.github.daviaarrudaofc.libaryAPI.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LivroValidator {
    private LivroRepository livroRepository;

    private static final int ANO_EXIGENCIA_PRECO = 2020;

    public void validar(Livro livro){
        if(existeLivroComIsbn(livro)){
            throw new RegistroDuplicadoException("ISBN já cadastrado");
        }

        if(isPrecoObrigatorioNulo(livro)){
            throw new CampoInvalidoException("preco", "Para livros com ano de publicacao a partir de 2020, o preço é obrigatório");
        }
    }

    private boolean isPrecoObrigatorioNulo(Livro livro) {
        return  livro.getPreco() == null &&
                livro.getDataPublicacao().getYear() >= ANO_EXIGENCIA_PRECO;
    }

    private boolean existeLivroComIsbn(Livro livro) {
        // Pega o ISBN do livro recebido como parâmetro e busca no banco
        // um livro com esse mesmo ISBN. Se encontrar, guarda o livro no Optional.
        Optional<Livro> livroEncontrado = livroRepository.findByIsbn(livro.getIsbn());

        if (livro.getId() == null) { // O livro recebido está sem ID: cadastro novo.
            // A busca encontrou um livro com esse ISBN?
            // true: já existe no banco e estou tentando cadastrar outro com o mesmo ISBN.
            // false: não encontrou, então não há duplicidade.
            // O return encerra este método e devolve o resultado ao validar().
            return livroEncontrado.isPresent();
        }

        // Só chega aqui se o livro recebido tem ID: atualização.
        // Verifica se o ISBN encontrado pertence a outro livro, não ao próprio.
        return livroEncontrado
                .map(Livro::getId)
                .stream()
                .anyMatch(id -> !id.equals(livro.getId()));
    }
}
