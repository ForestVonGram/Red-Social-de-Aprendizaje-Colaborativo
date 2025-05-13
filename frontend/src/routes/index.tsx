import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Landing from "@/pages/LandingPage";
import LoginPage from "@/pages/LoginPage";
import RegisterPage from "@/pages/RegisterPage";
import Messages from "@/pages/Messages";

export default function AppRoutes() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Landing />} />
                <Route path="/LoginPage" element={<LoginPage />} />
                <Route path="/RegisterPage" element={<RegisterPage />} />
                <Route path="/Messages" element={<Messages />} />
                {/* Aquí van las rutas */}
            </Routes>
        </Router>
    );
}
