import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/ContentPublishing.css';

interface Content {
    id: number;
    titulo: string;
    descripcion: string;
    tipo: string;
    url: string;
    fechaPublicacion: string;
    autor: {
        nombre: string;
        correo: string;
    };
    valoracionPromedio: number;
}

const ContentPublishing: React.FC = () => {
    const navigate = useNavigate();
    const [content, setContent] = useState<Content[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [newContent, setNewContent] = useState({
        titulo: '',
        descripcion: '',
        tipo: 'texto',
        url: ''
    });

    useEffect(() => {
        fetchContent();
    }, []);

    const fetchContent = async () => {
        try {
            const response = await axios.get('/api/contenido');
            setContent(response.data);
            setError(null);
        } catch (error: any) {
            console.error('Error al cargar contenido:', error);
            setError('Error al cargar el contenido');
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await axios.post('/api/contenido/publicar', newContent);
            setNewContent({
                titulo: '',
                descripcion: '',
                tipo: 'texto',
                url: ''
            });
            fetchContent();
        } catch (error: any) {
            console.error('Error al publicar contenido:', error);
            setError('Error al publicar el contenido');
        }
    };

    if (loading) return <div className="loading">Cargando contenido...</div>;
    if (error) return <div className="error">{error}</div>;

    return (
        <div className="content-publishing">
            <div className="animated-bg"></div>

            <header className="header">
                <div className="logo">NeuronApp</div>
                <nav className="navbar">
                    <a href="/">Inicio</a>
                    <a href="/Profile">Perfil</a>
                    <a href="/Messages">Mensajes</a>
                </nav>
            </header>

            <main className="main-content">
                <section className="publish-section">
                    <h2>Publicar Nuevo Contenido</h2>
                    <form onSubmit={handleSubmit} className="publish-form">
                        <input
                            type="text"
                            placeholder="Título"
                            value={newContent.titulo}
                            onChange={(e) => setNewContent({ ...newContent, titulo: e.target.value })}
                            required
                        />
                        <textarea
                            placeholder="Descripción"
                            value={newContent.descripcion}
                            onChange={(e) => setNewContent({ ...newContent, descripcion: e.target.value })}
                            required
                        />
                        <select
                            value={newContent.tipo}
                            onChange={(e) => setNewContent({ ...newContent, tipo: e.target.value })}
                        >
                            <option value="texto">Texto</option>
                            <option value="imagen">Imagen</option>
                            <option value="video">Video</option>
                            <option value="documento">Documento</option>
                        </select>
                        <input
                            type="url"
                            placeholder="URL del contenido"
                            value={newContent.url}
                            onChange={(e) => setNewContent({ ...newContent, url: e.target.value })}
                            required
                        />
                        <button type="submit" className="publish-btn">Publicar</button>
                    </form>
                </section>

                <section className="content-list">
                    {content.map((item) => (
                        <article key={item.id} className="content-card">
                            <div className="media-container">
                                {item.tipo === 'imagen' && <img src={item.url} alt={item.titulo} />}
                                {item.tipo === 'video' && <video src={item.url} controls />}
                                {item.tipo === 'documento' && <iframe src={item.url} title={item.titulo} />}
                            </div>
                            <h3 className="title">{item.titulo}</h3>
                            <p className="description">{item.descripcion}</p>
                            <div className="content-meta">
                                <span>Por: {item.autor.nombre}</span>
                                <span>Valoración: {item.valoracionPromedio.toFixed(1)}</span>
                                <span>Fecha: {new Date(item.fechaPublicacion).toLocaleDateString()}</span>
                            </div>
                        </article>
                    ))}
                </section>
            </main>
        </div>
    );
};

export default ContentPublishing;
