package org.minsait.mokito.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.minsait.mokito.models.Examen;
import org.minsait.mokito.repositories.ExamenRepository;
import org.minsait.mokito.repositories.PreguntaRepository;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InvalidApplicationException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    @Mock
    ExamenRepository examenRepository;
    @Mock
    PreguntaRepository preguntaRepository;
    @InjectMocks
    ExamenServiceImpl examenService;

    @Test
    void testFindExamenPorNombre() {
        //Datos simulados
        Mockito.when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        //Llamada al método a testear
        Optional<Examen> examen = examenService.findExamenPorNombre("Matemáticas");

        //Verificaciones
        assertTrue(examen.isPresent());
    }

    @Test
    void testPregutasExamen() {
        //Datos simulados
        Mockito.when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        Mockito.when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        //Llamada al método a testear
        Examen examen = examenService.findExamenPorNombreConPreguntas("Historia");
        assertNotNull(examen.getPreguntas());
        //Verificaciones
        assertTrue(examen.getPreguntas().contains("Aritmética"));

        //times, atMost, atMostOnce, atLeast, atLeastOnce, never
        //verify(examenRepository, atLeast(1)).findAll();

        verify(examenRepository, atLeast(1)).findAll();
        //verify(examenService, times(1)).findExamenPorNombre("Historia");
    }

    @Test
    void TestException() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            examenService.findExamenPorNombreConPreguntas("Historia");
        });

        assertTrue(IllegalArgumentException.class.isInstance(exception));
    }

    @Test
    void testDoThrow() {
        doThrow(IllegalArgumentException.class).when(preguntaRepository).savePreguntas(anyList());
        Examen examen = Datos.EXAMEN_1;
        examen.setPreguntas(Datos.PREGUNTAS);
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            examenService.findExamenPorNombreConPreguntas(null);
            examenService.findExamenPorNombreConPreguntas("");
        });



        Assertions.assertEquals(IllegalArgumentException.class, thrown.getClass());
        assertThrows(IllegalArgumentException.class, () -> {
            examenService.save(examen);
        });

        Examen examen2=examenService.findExamenPorNombreConPreguntas("Geogradi1");
        assertNull(examen2);
    }

    @Test
    void testDoAnswer() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        doAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0);
            return id == 1 ? Datos.PREGUNTAS : Collections.EMPTY_LIST;
        }).when(preguntaRepository).findPreguntasByExamenId(anyLong());
        //when(preguntaRepository.findPreguntasByExamenId(1L)).thenReturn(Datos.PREGUNTAS);
        //when(preguntaRepository.findPreguntasByExamenId(2L)).thenReturn(Datos.PREGUNTAS);

        Examen examen = examenService.findExamenPorNombreConPreguntas("Historia");

        assertNotNull(examen);
        assertAll(
                () -> assertNotNull(examen),
                () -> assertTrue(examen.getPreguntas().isEmpty()),
                () -> assertEquals(0, examen.getPreguntas().size())

        );
    }

    @Captor
    ArgumentCaptor<Long> captor;

    @Test
    void testArgumentCaptor() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = examenService.findExamenPorNombreConPreguntas("Historia");
        verify(preguntaRepository).findPreguntasByExamenId(captor.capture());
        assertEquals(3L, captor.getValue());
    }

    @Test
    void testSaveExamen() {
        Examen examen = Datos.EXAMEN_1;
        examen.setPreguntas(Datos.PREGUNTAS);

        when(examenRepository.save(examen)).thenReturn(examen);
        Examen examenGuardado = examenService.save(examen);

        assertNotNull(examenGuardado);
        assertEquals(5l, examenGuardado.getId());
        assertEquals("Matemáticas", examenGuardado.getNombre());
        assertEquals(3, examenGuardado.getPreguntas().size());
        assertFalse(examenGuardado.getPreguntas().contains("Geometría"));
        assertThrows(IllegalArgumentException.class, () -> {
            examenService.save(null);
        });
    }

    @Test
    void testPreguntaExamen() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = examenService.findExamenPorNombreConPreguntas("Física");

        assertNotNull(examen);

        assertTrue(examen.getPreguntas().contains("Integrales"));

        verify(examenRepository, atLeast(1)).findAll();
        verify(preguntaRepository, times(1)).findPreguntasByExamenId(anyLong());
    }

    @Test
    void testSetters() {
        Examen examen = Datos.EXAMEN_1;
        examen.setId(1L);
        examen.setNombre("Matemáticas");
        examen.setPreguntas(Datos.PREGUNTAS);
        assertNotNull(examen);
        assertEquals(1L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
        assertEquals(3, examen.getPreguntas().size());
    }

}