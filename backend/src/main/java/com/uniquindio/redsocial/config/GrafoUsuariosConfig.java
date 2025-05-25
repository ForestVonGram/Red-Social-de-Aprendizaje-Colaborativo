package com.uniquindio.redsocial.config;

import com.uniquindio.redsocial.estructuras.GrafoUsuarios;
import com.uniquindio.redsocial.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;

@Configuration
@EnableScheduling
@EnableCaching
@RequiredArgsConstructor
@Slf4j
public class GrafoUsuariosConfig {

    private final UsuarioRepository usuarioRepository;
    private GrafoUsuarios grafoUsuarios;

    @Bean
    public GrafoUsuarios grafoUsuarios() {
        log.info("Inicializando grafo de usuarios");
        grafoUsuarios = new GrafoUsuarios();
        cargarUsuariosIniciales();
        return grafoUsuarios;
    }

    private void cargarUsuariosIniciales() {
        try {
            log.info("Cargando usuarios iniciales al grafo");
            usuarioRepository.findAll().forEach(usuario -> {
                grafoUsuarios.agregarUsuario(usuario);
                log.debug("Usuario agregado al grafo: {}", usuario.getCorreo());
            });
            log.info("Carga inicial de usuarios completada");
        } catch (Exception e) {
            log.error("Error al cargar usuarios iniciales: {}", e.getMessage());
            throw new RuntimeException("Error al inicializar el grafo de usuarios", e);
        }
    }

    @Scheduled(fixedRate = 3600000) // Actualizar cada hora
    public void actualizarGrafo() {
        try {
            log.info("Iniciando actualización programada del grafo");
            GrafoUsuarios nuevoGrafo = new GrafoUsuarios();

            usuarioRepository.findAll().forEach(usuario -> {
                nuevoGrafo.agregarUsuario(usuario);
                log.debug("Usuario actualizado en el grafo: {}", usuario.getCorreo());
            });

            this.grafoUsuarios = nuevoGrafo;
            log.info("Actualización del grafo completada exitosamente");
        } catch (Exception e) {
            log.error("Error durante la actualización programada del grafo: {}", e.getMessage());
        }
    }

    @Bean
    public GrafoUsuarios.GrafoConfig grafoConfig() {
        return GrafoUsuarios.GrafoConfig.builder()
                .maxNodos(1000) // Límite máximo de nodos
                .maxConexionesPorUsuario(100) // Límite de conexiones por usuario
                .tiempoExpiracionCache(3600) // Tiempo de expiración de caché en segundos
                .habilitarVisualizacion(true) // Habilitar visualización del grafo
                .build();
    }

    public void reiniciarGrafo() {
        log.warn("Reiniciando grafo de usuarios");
        try {
            grafoUsuarios = new GrafoUsuarios();
            cargarUsuariosIniciales();
            log.info("Grafo reiniciado exitosamente");
        } catch (Exception e) {
            log.error("Error al reiniciar el grafo: {}", e.getMessage());
            throw new RuntimeException("Error al reiniciar el grafo", e);
        }
    }

    public boolean verificarIntegridadGrafo() {
        log.info("Verificando integridad del grafo");
        try {
            boolean integridadCorrecta = grafoUsuarios.verificarIntegridad();
            if (!integridadCorrecta) {
                log.warn("Se detectaron inconsistencias en el grafo");
                reiniciarGrafo();
            }
            return integridadCorrecta;
        } catch (Exception e) {
            log.error("Error al verificar la integridad del grafo: {}", e.getMessage());
            return false;
        }
    }
}