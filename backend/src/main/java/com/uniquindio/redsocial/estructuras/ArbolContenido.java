package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.Contenido;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@NoArgsConstructor
@Getter
public class ArbolContenido {

    private NodoContenido raiz;
    private int size;

    @Getter
    private static class NodoContenido {
        private Contenido contenido;
        private NodoContenido izquierdo;
        private NodoContenido derecho;
        private int altura;
        private int factorEquilibrio;

        public NodoContenido(Contenido contenido) {
            this.contenido = contenido;
            this.altura = 1;
            this.factorEquilibrio = 0;
        }
    }

    public void insertarContenido(Contenido contenido) {
        if (contenido == null) {
            throw new IllegalArgumentException("El contenido no puede ser nulo");
        }
        raiz = insertarAVL(raiz, contenido);
        size++;
    }

    private NodoContenido insertarAVL(NodoContenido nodo, Contenido contenido) {
        if (nodo == null) {
            return new NodoContenido(contenido);
        }

        int comparacion = contenido.getFechaPublicacion().compareTo(nodo.contenido.getFechaPublicacion());

        if (comparacion < 0) {
            nodo.izquierdo = insertarAVL(nodo.izquierdo, contenido);
        } else if (comparacion > 0) {
            nodo.derecho = insertarAVL(nodo.derecho, contenido);
        } else {
            if (contenido.getId() < nodo.contenido.getId()) {
                nodo.izquierdo = insertarAVL(nodo.izquierdo, contenido);
            } else {
                nodo.derecho = insertarAVL(nodo.derecho, contenido);
            }
        }

        return balancearNodo(nodo);
    }

    private NodoContenido balancearNodo(NodoContenido nodo) {
        actualizarAlturaYFE(nodo);

        if (nodo.factorEquilibrio > 1) {
            if (obtenerFE(nodo.izquierdo) >= 0) {
                return rotacionDerecha(nodo);
            } else {
                nodo.izquierdo = rotacionIzquierda(nodo.izquierdo);
                return rotacionDerecha(nodo);
            }
        }

        if (nodo.factorEquilibrio < -1) {
            if (obtenerFE(nodo.derecho) <= 0) {
                return rotacionIzquierda(nodo);
            } else {
                nodo.derecho = rotacionDerecha(nodo.derecho);
                return rotacionIzquierda(nodo);
            }
        }

        return nodo;
    }

    private NodoContenido rotacionDerecha(NodoContenido y) {
        NodoContenido x = y.izquierdo;
        NodoContenido T2 = x.derecho;

        x.derecho = y;
        y.izquierdo = T2;

        actualizarAlturaYFE(y);
        actualizarAlturaYFE(x);

        return x;
    }

    private NodoContenido rotacionIzquierda(NodoContenido x) {
        NodoContenido y = x.derecho;
        NodoContenido T2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = T2;

        actualizarAlturaYFE(x);
        actualizarAlturaYFE(y);

        return y;
    }

    private void actualizarAlturaYFE(NodoContenido nodo) {
        if (nodo == null) return;

        nodo.altura = Math.max(obtenerAltura(nodo.izquierdo), obtenerAltura(nodo.derecho)) + 1;
        nodo.factorEquilibrio = obtenerAltura(nodo.izquierdo) - obtenerAltura(nodo.derecho);
    }

    private int obtenerAltura(NodoContenido nodo) {
        return nodo == null ? 0 : nodo.altura;
    }

    private int obtenerFE(NodoContenido nodo) {
        return nodo == null ? 0 : nodo.factorEquilibrio;
    }

    public List<Contenido> buscarPorFecha(Date fecha) {
        List<Contenido> resultados = new ArrayList<>();
        buscarPorFechaRecursivo(raiz, fecha, resultados);
        return resultados;
    }

