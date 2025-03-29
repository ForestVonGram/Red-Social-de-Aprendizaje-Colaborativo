import React, { useState } from "react";
import api from "../api/api";

const Register = () => {
    const [usuario, setUsuario] = useState({
        nombre: "",
        correo: "",
        contrasenia: "",
    });

    const handleChange = (e) => {
        setUsuario({ ...usuario, [e.target.name]: e.target.value });
    };
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post("/registrar", usuario);
            alert(response.data);
        } catch (error) {
            console.error("Error en el registro: ", error.response.data);
        }
    };
    return (
        <form onSubmit={handleSubmit}>
            <input type="text" name="nombre" placeholder="Nombre" onChange={handleChange} />
            <input type="email" name="correo" placeholder="Correo" onChange={handleChange} />
            <input type="password" name="contrasenia" placeholder="Contraseña" onChange={handleChange} />
            <button type="submit">Registrar</button>
        </form>
    );
};

export default Register;