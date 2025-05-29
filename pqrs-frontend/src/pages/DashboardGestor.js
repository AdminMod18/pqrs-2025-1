import React, { useEffect, useState } from 'react';
import axios from 'axios';
import AnexoUploaderSoloVer from './AnexoUploaderSoloVer'; // Ajusta la ruta si es necesario

function DashboardGestor() {
  const token = localStorage.getItem('token');
  const [radicados, setRadicados] = useState([]);
  const [respuestaTemporal, setRespuestaTemporal] = useState({});

  const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    }
  });

  const fetchRadicados = async () => {
    try {
      const response = await api.get('/radicados');
      setRadicados(response.data);
    } catch (error) {
      console.error('Error al cargar radicados:', error);
    }
  };

  const handleEstadoChange = async (id, nuevoEstado) => {
    try {
      await api.put(`/radicados/${id}/estado`, { estado: nuevoEstado });
      fetchRadicados();
    } catch (error) {
      console.error('Error al cambiar el estado:', error);
    }
  };

  const handleResponder = async (id) => {
    const respuesta = respuestaTemporal[id];
    if (!respuesta || respuesta.trim() === '') {
      alert('La respuesta no puede estar vacía.');
      return;
    }

    try {
      await api.put(`/radicados/${id}/respuesta`, { respuesta });
      fetchRadicados();
    } catch (error) {
      console.error('Error al guardar la respuesta:', error);
    }
  };

  useEffect(() => {
    fetchRadicados();
  }, []);

  return (
    <div className="container mt-4">
      <h2 className="mb-4">Panel del Gestor</h2>

      {radicados.length === 0 ? (
        <p className="text-muted">No hay radicados disponibles.</p>
      ) : (
        <ul className="list-group">
          {radicados.map((radicado) => (
            <li key={radicado.id} className="list-group-item mb-3 shadow-sm rounded">
              <p><strong>ID:</strong> {radicado.id}</p>
              <p><strong>Usuario:</strong> {radicado.usuario?.nombre || 'N/A'}</p>
              <p><strong>Descripción:</strong> {radicado.descripcion}</p>
              <p><strong>Estado actual:</strong> {radicado.estado}</p>
              <p><strong>Respuesta:</strong> {radicado.respuesta || 'No respondida'}</p>

              <div className="mt-3">
                <label className="form-label">Cambiar estado:</label>
                <select
                  className="form-select"
                  value={radicado.estado}
                  onChange={(e) => handleEstadoChange(radicado.id, e.target.value)}
                >
                  <option value="ENVIADA">ENVIADA</option>
                  <option value="EN PROCESO">EN PROCESO</option>
                  <option value="CERRADA">CERRADA</option>
                  <option value="RECHAZADA">RECHAZADA</option>
                </select>
              </div>

              <div className="mt-3">
                <label className="form-label">Escribir respuesta:</label>
                <textarea
                  className="form-control mb-2"
                  rows={3}
                  value={respuestaTemporal[radicado.id] ?? radicado.respuesta ?? ''}
                  onChange={(e) =>
                    setRespuestaTemporal({
                      ...respuestaTemporal,
                      [radicado.id]: e.target.value,
                    })
                  }
                />
                <button
                  className="btn btn-sm btn-primary"
                  onClick={() => handleResponder(radicado.id)}
                >
                  Guardar respuesta
                </button>
              </div>

              <div className="mt-3">
                <AnexoUploaderSoloVer radicadoId={radicado.id} />
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default DashboardGestor;
