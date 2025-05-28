package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.GrupoEstudio;
import com.uniquindio.redsocial.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ListaGruposTest {

    private ListaGrupos listaGrupos;
    private Usuario usuario1;
    private Usuario usuario2;
    private GrupoEstudio grupo1;
    private GrupoEstudio grupo2;

    @BeforeEach
    void setUp() {
        listaGrupos = new ListaGrupos();
        usuario1 = new Usuario("Ana", "ana@uq.edu.co", "123", List.of("Java"));
        usuario1.setId(1L);

        usuario2 = new Usuario("Luis", "luis@uq.edu.co", "456", List.of("Python"));
        usuario2.setId(2L);

        grupo1 = new GrupoEstudio();
        grupo1.setId(1L);
        grupo1.setTemaCentral("Grupo Java");

        grupo2 = new GrupoEstudio();
        grupo2.setId(2L);
        grupo2.setTemaCentral("Grupo Python");
    }

    @Test
    void testAgregarUsuario() {
        assertTrue(listaGrupos.agregarUsuario(usuario1, grupo1));
        assertEquals(1, listaGrupos.getTamanio());
        assertTrue(listaGrupos.contieneMiembro(usuario1.getCorreo()));
    }

    @Test
    void testEliminarUsuario() {
        listaGrupos.agregarUsuario(usuario1, grupo1);
        assertTrue(listaGrupos.eliminarUsuarioPorCorreo(usuario1.getCorreo()));
        assertFalse(listaGrupos.contieneMiembro(usuario1.getCorreo()));
    }

    @Test
    void testObtenerMiembrosPorGrupo() {
        listaGrupos.agregarUsuario(usuario1, grupo1);
        listaGrupos.agregarUsuario(usuario2, grupo2);

        List<Usuario> miembrosGrupo1 = listaGrupos.obtenerMiembrosPorGrupo(grupo1);
        assertEquals(1, miembrosGrupo1.size());
        assertTrue(miembrosGrupo1.contains(usuario1));
    }

    @Test
    void testObtenerMiembrosPorGrupos() {
        listaGrupos.agregarUsuario(usuario1, grupo1);
        listaGrupos.agregarUsuario(usuario2, grupo2);

        Map<GrupoEstudio, List<Usuario>> miembrosPorGrupo = listaGrupos.obtenerMiembrosPorGrupos();
        assertEquals(2, miembrosPorGrupo.size());
        assertTrue(miembrosPorGrupo.get(grupo1).contains(usuario1));
        assertTrue(miembrosPorGrupo.get(grupo2).contains(usuario2));
    }

    @Test
    void testCapacidadMaxima() {
        for (int i = 0; i < 51; i++) {
            Usuario usuario = new Usuario();
            usuario.setCorreo("usuario" + i + "@uq.edu.co");
            if (i < 50) {
                assertTrue(listaGrupos.agregarUsuario(usuario, grupo1));
            } else {
                assertThrows(IllegalStateException.class, () ->
                        listaGrupos.agregarUsuario(usuario, grupo1));
            }
        }
    }

    @Test
    void testFechaIngreso() {
        listaGrupos.agregarUsuario(usuario1, grupo1);
        assertTrue(listaGrupos.obtenerFechaIngreso(usuario1.getCorreo()).isPresent());
    }

    @Test
    void testLimpiarLista() {
        listaGrupos.agregarUsuario(usuario1, grupo1);
        listaGrupos.limpiarLista();
        assertTrue(listaGrupos.estaVacia());
        assertEquals(0, listaGrupos.getTamanio());
    }
}