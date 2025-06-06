package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.Usuario;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Getter
public class GrafoUsuarios {
    private final Map<String, Usuario> usuarios = new HashMap<>();
    private final Map<String, Set<String>> conexiones = new HashMap<>();

    public Usuario getUsuario(String correo) {
        return usuarios.get(correo);
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.putIfAbsent(usuario.getCorreo(), usuario);
        conexiones.putIfAbsent(usuario.getCorreo(), new HashSet<>());
    }

    public void eliminarUsuario(String correo) {
        if (usuarios.containsKey(correo)) {
            Set<String> conexionesUsuario = conexiones.get(correo);
            if (conexionesUsuario != null) {
                for (String conexion : new HashSet<>(conexionesUsuario)) {
                    eliminarConexion(correo, conexion);
                }
            }
            usuarios.remove(correo);
            conexiones.remove(correo);
        }
    }

    public void conectarUsuarios(String correo1, String correo2) {
        if (usuarios.containsKey(correo1) && usuarios.containsKey(correo2)) {
            conexiones.get(correo1).add(correo2);
            conexiones.get(correo2).add(correo1);
        }
    }

    public void eliminarConexion(String correo1, String correo2) {
        if (usuarios.containsKey(correo1) && usuarios.containsKey(correo2)) {
            conexiones.getOrDefault(correo1, new HashSet<>()).remove(correo2);
            conexiones.getOrDefault(correo2, new HashSet<>()).remove(correo1);
        }
    }

    public boolean estanConectados(String correo1, String correo2) {
        return conexiones.getOrDefault(correo1, new HashSet<>()).contains(correo2);
    }

    public Set<Usuario> obtenerAmigos(String correo) {
        Set<Usuario> amigos = new HashSet<>();
        Set<String> conexionesUsuario = conexiones.getOrDefault(correo, new HashSet<>());
        for (String correoAmigo : conexionesUsuario) {
            amigos.add(usuarios.get(correoAmigo));
        }
        return amigos;
    }

    public Set<Usuario> recomendarAmigos(String correo) {
        Set<Usuario> recomendados = new HashSet<>();
        Set<String> amigosDirectos = conexiones.getOrDefault(correo, new HashSet<>());

        for (String amigo : amigosDirectos) {
            Set<String> amigosDeAmigo = conexiones.getOrDefault(amigo, new HashSet<>());
            for (String posible : amigosDeAmigo) {
                if (!posible.equals(correo) && !amigosDirectos.contains(posible)) {
                    recomendados.add(usuarios.get(posible));
                }
            }
        }

        return recomendados;
    }

