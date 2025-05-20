import React, { useState } from "react";
import axios from "axios";
import "../styles/LoginPageStyle.css";

const LoginPage: React.FC = () => {
    const [correo, setCorreo] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError("");
        setLoading(true);
        try {
            const loginRes = await axios.post("/api/login", { correo, password });
            if (loginRes.data === true) {
                const userRes = await axios.get(`/api/login/${correo}`);
                if (userRes.data && userRes.data.id) {
                    localStorage.setItem("usuarioId", userRes.data.id);
                    window.location.href = "/Messages";
                } else {
                    setError("No se pudo obtener el usuario.");
                }
            } else {
                setError("Credenciales incorrectas.");
            }
        } catch (err) {
            setError("Error al iniciar sesión.");
        }
        setLoading(false);
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