package com.productos.negocio;

import com.productos.datos.Conexion;
import com.productos.datos.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OperacionesProducto {

    public boolean registrarProducto(Producto p) {
        // MAPEO TÁCTICO: Conectamos los nombres antiguos (nombre_pr, precio_pr)
        // con los nuevos datos de Vectorheart. cantidad_pr e id_cat se mandan en 1 por defecto.
        String sql = "INSERT INTO tb_producto (nombre_pr, artista, precio_pr, categoria, color_base, formato, amenaza, ruta_imagen, cantidad_pr, id_cat) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1, 1)";
        Conexion db = new Conexion();

        try (Connection con = db.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Emparejamiento de datos
            ps.setString(1, p.getTitulo());       // Va a nombre_pr
            ps.setString(2, p.getArtista());      // Va a artista
            ps.setDouble(3, p.getPrecio());       // Va a precio_pr
            ps.setString(4, p.getCategoria());    // Va a categoria
            ps.setString(5, p.getColorBase());    // Va a color_base
            ps.setString(6, p.getFormato());      // Va a formato
            ps.setInt(7, p.getAmenaza());         // Va a amenaza
            ps.setString(8, p.getRutaImagen());   // Va a ruta_imagen

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("SYS.ERR // Falla de inserción en tb_producto: " + e.getMessage());
            return false;
        }
    }

    // =========================================================
    // LECTURA DE INVENTARIO (EXTRAER TODO)
    // =========================================================
    public java.util.List<Producto> obtenerInventario() {
        java.util.List<Producto> lista = new java.util.ArrayList<>();
        String sql = "SELECT * FROM tb_producto ORDER BY id_pr DESC";
        Conexion db = new Conexion();

        try (Connection con = db.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_pr"));
                p.setTitulo(rs.getString("nombre_pr"));
                p.setArtista(rs.getString("artista"));
                p.setPrecio(rs.getDouble("precio_pr"));
                p.setCategoria(rs.getString("categoria"));
                p.setColorBase(rs.getString("color_base"));
                p.setFormato(rs.getString("formato"));
                p.setAmenaza(rs.getInt("amenaza"));
                p.setRutaImagen(rs.getString("ruta_imagen"));
                p.setEstadoObra(rs.getString("estado_obra")); // 'ACTIVO' o 'OCULTO'
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("SYS.ERR // Error al leer inventario: " + e.getMessage());
        }
        return lista;
    }

    // =========================================================
    // BORRADO LÓGICO (CAMBIAR ESTADO A OCULTO/ACTIVO)
    // =========================================================
    public boolean cambiarEstadoObra(int idPr, String nuevoEstado) {
        String sql = "UPDATE tb_producto SET estado_obra = ? WHERE id_pr = ?";
        Conexion db = new Conexion();

        try (Connection con = db.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPr);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("SYS.ERR // Error al cambiar estado: " + e.getMessage());
            return false;
        }
    }
}