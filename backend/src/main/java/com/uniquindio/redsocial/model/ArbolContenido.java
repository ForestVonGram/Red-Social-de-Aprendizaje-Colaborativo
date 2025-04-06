package com.uniquindio.redsocial.model;

import java.util.ArrayList;
import java.util.List;

public class ArbolContenido {

    private Nodo raiz;

    private class Nodo {
        Contenido contenido;
        Nodo izquierda, derecha;

        Nodo(Contenido contenido) {
            this.contenido = contenido;
        }
    }

    public void insertar(Contenido contenido) {
        raiz = insertarRec(raiz, contenido);
    }

    private Nodo insertarRec(Nodo nodo, Contenido contenido) {
        if (nodo == null) return new Nodo(contenido);
        if (contenido.getTitulo().compareToIgnoreCase(nodo.contenido.getTitulo()) < 0)
            nodo.izquierda = insertarRec(nodo.izquierda, contenido);
        else
            nodo.derecha = insertarRec(nodo.derecha, contenido);
        return nodo;
    }

    public List<Contenido> buscarPorTitulo(String titulo) {
        List<Contenido> resultados = new ArrayList<>();
        buscarRec(raiz, titulo.toLowerCase(), resultados);
        return resultados;
    }

    private void buscarRec(Nodo nodo, String clave, List<Contenido> resultados) {
        if (nodo == null) return;
        if (nodo.contenido.getTitulo().toLowerCase().contains(clave))
            resultados.add(nodo.contenido);
        buscarRec(nodo.izquierda, clave, resultados);
        buscarRec(nodo.derecha, clave, resultados);
    }
}

