package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.Usuario;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GrafoUsuariosTest {

    @Test
    public void testAgregarYConectarUsuarios() {
        GrafoUsuarios grafo = new GrafoUsuarios();
        Usuario u1 = new Usuario("1", "Ana", "ana@uq.edu.co", "123", List.of("IA"), null);
        Usuario u2 = new Usuario("2", "Luis", "luis@uq.edu.co", "456", List.of("Redes"), null);

        grafo.agregarUsuario(u1);
        grafo.agregarUsuario(u2);
        grafo.conectarUsuarios(u1.getCorreo(), u2.getCorreo());

        assertTrue(grafo.estanConectados(u1.getCorreo(), u2.getCorreo()));
    }

    @Test
    public void testEliminarConexion() {
        GrafoUsuarios grafo = new GrafoUsuarios();
        Usuario u1 = new Usuario("1", "Ana", "ana@uq.edu.co", "123", List.of("IA"), null);
        Usuario u2 = new Usuario("2", "Luis", "luis@uq.edu.co", "456", List.of("Redes"), null);

        grafo.agregarUsuario(u1);
        grafo.agregarUsuario(u2);
        grafo.conectarUsuarios(u1.getCorreo(), u2.getCorreo());
        grafo.eliminarConexion(u1.getCorreo(), u2.getCorreo());

        assertFalse(grafo.estanConectados(u1.getCorreo(), u2.getCorreo()));
    }
}
