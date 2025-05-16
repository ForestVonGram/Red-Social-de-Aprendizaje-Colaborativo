import React from "react";
import "../styles/ModeratorPanel.css";

const ModeratorPanel: React.FC = () => (
  <div>
    <header>
      <div className="logo">NeuronApp - Moderador</div>
    </header>
    <div className="container">
      <h1>Panel del Moderador</h1>

      {/* Sección 1: Gestión de Usuarios */}
      <section className="panel-section">
        <h2>Gestión de Usuarios</h2>
        <table className="user-table">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Email</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Juan Pérez</td>
              <td>juan@example.com</td>
              <td>
                <button className="view-profile">Ver Perfil</button>
                <button className="delete-user">Eliminar</button>
              </td>
            </tr>
            {/* Más filas de usuarios */}
          </tbody>
        </table>
      </section>

      {/* Sección 2: Gestión de Contenidos */}
      <section className="panel-section">
        <h2>Gestión de Contenidos</h2>
        <table className="content-table">
          <thead>
            <tr>
              <th>Título</th>
              <th>Autor</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Introducción a la Física</td>
              <td>Ana Torres</td>
              <td>
                <button className="edit-content">Editar</button>
                <button className="delete-content">Eliminar</button>
              </td>
            </tr>
            {/* Más filas de contenidos */}
          </tbody>
        </table>
      </section>

      {/* Sección 3: Visualización del Grafo */}
      <section className="panel-section">
        <h2>Visualización del Grafo de Afinidad</h2>
        <div className="graph-container">
          <canvas id="affinityGraph" width={800} height={400}></canvas>
        </div>
      </section>

      {/* Sección 4: Generación de Reportes */}
      <section className="panel-section">
        <h2>Generación de Reportes</h2>
        <div className="report-buttons">
          <button className="report-button">Contenidos Más Valorados</button>
          <button className="report-button">Estudiantes con Más Conexiones</button>
          <div className="shortest-path">
            <input type="text" placeholder="Usuario A" />
            <input type="text" placeholder="Usuario B" />
            <button className="report-button">Camino Más Corto</button>
          </div>
        </div>
      </section>

      {/* Sección 5: Detección de Clústeres */}
      <section className="panel-section">
        <h2>Detección de Clústeres</h2>
        <div className="clusters">
          <ul>
            <li>Clúster 1: Juan, Ana, Pedro</li>
            <li>Clúster 2: María, Luis, Carla</li>
            {/* Más clústeres */}
          </ul>
        </div>
      </section>

      {/* Sección 6: Niveles de Participación */}
      <section className="panel-section">
        <h2>Niveles de Participación</h2>
        <ol className="participation-levels">
          <li>Juan Pérez - 120 interacciones</li>
          <li>Ana Torres - 110 interacciones</li>
          <li>Pedro Gómez - 95 interacciones</li>
          {/* Más niveles */}
        </ol>
      </section>
    </div>
  </div>
);

export default ModeratorPanel;