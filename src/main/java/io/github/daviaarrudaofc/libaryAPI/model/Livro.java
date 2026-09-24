package io.github.daviaarrudaofc.libaryAPI.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data// composta por tudo isso: o getter,setter,toString,EqualsAndHashCode,RequiredArgsConstructor
@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "isbn", length = 20, nullable = false)
    private String isbn;

    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @Column(name = "data_publicacao")
    private LocalDate dataPublicacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", length = 30, nullable = false)
    private GeneroLivro genero;

    @Column(name = "preco", precision = 12)
    private BigDecimal preco;


    @ManyToOne(cascade =  CascadeType.ALL)// refere ao mapeamento, muitos livros para um autor
    @JoinColumn(name = "id_autor")
    private Autor autor;

}
