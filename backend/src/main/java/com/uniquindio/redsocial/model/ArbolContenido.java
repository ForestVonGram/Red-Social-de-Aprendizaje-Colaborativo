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

    public void elimminarPorTitulo(String titulo) {
        raiz = eliminarRec(raiz, titulo.toLowerCase());
    }

    private Nodo eliminarRec(Nodo nodo, String titulo) {
        if (nodo == null) return null;

        int comparacion = titulo.compareTo(nodo.contenido.getTitulo().toLowerCase());

        if (comparacion < 0) {
            nodo.izquierda = eliminarRec(nodo.izquierda, titulo);
        } else if (comparacion > 0) {
            nodo.derecha = eliminarRec(nodo.derecha, titulo);
        } else {
            if (nodo.izquierda == null && nodo.derecha == null) {
                return null;
            }

            if (nodo.izquierda == null) return nodo.derecha;
            if (nodo.derecha == null) return nodo.izquierda;

            Nodo sucesor = encontrarMinimo(nodo.derecha);
            nodo.contenido = sucesor.contenido;
            nodo.derecha = eliminarRec(nodo.derecha, sucesor.contenido.getTitulo().toLowerCase());
        }

        return nodo;
    }

    private Nodo encontrarMinimo(Nodo nodo) {
        while (nodo.izquierda != null) {
            nodo = nodo.izquierda;
        }
        return nodo;
    }
}

