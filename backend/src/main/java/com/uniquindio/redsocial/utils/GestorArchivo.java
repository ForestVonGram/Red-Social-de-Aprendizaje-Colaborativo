package com.uniquindio.redsocial.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.uniquindio.redsocial.estructuras.GrafoUsuarios;
import com.uniquindio.redsocial.model.Usuario;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GestorArchivo {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void guardarUsuarios(List<Usuario> usuarios, String ruta) throws IOException {
        try (Writer writer = new FileWriter(ruta)) {
            gson.toJson(usuarios, writer);
        }
    }

    public static List<Usuario> cargarUsuarios(String ruta) throws IOException {
        try (Reader reader = new FileReader(ruta)) {
            Type tipoLista = new TypeToken<List<Usuario>>() {}.getType();
            return gson.fromJson(reader, tipoLista);
        }
    }

    public static void guardarConexiones(Map<String, Set<String>> conexiones, String ruta) throws IOException {
        try (Writer writer = new FileWriter(ruta)) {
            gson.toJson(conexiones, writer);
        }
    }

    public static Map<String, Set<String>> cargarConexiones(String ruta) throws IOException {
        try (Reader reader = new FileReader(ruta)) {
            Type tipoMapa = new TypeToken<Map<String, Set<String>>>() {}.getType();
            return gson.fromJson(reader, tipoMapa);
        }
    }

    public static void guardarTodo(GrafoUsuarios grafo, String rutaUsuarios, String rutaConexiones) throws IOException {
        guardarUsuarios(new ArrayList<>(grafo.getUsuarios().values()), rutaUsuarios);
        guardarConexiones(grafo.getConexiones(), rutaConexiones);
    }

    public static void cargarTodo(GrafoUsuarios grafo, String rutaUsuarios, String rutaConexiones) throws IOException {
        List<Usuario> usuarios = cargarUsuarios(rutaUsuarios);
        Map<String, Set<String>> conexiones = cargarConexiones(rutaConexiones);

        for (Usuario usuario : usuarios) {
            grafo.agregarUsuario(usuario);
        }

        for (Map.Entry<String, Set<String>> entry : conexiones.entrySet()) {
            for (String amigo : entry.getValue()) {
                grafo.conectarUsuarios(entry.getKey(), amigo);
            }
        }
    }
}
