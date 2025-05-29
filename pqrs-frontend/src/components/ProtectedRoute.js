import React from "react";
import { Navigate } from "react-router-dom";

function ProtectedRoute({ children, allowedRoles = [] }) {
const token = localStorage.getItem("token");

let user = null;
try {
user = JSON.parse(localStorage.getItem("user"));
} catch (err) {
console.warn("Error al parsear el usuario:", err);
}

// Si no hay token o usuario válido, redirige al login
if (!token || !user) {
return <Navigate to="/login" replace />;
}

// Si se especifican roles permitidos y el rol del usuario no está incluido, redirige
if (allowedRoles.length > 0 && !allowedRoles.includes(user.rol)) {
return <Navigate to="/" replace />;
}

return children;
}

export default ProtectedRoute;