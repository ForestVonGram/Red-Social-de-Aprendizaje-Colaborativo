import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

export default function Navbar() {
    return (
        <header className="w-full bg-white shadow-sm border-b">
            <div className="max-w-7xl mx-auto px-4 py-3 flex items-center justify-between">
                {/* Logo o nombre de la red social */}
                <div className="text-xl font-bold text-primary">RedSocial</div>

                {/* Menú principal */}
                <nav className="flex items-center gap-4">
                    <Button variant="ghost">Explorar contenidos</Button>
                    <Button variant="ghost">Mi perfil</Button>
                </nav>

                {/* Buscador */}
                <div className="w-48">
                    <Input type="text" placeholder="Buscar..." />
                </div>
            </div>
        </header>
    )
}
