// /components/Footer.js
import React from 'react';

const Footer = () => {
  return (
    <footer className="bg-dark text-white mt-auto py-3">
      <div className="container text-center">
        <p className="mb-1">&copy; {new Date().getFullYear()} PQRS App. Todos los derechos reservados.</p>
        <p className="mb-0">
          <a href="/privacy" className="text-white text-decoration-none me-3">Política de privacidad</a>
          <a href="/terms" className="text-white text-decoration-none">Términos de servicio</a>
        </p>
      </div>
    </footer>
  );
};

export default Footer;
