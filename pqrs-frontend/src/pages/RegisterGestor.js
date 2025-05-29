// pages/RegisterCliente.js
import React, { useState } from "react";
import { registerUser } from "../services/Api";

export default function RegisterCliente() {
  const [form, setForm] = useState({ username: "", password: "", email: "" });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await registerUser({ ...form, roles: ["GESTOR"] });
      alert("Registro como GESTOR exitoso");
    } catch (err) {
      console.error(err);
      alert("Error en el registro");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Registro Cliente</h2>
      <input name="username" placeholder="Usuario" onChange={handleChange} />
      <input name="email" placeholder="Email" onChange={handleChange} />
      <input name="password" type="password" placeholder="Contraseña" onChange={handleChange} />
      <button type="submit">Registrarse</button>
    </form>
  );
}
