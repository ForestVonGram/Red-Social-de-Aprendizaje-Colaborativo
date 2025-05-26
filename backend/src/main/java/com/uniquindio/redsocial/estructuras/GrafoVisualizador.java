package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.Usuario;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class GrafoVisualizador {

    static {
        System.setProperty("org.graphstream.ui", "swing");
    }

    public void mostrarGrafo(GrafoUsuarios grafoUsuarios) {
        Graph grafoVisual = new SingleGraph("Grafo de Usuarios");
        configurarEstilosGrafo(grafoVisual);
        agregarNodos(grafoVisual, grafoUsuarios);
        agregarAristas(grafoVisual, grafoUsuarios);
        mostrarVisualizacion(grafoVisual);
    }

    private void configurarEstilosGrafo(Graph grafoVisual) {
        String estilos = """
            node {
                fill-color: #4CAF50;
                size: 30px;
                text-size: 12px;
                text-style: bold;
                text-color: #000000;
                text-background-mode: rounded-box;
                text-background-color: #FFFFFF;
                text-padding: 2px;
                stroke-mode: plain;
                stroke-color: #2E7D32;
                stroke-width: 2px;
            }
            edge {
                fill-color: #90CAF9;
                size: 2px;
                arrow-size: 8px, 6px;
            }
            node:clicked {
                fill-color: #FFA726;
            }
            node:selected {
                fill-color: #FF7043;
            }
            edge:selected {
                fill-color: #FF5722;
                size: 3px;
            }
            """;
        grafoVisual.setAttribute("ui.stylesheet", estilos);
        grafoVisual.setAttribute("ui.quality");
        grafoVisual.setAttribute("ui.antialias");
    }

    private void agregarNodos(Graph grafoVisual, GrafoUsuarios grafoUsuarios) {
        for (Map.Entry<String, Usuario> entry : grafoUsuarios.getUsuarios().entrySet()) {
            Usuario usuario = entry.getValue();
            Node nodo = grafoVisual.addNode(usuario.getCorreo());
            configurarNodo(nodo, usuario);
        }
    }

    private void configurarNodo(Node nodo, Usuario usuario) {
        String label = String.format("%s\n(%s)", usuario.getNombre(), usuario.getCorreo());
        nodo.setAttribute("ui.label", label);

        StringBuilder tooltip = new StringBuilder();
        tooltip.append("Nombre: ").append(usuario.getNombre()).append("\n");
        tooltip.append("Correo: ").append(usuario.getCorreo()).append("\n");
        if (usuario.getIntereses() != null && !usuario.getIntereses().isEmpty()) {
            tooltip.append("Intereses: ").append(String.join(", ", usuario.getIntereses()));
        }
        nodo.setAttribute("ui.tooltip", tooltip.toString());
    }

    private void agregarAristas(Graph grafoVisual, GrafoUsuarios grafoUsuarios) {
        for (Map.Entry<String, Set<String>> entry : grafoUsuarios.getConexiones().entrySet()) {
            String usuario = entry.getKey();
            for (String conexion : entry.getValue()) {
                agregarArista(grafoVisual, usuario, conexion);
            }
        }
    }

    private void agregarArista(Graph grafoVisual, String usuario1, String usuario2) {
        String edgeId = generarIdArista(usuario1, usuario2);
        if (grafoVisual.getEdge(edgeId) == null) {
            grafoVisual.addEdge(edgeId, usuario1, usuario2);
        }
    }

    private String generarIdArista(String usuario1, String usuario2) {
        return usuario1.compareTo(usuario2) < 0 ?
                usuario1 + "-" + usuario2 :
                usuario2 + "-" + usuario1;
    }

    private void mostrarVisualizacion(Graph grafoVisual) {
        try {
            Viewer viewer = grafoVisual.display();
            viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);

            viewer.enableAutoLayout();

        } catch (Exception e) {
            System.err.println("Error al visualizar el grafo: " + e.getMessage());
        }
    }

    public void actualizarVisualizacion(GrafoUsuarios grafoUsuarios) {
        mostrarGrafo(grafoUsuarios);
    }

    public void exportarGrafoAImagen(Graph grafoVisual, String rutaArchivo) {
        try {
            grafoVisual.setAttribute("ui.screenshot", rutaArchivo);
        } catch (Exception e) {
            System.err.println("Error al exportar el grafo a imagen: " + e.getMessage());
        }
    }
}