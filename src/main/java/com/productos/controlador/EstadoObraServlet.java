package com.productos.controlador;

import com.productos.negocio.OperacionesProducto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "EstadoObraServlet", urlPatterns = {"/EstadoObraServlet"})
public class EstadoObraServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validar seguridad (Solo Gestor o Admin)
        if (request.getSession().getAttribute("usuario_rol") == null || (Integer) request.getSession().getAttribute("usuario_rol") > 2) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int idObra = Integer.parseInt(request.getParameter("id_obra"));
            String accion = request.getParameter("accion"); // Trae "OCULTAR" o "ACTIVAR"

            // Si la acción es OCULTAR, el nuevo estado será OCULTO. Si no, será ACTIVO.
            String nuevoEstado = "OCULTAR".equals(accion) ? "OCULTO" : "ACTIVO";

            OperacionesProducto op = new OperacionesProducto();
            if (op.cambiarEstadoObra(idObra, nuevoEstado)) {
                response.sendRedirect("inventario.jsp?status=updated");
            } else {
                response.sendRedirect("inventario.jsp?status=error");
            }
        } catch (Exception e) {
            System.out.println("SYS.CRIT // Error al procesar estado de obra: " + e.getMessage());
            response.sendRedirect("inventario.jsp?status=critical");
        }
    }
}