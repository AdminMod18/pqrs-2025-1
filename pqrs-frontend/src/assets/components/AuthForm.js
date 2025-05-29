// src/components/AuthForm.js
import React from "react";
import { Form, Button } from "react-bootstrap";

const AuthForm = ({ title, onSubmit, formData, setFormData, submitLabel }) => {
  return (
    <div className="p-4 border rounded bg-white shadow-sm">
      <h3 className="mb-4">{title}</h3>
      <Form onSubmit={onSubmit}>
        <Form.Group controlId="username" className="mb-3">
          <Form.Label>Usuario</Form.Label>
          <Form.Control
            type="text"
            placeholder="Ingresa tu usuario"
            value={formData.username}
            onChange={(e) =>
              setFormData({ ...formData, username: e.target.value })
            }
            required
          />
        </Form.Group>

        <Form.Group controlId="password" className="mb-3">
          <Form.Label>Contraseña</Form.Label>
          <Form.Control
            type="password"
            placeholder="Contraseña"
            value={formData.password}
            onChange={(e) =>
              setFormData({ ...formData, password: e.target.value })
            }
            required
          />
        </Form.Group>

        {formData.email !== undefined && (
          <Form.Group controlId="email" className="mb-3">
            <Form.Label>Correo electrónico</Form.Label>
            <Form.Control
              type="email"
              placeholder="ejemplo@correo.com"
              value={formData.email}
              onChange={(e) =>
                setFormData({ ...formData, email: e.target.value })
              }
              required
            />
          </Form.Group>
        )}

        <Button type="submit" variant="primary" className="w-100">
          {submitLabel}
        </Button>
      </Form>
    </div>
  );
};

export default AuthForm;
