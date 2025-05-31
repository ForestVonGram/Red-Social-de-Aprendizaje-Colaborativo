import React, { useEffect } from "react";
import "../styles/GrafoView.css";

interface Usuario {
    correo: string;
    gruposEstudio: string[];
    contenidosValorados: string[];
}

interface GrafoUsuarios {
    conectarUsuarios: (correo1: string, correo2: string) => void;
    estanConectados: (correo1: string, correo2: string) => boolean;
}

interface Props {
    usuarios: Usuario[];
    grafoUsuarios: GrafoUsuarios;
}

const ConectarPorAfinidad: React.FC<Props> = ({ usuarios, grafoUsuarios }) => {
    useEffect(() => {
        for (let i = 0; i < usuarios.length; i++) {
            for (let j = i + 1; j < usuarios.length; j++) {
                const u1 = usuarios[i];
                const u2 = usuarios[j];

                if (!grafoUsuarios.estanConectados(u1.correo, u2.correo)) {
                    const mismosGrupos = u1.gruposEstudio.some(grupo => u2.gruposEstudio.includes(grupo));
                    const mismosContenidos = u1.contenidosValorados.some(contenido => u2.contenidosValorados.includes(contenido));

                    if (mismosGrupos || mismosContenidos) {
                        grafoUsuarios.conectarUsuarios(u1.correo, u2.correo);
                    }
                }
            }
        }
    }, [usuarios, grafoUsuarios]);

    return (
        <div className="afinidad-container">
            <h1 className="titulo">Conectando usuarios por afinidad...</h1>
        </div>
    );
};

export default ConectarPorAfinidad;
