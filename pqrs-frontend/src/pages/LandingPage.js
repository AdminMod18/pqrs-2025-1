// src/pages/LandingPage.js
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import 'bootstrap/dist/css/bootstrap.min.css';

export default function LandingPage() {
  const navigate = useNavigate();

  return (
    <div className="min-vh-100 d-flex flex-column justify-content-center align-items-center text-white text-center px-3 bg-dark position-relative">
      <div className="position-absolute top-0 end-0 m-4">
        <button
          onClick={() => navigate("/login")}
          className="btn btn-primary me-2"
        >
          Iniciar sesión
        </button>
        <button
          onClick={() => navigate("/register")}
          className="btn btn-outline-light"
        >
          Registrarse
        </button>
      </div>

      <motion.h1
        className="display-4 fw-bold"
        initial={{ opacity: 0, y: -40 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 1 }}
      >
        Bienvenido a la Plataforma PQRS
      </motion.h1>

      <motion.p
        className="lead mt-3"
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.5 }}
      >
        Gestiona, visualiza y responde a tus solicitudes de forma fácil y segura.
      </motion.p>

      <motion.div
        className="mt-5"
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 1 }}
      >
        <button
          onClick={() => navigate("/login")}
          className="btn btn-success me-3"
        >
          Entrar ahora
        </button>
        <button
          onClick={() => navigate("/register")}
          className="btn btn-outline-light"
        >
          Crear cuenta
        </button>
      </motion.div>

      <motion.img
        src="https://pqrs.soportesis.com//dist/img/logo_pqrs.png"
        alt="logo animado"
        className="mt-5"
        style={{ width: "100px" }}
        initial={{ opacity: 0 }}
        animate={{ opacity: 1, y: [0, -10, 0] }}
        transition={{ delay: 1.5, repeat: Infinity, duration: 2 }}
      />
    </div>
  );
}
