package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GrafoUsuariosTest {

    private GrafoUsuarios grafo;
    private Usuario u1, u2, u3;

    @BeforeEach
    void setUp() {
        grafo = new GrafoUsuarios();
        u1 = new Usuario(1L, "Ana", "ana@uq.edu.co", "123", List.of("IA"), null);
        u2 = new Usuario(2L, "Luis", "luis@uq.edu.co", "456", List.of("Redes"), null);
        u3 = new Usuario(3L, "Carlos", "carlos@uq.edu.co", "789", List.of("Bases de Datos"), null);
    }

    @Test
    public void testAgregarUsuario() {
        grafo.agregarUsuario(u1);
        assertNotNull(grafo.getUsuario(u1.getCorreo()));
        assertEquals(u1, grafo.getUsuario(u1.getCorreo()));
    }

    @Test
    public void testEliminarUsuario() {
        grafo.agregarUsuario(u1);
        grafo.agregarUsuario(u2);
        grafo.conectarUsuarios(u1.getCorreo(), u2.getCorreo());

        grafo.eliminarUsuario(u1.getCorreo());

        assertNull(grafo.getUsuario(u1.getCorreo()));
        assertFalse(grafo.estanConectados(u1.getCorreo(), u2.getCorreo()));
    }

    @Test
    public void testConectarUsuarios() {
        grafo.agregarUsuario(u1);
        grafo.agregarUsuario(u2);
        grafo.conectarUsuarios(u1.getCorreo(), u2.getCorreo());

        assertTrue(grafo.estanConectados(u1.getCorreo(), u2.getCorreo()));
        assertTrue(grafo.estanConectados(u2.getCorreo(), u1.getCorreo()));
    }

    @Test
    public void testEliminarConexion() {
        grafo.agregarUsuario(u1);
        grafo.agregarUsuario(u2);
        grafo.conectarUsuarios(u1.getCorreo(), u2.getCorreo());
        grafo.eliminarConexion(u1.getCorreo(), u2.getCorreo());

        assertFalse(grafo.estanConectados(u1.getCorreo(), u2.getCorreo()));
        assertFalse(grafo.estanConectados(u2.getCorreo(), u1.getCorreo()));
    }

    @Test
    public void testObtenerAmigos() {
        grafo.agregarUsuario(u1);
        grafo.agregarUsuario(u2);
        grafo.agregarUsuario(u3);

        grafo.conectarUsuarios(u1.getCorreo(), u2.getCorreo());
        grafo.conectarUsuarios(u1.getCorreo(), u3.getCorreo());

        Set<Usuario> amigos = grafo.obtenerAmigos(u1.getCorreo());
        assertEquals(2, amigos.size());
        assertTrue(amigos.contains(u2));
        assertTrue(amigos.contains(u3));
    }

    @Test
    public void testRecomendarAmigos() {
        grafo.agregarUsuario(u1);
        grafo.agregarUsuario(u2);
        grafo.agregarUsuario(u3);

        grafo.conectarUsuarios(u1.getCorreo(), u2.getCorreo());
        grafo.conectarUsuarios(u2.getCorreo(), u3.getCorreo());

        Set<Usuario> recomendados = grafo.recomendarAmigos(u1.getCorreo());
        assertEquals(1, recomendados.size());
        assertTrue(recomendados.contains(u3));
    }

    @Test
    public void testBuscarRutaMasCorta() {
        grafo.agregarUsuario(u1);
        grafo.agregarUsuario(u2);
        grafo.agregarUsuario(u3);

        grafo.conectarUsuarios(u1.getCorreo(), u2.getCorreo());
        grafo.conectarUsuarios(u2.getCorreo(), u3.getCorreo());

        List<Usuario> ruta = grafo.buscarRutaMasCorta(u1.getCorreo(), u3.getCorreo());
        assertEquals(3, ruta.size());
        assertEquals(u1, ruta.get(0));
        assertEquals(u2, ruta.get(1));
        assertEquals(u3, ruta.get(2));
    }

    @Test
    public void testDetectarClusters() {
        grafo.agregarUsuario(u1);
        grafo.agregarUsuario(u2);
        grafo.agregarUsuario(u3);

        grafo.conectarUsuarios(u1.getCorreo(), u2.getCorreo());
        // u3 queda aislado

        List<Set<Usuario>> clusters = grafo.detectarClusters();
        assertEquals(2, clusters.size());
    }

    @Test
    public void testVerificarIntegridad() {
        grafo.agregarUsuario(u1);
        grafo.agregarUsuario(u2);
        grafo.conectarUsuarios(u1.getCorreo(), u2.getCorreo());

        assertTrue(grafo.verificarIntegridad());
    }

    @Test
    public void testObtenerUsuariosConMasConexiones() {
        grafo.agregarUsuario(u1);
        grafo.agregarUsuario(u2);
        grafo.agregarUsuario(u3);

        grafo.conectarUsuarios(u1.getCorreo(), u2.getCorreo());
        grafo.conectarUsuarios(u1.getCorreo(), u3.getCorreo());

        List<Usuario> masConectados = grafo.obtenerUsuariosConMasConexiones(1);
        assertEquals(1, masConectados.size());
        assertEquals(u1, masConectados.get(0));
    }

    @Test
    public void testObtenerUsuariosConectadosIndirectamente() {
        grafo.agregarUsuario(u1);
        grafo.agregarUsuario(u2);
        grafo.agregarUsuario(u3);

        grafo.conectarUsuarios(u1.getCorreo(), u2.getCorreo());
        grafo.conectarUsuarios(u2.getCorreo(), u3.getCorreo());

        Set<Usuario> conectados = grafo.obtenerUsuariosConectadosIndirectamente(u1.getCorreo());
        assertEquals(2, conectados.size());
        assertTrue(conectados.contains(u2));
        assertTrue(conectados.contains(u3));
    }
}