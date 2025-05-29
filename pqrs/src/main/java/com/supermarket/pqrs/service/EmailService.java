package com.supermarket.pqrs.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCredenciales(String correoDestino, String username, String password) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

            helper.setTo(correoDestino);
            helper.setSubject("Credenciales de acceso");
            helper.setText(
                    String.format("Hola %s,\n\nTus credenciales son:\nUsuario: %s\nContraseña: %s",
                            username, username, password)
            );

            // 👉 Establecer remitente (correo configurado en application.properties)
            helper.setFrom("hugo.latorre@infracommerce.lat"); // reemplaza por el mismo que usas en spring.mail.username

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        }
    }
}
