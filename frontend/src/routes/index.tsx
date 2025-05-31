import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Landing from "@/pages/LandingPage";
import LoginPage from "@/pages/LoginPage";
import RegisterPage from "@/pages/RegisterPage";
import Messages from "@/pages/Messages";
import Profile from "@/pages/Profile";
import ContentPublishing from "@/pages/ContentPublishing";
import ViewContentPage from "@/pages/ViewContentPage";
import GrafoView from "@/pages/GrafoView";
import ProtectedRoute from "@/components/ProtectedRoute";
import Moderador from "@/pages/ModeratorPanel";


export default function AppRoutes() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Landing />} />
                <Route path="/LoginPage" element={<LoginPage />} />
                <Route path="/RegisterPage" element={<RegisterPage />} />
                <Route path="/Messages" element={<Messages />} />
                <Route path="/Profile" element={<Profile />} />
                <Route path="/Contenido" element={<ContentPublishing />} />
                <Route path="/ViewContentPage" element={<ViewContentPage />} />
                <Route path="/GrafoView" element={<GrafoView usuarios={[]} grafoUsuarios={{conectarUsuarios: () => {}, estanConectados: () => false,}} />} />
                <Route path="/Moderador" element={
                        <Moderador />
                } />
                {/* Aquí van las rutas */}
            </Routes>
        </Router>
    );
}
