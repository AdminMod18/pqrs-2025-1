import React, { useState, useEffect } from 'react';
import axios from 'axios';

function AnexoUploader({ radicadoId }) {
  const [file, setFile] = useState(null);
  const [anexos, setAnexos] = useState([]);

  const token = localStorage.getItem("token");

  const api = axios.create({
    baseURL: "http://localhost:8080/api",
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

  const handleUpload = async (e) => {
    e.preventDefault();
    if (!file) return;

    const formData = new FormData();
    formData.append("archivo", file);
    formData.append("radicadoId", radicadoId);

    try {
      await api.post("/anexos", formData, {
        headers: { "Content-Type": "multipart/form-data" }
      });
      setFile(null);
      fetchAnexos();
    } catch (err) {
      alert("Error al subir anexo");
    }
  };

  useEffect(() => {
    fetchAnexos();
  }, [radicadoId]);

  return (
    <div>
      <form onSubmit={handleUpload} className="d-flex gap-2 align-items-center mb-3">
        <input
          type="file"
          onChange={(e) => setFile(e.target.files[0])}
          className="form-control"
        />
        <button className="btn btn-sm btn-secondary">Subir</button>
      </form>

      <ul className="list-group">
        {anexos.map((a) => (
          <li key={a.id} className="list-group-item d-flex justify-content-between align-items-center">
            <a
              href={`http://localhost:8080/api/anexos/${a.id}/descargar`}
              target="_blank"
              rel="noopener noreferrer"
            >
              {a.nombre || `Anexo ${a.id}`}
            </a>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default AnexoUploader;
