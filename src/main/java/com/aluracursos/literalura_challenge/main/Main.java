package com.aluracursos.literalura_challenge.main;

import com.aluracursos.literalura_challenge.models.*;
import com.aluracursos.literalura_challenge.repository.IRepositoryAuthors;
import com.aluracursos.literalura_challenge.repository.IRepositoryBooks;
import com.aluracursos.literalura_challenge.service.APIConsumption;
import com.aluracursos.literalura_challenge.service.DataConverter;

import java.util.*;

public class Main {
    private Scanner input = new Scanner(System.in);
    private APIConsumption consumoApi = new APIConsumption();
    private DataConverter conversor = new DataConverter();
    private final static String URL_BASE = "https://gutendex.com/books/?search=";

    private IRepositoryAuthors autoresRepository;
    private IRepositoryBooks librosRepository;

    public Main(IRepositoryAuthors autoresRepository, IRepositoryBooks librosRepository) {
        this.autoresRepository = autoresRepository;
        this.librosRepository = librosRepository;
    }

    public void menu() {
        var opcion = -1;
        System.out.println("\nBienvenido a LiterAlura! Selecciona una opción: ");
        while (opcion != 0) {
            var menu = """
                    1. Buscar libros por título
                    2. Listar libros registrados
                    3. Listar autores registrados
                    4. Listar libros por idioma
                    5. Listar autores vivos en un determinado año                   
                    0. Salir
                    """;
            System.out.println(menu);
            opcion = input.nextInt();
            input.nextLine();

            switch (opcion) {
                case 1:
                    agregarLibros();
                    break;
                case 2:
                    librosRegistrados();
                    break;
                case 3:
                    autoresRegistrados();
                    break;
                case 4:
                    listarPorIdioma();
                    break;
                case 5:
                    autoresPorAño();
                    break;
                case 0:
                    System.out.println("Hasta luego, gracias por usar LiterAlura.\n");
                    break;

                default:
                    System.out.println("Opción no válida, ingrese una opción válida\n");
            }

        }
    }

    private Data getDatosLibros() {
        var nombreLibro = input.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "+"));
        Data datosLibros = conversor.obtenerDatos(json, Data.class);
        return datosLibros;
    }

    private Books crearLibro(DataBooks datosLibros, Authors autor) {
        if (autor != null) {
            return new Books(datosLibros, autor);
        } else {
            System.out.println("El autor no está definido, no se puede crear el libro");
            return null;
        }
    }

    private void agregarLibros() {
        System.out.println("Escribe el libro a buscar: ");
        Data datos = getDatosLibros();
        if (!datos.resultados().isEmpty()) {
            DataBooks datosLibro = datos.resultados().get(0);
            DataAuthors datosAutores = datosLibro.autor().get(0);
            Books libro = null;
            Books libroRepositorio = librosRepository.findByTitulo(datosLibro.titulo());
            if (libroRepositorio != null) {
                System.out.println("Este libro ya está ingresado, no es posible agregar uno igual");
                System.out.println(libroRepositorio.toString());
            } else {
                Authors autorRepositorio = autoresRepository.findByNameIgnoreCase(datosLibro.autor().get(0).nombreAutor());
                if (autorRepositorio != null) {
                    libro = crearLibro(datosLibro, autorRepositorio);
                    librosRepository.save(libro);
                    System.out.println("----- LIBRO INGRESADO -----\n");
                    System.out.println(libro);
                } else {
                    Authors autor = new Authors(datosAutores);
                    autor = autoresRepository.save(autor);
                    libro = crearLibro(datosLibro, autor);
                    librosRepository.save(libro);
                    System.out.println("----- LIBRO INGRESADO -----\n");
                    System.out.println(libro);
                }
            }
        } else {
            System.out.println("El libro no existe en el recurso de consulta, ingresa otro");
        }
    }

    private void librosRegistrados() {
        List<Books> libros = librosRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados");
            return;
        }
        System.out.println("----- LOS LIBROS EN REGISTRO SON: -----\n");
        libros.stream()
                .sorted(Comparator.comparing(Books::getTitulo))
                .forEach(System.out::println);
    }

    private void autoresRegistrados() {
        List<Authors> autores = autoresRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados");
            return;
        }
        System.out.println("----- LOS AUTORES EN REGISTRO SON: -----\n");
        autores.stream()
                .sorted(Comparator.comparing(Authors::getName))
                .forEach(System.out::println);
    }

    private void autoresPorAño() {
        System.out.println("Escribe el año en el que deseas buscar: ");
        var año = input.nextInt();
        input.nextLine();
        if (año < 0) {
            System.out.println("El año debe ser mayor a cero, intenta de nuevo");
            return;
        }
        List<Authors> autoresPorAño = autoresRepository.findByAñoNacimientoLessThanEqualAndAñoMuerteGreaterThanEqual(año, año);
        if (autoresPorAño.isEmpty()) {
            System.out.println("No hay autores registrados para ese año");
            return;
        }
        System.out.println("----- LOS AUTORES VIVOS EN REGISTRO EN EL AÑO " + año + " SON: -----\n");
        autoresPorAño.stream()
                .sorted(Comparator.comparing(Authors::getName))
                .forEach(System.out::println);
    }

    private void listarPorIdioma() {
        System.out.println("Escribe el idioma según las opciones: ");
        String menu = """
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """;
        System.out.println(menu);
        var idioma = input.nextLine();
        if (!idioma.equals("es") && !idioma.equals("en") && !idioma.equals("fr") && !idioma.equals("pt")) {
            System.out.println("Idioma no válido, intenta de nuevo");
            return;
        }
        List<Books> librosPorIdioma = librosRepository.findByLenguajesContaining(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No hay libros registrados en ese idioma");
            return;
        }
        System.out.println("----- LOS LIBROS INGRESADOS EN EL IDIOMA SELECCIONADO SON: -----\n");
        librosPorIdioma.stream()
                .sorted(Comparator.comparing(Books::getTitulo))
                .forEach(System.out::println);
    }
}
