package org.minsait.mokito.repositories;

import org.minsait.mokito.models.Examen;

import java.util.List;

public interface ExamenRepository {
    List<Examen> findAll();
    Examen save(Examen examen);

}
