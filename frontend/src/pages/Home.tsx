import React from "react";
import "@/styles/Home.css";

const Home: React.FC = () => {
    return (
        <html lang="es">
        <head>
            <meta charSet="UTF-8"/>
            <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            <title>NeuronApp</title>
            <link rel="stylesheet" href="estilos.css"/>
            <link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet"/>
        </head>
        <body>
        <div id="pantallainicio">

            <div id="rectangle"></div>

            <div id="acercade">Acerca de</div>
            <div id="foro">Foro</div>
            <div id="documentacion">Documentación</div>
            <div id="iniciarsesion">Iniciar sesión</div>

            <div id="fondo-inicio"></div>

            <div id="titulo-neuronapp">NeuronApp</div>

            <div id="descripcion-app">
                Una red social para el aprendizaje colaborativo, impulsada por conexiones inteligentes.
            </div>

            <div id="--------------">Características</div>

            <div id="rectangleCaracteristicas"></div>

            <div id="intuitiva">Intuitiva</div>
            <div id="rapida">Rápida</div>
            <div id="recursiva">Recursiva</div>
            <div id="polaniaelcacique">Polanía el Cacique</div>

        </div>
        </body>
        </html>
    );
};

export default Home;
