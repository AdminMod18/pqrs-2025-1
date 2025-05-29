// /services/authService.js

import axios from "axios";

// ✅ URL base corregida para apuntar directamente al controlador de autenticación
const API_URL = "http://localhost:8080/api/auth";

// 🔐 Login
export const login = async (credentials) => {
  const response = await axios.post(`${API_URL}/login`, credentials);
  return response.data;
};

// 📝 Registro
export const register = async (userData) => {
  const response = await axios.post(`${API_URL}/register`, userData);
  return response.data;
};
