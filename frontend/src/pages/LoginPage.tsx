import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../styles/LoginPageStyle.css";

const LoginPage: React.FC = () => {
    const navigate = useNavigate();
    const [correo, setCorreo] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError("");
        setLoading(true);
        try {
            const loginRes = await axios.post('/api/login', {
                correo,
                contrasenia: password
            });

            if (loginRes.data && loginRes.data.usuario) {
                // Store user data in localStorage
                localStorage.setItem("userCorreo", loginRes.data.usuario.correo);
                localStorage.setItem("userEmail", loginRes.data.usuario.correo);
                localStorage.setItem("usuarioId", loginRes.data.usuario.id);
                navigate("/Profile");
            } else {
                setError("Error en la autenticación");
            }
        } catch (err: any) {
            console.error("Error de login:", err);
            setError(err.response?.data?.mensaje || "Error al iniciar sesión");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-container">
            <form className="login-form" onSubmit={handleSubmit}>
                <div className="login-title">NeuronApp - Iniciar Sesión</div>
                <input
                    className="login-input"
                    type="email"
                    placeholder="Correo electrónico"
                    value={correo}
                    onChange={e => setCorreo(e.target.value)}
                    required
                    autoFocus
                />
                <input
                    className="login-input"
                    type="password"
                    placeholder="Contraseña"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    required
                />
                <button className="login-button" type="submit" disabled={loading}>
                    {loading ? "Entrando..." : "Entrar"}
                </button>
                {error && <div className="login-error">{error}</div>}
                {loading && <div className="login-loader">Verificando...</div>}
            </form>
        </div>
    );
};

export default LoginPage;
