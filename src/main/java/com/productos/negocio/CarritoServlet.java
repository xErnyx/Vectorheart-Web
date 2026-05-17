package com.productos.negocio;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

// ESTA ES LA RUTA EXACTA A LA QUE LLAMARÁ EL JAVASCRIPT
@WebServlet("/CarritoServlet")
public class CarritoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Configuramos la transmisión para que responda en formato JSON puro
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // 1. ESCUDO TÁCTICO: ¿Hay un usuario logueado?
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario_id") == null) {
            out.print("{\"status\":\"error\", \"message\":\"[SYS.WARN] Operador no identificado. Inicie sesión.\"}");
            return;
        }

        // 2. Extraemos el ID del usuario y la orden que mandó JavaScript
        int idUsuario = (Integer) session.getAttribute("usuario_id");
        String action = request.getParameter("action");

        OperacionesCliente operaciones = new OperacionesCliente();
        boolean exito = false;

        try {
            // 3. ENRUTADOR DE ÓRDENES
            if ("add".equals(action)) {
                String idProducto = request.getParameter("idProducto");
                String titulo = request.getParameter("titulo");
                String imagen = request.getParameter("imagen");
                // Parseamos el precio con cuidado
                double precio = 0.0;
                try {
                    precio = Double.parseDouble(request.getParameter("precio"));
                } catch (NumberFormatException e) {
                    System.out.println("SYS.ERR // Precio inválido recibido: " + request.getParameter("precio"));
                }

                exito = operaciones.agregarAlCarrito(idUsuario, idProducto, titulo, imagen, precio);

            } else if ("remove".equals(action)) {
                String idProducto = request.getParameter("idProducto");
                exito = operaciones.eliminarDelCarrito(idUsuario, idProducto);

            } else if ("clear".equals(action)) {
                exito = operaciones.purgarCarrito(idUsuario);

            } else {
                out.print("{\"status\":\"error\", \"message\":\"[SYS.ERR] Comando desconocido.\"}");
                return;
            }

            // 4. RESPUESTA AL NAVEGADOR
            if (exito) {
                out.print("{\"status\":\"success\"}");
            } else {
                out.print("{\"status\":\"error\", \"message\":\"[SYS.ERR] Falla en la base de datos PostgreSQL.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Convertimos las comillas dobles a simples para no romper el JSON
            String errorLimpio = e.getMessage().replace("\"", "'");
            out.print("{\"status\":\"error\", \"message\":\"[SYS.FATAL] Error interno: " + errorLimpio + "\"}");
        }
    }
}