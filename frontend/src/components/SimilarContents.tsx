import React from 'react';

const SimilarContents: React.FC = () => {
    return (
        <section className="similares">
            <h2>Contenidos Similares</h2>
            <div className="contenido-lista">
                <div className="contenido-card">
                    <h3>Contenido 1</h3>
                    <p>Pequeña descripción...</p>
                </div>
                <div className="contenido-card">
                    <h3>Contenido 2</h3>
                    <p>Pequeña descripción...</p>
                </div>
                <div className="contenido-card">
                    <h3>Contenido 3</h3>
                    <p>Pequeña descripción...</p>
                </div>
            </div>
        </section>
    );
};

export default SimilarContents;
