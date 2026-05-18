package com.productos.controlador;

import com.productos.datos.Conexion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "CatalogoServlet", urlPatterns = {"/api/catalogo"})
public class CatalogoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Configurar la respuesta como JSON puro (Igual que una API externa)
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Evitar el caché del navegador (CORS)
        response.setHeader("Access-Control-Allow-Origin", "*");

        PrintWriter out = response.getWriter();
        StringBuilder json = new StringBuilder();
        json.append("[");

        // Extraer SOLO las obras con estado 'ACTIVO'
        String sql = "SELECT * FROM tb_producto WHERE estado_obra = 'ACTIVO' ORDER BY id_pr DESC";
        Conexion db = new Conexion();

        try (Connection con = db.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean first = true;
            while (rs.next()) {
                if (!first) {
                    json.append(",");
                }

                // Construcción manual del bloque JSON
                json.append("{");
                json.append("\"id_pr\": ").append(rs.getInt("id_pr")).append(",");
                json.append("\"titulo\": \"").append(escapeJson(rs.getString("nombre_pr"))).append("\",");
                json.append("\"artista\": \"").append(escapeJson(rs.getString("artista"))).append("\",");
                json.append("\"precio\": ").append(rs.getDouble("precio_pr")).append(",");
                json.append("\"categoria\": \"").append(escapeJson(rs.getString("categoria"))).append("\",");
                json.append("\"color_base\": \"").append(escapeJson(rs.getString("color_base"))).append("\",");
                json.append("\"formato\": \"").append(escapeJson(rs.getString("formato"))).append("\",");
                json.append("\"amenaza\": ").append(rs.getInt("amenaza")).append(",");
                json.append("\"ruta_imagen\": \"").append(escapeJson(rs.getString("ruta_imagen"))).append("\"");
                json.append("}");

                first = false;
            }
        } catch (Exception e) {
            System.out.println("SYS.ERR // Falla en CatalogoServlet: " + e.getMessage());
            out.print("[]");
            return;
        }

        json.append("]");
        out.print(json.toString());
        out.flush();
    }

    // Escudo para limpiar caracteres que romperían el JSON (comillas dobles, saltos de línea)
    private String escapeJson(String data) {
        if (data == null) return "";
        return data.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}