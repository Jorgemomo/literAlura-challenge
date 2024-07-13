package com.aluracursos.literalura_challenge.service;

public interface IDataConverter {
    <T> T obtenerDatos(String json, Class<T> clase);
}

