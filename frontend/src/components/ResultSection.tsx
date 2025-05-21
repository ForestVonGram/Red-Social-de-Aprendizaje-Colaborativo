import React from 'react';

const ResultSection: React.FC = () => {
    return (
        <section className="resultado">
            <h2>Resultado</h2>
            <div className="contenido-card">
                <h3>Nombre del Contenido</h3>
                <p>Descripción o detalle del contenido encontrado...</p>
            </div>
        </section>
    );
};

export default ResultSection;
