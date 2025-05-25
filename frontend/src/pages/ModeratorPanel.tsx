import React, { useEffect, useRef } from "react";
import * as d3 from "d3";
import axios from "axios";
import "../styles/ModeratorPanel.css";

const GrafoVisualizacion: React.FC = () => {
  const svgRef = useRef<SVGSVGElement>(null);

  useEffect(() => {
    const cargarGrafo = async () => {
      try {
        const response = await axios.get('/api/grafo/estructura');
        const data = response.data;

        if (!svgRef.current) return;

        const width = 800;
        const height = 400;

        d3.select(svgRef.current).selectAll("*").remove();

        const svg = d3.select(svgRef.current)
            .attr("width", width)
            .attr("height", height);

        const simulation = d3.forceSimulation(data.nodes)
            .force("link", d3.forceLink(data.links).id((d: any) => d.id))
            .force("charge", d3.forceManyBody().strength(-100))
            .force("center", d3.forceCenter(width / 2, height / 2));

        const links = svg.append("g")
            .selectAll("line")
            .data(data.links)
            .enter()
            .append("line")
            .style("stroke", "#999")
            .style("stroke-width", 1);

        const nodes = svg.append("g")
            .selectAll("circle")
            .data(data.nodes)
            .enter()
            .append("circle")
            .attr("r", 5)
            .style("fill", "#1f77b4");

        const labels = svg.append("g")
            .selectAll("text")
            .data(data.nodes)
            .enter()
            .append("text")
            .text((d: any) => d.label)
            .attr("font-size", "12px")
            .attr("dx", 8)
            .attr("dy", 3);

        simulation.on("tick", () => {
          links
              .attr("x1", (d: any) => d.source.x)
              .attr("y1", (d: any) => d.source.y)
              .attr("x2", (d: any) => d.target.x)
              .attr("y2", (d: any) => d.target.y);

          nodes
              .attr("cx", (d: any) => d.x)
              .attr("cy", (d: any) => d.y);

          labels
              .attr("x", (d: any) => d.x)
              .attr("y", (d: any) => d.y);
        });
      } catch (error) {
        console.error('Error al cargar el grafo:', error);
      }
    };

    cargarGrafo();
  }, []);

  return (
      <div className="graph-container">
        <svg ref={svgRef}></svg>
      </div>
  );
};


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
          <GrafoVisualizacion />
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