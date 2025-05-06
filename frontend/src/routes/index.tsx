import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Landing from "@/pages/LandingPage";

export default function AppRoutes() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Landing />} />
                {/* Puedes agregar más rutas aquí */}
            </Routes>
        </Router>
    );
}
