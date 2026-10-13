package io.github.daviaarrudaofc.libaryAPI.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String login;

    @Column
    private String senha;

    @Type(ListArrayType.class)// é como se fosse o tradutor: ele vai traduzir roles para este tipo
    @Column(name = "roles", columnDefinition = "varchar[]")
    private List<String> roles;

}
