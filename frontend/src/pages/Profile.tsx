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

    // Para obtener el correo del usuario, que ahora no está definido, asumamos que
    // está guardado en localStorage o en alguna parte accesible:
    const correo = localStorage.getItem("userCorreo");  // o la fuente correcta

    useEffect(() => {
        const fetchProfile = async () => {
            if (!correo) {
                setError("No se encontró correo del usuario");
                setLoading(false);
                return;
            }
            try {
                setLoading(true);
                const response = await axios.get(`/api/usuarios/${correo}`);
                setProfile(response.data);
            } catch (error: any) {
                console.error("Error al cargar el perfil:", error);
                setError("Error al cargar el perfil");
            } finally {
                setLoading(false);
            }
        };

        fetchProfile();
    }, [correo]);

    const handleLogout = () => {
        // Limpiar todas las claves de autenticación del localStorage
        localStorage.removeItem("userCorreo");
        localStorage.removeItem("userEmail");
        localStorage.removeItem("usuarioId");
        navigate("/LoginPage");
    };

    // Mostrar tabs dinámicamente según activeTab
    const renderContentByTab = () => {
        switch (activeTab) {
            case "publicaciones":
                return (
                    <div className="content-container">
                        <div className="content-header">Contenidos publicados</div>
                        {profile?.contenidos?.length ? (
                            profile.contenidos.map((contenido) => (
                                <div key={contenido.id} className="content-item">
                                    <div className="content-title">{contenido.titulo}</div>
                                    <div className="content-rating">{contenido.valoracionPromedio.toFixed(1)}</div>
                                </div>
                            ))
                        ) : (
                            <div>No hay contenidos publicados.</div>
                        )}
                    </div>
                );
            case "historial":
                return <div className="content-container">Historial (próximamente)</div>;
            case "valoraciones":
                return <div className="content-container">Valoraciones (próximamente)</div>;
            case "grupos":
                return <div className="content-container">Grupos (próximamente)</div>;
            default:
                return null;
        }
    };

    if (loading) return <div className="loading">Cargando perfil...</div>;
    if (error) return <div className="error">{error}</div>;
    if (!profile) return null;

    return (
        <div>
            <div className="background-overlay"></div>

            <nav className="nav-container">
                <a href="/" className="logo">
                    NeuronApp
                </a>
                <div className="nav-links">
                    <a href="/">Inicio</a>
                    <a href="/ViewContentPage">Explorar</a>
                    <a href="#" className="active">
                        Perfil
                    </a>
                    <a href="/Messages">Mensajes</a>
                    <a href="#" onClick={handleLogout}>
                        Salir
                    </a>
                    <a href="/Contenido">Publicar</a>
                    <a href="/GrafoView">Grafo</a>
                </div>
            </nav>

            <div className="profile-container">
                <div className="profile-header">
                    <div className="profile-info">
                        <div className="profile-name">{profile.nombre}</div>
                        <div className="profile-role">{profile.rol}</div>
                        <div className="profile-interests">Intereses: {profile?.intereses?.length ? profile.intereses.join(", ") : "No especificados"}</div>
                    </div>
                    <button className="edit-button">Editar perfil</button>
                </div>
            </div>

            <div className="profile-tabs">
                <a
                    href="#"
                    className={`profile-tab ${activeTab === "publicaciones" ? "active" : ""}`}
                    onClick={(e) => {
                        e.preventDefault();
                        setActiveTab("publicaciones");
                    }}
                >
                    Publicaciones
                </a>
                <a
                    href="#"
                    className={`profile-tab ${activeTab === "historial" ? "active" : ""}`}
                    onClick={(e) => {
                        e.preventDefault();
                        setActiveTab("historial");
                    }}
                >
                    Historial
                </a>
                <a
                    href="#"
                    className={`profile-tab ${activeTab === "valoraciones" ? "active" : ""}`}
                    onClick={(e) => {
                        e.preventDefault();
                        setActiveTab("valoraciones");
                    }}
                >
                    Valoraciones
                </a>
                <a
                    href="#"
                    className={`profile-tab ${activeTab === "grupos" ? "active" : ""}`}
                    onClick={(e) => {
                        e.preventDefault();
                        setActiveTab("grupos");
                    }}
                >
                    Grupos
                </a>
            </div>

            {renderContentByTab()}
        </div>
    );
};

export default Profile;
