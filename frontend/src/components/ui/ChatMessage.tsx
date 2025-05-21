// components/ui/ChatMessage.tsx
import React from 'react';
import '../../styles/Messages.css';

interface ChatMessageProps {
    text: string;
    timestamp: string;
    type: 'sent' | 'received';
}

const ChatMessage: React.FC<ChatMessageProps> = ({ text, timestamp, type }) => {
    return (
        <div className={`message ${type}`}>
            <p>{text}</p>
            <span className="timestamp">{timestamp}</span>
        </div>
    );
};

export default ChatMessage;