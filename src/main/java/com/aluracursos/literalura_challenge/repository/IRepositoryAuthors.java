package com.aluracursos.literalura_challenge.repository;

import com.aluracursos.literalura_challenge.models.Authors;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRepositoryAuthors extends JpaRepository<Authors, Long> {
    Authors findByNameIgnoreCase(String nombre);

    List<Authors> findByAñoNacimientoLessThanEqualAndAñoMuerteGreaterThanEqual(int añoInicial, int añoFinal);
}