    private void buscarPorFechaRecursivo(NodoContenido nodo, Date fecha, List<Contenido> resultados) {
        if (nodo == null) return;

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(fecha);
        cal2.setTime(nodo.contenido.getFechaPublicacion());

        boolean mismoDia = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

        if (mismoDia) {
            resultados.add(nodo.contenido);
        }

        if (fecha.compareTo(nodo.contenido.getFechaPublicacion()) <= 0) {
            buscarPorFechaRecursivo(nodo.izquierdo, fecha, resultados);
        }
        if (fecha.compareTo(nodo.contenido.getFechaPublicacion()) >= 0) {
            buscarPorFechaRecursivo(nodo.derecho, fecha, resultados);
        }
    }

    public List<Contenido> buscarPorTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo no puede ser nulo o vacío");
        }

        List<Contenido> resultados = new ArrayList<>();
        buscarPorTipoRecursivo(raiz, tipo.toUpperCase(), resultados);
        return resultados;
    }

    private void buscarPorTipoRecursivo(NodoContenido nodo, String tipo, List<Contenido> resultados) {
        if (nodo == null) return;

        if (nodo.contenido.getTipo().equals(tipo)) {
            resultados.add(nodo.contenido);
        }

        buscarPorTipoRecursivo(nodo.izquierdo, tipo, resultados);
        buscarPorTipoRecursivo(nodo.derecho, tipo, resultados);
    }

    public List<Contenido> obtenerContenidoInOrder() {
        List<Contenido> resultado = new ArrayList<>();
        inOrderRecursivo(raiz, resultado);
        return resultado;
    }

    private void inOrderRecursivo(NodoContenido nodo, List<Contenido> resultado) {
        if (nodo == null) return;
        inOrderRecursivo(nodo.izquierdo, resultado);
        resultado.add(nodo.contenido);
        inOrderRecursivo(nodo.derecho, resultado);
    }

    public void eliminarContenido(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        raiz = eliminarContenidoRecursivo(raiz, id);
        size--;
    }

    private NodoContenido eliminarContenidoRecursivo(NodoContenido nodo, Long id) {
        if (nodo == null) {
            return null;
        }

        if (id < nodo.contenido.getId()) {
            nodo.izquierdo = eliminarContenidoRecursivo(nodo.izquierdo, id);
        } else if (id > nodo.contenido.getId()) {
            nodo.derecho = eliminarContenidoRecursivo(nodo.derecho, id);
        } else {
            if (nodo.izquierdo == null) {
                return nodo.derecho;
            } else if (nodo.derecho == null) {
                return nodo.izquierdo;
            }

            NodoContenido sucesor = encontrarMinimo(nodo.derecho);
            nodo.derecho = eliminarMinimo(nodo.derecho);
            sucesor.izquierdo = nodo.izquierdo;
            sucesor.derecho = nodo.derecho;
            return balancearNodo(sucesor);
        }

        return balancearNodo(nodo);
    }

    private NodoContenido eliminarMinimo(NodoContenido nodo) {
        if (nodo.izquierdo == null) {
            return nodo.derecho;
        }
        nodo.izquierdo = eliminarMinimo(nodo.izquierdo);
        return balancearNodo(nodo);
    }

    private NodoContenido encontrarMinimo(NodoContenido nodo) {
        while (nodo.izquierdo != null) {
            nodo = nodo.izquierdo;
        }
        return nodo;
    }

    // Métodos adicionales de utilidad
    public boolean estaVacio() {
        return raiz == null;
    }

    public int obtenerTamanio() {
        return size;
    }

    public void limpiarArbol() {
        raiz = null;
        size = 0;
    }

    public int obtenerAlturaArbol() {
        return obtenerAltura(raiz);
    }

    public boolean estaBalanceado() {
        return verificarBalance(raiz);
    }

    private boolean verificarBalance(NodoContenido nodo) {
        if (nodo == null) {
            return true;
        }

        int fe = obtenerFE(nodo);
        if (fe > 1 || fe < -1) {
            return false;
        }

        return verificarBalance(nodo.izquierdo) && verificarBalance(nodo.derecho);
    }
}