package com.productos.negocio;

import com.productos.datos.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OperacionesFactura {

    // Método que registra la compra y nos devuelve el número de factura generado
    public int registrarFactura(int idUsuario, String metodoPago, double total) {
        // Usamos RETURNING id_factura para que PostgreSQL nos devuelva el código exacto del recibo
        String sql = "INSERT INTO tb_factura (id_usuario, metodo_pago, total_pagado) VALUES (?, ?, ?) RETURNING id_factura";
        Conexion db = new Conexion();

        try (Connection con = db.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setString(2, metodoPago);
            ps.setDouble(3, total);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_factura"); // Retornamos el código de éxito
                }
            }
        } catch (Exception e) {
            System.out.println("SYS.ERR // Falla de contención al generar factura: " + e.getMessage());
        }
        return -1; // -1 significa que hubo un error crítico
    }
    // =========================================================
    // EXTRAER HISTORIAL DE COMPRAS DE UN USUARIO
    // =========================================================
    public java.util.List<com.productos.datos.Factura> obtenerHistorial(int idUsuario) {
        java.util.List<com.productos.datos.Factura> lista = new java.util.ArrayList<>();
        String sql = "SELECT * FROM tb_factura WHERE id_usuario = ? ORDER BY id_factura DESC";
        com.productos.datos.Conexion db = new com.productos.datos.Conexion();

        try (Connection con = db.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    com.productos.datos.Factura f = new com.productos.datos.Factura();
                    f.setIdFactura(rs.getInt("id_factura"));
                    f.setIdUsuario(rs.getInt("id_usuario"));
                    f.setMetodoPago(rs.getString("metodo_pago"));
                    f.setTotalPagado(rs.getDouble("total_pagado"));
                    f.setFecha(rs.getTimestamp("fecha"));
                    lista.add(f);
                }
            }
        } catch (Exception e) {
            System.out.println("SYS.ERR // Falla al extraer historial de facturas: " + e.getMessage());
        }
        return lista;
    }
    // =========================================================
    // MÓDULO ADMINISTRADOR: ESTADÍSTICAS GLOBALES
    // =========================================================
    public double obtenerIngresosGlobales() {
        double total = 0;
        String sql = "SELECT SUM(total_pagado) AS ingresos FROM tb_factura";
        com.productos.datos.Conexion db = new com.productos.datos.Conexion();
        try (java.sql.Connection con = db.getConexion();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            if (rs.next()) total = rs.getDouble("ingresos");
        } catch (Exception e) {
            System.out.println("SYS.ERR // Falla al obtener ingresos globales: " + e.getMessage());
        }
        return total;
    }

    public int obtenerTotalFacturas() {
        int total = 0;
        String sql = "SELECT COUNT(id_factura) AS conteo FROM tb_factura";
        com.productos.datos.Conexion db = new com.productos.datos.Conexion();
        try (java.sql.Connection con = db.getConexion();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            if (rs.next()) total = rs.getInt("conteo");
        } catch (Exception e) {
            System.out.println("SYS.ERR // Falla al obtener conteo de facturas: " + e.getMessage());
        }
        return total;
    }
}