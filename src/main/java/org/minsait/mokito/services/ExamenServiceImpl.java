package org.minsait.mokito.services;

import org.minsait.mokito.models.Examen;
import org.minsait.mokito.repositories.ExamenRepository;
import org.minsait.mokito.repositories.PreguntaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements ExamenService {

    private ExamenRepository examenRepository;
    private PreguntaRepository preguntaRepository;

    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {
        return this.examenRepository.findAll().stream()
                .filter(examen -> examen.getNombre().equals(nombre)).findFirst();
    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Optional<Examen> examen = findExamenPorNombre(nombre);

        if (examen.isPresent()) {
            examen.get().setPreguntas(preguntaRepository.findPreguntasByExamenId(
                    examen.get().getId()
            ));
            return examen.get();
        }
        return null;
    }

    @Override
    public Examen save(Examen examen) {
        //Validar si tiene preguntas. Si tiene preguntas guardar el examen con preguntas
        List<String> preguntas = examen.getPreguntas();

        if (preguntas.size() > 0) {
            //Tiene preguntas
            this.preguntaRepository.savePreguntas(preguntas);
        }

        //Si no solo guardar examen
        this.examenRepository.save(examen);
        return examen;
    }
}
