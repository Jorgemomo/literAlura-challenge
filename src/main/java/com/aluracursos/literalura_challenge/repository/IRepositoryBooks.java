package com.aluracursos.literalura_challenge.repository;

import com.aluracursos.literalura_challenge.models.Books;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRepositoryBooks extends JpaRepository<Books, Long> {
    Books findByTitulo(String titulo);

    List<Books> findByLenguajesContaining(String lenguaje);

}
