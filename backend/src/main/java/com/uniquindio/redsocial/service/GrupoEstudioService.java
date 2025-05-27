package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.estructuras.GrafoUsuarios;
import com.uniquindio.redsocial.model.GrupoEstudio;
import com.uniquindio.redsocial.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GrupoEstudioService {
    private final GrafoUsuarios grafoUsuarios = new GrafoUsuarios();
    private final Map<String, Usuario> usuarios = new HashMap<>();

    /**
     * Registra un usuario en el sistema para participar en grupos de estudio.
     * @param usuario El usuario a registrar
     * @throws IllegalArgumentException si el usuario es nulo o sus datos son inválidos
     * @throws IllegalStateException si el usuario ya está registrado
     */
    public void registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo del usuario no puede estar vacío");
        }
        if (usuarios.containsKey(usuario.getCorreo())) {
            throw new IllegalStateException("El usuario con correo " + usuario.getCorreo() + " ya está registrado");
        }

        usuarios.put(usuario.getCorreo(), usuario);
        grafoUsuarios.agregarUsuario(usuario);
    }

    /**
     * Elimina un usuario del sistema.
     * @param correo El correo del usuario a eliminar
     * @throws IllegalArgumentException si el correo es nulo o vacío
     * @throws IllegalStateException si el usuario no está registrado
     */
    public void eliminarUsuario(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }
        if (!usuarios.containsKey(correo)) {
            throw new IllegalStateException("El usuario con correo " + correo + " no está registrado");
        }

        usuarios.remove(correo);
        grafoUsuarios.eliminarUsuario(correo);
    }

    /**
     * Conecta dos usuarios en el grafo.
     * @param correo1 El correo del primer usuario
     * @param correo2 El correo del segundo usuario
     * @throws IllegalArgumentException si alguno de los correos es nulo o vacío
     * @throws IllegalStateException si alguno de los usuarios no está registrado
     */
    public void conectarUsuarios(String correo1, String correo2) {
        if (correo1 == null || correo1.trim().isEmpty() || correo2 == null || correo2.trim().isEmpty()) {
            throw new IllegalArgumentException("Los correos no pueden estar vacíos");
        }
        if (!usuarios.containsKey(correo1)) {
            throw new IllegalStateException("El usuario con correo " + correo1 + " no está registrado");
        }
        if (!usuarios.containsKey(correo2)) {
            throw new IllegalStateException("El usuario con correo " + correo2 + " no está registrado");
        }

        grafoUsuarios.conectarUsuarios(correo1, correo2);
    }

    /**
     * Forma grupos de estudio basados en intereses comunes entre usuarios conectados.
     * @return Lista de grupos de estudio formados
     */
    public List<GrupoEstudio> formarGruposPorIntereses() {
        if (usuarios.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> procesados = new HashSet<>();
        List<GrupoEstudio> grupos = new ArrayList<>();

        for (Usuario usuario : usuarios.values()) {
            if (procesados.contains(usuario.getCorreo())) continue;

            Set<Usuario> relacionados = grafoUsuarios.obtenerAmigos(usuario.getCorreo());
            if (relacionados.isEmpty()) continue;

            List<Usuario> miembrosGrupo = new ArrayList<>();
            miembrosGrupo.add(usuario);

            for (Usuario posible : relacionados) {
                if (!procesados.contains(posible.getCorreo()) &&
                        tienenInteresesEnComun(usuario, posible)) {
                    miembrosGrupo.add(posible);
                }
            }

            if (miembrosGrupo.size() > 1) {
                String temaComun = obtenerTemaComun(miembrosGrupo);
                // Crear el grupo con el primer usuario como líder y el tema común
                GrupoEstudio nuevoGrupo = new GrupoEstudio(temaComun, usuario);

                // Agregar los demás miembros (el líder ya está incluido por el constructor)
                for (int i = 1; i < miembrosGrupo.size(); i++) {
                    nuevoGrupo.agregarMiembro(miembrosGrupo.get(i));
                }

                grupos.add(nuevoGrupo);

                // Marcar todos los miembros como procesados
                for (Usuario u : miembrosGrupo) {
                    procesados.add(u.getCorreo());
                }
            }
        }

        return grupos;
    }

    /**
     * Verifica si dos usuarios tienen al menos un interés en común.
     * @param u1 Primer usuario
     * @param u2 Segundo usuario
     * @return true si tienen al menos un interés en común, false en caso contrario
     * @throws IllegalArgumentException si alguno de los usuarios es nulo
     */
    private boolean tienenInteresesEnComun(Usuario u1, Usuario u2) {
        if (u1 == null || u2 == null) {
            throw new IllegalArgumentException("Los usuarios no pueden ser nulos");
        }

        if (u1.getIntereses() == null || u2.getIntereses() == null) {
            return false;
        }

        for (String interes : u1.getIntereses()) {
            if (interes != null && u2.getIntereses().contains(interes)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene el tema más común entre un grupo de usuarios.
     * @param grupo Lista de usuarios
     * @return El interés más común, o "Interés General" si no hay intereses o el grupo está vacío
     * @throws IllegalArgumentException si el grupo es nulo
     */
    private String obtenerTemaComun(List<Usuario> grupo) {
        if (grupo == null) {
            throw new IllegalArgumentException("El grupo no puede ser nulo");
        }

        if (grupo.isEmpty()) {
            return "Interés General";
        }

        Map<String, Integer> contador = new HashMap<>();

        for (Usuario u : grupo) {
            if (u != null && u.getIntereses() != null) {
                for (String interes : u.getIntereses()) {
                    if (interes != null && !interes.trim().isEmpty()) {
                        contador.put(interes, contador.getOrDefault(interes, 0) + 1);
                    }
                }
            }
        }

        if (contador.isEmpty()) {
            return "Interés General";
        }

        return contador.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Interés General");
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     * @return Lista de todos los usuarios
     */
    public List<Usuario> obtenerTodosLosUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    /**
     * Obtiene un usuario por su correo.
     * @param correo El correo del usuario
     * @return El usuario encontrado
     * @throws IllegalArgumentException si el correo es nulo o vacío
     * @throws IllegalStateException si el usuario no está registrado
     */
    public Usuario obtenerUsuario(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }

        Usuario usuario = usuarios.get(correo);
        if (usuario == null) {
            throw new IllegalStateException("El usuario con correo " + correo + " no está registrado");
        }

        return usuario;
    }
}
