import React, { useState } from "react";
import { loginUser } from "../services/Api";
import jwtDecode from "jwt-decode";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

export default function LoginGestor() {
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
      if (decoded.roles.includes("GESTOR")) {
        navigate("/dashboard-gestor");
      } else {
        alert("No tienes rol GESTOR");
      }
    } catch (err) {
      console.error(err);
      alert("Error al iniciar sesión");
    }
  };

  return (
    <div className="container mt-5">
      <h2>Login Gestor</h2>
      <form onSubmit={handleSubmit} className="w-50 mx-auto">
        <input className="form-control mb-2" name="username" placeholder="Usuario" onChange={handleChange} />
        <input className="form-control mb-2" name="password" type="password" placeholder="Contraseña" onChange={handleChange} />
        <button className="btn btn-success w-100" type="submit">Iniciar Sesión</button>
      </form>
    </div>
  );
}
