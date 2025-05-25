package com.uniquindio.redsocial.estructuras;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Map;
import java.util.Set;

public class GrafoVisualizador {

    public static void mostrarGrafo(GrafoUsuarios grafoUsuarios) {
        Graph grafoVisual = new SingleGraph("Grafo de Usuarios");

        // Agregar nodos
        for (String usuario : grafoUsuarios.getUsuarios().keySet()) {
            grafoVisual.addNode(usuario).setAttribute("ui.label", usuario);
        }

        // Agregar aristas
        for (Map.Entry<String, Set<String>> entry : grafoUsuarios.getConexiones().entrySet()) {
            String usuario = entry.getKey();
            for (String conexion : entry.getValue()) {
                String edgeId = usuario + "-" + conexion;
                if (grafoVisual.getEdge(edgeId) == null && grafoVisual.getEdge(conexion + "-" + usuario) == null) {
                    grafoVisual.addEdge(edgeId, usuario, conexion);
                }
            }
        }

        // Configuración visual
        grafoVisual.setAttribute("ui.stylesheet", "node { fill-color: blue; size: 20px; text-size: 14px; } edge { fill-color: gray; }");
        grafoVisual.display();
    }
}
