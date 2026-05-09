package com.productos.negocio;
import com.productos.datos.Conexion;

public class Usuario {
    // Atributos de tu tabla tb_usuario
    private String nombre;
    private String cedula;
    private String correo;
    private String clave;

    // Constructor vacío requerido por JSP
    public Usuario() {}

    // Setters: Canales para recibir los datos desde la página web
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setClave(String clave) { this.clave = clave; }

    // El Metodo Maestro: Inyección a la Base de Datos
    public String registrarUsuario() {
        String respuesta = "";
        Conexion con = new Conexion();

        // Regla de Negocio: Todo usuario nuevo entra como Perfil 3 (Cliente) y Estado 1
        String sql = "INSERT INTO tb_usuario(id_per, id_est, nombre_us, cedula_us, correo_us, clave_us) " +
                "VALUES(3, 1, '" + this.nombre + "', '" + this.cedula + "', '" + this.correo + "', '" + this.clave + "')";

        try {
            respuesta = con.Ejecutar(sql);
        } catch (Exception e) {
            respuesta = "Error de inserción: " + e.getMessage();
        }
        return respuesta;
    }
    // El Metodo Maestro de Autenticación
    public int verificarLogin(String correoLogin, String claveLogin) {
        int perfilAsignado = 0; // 0 significa que las credenciales son inválidas
        Conexion con = new Conexion();

        // Buscamos si existe un usuario con ese correo y clave exactos
        String sql = "SELECT id_per, nombre_us FROM tb_usuario WHERE correo_us = '" + correoLogin + "' AND clave_us = '" + claveLogin + "'";

        try {
            java.sql.ResultSet rs = con.Consulta(sql);

            // Si rs.next() es verdadero, significa que encontró al usuario
            if (rs.next()) {
                perfilAsignado = rs.getInt("id_per"); // Recuperamos su rol (1, 2 o 3)
                this.nombre = rs.getString("nombre_us"); // Guardamos su nombre para saludarlo luego
            }
        } catch (Exception e) {
            System.out.println("Error en la matriz de login: " + e.getMessage());
        }

        return perfilAsignado; // Retorna el número de rol, o 0 si falló
    }

    // Añade también este pequeño "Get" para poder extraer el nombre desde el JSP
    public String getNombre() {
        return this.nombre;
    }
}