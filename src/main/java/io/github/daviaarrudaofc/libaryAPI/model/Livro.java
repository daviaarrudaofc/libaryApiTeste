package io.github.daviaarrudaofc.libaryAPI.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data// composta por tudo isso: o getter,setter,toString,EqualsAndHashCode,RequiredArgsConstructor
@Entity
@ToString(exclude = "autor") // n quer que imprima no toString o id_autor
@Table(name = "livro")
@EntityListeners(AuditingEntityListener.class)
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


    @ManyToOne(// refere ao mapeamento, muitos livros para um autor
          //  cascade =  CascadeType.ALL
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "id_autor")
    private Autor autor;


    @CreatedDate// Preenche automaticamente quando o registro é criado
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @LastModifiedDate// Atualiza automaticamente sempre que o registro for alterado
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "id_usuario")
    private UUID id_usuario;


}
