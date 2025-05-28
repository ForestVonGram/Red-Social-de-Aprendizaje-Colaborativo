import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../styles/Profile.css";

interface UserProfile {
    id: number;
    nombre: string;
    correo: string;
    rol: string;
    intereses: string[];
    contenidos: Contenido[];
}

interface Contenido {
    id: number;
    titulo: string;
    valoracionPromedio: number;
}

const Profile: React.FC = () => {
    const navigate = useNavigate();
    const [profile, setProfile] = useState<UserProfile | null>(null);
    const [activeTab, setActiveTab] = useState("publicaciones");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const userEmail = localStorage.getItem("userEmail");
        const token = localStorage.getItem("token");

        if (!userEmail || !token) {
            navigate("/LoginPage");
            return;
        }

        const fetchProfile = async () => {
            try {
                setLoading(true);
                const response = await axios.get(
                    `/api/usuarios/${userEmail}`,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            'Content-Type': 'application/json'
                        }
                    }
                );
                setProfile(response.data);
            } catch (error: any) {
                console.error("Error al cargar el perfil:", error);
                if (error.response?.status === 401) {
                    localStorage.clear();
                    navigate("/LoginPage");
                } else {
                    setError("Error al cargar el perfil");
                }
            } finally {
                setLoading(false);
            }
        };

        fetchProfile();
    }, [navigate]);

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("userEmail");
        localStorage.removeItem("usuarioId");
        navigate("/LoginPage");
    };

    if (loading) return <div className="loading">Cargando perfil...</div>;
    if (error) return <div className="error">{error}</div>;
    if (!profile) return null;

    return (
        <div>
            <div className="background-overlay"></div>

            <nav className="nav-container">
                <a href="/" className="logo">NeuronApp</a>
                <div className="nav-links">
                    <a href="/">Inicio</a>
                    <a href="#" className="active">Perfil</a>
                    <a href="#" onClick={handleLogout}>Salir</a>
                </div>
            </nav>

            <div className="profile-container">
                <div className="profile-header">
                    <div className="profile-info">
                        <div className="profile-name">{profile.nombre}</div>
                        <div className="profile-role">{profile.rol}</div>
                        <div className="profile-interests">
                            Intereses: {profile.intereses.join(", ")}
                        </div>
                    </div>
                    <button className="edit-button">Editar perfil</button>
                </div>
            </div>

            <div className="profile-tabs">
                <a href="#" className={`profile-tab ${activeTab === "publicaciones" ? "active" : ""}`}
                   onClick={() => setActiveTab("publicaciones")}>Publicaciones</a>
                <a href="#" className={`profile-tab ${activeTab === "historial" ? "active" : ""}`}
                   onClick={() => setActiveTab("historial")}>Historial</a>
                <a href="#" className={`profile-tab ${activeTab === "valoraciones" ? "active" : ""}`}
                   onClick={() => setActiveTab("valoraciones")}>Valoraciones</a>
                <a href="#" className={`profile-tab ${activeTab === "grupos" ? "active" : ""}`}
                   onClick={() => setActiveTab("grupos")}>Grupos</a>
            </div>

            <div className="content-container">
                <div className="content-header">Contenidos publicados</div>
                {profile.contenidos?.map(contenido => (
                    <div key={contenido.id} className="content-item">
                        <div className="content-title">{contenido.titulo}</div>
                        <div className="content-rating">{contenido.valoracionPromedio.toFixed(1)}</div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Profile;