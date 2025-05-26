package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.model.Valoracion;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Getter
public class HistorialContenidos {

    private static class NodoHistorial {
        private final Object dato;
        private final LocalDateTime fecha;
        private NodoHistorial siguiente;

        public NodoHistorial(Object dato) {
            this.dato = dato;
            this.fecha = LocalDateTime.now();
        }
    }

    private NodoHistorial cabeza;
    private int tamanio;
    private static final int LIMITE_HISTORIAL = 100;

    public void agregar(Object dato) {
        if (dato == null) {
            throw new IllegalArgumentException("No se puede agregar un dato nulo al historial");
        }

        NodoHistorial nuevo = new NodoHistorial(dato);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
        tamanio++;

        // Limitar el tamaño del historial
        if (tamanio > LIMITE_HISTORIAL) {
            eliminarUltimo();
        }
    }

    private void eliminarUltimo() {
        if (cabeza == null || cabeza.siguiente == null) {
            cabeza = null;
            tamanio = 0;
            return;
        }

        NodoHistorial actual = cabeza;
        while (actual.siguiente.siguiente != null) {
            actual = actual.siguiente;
        }
        actual.siguiente = null;
        tamanio--;
    }

    public List<Object> obtenerHistorialCompleto() {
        return toList();
    }

    public List<Object> obtenerHistorialPorFecha(LocalDateTime desde, LocalDateTime hasta) {
        List<Object> historialFiltrado = new ArrayList<>();
        NodoHistorial actual = cabeza;

        while (actual != null) {
            if (actual.fecha.isAfter(desde) && actual.fecha.isBefore(hasta)) {
                historialFiltrado.add(actual.dato);
            }
            actual = actual.siguiente;
        }

        return historialFiltrado;
    }

    private List<Object> toList() {
        List<Object> lista = new ArrayList<>();
        NodoHistorial actual = cabeza;
        while (actual != null) {
            lista.add(actual.dato);
            actual = actual.siguiente;
        }
        return lista;
    }

    public List<Contenido> obtenerContenidos() {
        return obtenerElementosPorTipo(Contenido.class);
    }

    public List<Valoracion> obtenerValoraciones() {
        return obtenerElementosPorTipo(Valoracion.class);
    }

    public List<Contenido> obtenerContenidosPorUsuario(Usuario usuario) {
        return obtenerContenidos().stream()
                .filter(contenido -> contenido.getAutor().equals(usuario))
                .collect(Collectors.toList());
    }

    public List<Valoracion> obtenerValoracionesPorContenido(Contenido contenido) {
        return obtenerValoraciones().stream()
                .filter(valoracion -> valoracion.getContenido().equals(contenido))
                .collect(Collectors.toList());
    }

    private <T> List<T> obtenerElementosPorTipo(Class<T> tipo) {
        List<T> elementos = new ArrayList<>();
        NodoHistorial actual = cabeza;

        while (actual != null) {
            if (tipo.isInstance(actual.dato)) {
                elementos.add(tipo.cast(actual.dato));
            }
            actual = actual.siguiente;
        }

        return elementos;
    }

    public void limpiarHistorial() {
        cabeza = null;
        tamanio = 0;
    }

    public boolean estaVacio() {
        return cabeza == null;
    }

    public Optional<Object> obtenerUltimoElemento() {
        return Optional.ofNullable(cabeza).map(nodo -> nodo.dato);
    }

    public boolean contiene(Object elemento) {
        NodoHistorial actual = cabeza;
        while (actual != null) {
            if (actual.dato.equals(elemento)) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }
}