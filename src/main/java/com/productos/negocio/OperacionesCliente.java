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
        String checkSql = "SELECT id_fav FROM tb_favorito WHERE id_usuario = ? AND producto_id = ?";
        String insertSql = "INSERT INTO tb_favorito (id_usuario, producto_id, producto_titulo, producto_imagen) VALUES (?, ?, ?, ?)";
        Conexion db = new Conexion();

        try (Connection con = db.getConexion();
             PreparedStatement psCheck = con.prepareStatement(checkSql)) {
            psCheck.setInt(1, idUsuario);
            psCheck.setString(2, idProducto);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) return true;

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
    // MÓDULO 2: GESTIÓN DE CARRITO E HISTORIAL (NUEVO)
    // =========================================================

    public boolean agregarAlCarrito(int idUsuario, String idProducto, String titulo, String imagen, double precio) {
        // 1. Verificamos si ya está en el carrito. Si está, no hacemos nada (es arte digital, no se compran 2 iguales)
        String checkSql = "SELECT id_carrito FROM tb_carrito WHERE id_usuario = ? AND producto_id = ?";
        String insertSql = "INSERT INTO tb_carrito (id_usuario, producto_id, producto_titulo, producto_imagen, precio_unitario) VALUES (?, ?, ?, ?, ?)";

        Conexion db = new Conexion();

        try (Connection con = db.getConexion();
             PreparedStatement psCheck = con.prepareStatement(checkSql)) {

            psCheck.setInt(1, idUsuario);
            psCheck.setString(2, idProducto);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) return true; // Ya está en el carrito

            // 2. Insertamos el nuevo módulo en el carrito
            try (PreparedStatement psInsert = con.prepareStatement(insertSql)) {
                psInsert.setInt(1, idUsuario);
                psInsert.setString(2, idProducto);
                psInsert.setString(3, titulo);
                psInsert.setString(4, imagen);
                psInsert.setDouble(5, precio);
                return psInsert.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("SYS.ERR // Falla al insertar en carrito: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarDelCarrito(int idUsuario, String idProducto) {
        String sql = "DELETE FROM tb_carrito WHERE id_usuario = ? AND producto_id = ?";
        Conexion db = new Conexion();

        try (Connection con = db.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, idProducto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("SYS.ERR // Falla al eliminar del carrito: " + e.getMessage());
            return false;
        }
    }

    // Método crucial para "PURGAR" el carrito sin tener que mandar ID por ID
    public boolean purgarCarrito(int idUsuario) {
        String sql = "DELETE FROM tb_carrito WHERE id_usuario = ?";
        Conexion db = new Conexion();

        try (Connection con = db.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("SYS.ERR // Falla al purgar carrito: " + e.getMessage());
            return false;
        }
    }

    // =========================================================
    // MÓDULO 3: SINCRONIZACIÓN DE MEMORIA (DESCARGA DE DATOS)
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
        return json.toString();
    }

    // NUEVO: Descarga del carrito al iniciar sesión
    public String obtenerCarritoJSON(int idUsuario) {
        String sql = "SELECT producto_id, producto_titulo, producto_imagen, precio_unitario FROM tb_carrito WHERE id_usuario = ?";
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
                        .append("\"image\":\"").append(rs.getString("producto_imagen")).append("\",")
                        .append("\"price\":").append(rs.getDouble("precio_unitario")) // Ojo, sin comillas porque es número
                        .append("}");
                primero = false;
            }
        } catch (SQLException e) {
            System.out.println("SYS.ERR // Falla al descargar carrito: " + e.getMessage());
        }
        json.append("]");
        return json.toString();
    }

    // =========================================================
    // MÓDULO 4: FACTURACIÓN Y TRANSACCIONES
    // =========================================================
    public int procesarFacturacion(int idUsuario, String metodoPago, double total) {
        String sqlInsertPedido = "INSERT INTO tb_pedido (id_usuario, total, metodo_pago, estado_pedido) VALUES (?, ?, ?, 'COMPLETADO') RETURNING id_pedido";
        String sqlSelectCarrito = "SELECT producto_id, producto_titulo, cantidad, precio_unitario FROM tb_carrito WHERE id_usuario = ?";
        String sqlInsertDetalle = "INSERT INTO tb_detalle_pedido (id_pedido, producto_id, producto_titulo, cantidad, precio_unitario) VALUES (?, ?, ?, ?, ?)";
        String sqlDeleteCarrito = "DELETE FROM tb_carrito WHERE id_usuario = ?";

        Conexion db = new Conexion();
        int idPedidoGenerado = -1;

        try (Connection con = db.getConexion()) {
            con.setAutoCommit(false); // INICIAMOS TRANSACCIÓN SEGURA

            // 1. Crear el Pedido (La Cabecera)
            try (PreparedStatement psPedido = con.prepareStatement(sqlInsertPedido)) {
                psPedido.setInt(1, idUsuario);
                psPedido.setDouble(2, total);
                psPedido.setString(3, metodoPago);
                ResultSet rs = psPedido.executeQuery();
                if (rs.next()) {
                    idPedidoGenerado = rs.getInt("id_pedido");
                }
            }

            if (idPedidoGenerado != -1) {
                // 2. Copiar los items del Carrito a los Detalles
                try (PreparedStatement psSelect = con.prepareStatement(sqlSelectCarrito);
                     PreparedStatement psDetalle = con.prepareStatement(sqlInsertDetalle)) {

                    psSelect.setInt(1, idUsuario);
                    ResultSet rsCart = psSelect.executeQuery();

                    while (rsCart.next()) {
                        psDetalle.setInt(1, idPedidoGenerado);
                        psDetalle.setString(2, rsCart.getString("producto_id"));
                        psDetalle.setString(3, rsCart.getString("producto_titulo"));
                        psDetalle.setInt(4, rsCart.getInt("cantidad"));
                        psDetalle.setDouble(5, rsCart.getDouble("precio_unitario"));
                        psDetalle.executeUpdate();
                    }
                }

                // 3. Destruir el Carrito
                try (PreparedStatement psDelete = con.prepareStatement(sqlDeleteCarrito)) {
                    psDelete.setInt(1, idUsuario);
                    psDelete.executeUpdate();
                }

                con.commit(); // CONFIRMAMOS
                return idPedidoGenerado;
            } else {
                con.rollback();
            }
        } catch (SQLException e) {
            System.out.println("SYS.ERR // Falla crítica en facturación: " + e.getMessage());
        }
        return -1;
    }

    // =========================================================
    // MÓDULO 5: EXTRACCIÓN DE HISTORIAL PARA EL FRONTEND (JSON)
    // =========================================================
    public String obtenerHistorialPedidosJSON(int idUsuario) {
        StringBuilder json = new StringBuilder("[");
        // Buscamos en nuestra tabla oficial y real: tb_factura
        String sql = "SELECT * FROM tb_factura WHERE id_usuario = ? ORDER BY id_factura DESC";
        com.productos.datos.Conexion db = new com.productos.datos.Conexion();

        try (java.sql.Connection con = db.getConexion();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                boolean first = true;
                while (rs.next()) {
                    if (!first) json.append(",");

                    // Formateamos la fecha para que se vea táctica (Ej: 2026-05-18 14:30)
                    String fechaF = rs.getTimestamp("fecha").toString().substring(0, 16);
                    // Formateamos el ID a 6 dígitos (Ej: 000001)
                    String idF = String.format("%06d", rs.getInt("id_factura"));

                    json.append("{");
                    json.append("\"id\":\"").append(idF).append("\",");
                    json.append("\"metodo\":\"").append(rs.getString("metodo_pago")).append("\",");
                    json.append("\"total\":").append(rs.getDouble("total_pagado")).append(",");
                    json.append("\"fecha\":\"").append(fechaF).append("\"");
                    json.append("}");

                    first = false;
                }
            }
        } catch (Exception e) {
            System.out.println("SYS.ERR // Falla al empaquetar JSON de historial: " + e.getMessage());
        }
        json.append("]");
        return json.toString();
    }

    // =========================================================
    // MÓDULO ADMINISTRADOR: LISTADO DE PERSONAL TÁCTICO
    // =========================================================
    public String obtenerTodosLosUsuariosJSON() {
        StringBuilder json = new StringBuilder("[");
        // Ajustado a tus columnas reales: id_per (Rol) y id_us (ID)
        String sql = "SELECT * FROM tb_usuario ORDER BY id_per ASC, id_us ASC";
        com.productos.datos.Conexion db = new com.productos.datos.Conexion();

        try (java.sql.Connection con = db.getConexion();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            boolean first = true;
            while (rs.next()) {
                if (!first) json.append(",");
                json.append("{");
                json.append("\"id\":").append(rs.getInt("id_us")).append(",");
                json.append("\"nombre\":\"").append(rs.getString("nombre_us")).append("\",");
                json.append("\"correo\":\"").append(rs.getString("correo_us")).append("\",");
                json.append("\"rol\":").append(rs.getInt("id_per"));
                json.append("}");
                first = false;
            }
        } catch (Exception e) {
            System.out.println("SYS.ERR // Falla al extraer usuarios globales: " + e.getMessage());
        }
        json.append("]");
        return json.toString();
    }
}