    public List<Usuario> buscarRutaMasCorta(String correoOrigen, String correoDestino) {
        if (!usuarios.containsKey(correoOrigen) || !usuarios.containsKey(correoDestino)) {
            return Collections.emptyList();
        }

        Queue<String> cola = new LinkedList<>();
        Map<String, String> predecesores = new HashMap<>();
        Set<String> visitados = new HashSet<>();

        cola.add(correoOrigen);
        visitados.add(correoOrigen);

        while (!cola.isEmpty()) {
            String actual = cola.poll();

            for (String vecino : conexiones.getOrDefault(actual, new HashSet<>())) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    predecesores.put(vecino, actual);
                    cola.add(vecino);

                    if (vecino.equals(correoDestino)) {
                        List<Usuario> ruta = new LinkedList<>();
                        String paso = correoDestino;

                        while (paso != null) {
                            ruta.add(0, usuarios.get(paso));
                            paso = predecesores.get(paso);
                        }

                        return ruta;
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    public List<Usuario> obtenerUsuariosConMasConexiones(int top) {
        List<Map.Entry<String, Integer>> listaConexiones = new ArrayList<>();

        for (Map.Entry<String, Set<String>> entry : conexiones.entrySet()) {
            listaConexiones.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().size()));
        }

        listaConexiones.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<Usuario> resultado = new ArrayList<>();

        for (int i = 0; i < Math.min(top, listaConexiones.size()); i++) {
            String correo = listaConexiones.get(i).getKey();
            resultado.add(usuarios.get(correo));
        }

        return resultado;
    }

    public Set<Usuario> obtenerUsuariosConectadosIndirectamente(String correo) {
        Set<Usuario> conectados = new HashSet<>();
        if (!usuarios.containsKey(correo)) return conectados;

        Set<String> visitados = new HashSet<>();
        Queue<String> cola = new LinkedList<>();

        cola.add(correo);
        visitados.add(correo);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            for (String vecino : conexiones.getOrDefault(actual, new HashSet<>())) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.add(vecino);
                    conectados.add(usuarios.get(vecino));
                }
            }
        }
        return conectados;
    }
    public List<Set<Usuario>> detectarClusters() {
        List<Set<Usuario>> clusters = new ArrayList<>();
        Set<String> visitados = new HashSet<>();

        for (String correo : usuarios.keySet()) {
            if (!visitados.contains(correo)) {
                Set<Usuario> cluster = new HashSet<>();
                explorarCluster(correo, visitados, cluster);
                clusters.add(cluster);
            }
        }

        return clusters;
    }

    private void explorarCluster(String correo, Set<String> visitados, Set<Usuario> cluster) {
        Queue<String> cola = new LinkedList<>();
        cola.add(correo);
        visitados.add(correo);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            cluster.add(usuarios.get(actual));

            for (String vecino : conexiones.getOrDefault(actual, new HashSet<>())) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.add(vecino);
                }
            }
        }
    }

    public boolean verificarIntegridad() {
        try {
            // 1. Verificar que todos los usuarios en conexiones existan en el mapa de usuarios
            for (String correo : conexiones.keySet()) {
                if (!usuarios.containsKey(correo)) {
                    return false;
                }
            }

            // 2. Verificar que todos los usuarios tengan su entrada en el mapa de conexiones
            for (String correo : usuarios.keySet()) {
                if (!conexiones.containsKey(correo)) {
                    return false;
                }
            }

            // 3. Verificar que las conexiones sean bidireccionales
            for (Map.Entry<String, Set<String>> entry : conexiones.entrySet()) {
                String correo1 = entry.getKey();
                for (String correo2 : entry.getValue()) {
                    // Verificar que el usuario conectado existe
                    if (!usuarios.containsKey(correo2)) {
                        return false;
                    }
                    // Verificar que la conexión es bidireccional
                    if (!conexiones.get(correo2).contains(correo1)) {
                        return false;
                    }
                }
            }

            // 4. Verificar que no haya auto-conexiones
            for (Map.Entry<String, Set<String>> entry : conexiones.entrySet()) {
                if (entry.getValue().contains(entry.getKey())) {
                    return false;
                }
            }

            // 5. Verificar que no haya usuarios null o conexiones null
            if (usuarios.containsKey(null) || usuarios.containsValue(null) ||
                    conexiones.containsKey(null) || conexiones.containsValue(null)) {
                return false;
            }

            // 6. Verificar que no haya correos vacíos
            for (String correo : usuarios.keySet()) {
                if (correo == null || correo.trim().isEmpty()) {
                    return false;
                }
            }

            // 7. Verificar que cada usuario tenga un objeto Usuario válido
            for (Usuario usuario : usuarios.values()) {
                if (usuario == null || usuario.getCorreo() == null ||
                        !usuario.getCorreo().equals(usuarios.get(usuario.getCorreo()).getCorreo())) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            // Si ocurre cualquier excepción durante la verificación, consideramos que el grafo no es íntegro
            return false;
        }
    }

    @Builder
    @Data
    public static class GrafoConfig {
        private int maxNodos;
        private int maxConexionesPorUsuario;
        private int tiempoExpiracionCache;
        private boolean habilitarVisualizacion;
    }
}
