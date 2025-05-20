import React, { useEffect, useState } from "react";
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

const obtenerUsuarioActualId = (): number | null => {
    const id = localStorage.getItem("usuarioId");
    return id ? Number(id) : null;
};

const Messages: React.FC = () => {
    const [conversaciones, setConversaciones] = useState<Conversacion[]>([]);
    const [mensajes, setMensajes] = useState<Mensaje[]>([]);
    const [conversacionSeleccionada, setConversacionSeleccionada] = useState<Conversacion | null>(null);
    const [nuevoMensaje, setNuevoMensaje] = useState("");
    const usuarioActualId = obtenerUsuarioActualId();

    useEffect(() => {
        if (!usuarioActualId) return;
        axios
            .get(`/usuarios/${usuarioActualId}/conversaciones`)
            .then(res => setConversaciones(res.data))
            .catch(err => console.error(err));
    }, [usuarioActualId]);

    const seleccionarConversacion = (conv: Conversacion) => {
        setConversacionSeleccionada(conv);
        axios
            .get(`/api/mensajes/conversacion/${conv.id}`)
            .then(res => setMensajes(res.data))
            .catch(err => console.error(err));
    };

    const enviarMensaje = () => {
        if (!conversacionSeleccionada || !nuevoMensaje.trim() || !usuarioActualId) return;
        axios
            .post("/api/mensajes", {
                remitenteId: usuarioActualId,
                conversacionId: conversacionSeleccionada.id,
                contenido: nuevoMensaje,
            })
            .then(res => {
                setMensajes([...mensajes, res.data]);
                setNuevoMensaje("");
            })
            .catch(err => console.error(err));
    };

    if (!usuarioActualId) {
        return (
            <div className="no-auth">
                <h2>Debes iniciar sesión para ver tus mensajes.</h2>
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
                    <input type="text" placeholder="Buscar estudiante..." className="search-input" />
                    <ul className="conversation-list">
                        {conversaciones.map(conv => {
                            const nombre = conv.participantes.find(u => u.id !== usuarioActualId)?.nombre ?? "Chat";
                            return (
                                <li
                                    key={conv.id}
                                    className={`conversation ${conversacionSeleccionada?.id === conv.id ? "active" : ""}`}
                                    onClick={() => seleccionarConversacion(conv)}
                                >
                                    <div className="avatar">{nombre.slice(0, 2).toUpperCase()}</div>
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
                        />
                        <button className="send-btn" onClick={enviarMensaje}>
                            Enviar
                        </button>
                    </div>
                </main>
            </div>
        </div>
    );
};

export default Messages;