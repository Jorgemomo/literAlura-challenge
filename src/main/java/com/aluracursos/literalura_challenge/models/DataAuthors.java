package com.aluracursos.literalura_challenge.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataAuthors(
        @JsonAlias("name") String nombreAutor,
        @JsonAlias("birth_year") int añoNacimiento,
        @JsonAlias("death_year") int añoMuerte
) {
}
