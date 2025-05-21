// pages/Messages.tsx
import React, { useState } from 'react';
import '../../styles/Messages.css';
import ChatInput from '../components/ui/ChatInput';
import ChatMessage from '../components/ui/ChatMessage';

interface Message {
    id: number;
    text: string;
    timestamp: string;
    sender: 'me' | 'other';
}

const Messages: React.FC = () => {
    const [messages, setMessages] = useState<Message[]>([
        { id: 1, text: '¡Hola! ¿Listo para el estudio?', timestamp: '10:00 AM', sender: 'other' },
        { id: 2, text: '¡Sí! Empecemos.', timestamp: '10:01 AM', sender: 'me' },
    ]);

    const handleSend = (text: string) => {
        const newMessage: Message = {
            id: messages.length + 1,
            text,
            timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
            sender: 'me',
        };
        setMessages([...messages, newMessage]);
    };

    return (
        <div>
            <header className="header">
                <div className="logo">NeuronApp</div>
            </header>

            <div className="chat-wrapper">
                <aside className="sidebar">
                    <input type="text" placeholder="Buscar estudiante..." className="search-input" />
                    <ul className="conversation-list">
                        <li className="conversation active">
                            <div className="avatar">JP</div>
                            <div className="conversation-info">
                                <strong>Juan Pérez</strong>
                                <span>Último mensaje...</span>
                            </div>
                        </li>
                        <li className="conversation">
                            <div className="avatar">AT</div>
                            <div className="conversation-info">
                                <strong>Ana Torres</strong>
                                <span>¿Estás disponible?</span>
                            </div>
                        </li>
                    </ul>
                </aside>

                <main className="chat-area">
                    <div className="chat-header">
                        <h2>Juan Pérez</h2>
                    </div>
                    <div className="chat-messages">
                        {messages.map((msg) => (
                            <ChatMessage
                                key={msg.id}
                                text={msg.text}
                                timestamp={msg.timestamp}
                                sender={msg.sender}
                            />
                        ))}
                    </div>
                    <ChatInput onSend={handleSend} />
                </main>
            </div>
        </div>
    );
};

export default Messages;
