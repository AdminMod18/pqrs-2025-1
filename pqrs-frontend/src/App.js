import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import DashboardUsuario from "./pages/DashboardUsuario";
import DashboardGestor from "./pages/DashboardGestor";
import ProtectedRoute from "./components/ProtectedRoute";
import LandingPage from "./pages/LandingPage";

function App() {
  return (
    <Router>
      <Navbar />
      <div className="container mt-4">
        <Routes>
          <Route path="/" element={<LandingPage />} />
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/home" element={<Home />} />

          {/* Rutas protegidas */}
          <Route
            path="/dashboard-usuario"
            element={
              <ProtectedRoute allowedRoles={["CLIENTE"]}>
                <DashboardUsuario />
              </ProtectedRoute>
            }
          />
          <Route
            path="/dashboard-gestor"
            element={
              <ProtectedRoute allowedRoles={["GESTOR"]}>
                <DashboardGestor />
              </ProtectedRoute>
            }
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
