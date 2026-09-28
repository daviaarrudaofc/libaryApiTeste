package io.github.daviaarrudaofc.libaryAPI.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "autor", schema = "public")
@Getter
@Setter
@ToString(exclude = "livros")
@EntityListeners(AuditingEntityListener.class) // Ativa o monitoramento da entidade
                                        // para preencher automaticamente datas de criação e atualização
public class Autor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID) // vai ser gerado automaticamente, nao precisa se preoucpar, pq o propio JPA vai gerar
    private UUID id;

    @Column(name = "nome", length = 100,nullable = false)// nullable = significa que nao pode ser not null=como definimos no banco de dados
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "nacionalidade",length = 50, nullable = false)
    private String nacionalidade;

    @Deprecated
    public Autor(){
        // para uso do fremework
    }

    public Autor(UUID id, String nome, LocalDate dataNascimento, String nacionalidade) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.nacionalidade = nacionalidade;
    }
    @OneToMany(
            mappedBy = "autor",
           // cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )//refere ao mapeamento,um autor para muitos livros
    private List<Livro> livros;

    @CreatedDate// Preenche automaticamente quando o registro é criado
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @LastModifiedDate// Atualiza automaticamente sempre que o registro for alterado
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "id_usuario")
    private UUID id_usuario;

}
