// src/components/Login.jsx
import React, { useState } from 'react';
import axios from 'axios';

export default function Login() {
  const [loginData, setLoginData] = useState({ username: '', password: '' });

  const handleLogin = async () => {
    try {
      const response = await axios.post('http://localhost:8080/auth/login', loginData);
      alert('Login exitoso');
      console.log(response.data);
    } catch (error) {
      alert('Error al iniciar sesión');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-blue-50">
      <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-sm">
        <h2 className="text-2xl font-bold text-center mb-6 text-blue-600">Iniciar Sesión</h2>
        <input
          type="text"
          placeholder="Usuario"
          value={loginData.username}
          onChange={(e) => setLoginData({ ...loginData, username: e.target.value })}
          className="w-full mb-4 p-2 border rounded"
        />
        <input
          type="password"
          placeholder="Contraseña"
          value={loginData.password}
          onChange={(e) => setLoginData({ ...loginData, password: e.target.value })}
          className="w-full mb-6 p-2 border rounded"
        />
        <button
          onClick={handleLogin}
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
        >
          Entrar
        </button>
      </div>
    </div>
  );
}
