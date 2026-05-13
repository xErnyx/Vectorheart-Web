package com.productos.negocio;

import com.productos.datos.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OperacionesCliente {

    // =========================================================
    // MÓDULO 1: GESTIÓN DE FAVORITOS (ARCHIVOS CLASIFICADOS)
    // =========================================================

    public boolean agregarFavorito(int idUsuario, String idProducto, String titulo, String imagen) {
        // 1. Escudo Táctico: Verificamos si ya existe para no duplicar datos
        String checkSql = "SELECT id_fav FROM tb_favorito WHERE id_usuario = ? AND producto_id = ?";
        String insertSql = "INSERT INTO tb_favorito (id_usuario, producto_id, producto_titulo, producto_imagen) VALUES (?, ?, ?, ?)";

        // FIX APLICADO: Primero instanciamos tu clase, luego sacamos la conexión
        Conexion db = new Conexion();

        try (Connection con = db.getConexion();
             PreparedStatement psCheck = con.prepareStatement(checkSql)) {

            psCheck.setInt(1, idUsuario);
            psCheck.setString(2, idProducto);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                return true; // El módulo ya estaba en favoritos, abortamos inserción.
            }

            // 2. Si no existe, lo insertamos en la caja fuerte
            try (PreparedStatement psInsert = con.prepareStatement(insertSql)) {
                psInsert.setInt(1, idUsuario);
                psInsert.setString(2, idProducto);
                psInsert.setString(3, titulo);
                psInsert.setString(4, imagen);
                return psInsert.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("SYS.ERR // Falla al insertar favorito: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarFavorito(int idUsuario, String idProducto) {
        String sql = "DELETE FROM tb_favorito WHERE id_usuario = ? AND producto_id = ?";

        // FIX APLICADO: Instanciamos tu clase Conexion
        Conexion db = new Conexion();

        try (Connection con = db.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setString(2, idProducto);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("SYS.ERR // Falla al eliminar favorito: " + e.getMessage());
            return false;
        }
    }

    // =========================================================
    // MÓDULO 2: GESTIÓN DE CARRITO (PENDIENTE PARA LA SIGUIENTE FASE)
    // =========================================================

    // (Aquí construiremos los métodos del carrito una vez que dominemos los favoritos)

    // =========================================================
    // MÓDULO 3: SINCRONIZACIÓN DE MEMORIA (DESCARGA DE FAVORITOS)
    // =========================================================
    public String obtenerFavoritosJSON(int idUsuario) {
        String sql = "SELECT producto_id, producto_titulo, producto_imagen FROM tb_favorito WHERE id_usuario = ?";
        StringBuilder json = new StringBuilder("[");

        Conexion db = new Conexion();
        try (Connection con = db.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            boolean primero = true;
            while (rs.next()) {
                if (!primero) json.append(",");
                json.append("{")
                        .append("\"id\":\"").append(rs.getString("producto_id")).append("\",")
                        .append("\"title\":\"").append(rs.getString("producto_titulo")).append("\",")
                        .append("\"image\":\"").append(rs.getString("producto_imagen")).append("\"")
                        .append("}");
                primero = false;
            }
        } catch (SQLException e) {
            System.out.println("SYS.ERR // Falla al descargar favoritos: " + e.getMessage());
        }

        json.append("]");
        return json.toString(); // Devuelve el paquete JSON listo para el navegador
    }
}