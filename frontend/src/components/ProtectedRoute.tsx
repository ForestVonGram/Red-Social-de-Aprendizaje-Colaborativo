import React, { useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';

interface ProtectedRouteProps {
    children: React.ReactNode;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);

    useEffect(() => {
        const usuarioId = localStorage.getItem('usuarioId');
        setIsAuthenticated(!!usuarioId);
    }, []);

    if (isAuthenticated === null) {
        // Still checking authentication
        return <div>Verificando autenticación...</div>;
    }

    if (!isAuthenticated) {
        // Not authenticated, redirect to login
        return <Navigate to="/LoginPage" replace />;
    }

    // Authenticated, render children
    return <>{children}</>;
};

export default ProtectedRoute;
