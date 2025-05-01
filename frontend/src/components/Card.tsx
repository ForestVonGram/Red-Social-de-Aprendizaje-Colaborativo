import { Button } from "@/components/ui/button";

interface CardProps {
    title: string;
    description: string;
    author: string;
    ratings: number;
    tags: string[];
}

const Card = ({ title, description, author, ratings, tags }: CardProps) => {
    return (
        <div className="bg-white border shadow-sm rounded-lg p-4">
            <h3 className="text-xl font-semibold text-primary">{title}</h3>
            <p className="text-sm text-gray-500 mt-2">{description}</p>

            <div className="mt-4 flex justify-between items-center">
                {/* Autor */}
                <span className="text-xs text-gray-400">Por: {author}</span>

                {/* Valoración */}
                <div className="flex items-center text-yellow-500">
                    <span>{ratings}</span>
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        className="h-4 w-4 ml-1"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                        strokeWidth="2"
                    >
                        <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            d="M12 17.25l4.35 2.29-1.16-4.88L20 9.12l-4.91-.42-1.92-4.64-1.92 4.64-4.91.42 3.81 5.53-1.16 4.88L12 17.25z"
                        />
                    </svg>
                </div>
            </div>

            {/* Tags */}
            <div className="mt-2 text-xs text-gray-500">
                {tags.map((tag, index) => (
                    <span key={index} className="mr-2 bg-gray-200 px-2 py-1 rounded-full">
            {tag}
          </span>
                ))}
            </div>

            <div className="mt-4 flex justify-between">
                {/* Botón Ver más */}
                <Button variant="outline">Ver más</Button>
                {/* Botón Valorar */}
                <Button variant="outline" className="ml-2">
                    Valorar
                </Button>
            </div>
        </div>
    )
}

export default Card;
