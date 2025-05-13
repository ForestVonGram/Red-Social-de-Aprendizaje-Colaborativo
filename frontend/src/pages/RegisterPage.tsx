import {FC, useState} from 'react';
import axios from 'axios';
import '../styles/RegisterPageStyle.css';

const RegisterPage: FC = () => {
    const [formData, setFormData] = useState({
        nombre: '',
        correo: '',
        contrasenia: ''
    });

    const [mensaje, setMensaje] = useState('');

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.id]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const payload = {
                nombre: formData.nombre,
                correo: formData.correo,
                contrasenia: formData.contrasenia
            };

            const response = await axios.post('http://localhost:3000/api/usuarios/registro', payload);
            setMensaje(response.data);
        } catch (error: any) {
            setMensaje('Error al registrar el usuario');
            console.error(error);
        }
    };

    return (
        <div id="register">
            <div id="futuristic-light-wav"></div>

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
                            <label className="input-label" htmlFor="name">Name</label>
                            <input type="text" id="name" className="input-field" placeholder="Enter your name" />
                        </div>

                        <div className="input-group">
                            <label className="input-label" htmlFor="email">Email</label>
                            <input type="email" id="email" className="input-field" placeholder="Enter your email" />
                        </div>

                        <div className="input-group">
                            <label className="input-label" htmlFor="password">Password</label>
                            <input type="password" id="password" className="input-field" placeholder="Enter your password" />
                        </div>

                        <div id="loginaccount">
                            <p id="alreadyhaveanaccount">Already have an account?</p>
                            <a href="/LoginPage" className="login-link">Log in</a>
                            <button id="webbuttonprimarydefa">
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
