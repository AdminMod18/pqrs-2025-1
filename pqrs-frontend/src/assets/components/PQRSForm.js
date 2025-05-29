import React, { useState } from 'react';
import axios from 'axios';

const PQRSForm = () => {
  const [tipo, setTipo] = useState('');
  const [comentarios, setComentarios] = useState('');
  const [anexo, setAnexo] = useState(null);
  const [mensaje, setMensaje] = useState('');

  // ✅ Obtener token del localStorage (ajusta si lo guardas diferente)
  const token = localStorage.getItem('token');

  // ✅ Suponiendo que guardaste el clienteId también en localStorage (ajusta según tu flujo)
  const clienteId = localStorage.getItem('clienteId') || 1;

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    const pqrs = {
      cliente: { id: clienteId },
      tipo,
      comentarios,
      anexo: null,
    };

    formData.append('pqrs', new Blob([JSON.stringify(pqrs)], { type: 'application/json' }));
    if (anexo) {
      formData.append('file', anexo);
    }

    try {
      await axios.post('http://localhost:8080/api/radicados/registrar', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${token}`, // ⬅️ incluye el token aquí
        },
      });

      setMensaje('PQRS enviada con éxito.');
      setTipo('');
      setComentarios('');
      setAnexo(null);
    } catch (error) {
      console.error(error);
      if (error.response && error.response.status === 403) {
        setMensaje('No autorizado: verifica tu sesión.');
      } else {
        setMensaje('Error al enviar la PQRS.');
      }
    }
  };

  return (
    <div className="container">
      <h2 className="mb-4">Crear Nueva PQRS</h2>
      {mensaje && <div className="alert alert-info">{mensaje}</div>}
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Tipo de PQRS</label>
          <select
            className="form-select"
            value={tipo}
            onChange={(e) => setTipo(e.target.value)}
            required
          >
            <option value="">Seleccione...</option>
            <option value="PETICION">Petición</option>
            <option value="QUEJA">Queja</option>
            <option value="RECLAMO">Reclamo</option>
            <option value="SUGERENCIA">Sugerencia</option>
          </select>
        </div>

        <div className="mb-3">
          <label className="form-label">Comentarios</label>
          <textarea
            className="form-control"
            rows="4"
            value={comentarios}
            onChange={(e) => setComentarios(e.target.value)}
            required
          ></textarea>
        </div>

        <div className="mb-3">
          <label className="form-label">Anexo (opcional)</label>
          <input
            type="file"
            className="form-control"
            onChange={(e) => setAnexo(e.target.files[0])}
          />
        </div>

        <button type="submit" className="btn btn-primary">Enviar PQRS</button>
      </form>
    </div>
  );
};

export default PQRSForm;
