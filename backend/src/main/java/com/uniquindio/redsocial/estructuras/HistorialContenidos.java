package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.model.Valoracion;

import java.util.ArrayList;
import java.util.List;

public class HistorialContenidos {
    private class NodoHistorial {
        Object dato;
        NodoHistorial siguiente;

        public NodoHistorial(Object dato) {
            this.dato = dato;
        }
    }

    private NodoHistorial cabeza;

    public void agregar(Object dato) {
        NodoHistorial nuevo = new NodoHistorial(dato);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
    }

    public List<Object> toList() {
        List<Object> lista = new ArrayList<>();
        NodoHistorial actual = cabeza;
        while (actual != null) {
            lista.add(actual.dato);
            actual = actual.siguiente;
        }
        return lista;
    }

    public List<Contenido> getContenidos() {
        List<Contenido> contenidos = new ArrayList<>();
        NodoHistorial actual = cabeza;
        while (actual != null) {
            if (actual.dato instanceof Contenido contenido)
                contenidos.add(contenido);
            actual = actual.siguiente;
        }
        return contenidos;
    }

    public List<Valoracion> getValoraciones() {
        List<Valoracion> valoraciones = new ArrayList<>();
        NodoHistorial actual = cabeza;
        while (actual != null) {
            if (actual.dato instanceof Valoracion valoracion)
                valoraciones.add(valoracion);
            actual = actual.siguiente;
        }

        return valoraciones;
    }
}
