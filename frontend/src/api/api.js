import axios from "axios";

const API_URL = "http://localhost:8080/usuarios";

const api = axios.create({
    baseURL: API_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

//Maneja errores globales
api.interceptors.response.use((response) => {
    return response;
}, (error) => {
    if (error.response && error.response.status === 401) {
        alert("Sesión expirada. Inicia sesión nuevamente.");
        localStorage.removeItem("token");
        window.location.href = "/login";
    }
    return Promise.reject(error);
});

export default api;