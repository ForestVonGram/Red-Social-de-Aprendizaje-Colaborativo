import React, { useState } from "react";
import api from "../api/api";

const Login = () =>{
    const [credenciales, setCredenciales] = useState({
        correo: "",
        contrasenia: "",
    });
    const handleChange = (e) => {
        setCredenciales({ ...credenciales, [e.target.name]: e.target.value });
    };
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post("/login", null, {params: credenciales});
            localStorage.setItem("token", response.data.token);
            alert("Inicio de sesión exitoso");
        } catch (error) {
            alert("Credenciales incorrectas");
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input type="email" name="correo" placeholder="Correo" onChange={handleChange} />
            <input type="password" name="contrasenia" placeholder="Contraseña" onChange={handleChange} />
            <button type="submit">Iniciar Sesión</button>
        </form>
    );
};

export default Login;