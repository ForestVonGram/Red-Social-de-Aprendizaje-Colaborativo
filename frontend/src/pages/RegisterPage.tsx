/// <reference types="vite/client" />
import {FC, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/RegisterPageStyle.css';

const RegisterPage: FC = () => {
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        nombre: '',
        correo: '',
        contrasenia: ''
    });

    const [mensaje, setMensaje] = useState('');

    const validarFormulario = () => {
        if (!formData.nombre.trim()) return "El nombre es requerido";
        if (!formData.correo.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) return "Email inválido";
        if (formData.contrasenia.length < 6) return "La contraseña debe tener al menos 6 caracteres";
        return null;
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.id]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const error = validarFormulario();
        if (error) {
            setMensaje(error);
            return;
        }

        const payload = {
            nombre: formData.nombre,
            correo: formData.correo,
            contrasenia: formData.contrasenia,
            intereses: []
        };

        setIsLoading(true);
        try {
            const response = await axios.post(`${import.meta.env.VITE_API_URL}/api/register`, payload);
            navigate('/LoginPage', { state: { message: 'Registro exitoso' } });
        } catch (error: any) {
            setMensaje(error.response?.data?.mensaje || 'Error al registrar el usuario');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div id="register">

            <div id="titledev">
                <h1 id="neuronapp">NeuronApp</h1>
                <div id="path"></div>
                <p id="learninghappenstroug">Learning happens through connection</p>
            </div>

            <div id="registerdev">
                <div id="rectangle">
                    <h2 className="input-label" style={{ textAlign: "center", marginBottom: "2rem" }}>
                        Register
                    </h2>
                        <form onSubmit={handleSubmit}>
                            <div className="input-group">
                                <label className="input-label" htmlFor="nombre">Name</label>
                                <input
                                    type="text"
                                    id="nombre"
                                    className="input-field"
                                    placeholder="Enter your name"
                                    value={formData.nombre}
                                    onChange={handleChange}
                                />
                            </div>

                            <div className="input-group">
                                <label className="input-label" htmlFor="correo">Email</label>
                                <input
                                    type="email"
                                    id="correo"
                                    className="input-field"
                                    placeholder="Enter your email"
                                    value={formData.correo}
                                    onChange={handleChange}
                                />
                            </div>

                            <div className="input-group">
                                <label className="input-label" htmlFor="contrasenia">Password</label>
                                <input
                                    type="password"
                                    id="contrasenia"
                                    className="input-field"
                                    placeholder="Enter your password"
                                    value={formData.contrasenia}
                                    onChange={handleChange}
                                />
                            </div>

                            <div id="loginaccount">
                                <p id="alreadyhaveanaccount">Already have an account?</p>
                                <a href="/LoginPage" className="login-link">Log in</a>
                                <button id="webbuttonprimarydefa" type="submit">
                                    <span id="label">Register</span>
                                </button>
                            </div>
                        </form>

                    {mensaje && <p style={{ textAlign: "center", marginTop: "1rem" }}>{mensaje}</p>}
                </div>
            </div>
        </div>
    );
};

export default RegisterPage;
