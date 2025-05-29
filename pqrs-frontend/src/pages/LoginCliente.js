// pages/LoginCliente.js
import React, { useState } from "react";
import { loginUser } from "../services/Api";
import jwtDecode from "jwt-decode";
import { useNavigate } from "react-router-dom";

export default function LoginCliente() {
  const [form, setForm] = useState({ username: "", password: "" });
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await loginUser(form);
      const token = res.data.token;
      localStorage.setItem("token", token);
      const decoded = jwtDecode(token);
      if (decoded.roles.includes("CLIENTE")) {
        navigate("/dashboard-cliente");
      } else {
        alert("No tienes rol CLIENTE");
      }
    } catch (err) {
      console.error(err);
      alert("Error al iniciar sesión");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Login Cliente</h2>
      <input name="username" onChange={handleChange} />
      <input name="password" type="password" onChange={handleChange} />
      <button type="submit">Iniciar Sesión</button>
    </form>
  );
}
