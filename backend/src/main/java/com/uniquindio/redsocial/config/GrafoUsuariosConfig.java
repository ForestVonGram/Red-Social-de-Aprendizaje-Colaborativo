package com.uniquindio.redsocial.config;

import com.uniquindio.redsocial.estructuras.GrafoUsuarios;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrafoUsuariosConfig {
    @Bean
    public GrafoUsuarios grafoUsuarios() {
        return new GrafoUsuarios();
    }
}
