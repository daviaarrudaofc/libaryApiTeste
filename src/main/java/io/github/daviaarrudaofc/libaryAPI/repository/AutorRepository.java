package io.github.daviaarrudaofc.libaryAPI.repository;

import io.github.daviaarrudaofc.libaryAPI.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AutorRepository extends JpaRepository<Autor, UUID> {
}
