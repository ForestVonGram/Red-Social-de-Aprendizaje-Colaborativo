import { Button } from "@/components/ui/button";

const Sidebar = () => {
    const sugerencias = [
        { nombre: "Laura Gómez", intereses: ["programación", "frontend"] },
        { nombre: "Carlos Díaz", intereses: ["matemáticas", "algoritmos"] },
        { nombre: "Marta Ruiz", intereses: ["JavaScript", "React"] },
    ]

    const estadisticas = {
        grupos: 12,
        nuevosContenidos: 5,
    }

    return (
        <div className="bg-white shadow-lg rounded-lg p-4 w-80">
            {/* Sección de sugerencias */}
            <div className="mb-6">
                <h3 className="text-xl font-semibold text-primary">Sugerencias de compañeros</h3>
                <div className="mt-4">
                    {sugerencias.map((sugerencia, index) => (
                        <div key={index} className="flex justify-between items-center mb-4">
                            <div className="text-sm font-medium">{sugerencia.nombre}</div>
                            <Button variant="outline" size="sm" className="text-xs">
                                Seguir
                            </Button>
                        </div>
                    ))}
                </div>
            </div>

            {/* Sección de estadísticas */}
            <div className="mb-6">
                <h3 className="text-xl font-semibold text-primary">Estadísticas rápidas</h3>
                <ul className="mt-4 text-sm text-gray-600">
                    <li>Grupos de estudio: {estadisticas.grupos}</li>
                    <li>Nuevos contenidos: {estadisticas.nuevosContenidos}</li>
                </ul>
            </div>

            {/* Botón unirse a grupo */}
            <div className="flex justify-center">
                <Button variant="default" className="w-full">
                    Unirse a grupo de estudio
                </Button>
            </div>
        </div>
    )
}

export default Sidebar
