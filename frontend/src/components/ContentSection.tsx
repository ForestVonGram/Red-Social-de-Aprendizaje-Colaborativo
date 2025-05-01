import Card from "@/components/Card"

const ContentSection = () => {
    const contenidos = [
        {
            title: "Aprende JavaScript",
            description: "Un curso completo para aprender JavaScript desde cero.",
            author: "Juan Pérez",
            ratings: 4,
            tags: ["programación", "JavaScript", "educación"]
        },
        {
            title: "Introducción a React",
            description: "Conoce los fundamentos de React para crear aplicaciones interactivas.",
            author: "Ana Martínez",
            ratings: 5,
            tags: ["programación", "React", "frontend"]
        },
        {
            title: "Fundamentos de Matemáticas",
            description: "Curso básico de matemáticas para estudiantes de ingeniería.",
            author: "Carlos López",
            ratings: 3,
            tags: ["matemáticas", "educación"]
        }
    ]

    return (
        <section className="max-w-7xl mx-auto px-4 py-8">
            <h2 className="text-2xl font-bold text-primary mb-6">Explora contenidos educativos</h2>

            {/* Filtro por etiquetas */}
            <div className="flex gap-4 mb-6">
                <button className="bg-gray-200 px-4 py-2 rounded-full">Programación</button>
                <button className="bg-gray-200 px-4 py-2 rounded-full">Matemáticas</button>
                <button className="bg-gray-200 px-4 py-2 rounded-full">Educación</button>
            </div>

            {/* Lista de tarjetas */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                {contenidos.map((contenido, index) => (
                    <Card key={index} {...contenido} />
                ))}
            </div>
        </section>
    )
}

export default ContentSection
