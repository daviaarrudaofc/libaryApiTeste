package io.github.daviaarrudaofc.libaryAPI.repository;

import io.github.daviaarrudaofc.libaryAPI.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {
}
