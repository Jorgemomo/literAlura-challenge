package com.aluracursos.literalura_challenge.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataBooks(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DataAuthors> autor,
        @JsonAlias("languages") List<String> languages,
        @JsonAlias("download_count") double numeroDescargas
) {
}

