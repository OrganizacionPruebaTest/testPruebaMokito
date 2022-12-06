package org.minsait.mokito.services;

import org.minsait.mokito.models.Examen;

import java.util.List;

public class Datos {

    public static final List<Examen> EXAMENES = List.of(
            new Examen(1L, "Matemáticas"),
            new Examen(2L, "Lengua"),
            new Examen(3L, "Historia"),
            new Examen(4L, "Física")
    );

    public static final Examen EXAMEN_1 = new Examen(5L, "Matemáticas");
    public static final List<String> PREGUNTAS = List.of("Aritmética", "Integrales", "Derivadas");


}
