package com.productos.negocio;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

// Esta etiqueta es la "URL invisible" a la que JavaScript enviará los datos
@WebServlet("/FavoritosServlet")
public class FavoritosServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. Configurar la radiofrecuencia (Formato de respuesta JSON para JavaScript)
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // 2. Escudo de Autenticación: Verificar si quien dispara está logueado
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario_id") == null) {
            out.print("{\"status\":\"error\", \"message\":\"Acceso Denegado. Sesion no iniciada.\"}");
            return;
        }

        // Extraemos la ID del usuario desde la memoria
        int idUsuario = (Integer) session.getAttribute("usuario_id");

        // 3. Desencriptar el paquete de datos que envió la página web
        String action = request.getParameter("action"); // Puede ser "add" o "remove"
        String idProducto = request.getParameter("idProducto");

        OperacionesCliente db = new OperacionesCliente();
        boolean exito = false;

        // 4. Ejecutar la directiva
        if ("add".equals(action)) {
            String titulo = request.getParameter("titulo");
            String imagen = request.getParameter("imagen");
            exito = db.agregarFavorito(idUsuario, idProducto, titulo, imagen);
        } else if ("remove".equals(action)) {
            exito = db.eliminarFavorito(idUsuario, idProducto);
        }

        // 5. Enviar confirmación de vuelta al JavaScript
        if (exito) {
            out.print("{\"status\":\"success\"}");
        } else {
            out.print("{\"status\":\"error\", \"message\":\"Fallo en la base de datos\"}");
        }
    }
}