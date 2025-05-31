import React, { useEffect } from "react";
import axios from "axios";
import "../styles/ContentPublishing.css";

const ViewContentPage: React.FC = () => {
    useEffect(() => {
        const fetchContent = async () => {
            try {
                const response = await axios.get("/api/contenido");
                console.log("Contenido: ", response.data);
            } catch (error: any) {
                console.error("Error al cargar contenido:", error.message);
            }
        };
        fetchContent();
    }, []);

    return (
        <>
            <div className="animated-bg"></div>

            <header>
                <div className="logo">NeuronApp</div>
                <nav className="navbar">
                    <a href="#">Search</a>
                    <a href="#">Community</a>
                    <a href="Messages">Messages</a>
                    <a href="/Profile">Profile</a>
                </nav>
            </header>

            <main>
                <div className="media-container">
                    {/* Ejemplo de vídeo */}
                    <video src="../assets/neurociencia.mp4" controls />
                </div>

                <h1 className="title">Introduction to Neuroscience</h1>
                <p className="description">
                    Learn the basic concepts of neuroscience and understand how the brain
                    functions.
                </p>
                <p className="date">Published on March 15, 2024</p>
            </main>
        </>
    );
};

export default ViewContentPage;
