import React from 'react';
import '../styles/Profilev2.css'; // Importamos el CSS para estilos

const Profile: React.FC = () => {
    return (
        <>
            {/* Fondo con overlay que cubre toda la pantalla */}
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

            {/* Contenedor principal del perfil */}
            <div className="profile-container">
                <div className="profile-header">
                    {/* Imagen del avatar */}
                    <img src="../assets/images/avatar.jpg" alt="Avatar" className="profile-avatar" />
                    {/* Información del usuario */}
                    <div className="profile-info">
                        <div className="profile-name">Juan Pérez</div>
                        <div className="profile-role">Estudiante de Ingeniería</div>
                        <div className="profile-interests">Intereses: IA, Algoritmos, Redes</div>
                    </div>
                    {/* Botón para editar perfil */}
                    <button className="edit-button">Editar perfil</button>
                </div>
            </div>

            {/* Tabs de navegación dentro del perfil */}
            <div className="profile-tabs">
                <a href="#" className="profile-tab active">Publicaciones</a>
                <a href="#" className="profile-tab">Historial</a>
                <a href="#" className="profile-tab">Valoraciones</a>
                <a href="#" className="profile-tab">Grupos</a>
                {/* Botón especial para publicar contenido */}
                <a href="/publish-content" className="profile-tab publish-tab">+ Publicar</a>
            </div>

            {/* Contenedor con los contenidos publicados */}
            <div className="content-container">
                <div className="content-header">Contenidos publicados</div>

                {/* Cada contenido publicado */}
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
        </>
    );
};

export default Profile;
