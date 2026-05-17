package com.productos.negocio;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/FacturacionServlet")
public class FacturacionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario_id") == null) {
            out.print("{\"status\":\"error\", \"message\":\"No autenticado\"}");
            return;
        }

        int idUsuario = (Integer) session.getAttribute("usuario_id");
        String metodoPago = request.getParameter("metodo");
        double total = Double.parseDouble(request.getParameter("total"));

        OperacionesCliente ops = new OperacionesCliente();

        // ¡DISPARO AL NÚCLEO DE LA BASE DE DATOS!
        int idFactura = ops.procesarFacturacion(idUsuario, metodoPago, total);

        if (idFactura != -1) {
            out.print("{\"status\":\"success\", \"id_factura\":" + idFactura + "}");
        } else {
            out.print("{\"status\":\"error\", \"message\":\"Fallo al procesar pago en BD\"}");
        }
    }
}