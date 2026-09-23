package io.github.daviaarrudaofc.libaryAPI.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "autor", schema = "public")
@Getter
@Setter
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
    @OneToMany(mappedBy = "autor")//refere ao mapeamento,um autor para muitos livros
    private List<Livro> livros;
}
