package org.minsait.mokito.services;

import org.minsait.mokito.models.Examen;

import java.util.List;
import java.util.Optional;

public interface ExamenService {
    Optional<Examen> findExamenPorNombre(String nombre);

    Examen findExamenPorNombreConPreguntas(String nombre);

    Examen save(Examen examen);
}
