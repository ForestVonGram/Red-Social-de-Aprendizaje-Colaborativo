import React from 'react';
import '../styles/StudyGroups.css';

const StudyGroups: React.FC = () => {
    return (
        <div>
            {/* Fondo desenfocado */}
            <div className="background-blur"></div>

            {/* Encabezado */}
            <header>
                <div className="logo">NeuronApp</div>
            </header>

            {/* Contenido principal */}
            <div className="container">
                <h1>Grupos sugeridos automáticamente</h1>

                {/* Tarjeta de grupo sugerido */}
                <div className="group-card">
                    <h2>Tema o materia: Álgebra Lineal</h2>
                    <p>Miembros sugeridos:</p>
                    <ul className="suggested-members">
                        <li>
                            Juan Pérez <button className="add-member">Agregar</button>
                        </li>
                        <li>
                            Ana Torres <button className="add-member">Agregar</button>
                        </li>
                        <li>
                            Pedro Gómez <button className="add-member">Agregar</button>
                        </li>
                    </ul>

                    {/* Acciones para el grupo */}
                    <div className="actions">
                        <button className="join">Unirse</button>
                        <button className="reject">Rechazar</button>
                    </div>
                </div>

                {/* Botón para ver mis grupos */}
                <button className="my-groups">Ver todos los grupos en los que participo</button>

                {/* Historial de participación */}
                <div className="history">
                    <h2>Historial de participación</h2>
                    <ul>
                        <li><a href="#">Grupo de Matemáticas - Abril</a></li>
                        <li><a href="#">Grupo de Historia - Marzo</a></li>
                        <li><a href="#">Grupo de Física - Febrero</a></li>
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default StudyGroups;
