import React, { useState } from "react";
import axios from "axios";

function PQRSForm() {
  const [formData, setFormData] = useState({
    tipo: "",
    descripcion: "",
    clienteId: "",
  });

  const [success, setSuccess] = useState(false);
  const [error, setError] = useState("");

  const token = localStorage.getItem("token");

  const api = axios.create({
    baseURL: "http://localhost:8080/api",
    headers: { Authorization: `Bearer ${token}` }
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post("/radicados", formData); // Asume endpoint para radicar
      setSuccess(true);
      setError("");
      setFormData({ tipo: "", descripcion: "", clienteId: "" });
    } catch (err) {
      setError("Error al enviar PQRS");
      setSuccess(false);
    }
  };

  return (
    <div className="container mt-4">
      <h2>Radicar PQRS</h2>
      <form onSubmit={handleSubmit} className="card p-4">
        <div className="mb-3">
          <label className="form-label">Tipo de PQRS</label>
          <select
            name="tipo"
            value={formData.tipo}
            onChange={handleChange}
            className="form-select"
            required
          >
            <option value="">Seleccionar</option>
            <option value="peticion">Petición</option>
            <option value="queja">Queja</option>
            <option value="reclamo">Reclamo</option>
            <option value="sugerencia">Sugerencia</option>
          </select>
        </div>

        <div className="mb-3">
          <label className="form-label">Descripción</label>
          <textarea
            name="descripcion"
            value={formData.descripcion}
            onChange={handleChange}
            className="form-control"
            rows="4"
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">ID del Cliente</label>
          <input
            type="text"
            name="clienteId"
            value={formData.clienteId}
            onChange={handleChange}
            className="form-control"
            required
          />
        </div>

        <button type="submit" className="btn btn-primary">Enviar</button>

        {success && <div className="alert alert-success mt-3">PQRS enviado con éxito</div>}
        {error && <div className="alert alert-danger mt-3">{error}</div>}
      </form>
    </div>
  );
}

export default PQRSForm;
