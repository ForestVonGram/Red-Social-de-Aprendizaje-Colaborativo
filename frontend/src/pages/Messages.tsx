import React from "react";
import "../styles/Messages.css";

const Messages: React.FC = () => {
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
                    />
                    <ul className="conversation-list">
                        {/* Aquí se renderizarán las conversaciones desde el backend */}
                    </ul>
                </aside>

                <main className="chat-area">
                    <div className="chat-header">
                        <h2>Selecciona un usuario</h2>
                    </div>
                    <div className="chat-messages">
                        {/* Aquí se mostrarán los mensajes del chat seleccionado */}
                    </div>
                    <div className="chat-input">
                        <input type="text" placeholder="Escribe tu mensaje..." />
                        <button className="send-btn">Enviar</button>
                    </div>
                </main>
            </div>
        </div>
    );
};

export default Messages;
