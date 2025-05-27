package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.Contenido;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
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

    public void insertar(Contenido contenido) {
        insertarContenido(contenido);
    }

    public void eliminar(Contenido contenido) {
        if (contenido == null || contenido.getId() == null) {
            throw new IllegalArgumentException("El contenido o su ID no pueden ser nulos");
        }
        eliminarContenido(contenido.getId());
    }

    public List<Contenido> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede ser nulo o vacío");
        }

        List<Contenido> resultados = new ArrayList<>();
        buscarPorTituloRecursivo(raiz, titulo.toLowerCase(), resultados);
        return resultados;
    }

    private void buscarPorTituloRecursivo(NodoContenido nodo, String titulo, List<Contenido> resultados) {
        if (nodo == null) return;

        if (nodo.getContenido().getTitulo().toLowerCase().contains(titulo)) {
            resultados.add(nodo.getContenido());
        }

        buscarPorTituloRecursivo(nodo.getIzquierdo(), titulo, resultados);
        buscarPorTituloRecursivo(nodo.getDerecho(), titulo, resultados);
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
        cal2.setTime(Date.from(nodo.contenido.getFechaPublicacion().atZone(ZoneId.systemDefault()).toInstant()));

        boolean mismoDia = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

        if (mismoDia) {
            resultados.add(nodo.contenido);
        }

        if (!fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isAfter(nodo.contenido.getFechaPublicacion())) {
            buscarPorFechaRecursivo(nodo.izquierdo, fecha, resultados);
        }
        if (!fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isBefore(nodo.contenido.getFechaPublicacion())) {
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

        if (id < nodo.getContenido().getId()) {
            nodo.izquierdo = eliminarContenidoRecursivo(nodo.izquierdo, id);
        } else if (id > nodo.getContenido().getId()) {
            nodo.derecho = eliminarContenidoRecursivo(nodo.derecho, id);
        } else {
            // Caso 1: Nodo hoja o con un solo hijo
            if (nodo.izquierdo == null) {
                return nodo.derecho;
            } else if (nodo.derecho == null) {
                return nodo.izquierdo;
            }

            // Caso 2: Nodo con dos hijos
            NodoContenido sucesor = encontrarMinimo(nodo.derecho);
            nodo.contenido = sucesor.contenido;
            nodo.derecho = eliminarContenidoRecursivo(nodo.derecho, sucesor.contenido.getId());
        }

        return balancearNodo(nodo);
    }

    private void decrementarTamanio() {
        if (size > 0) {
            size--;
        }
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

    @Override
    public String toString() {
        return "ArbolContenido{" +
                "tamaño=" + size +
                ", altura=" + obtenerAlturaArbol() +
                ", balanceado=" + estaBalanceado() +
                '}';
    }
}