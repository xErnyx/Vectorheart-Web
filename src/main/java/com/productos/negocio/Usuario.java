package com.productos.negocio;
import com.productos.datos.Conexion;
import java.sql.ResultSet;

public class Usuario {
    private int id_us;
    private String nombre;
    private String cedula;
    private String correo;
    private String clave;
    private String foto; // <--- NUEVA PROPIEDAD

    public Usuario() {}

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setClave(String clave) { this.clave = clave; }
    public void setFoto(String foto) { this.foto = foto; } // <--- NUEVO SETTER

    public String getNombre() { return this.nombre; }
    public int getId_us() { return this.id_us; }
    public String getFoto() { return this.foto; } // <--- NUEVO GETTER

    public String registrarUsuario() {
        String respuesta = "";
        Conexion con = new Conexion();

        // SQL ACTUALIZADO: Ahora inyectamos la foto_us al final
        String sql = "INSERT INTO tb_usuario(id_per, id_est, nombre_us, cedula_us, correo_us, clave_us, foto_us) " +
                "VALUES(3, 1, '" + this.nombre + "', '" + this.cedula + "', '" + this.correo + "', '" + this.clave + "', '" + this.foto + "')";

        try {
            respuesta = con.Ejecutar(sql);
        } catch (Exception e) {
            respuesta = "Error de inserción: " + e.getMessage();
        }
        return respuesta;
    }

    public int verificarLogin(String correoLogin, String claveLogin) {
        int perfilAsignado = 0;
        Conexion con = new Conexion();

        // SQL ACTUALIZADO: Ahora pedimos la foto_us en la consulta
        String sql = "SELECT id_us, id_per, nombre_us, foto_us FROM tb_usuario WHERE correo_us = '" + correoLogin + "' AND clave_us = '" + claveLogin + "'";

        try {
            ResultSet rs = con.Consulta(sql);
            if (rs.next()) {
                this.id_us = rs.getInt("id_us");
                perfilAsignado = rs.getInt("id_per");
                this.nombre = rs.getString("nombre_us");
                this.foto = rs.getString("foto_us"); // <--- CAPTURAMOS LA FOTO DE LA BD
            }
        } catch (Exception e) {
            System.out.println("Error en la matriz de login: " + e.getMessage());
        }
        return perfilAsignado;
    }
}