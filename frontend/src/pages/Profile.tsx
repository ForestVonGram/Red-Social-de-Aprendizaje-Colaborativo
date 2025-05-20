import React from "react";
import "../styles/Profile.css"; // Asegúrate de que existe y está configurado

const Profile: React.FC = () => {
return (
<div>
    {/* Fondo de imagen */}
    <div className="background-overlay"></div>

    {/* Navegación superior */}
    <nav className="nav-container">
        <a href="#" className="logo">NeuronApp</a>
        <div className="nav-links">
            <a href="#">Inicio</a>
            <a href="#" className="active">Perfil</a>
            <a href="#">Salir</a>
        </div>
    </nav>

    {/* Contenedor del perfil */}
    <div className="profile-container">
        <div className="profile-header">
            <div className="profile-info">
                <div className="profile-name">Juan Pérez</div>
                <div className="profile-role">Estudiante de Ingeniería</div>
                <div className="profile-interests">
                    Intereses: IA, Algoritmos, Redes
                </div>
            </div>
            <button className="edit-button">Editar perfil</button>
        </div>
    </div>

    {/* Tabs de navegación */}
    <div className="profile-tabs">
        <a href="#" className="profile-tab active">Publicaciones</a>
        <a href="#" className="profile-tab">Historial</a>
        <a href="#" className="profile-tab">Valoraciones</a>
        <a href="#" className="profile-tab">Grupos</a>
    </div>

    {/* Contenido publicado */}
    <div className="content-container">
        <div className="content-header">Contenidos publicados</div>

        <div className="content-item">
            <div className="content-title">Introducción a Redes Neuronales</div>
            <div className="content-rating">4.7</div>
        </div>

        <div className="content-item">
            <div className="content-title">Fundamentos de Algoritmos</div>
            <div className="content-rating">4.3</div>
        </div>

        <div className="content-item">
            <div className="content-title">Resumen de Arquitectura de Computadoras</div>
            <div className="content-rating">3.8</div>
        </div>
    </div>
</div>
);
};

export default Profile;
