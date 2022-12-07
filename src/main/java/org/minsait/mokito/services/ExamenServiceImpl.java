package org.minsait.mokito.services;

import org.minsait.mokito.models.Examen;
import org.minsait.mokito.repositories.ExamenRepository;
import org.minsait.mokito.repositories.PreguntaRepository;

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
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del examen no puede ser nulo o vacío");
        }

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

        if (examen != null && examen.getPreguntas() != null) {
            //Tiene preguntas
            this.preguntaRepository.savePreguntas(examen.getPreguntas());
            this.examenRepository.save(examen);
        }

        //Si no solo guardar examen
        if (examen == null)
            throw new IllegalArgumentException("El examen no puede ser nulo");
        this.examenRepository.save(examen);
        return examen;
    }
}
