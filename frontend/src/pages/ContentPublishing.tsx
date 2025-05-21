import React from 'react';
import '../styles/ContentPublishing.css';

// Vista monolítica para mostrar contenido publicado
const ContentPublishing: React.FC = () => {
    return (
        <div>
            {/* Fondo animado (opcional, aquí es solo la imagen de fondo CSS) */}
            <div className="animated-bg"></div>

            {/* Cabecera */}
            <header>
                <div className="logo">NeuronApp</div>
                <nav className="navbar">
                    <a href="#">Explore</a>
                    <a href="#">Community</a>
                    <a href="#">Messages</a>
                    <a href="#">Login</a>
                </nav>
            </header>

            {/* Contenido principal */}
            <main>
                {/* Contenedor multimedia */}
                <div className="media-container">
                    {/*
            Inserta dinámicamente el contenido multimedia con un método o props.
            Ejemplos de uso (solo uno debe estar activo a la vez):

            // Video
            <video src="../assets/demo.mp4" controls></video>

            // Imagen
            <img src="../assets/neurona.png" alt="Contenido publicado" />

            // PDF o documento embebido
            <iframe src="../assets/doc.pdf" width="100%" height="400px"></iframe>
          */}
                </div>

                {/* Título del contenido */}
                <h1 className="title">Introduction to Neuroscience</h1>

                {/* Descripción del contenido */}
                <p className="description">
                    Learn the basic concepts of neuroscience and understand how the brain functions.
                </p>

                {/* Fecha de publicación */}
                <p className="date">Published on March 15, 2024</p>
            </main>
        </div>
    );
};

export default ContentPublishing;
