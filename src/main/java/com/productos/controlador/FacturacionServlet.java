package com.productos.controlador;

import com.productos.negocio.OperacionesFactura;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "FacturacionServlet", urlPatterns = {"/FacturacionServlet"})
public class FacturacionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Configuramos la respuesta en formato JSON para que app.js lo entienda
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Verificamos que el usuario esté logueado
        HttpSession session = request.getSession();
        if (session.getAttribute("usuario_valido") == null) {
            out.print("{\"status\":\"error\", \"message\":\"ACCESO DENEGADO.\"}");
            return;
        }

        try {
            // Extraemos los datos enviados por app.js y la sesión
            int idUsuario = (Integer) session.getAttribute("usuario_id");
            String metodo = request.getParameter("metodo");
            double total = Double.parseDouble(request.getParameter("total"));

            // Ejecutamos la inyección en PostgreSQL
            OperacionesFactura op = new OperacionesFactura();
            int idFactura = op.registrarFactura(idUsuario, metodo, total);

            if (idFactura > 0) {
                // NUEVO: Purgar el carrito en la base de datos tras la compra exitosa
                new com.productos.negocio.OperacionesCliente().purgarCarrito(idUsuario);

                // ÉXITO: Devolvemos el ID de la factura
                out.print("{\"status\":\"success\", \"id_factura\":" + idFactura + "}");
            } else {
                out.print("{\"status\":\"error\", \"message\":\"RECHAZO EN LA BÓVEDA.\"}");
            }

        } catch (Exception e) {
            System.out.println("SYS.CRIT // Error fatal en FacturacionServlet: " + e.getMessage());
            out.print("{\"status\":\"error\", \"message\":\"ERROR INTERNO DEL SERVIDOR.\"}");
        } finally {
            out.flush();
        }
    }
}