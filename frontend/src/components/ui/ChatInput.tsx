// components/ui/ChatInput.tsx
import React, { useState } from 'react';
import '../../styles/Messages.css';

interface ChatInputProps {
    onSend: (message: string) => void;
}

const ChatInput: React.FC<ChatInputProps> = ({ onSend }) => {
    const [input, setInput] = useState('');

    const handleSend = () => {
        if (input.trim() !== '') {
            onSend(input);
            setInput('');
        }
    };

    return (
        <div className="chat-input">
            <input
                type="text"
                placeholder="Escribe tu mensaje..."
                value={input}
                onChange={(e) => setInput(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && handleSend()}
            />
            <button className="send-btn" onClick={handleSend}>
                Enviar
            </button>
        </div>
    );
};

export default ChatInput;