// /pages/Home.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Home.css'; // Estilos para animación de globos

const Home = () => {
  const navigate = useNavigate();

  return (
    <div className="home-container text-white text-center d-flex flex-column justify-content-center align-items-center">
      <div className="overlay" />
      <h1 className="display-4 fw-bold">Bienvenido a la Plataforma PQRS</h1>
      <p className="lead">Envía, consulta y gestiona tus solicitudes de forma rápida y sencilla.</p>
      <div className="mt-4">
        <button className="btn btn-outline-light me-3 px-4 py-2" onClick={() => navigate('/login')}>
          Iniciar Sesión
        </button>
        <button className="btn btn-light px-4 py-2" onClick={() => navigate('/register')}>
          Registrarse
        </button>
      </div>
      <div className="balloons">
        {[...Array(10)].map((_, i) => (
          <div key={i} className={`balloon balloon-${i + 1}`} />
        ))}
      </div>
    </div>
  );
};

export default Home;
