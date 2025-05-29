import React, { useEffect, useState } from 'react';
import axios from 'axios';

function AnexoUploaderSoloVer({ radicadoId }) {
  const [anexos, setAnexos] = useState([]);
  const token = localStorage.getItem('token');

  const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: { Authorization: `Bearer ${token}` }
  });

  const fetchAnexos = async () => {
    try {
      const res = await api.get(`/anexos/radicado/${radicadoId}`);
      setAnexos(res.data);
    } catch (err) {
      console.error("Error al obtener anexos:", err);
    }
  };

  useEffect(() => {
    fetchAnexos();
  }, [radicadoId]);

  if (anexos.length === 0) {
    return <div className="mt-3"><strong>No hay anexos disponibles.</strong></div>;
  }

  return (
    <div className="mt-3">
      <strong>Anexos:</strong>
      <ul className="list-group mt-2">
        {anexos.map((a) => (
          <li key={a.id} className="list-group-item">
            <a href={`http://localhost:8080/api/anexos/${a.id}/descargar`} target="_blank" rel="noopener noreferrer">
              {a.nombre || `Anexo ${a.id}`}
            </a>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default AnexoUploaderSoloVer;
