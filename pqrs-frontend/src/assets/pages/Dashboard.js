// /pages/Dashboard.js
import React from 'react';
import { useNavigate } from 'react-router-dom';

const Dashboard = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    // Aquí se eliminaría el token de sesión más adelante
    navigate('/login');
  };

  return (
    <div className="container py-5">
      <div className="text-center mb-4">
        <h2 className="fw-bold">Bienvenido a tu Panel</h2>
        <p className="text-muted">Aquí puedes gestionar tus PQRS y ver tus solicitudes.</p>
      </div>

      <div className="row g-4 justify-content-center">
        <div className="col-md-4">
          <div className="card shadow-sm border-0 h-100">
            <div className="card-body text-center">
              <i className="bi bi-chat-left-text fs-1 text-primary"></i>
              <h5 className="card-title mt-3">Nueva PQRS</h5>
              <p className="card-text">Crea una nueva solicitud de Petición, Queja, Reclamo o Sugerencia.</p>
              <button
                className="btn btn-outline-primary w-100"
                onClick={() => navigate('/crear-pqrs')}
              >
                Crear PQRS
              </button>
            </div>
          </div>
        </div>

        <div className="col-md-4">
          <div className="card shadow-sm border-0 h-100">
            <div className="card-body text-center">
              <i className="bi bi-collection fs-1 text-success"></i>
              <h5 className="card-title mt-3">Mis Solicitudes</h5>
              <p className="card-text">Consulta el estado y detalles de tus solicitudes anteriores.</p>
              <button
                className="btn btn-outline-success w-100"
                onClick={() => navigate('/mis-solicitudes')}
              >
                Ver Solicitudes
              </button>
            </div>
          </div>
        </div>
      </div>

      <div className="text-center mt-5">
        <button className="btn btn-danger" onClick={handleLogout}>
          Cerrar Sesión
        </button>
      </div>
    </div>
  );
};

export default Dashboard;
