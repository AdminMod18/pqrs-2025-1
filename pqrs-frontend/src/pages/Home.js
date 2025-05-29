// src/pages/Home.jsx
import { Link } from 'react-router-dom';

export default function Home() {
  return (
    <div className="min-h-screen bg-gradient-to-b from-[#0d1117] to-[#161b22] text-white flex flex-col">
      {/* Navbar */}
      <header className="flex justify-between items-center px-6 py-4 bg-[#0d1117] border-b border-gray-700">
        <h1 className="text-2xl font-bold">Mi Plataforma</h1>
        <div>
          <Link to="/login" className="text-white border px-4 py-1 rounded hover:bg-white hover:text-black mr-2">
            Iniciar sesión
          </Link>
          <Link to="/register" className="bg-green-600 hover:bg-green-700 px-4 py-1 rounded">
            Registrarse
          </Link>
        </div>
      </header>

      {/* Contenido principal */}
      <main className="flex flex-col items-center justify-center flex-grow text-center px-4">
        <h2 className="text-4xl font-bold mb-4">Construye y lanza tu app en una sola plataforma</h2>
        <p className="text-lg text-gray-300 mb-8">
          Únete a la plataforma más intuitiva para gestionar solicitudes PQRS.
        </p>
        <div className="flex flex-col sm:flex-row gap-4">
          <Link to="/register" className="bg-green-600 px-6 py-2 rounded text-white hover:bg-green-700">
            Comenzar
          </Link>
          <Link to="/login" className="border border-white px-6 py-2 rounded hover:bg-white hover:text-black">
            Ya tengo cuenta
          </Link>
        </div>
      </main>
    </div>
  );
}
