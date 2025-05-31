import React, { useEffect, useState, useCallback } from "react";
import axios from "axios";
import "../styles/Messages.css";

interface Usuario {
    id: number;
    nombre: string;
    correo: string;
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

// Using configured axios instance with baseURL, no need for API_BASE_URL

const obtenerUsuarioActualId = (): number | null => {
    const id = localStorage.getItem("usuarioId");
    return id ? Number(id) : null;
};

const obtenerUsuarioActualEmail = (): string | null => {
    return localStorage.getItem("userEmail");
};

const Messages: React.FC = () => {
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [conversaciones, setConversaciones] = useState<Conversacion[]>([]);
    const [mensajes, setMensajes] = useState<Mensaje[]>([]);
    const [conversacionSeleccionada, setConversacionSeleccionada] = useState<Conversacion | null>(null);
    const [nuevoMensaje, setNuevoMensaje] = useState("");
    const [searchInput, setSearchInput] = useState("");
    const [resultadosBusqueda, setResultadosBusqueda] = useState<Usuario[]>([]);

    const usuarioActualId = obtenerUsuarioActualId();
    const usuarioActualEmail = obtenerUsuarioActualEmail();

    const fetchConversaciones = useCallback(async () => {
        if (!usuarioActualEmail) return;
        try {
            setIsLoading(true);
            const response = await axios.get(`/api/usuarios/${usuarioActualEmail}/conversaciones`);
            const data = response.data;
            setConversaciones(data.conversaciones ?? []);
        } catch (err) {
            setError("Error al cargar las conversaciones");
            console.error("Error:", err);
            setConversaciones([]);
        } finally {
            setIsLoading(false);
        }
    }, [usuarioActualEmail]);

    useEffect(() => {
        const fetchMensajes = async () => {
            if (!conversacionSeleccionada || !conversacionSeleccionada.id) {
                console.error("No se ha seleccionado una conversación válida.");
                return;
            }

            try {
                const response = await axios.get(`/api/mensajes/conversacion/${conversacionSeleccionada.id}`);
                setMensajes(response.data);
            } catch (error) {
                console.error("Error al cargar los mensajes:", error);
                setError("No se pudieron cargar los mensajes.");
            }
        };

        fetchMensajes();
    }, [conversacionSeleccionada]);

    const seleccionarConversacion = async (conv: Conversacion) => {
        setConversacionSeleccionada(conv);
        try {
            const response = await axios.get(`/api/mensajes/conversacion/${conv.id}`);
            setMensajes(Array.isArray(response.data) ? response.data : []);
        } catch (err) {
            setError("Error al cargar los mensajes");
            console.error("Error:", err);
            setMensajes([]);
        }
    };

    const enviarMensaje = async () => {
        if (!conversacionSeleccionada || !nuevoMensaje.trim() || !usuarioActualId) return;

        try {
            const response = await axios.post(`/api/mensajes`, {
                remitenteId: usuarioActualId,
                conversacionId: conversacionSeleccionada.id,
                contenido: nuevoMensaje.trim(),
            });
            setMensajes((prevMensajes) => [...prevMensajes, response.data]);
            setNuevoMensaje("");
        } catch (err) {
            setError("Error al enviar el mensaje");
            console.error("Error:", err);
        }
    };

    const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === "Enter" && !e.shiftKey) {
            e.preventDefault();
            enviarMensaje();
        }
    };

    const buscarUsuarios = async (nombre: string) => {
        if (!nombre.trim()) {
            setResultadosBusqueda([]);
            return;
        }

        try {
            const response = await axios.get(`/api/usuarios/buscar`, {
                params: { nombre },
            });
            // Ensure we're handling the response data correctly
            let usuarios = Array.isArray(response.data) ? response.data : [];

            // Filtrar el usuario actual de los resultados de búsqueda
            if (usuarioActualEmail) {
                usuarios = usuarios.filter(usuario => usuario.correo !== usuarioActualEmail);
            }

            setResultadosBusqueda(usuarios);
        } catch (err) {
            console.error("Error al buscar usuarios:", err);
            setResultadosBusqueda([]);
        }
    };

    const crearConversacion = async (correoDestino: string) => {
        try {
            const response = await axios.post(`/api/conversaciones/por-correo`, {
                correoEmisor: usuarioActualEmail,
                correoReceptor: correoDestino,
            });

            if (response.status === 201 || response.status === 200) {
                // Obtener la conversación recién creada
                const nuevaConversacion = response.data;

                // Actualizar la lista de conversaciones
                await fetchConversaciones();

                // Seleccionar automáticamente la conversación recién creada
                await seleccionarConversacion(nuevaConversacion);

                // Limpiar la búsqueda
                setSearchInput("");
                setResultadosBusqueda([]);
            }
        } catch (err) {
            console.error("Error al crear conversación:", err);
        }
    };

    if (!usuarioActualId) {
        return (
            <div className="no-auth">
                <h2>Debes iniciar sesión para ver tus mensajes.</h2>
                <a href="/LoginPage" className="login-link">
                    Iniciar sesión
                </a>
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
                        value={searchInput}
                        onChange={(e) => {
                            const valor = e.target.value;
                            setSearchInput(valor);
                            buscarUsuarios(valor);
                        }}
                    />
                    {resultadosBusqueda.length > 0 && (
                        <ul className="search-results">
                            {resultadosBusqueda.map((usuario) => (
                                <li
                                    key={usuario.id}
                                    className="search-result-item"
                                    onClick={() => crearConversacion(usuario.correo)}
                                >
                                    {usuario.nombre} ({usuario.correo})
                                </li>
                            ))}
                        </ul>
                    )}
                    <ul className="conversation-list">
                        {conversaciones.map((conv) => {
                            const nombre = conv.participantes.find((u) => u.id !== usuarioActualId)?.nombre ?? "Chat";
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
                                ? conversacionSeleccionada.participantes.find((u) => u.id !== usuarioActualId)?.nombre
                                : "Selecciona un usuario"}
                        </h2>
                    </div>
                    <div className="chat-messages">
                        {Array.isArray(mensajes) && mensajes.map((msg) => (
                            <div
                                key={msg.id}
                                className={`message ${msg.remitente.id === usuarioActualId ? "sent" : "received"}`}
                            >
                                <p>{msg.contenido}</p>
                                <span className="timestamp">
                                    {new Date(msg.fecha).toLocaleTimeString()}
                                </span>
                            </div>
                        ))}
                    </div>
                    <div className="chat-input">
                        <input
                            type="text"
                            placeholder="Escribe tu mensaje..."
                            value={nuevoMensaje}
                            onChange={(e) => setNuevoMensaje(e.target.value)}
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
