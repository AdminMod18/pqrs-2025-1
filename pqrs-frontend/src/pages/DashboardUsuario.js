import React, { useState, useEffect } from 'react';
import axios from 'axios';
import AnexoUploader from './AnexoUploader'; // Ajusta la ruta si es necesario

function DashboardUsuario() {
  const token = localStorage.getItem('token');
  const user = JSON.parse(localStorage.getItem('user'));
  const [descripcion, setDescripcion] = useState('');
  const [pqrsList, setPqrsList] = useState([]);

  const api = axios.create({
    baseURL: 'http://localhost:8080//api/clientes',
    headers: { Authorization: `Bearer ${token}` }
  });

  const fetchPQRS = async () => {
    try {
      const res = await api.get(`/radicados/usuario/${user.id}`);
      setPqrsList(res.data);
    } catch (err) {
      console.error("Error al obtener PQRS:", err);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/radicados', {
        descripcion,
        usuarioId: user.id
      });
      setDescripcion('');
      fetchPQRS();
    } catch (err) {
      alert("Error al enviar PQRS");
      console.error(err);
    }
  };

  useEffect(() => {
    fetchPQRS();
  }, []);

  return (
    <div className="container mt-4">
      <h2>Bienvenido, {user?.nombre || 'Usuario'}</h2>

      <h4 className="mt-4">Enviar nueva PQRS</h4>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <textarea
            className="form-control"
            placeholder="Describe tu solicitud..."
            value={descripcion}
            onChange={(e) => setDescripcion(e.target.value)}
            required
          />
        </div>
        <button className="btn btn-primary">Enviar</button>
      </form>

      <h4 className="mt-5">Tus PQRS</h4>
      {pqrsList.length === 0 ? (
        <p className="text-muted">Aún no has enviado ninguna PQRS.</p>
      ) : (
        <ul className="list-group">
          {pqrsList.map((item) => (
            <li key={item.id} className="list-group-item mb-3 shadow-sm rounded">
              <p><strong>ID:</strong> {item.id}</p>
              <p><strong>Descripción:</strong> {item.descripcion}</p>
              <p><strong>Estado:</strong> {item.estado}</p>
              <AnexoUploader radicadoId={item.id} />
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default DashboardUsuario;
