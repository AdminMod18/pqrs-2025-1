// services/api.js
import axios from 'axios';

// Base URL del backend (ajusta si tu puerto o URL es diferente)
const API_URL = 'http://localhost:8080/api';

// Axios sin token para login y registro
const API = axios.create({
  baseURL: API_URL,
});

// Axios con token (para endpoints protegidos)
const authAPI = axios.create({
  baseURL: API_URL,
});

// Agrega automáticamente el token a cada solicitud protegida
authAPI.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// =======================
// 🧾 Autenticación
// =======================

export const registerUser = (data) => API.post('/auth/register', data);

export const loginUser = (credentials) => API.post('/auth/login', credentials);

// =======================
// 📬 PQRS
// =======================

// Crear una nueva PQRS
export const createPQRS = (data) => authAPI.post('/radicados', data);

// Obtener todas las PQRS (requiere rol gestor)
export const getAllPQRS = () => authAPI.get('/radicados');

// Obtener PQRS por ID de usuario (requiere rol usuario)
export const getPQRSByUserId = (userId) =>
  authAPI.get(`/radicados/usuario/${userId}`);

// =======================
// 📎 Anexos (opcional)
// =======================

export const uploadAnexo = (radicadoId, file) => {
  const formData = new FormData();
  formData.append('file', file);
  return authAPI.post(`/anexos/upload/${radicadoId}`, formData);
};

// =======================
// 🚪 Logout (solo utilitario local)
// =======================

export const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
};
