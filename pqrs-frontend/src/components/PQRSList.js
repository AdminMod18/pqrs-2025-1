import React, { useEffect, useState } from "react";
import axios from "axios";

function PQRSList() {
  const [pqrsList, setPqrsList] = useState([]);
  const [error, setError] = useState("");

  const token = localStorage.getItem("token");

  const api = axios.create({
    baseURL: "http://localhost:8080/api",
    headers: { Authorization: `Bearer ${token}` }
  });

  const fetchPQRS = async () => {
    try {
      const res = await api.get("/radicados");
      setPqrsList(res.data);
    } catch (err) {
      console.error("Error al obtener la lista de PQRS:", err);
      setError("No se pudo cargar la lista de PQRS.");
    }
  };

  useEffect(() => {
    fetchPQRS();
  }, []);

  return (
    <div className="container mt-4">
      <h2>Listado de PQRS</h2>

      {error && <div className="alert alert-danger">{error}</div>}

      <table className="table table-bordered table-striped mt-3">
        <thead className="table-dark">
          <tr>
            <th>ID</th>
            <th>Tipo</th>
            <th>Descripción</th>
            <th>Cliente ID</th>
            <th>Fecha</th>
          </tr>
        </thead>
        <tbody>
          {pqrsList.length > 0 ? (
            pqrsList.map((p) => (
              <tr key={p.id}>
                <td>{p.id}</td>
                <td>{p.tipo}</td>
                <td>{p.descripcion}</td>
                <td>{p.clienteId}</td>
                <td>{p.fechaRadicado ? new Date(p.fechaRadicado).toLocaleString() : "-"}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="5" className="text-center">
                No hay registros de PQRS.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default PQRSList;
