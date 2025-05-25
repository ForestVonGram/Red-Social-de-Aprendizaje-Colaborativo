import React, { useEffect, useState, useCallback } from "react";
import axios from "axios";
import "../styles/Messages.css";

interface Usuario {
    id: number;
    nombre: string;
}

interface Conversacion {
    id: number;
    participantes: Usuario[];
}

interface Mensaje {
    id: string;
    contenido: string;
    fecha: string;
    remitente: Usuario;
}

const API_BASE_URL = import.meta.env.VITE_API_URL;

const obtenerUsuarioActualId = (): number | null => {
    const id = localStorage.getItem("usuarioId");
    return id ? Number(id) : null;
};

const Messages: React.FC = () => {
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [conversaciones, setConversaciones] = useState<Conversacion[]>([]);
    const [mensajes, setMensajes] = useState<Mensaje[]>([]);
    const [conversacionSeleccionada, setConversacionSeleccionada] = useState<Conversacion | null>(null);
    const [nuevoMensaje, setNuevoMensaje] = useState("");
    const usuarioActualId = obtenerUsuarioActualId();

    const fetchConversaciones = useCallback(async () => {
        if (!usuarioActualId) return;
        try {
            setIsLoading(true);
            const response = await axios.get(`${API_BASE_URL}/usuarios/${usuarioActualId}/conversaciones`);
            setConversaciones(response.data);
        } catch (err) {
            setError('Error al cargar las conversaciones');
            console.error('Error:', err);
        } finally {
            setIsLoading(false);
        }
    }, [usuarioActualId]);

    useEffect(() => {
        fetchConversaciones();
    }, [fetchConversaciones]);

    const seleccionarConversacion = async (conv: Conversacion) => {
        setConversacionSeleccionada(conv);
        try {
            const response = await axios.get(`${API_BASE_URL}/api/mensajes/conversacion/${conv.id}`);
            setMensajes(response.data);
        } catch (err) {
            setError('Error al cargar los mensajes');
            console.error('Error:', err);
        }
    };

    const enviarMensaje = async () => {
        if (!conversacionSeleccionada || !nuevoMensaje.trim() || !usuarioActualId) return;

        try {
            const response = await axios.post(`${API_BASE_URL}/api/mensajes`, {
                remitenteId: usuarioActualId,
                conversacionId: conversacionSeleccionada.id,
                contenido: nuevoMensaje.trim(),
            });
            setMensajes(prevMensajes => [...prevMensajes, response.data]);
            setNuevoMensaje("");
        } catch (err) {
            setError('Error al enviar el mensaje');
            console.error('Error:', err);
        }
    };

    const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            enviarMensaje();
        }
    };

    if (!usuarioActualId) {
        return (
            <div className="no-auth">
                <h2>Debes iniciar sesión para ver tus mensajes.</h2>
                <a href="/login" className="login-link">Iniciar sesión</a>
            </div>
        );
    }

    if (isLoading) {
        return <div className="loading">Cargando conversaciones...</div>;
    }

    if (error) {
        return (
            <div className="error-message">
                {error}
                <button onClick={fetchConversaciones} className="retry-button">
                    Reintentar
                </button>
            </div>
        );
    }

    return (
        <div>
            <header>
                <div className="logo">NeuronApp</div>
            </header>
            <div className="chat-wrapper">
                <aside className="sidebar">
                    <input
                        type="text"
                        placeholder="Buscar estudiante..."
                        className="search-input"
                        aria-label="Buscar estudiante"
                    />
                    <ul className="conversation-list">
                        {conversaciones.map(conv => {
                            const nombre = conv.participantes.find(u => u.id !== usuarioActualId)?.nombre ?? "Chat";
                            return (
                                <li
                                    key={conv.id}
                                    className={`conversation ${conversacionSeleccionada?.id === conv.id ? "active" : ""}`}
                                    onClick={() => seleccionarConversacion(conv)}
                                    role="button"
                                    tabIndex={0}
                                >
                                    <div className="avatar" aria-hidden="true">
                                        {nombre.slice(0, 2).toUpperCase()}
                                    </div>
                                    <div className="conversation-info">
                                        <strong>{nombre}</strong>
                                        <span>Conversación activa</span>
                                    </div>
                                </li>
                            );
                        })}
                    </ul>
                </aside>
                <main className="chat-area">
                    <div className="chat-header">
                        <h2>
                            {conversacionSeleccionada
                                ? conversacionSeleccionada.participantes.find(u => u.id !== usuarioActualId)?.nombre
                                : "Selecciona un usuario"}
                        </h2>
                    </div>
                    <div className="chat-messages">
                        {mensajes.map(msg => (
                            <div
                                key={msg.id}
                                className={`message ${msg.remitente.id === usuarioActualId ? "sent" : "received"}`}
                            >
                                <p>{msg.contenido}</p>
                                <span className="timestamp">{new Date(msg.fecha).toLocaleTimeString()}</span>
                            </div>
                        ))}
                    </div>
                    <div className="chat-input">
                        <input
                            type="text"
                            placeholder="Escribe tu mensaje..."
                            value={nuevoMensaje}
                            onChange={e => setNuevoMensaje(e.target.value)}
                            onKeyPress={handleKeyPress}
                            disabled={!conversacionSeleccionada}
                        />
                        <button
                            className="send-btn"
                            onClick={enviarMensaje}
                            disabled={!conversacionSeleccionada || !nuevoMensaje.trim()}
                        >
                            Enviar
                        </button>
                    </div>
                </main>
            </div>
        </div>
    );
};

export default Messages;