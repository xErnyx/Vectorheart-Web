package com.productos.controlador;

import com.productos.datos.Producto;
import com.productos.negocio.OperacionesProducto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "InventarioServlet", urlPatterns = {"/InventarioServlet"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // Umbral de búfer: 2MB
        maxFileSize = 1024 * 1024 * 10,       // Límite de imagen individual: 10MB
        maxRequestSize = 1024 * 1024 * 50     // Límite total del formulario: 50MB
)
public class InventarioServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "assets/art";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Control de Acceso Perimetral: Expulsar si no hay una sesión activa
        if (request.getSession().getAttribute("usuario_rol") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 1. Extracción de Metadatos del Formulario Táctico
            String titulo = request.getParameter("titulo");
            String artista = request.getParameter("artista");
            double precio = Double.parseDouble(request.getParameter("precio"));
            String categoria = request.getParameter("categoria");
            String colorBase = request.getParameter("colorBase");
            String formato = request.getParameter("formato");
            int amenaza = Integer.parseInt(request.getParameter("amenaza"));

            // 2. Procesamiento y Aislamiento del Archivo Binario (Imagen)
            Part part = request.getPart("imagenObra");
            String fileName = extraerNombreArchivo(part);

            // Generación de un identificador único para mitigar colisiones de archivos basura
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

            // Resolución del path absoluto en el despliegue de Tomcat
            String applicationPath = request.getServletContext().getRealPath("");
            String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;

            File uploadDir = new File(uploadFilePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // Creación del directorio en caliente si el servidor no lo tiene
            }

            // Inyección física del archivo en el disco duro del servidor
            part.write(uploadFilePath + File.separator + uniqueFileName);

            // Construcción del puntero de ruta relativa para guardar en la base de datos
            String dbImagePath = UPLOAD_DIR + "/" + uniqueFileName;

            // 3. Montaje sobre el molde de datos encapsulado (Producto)
            Producto p = new Producto();
            p.setTitulo(titulo);
            p.setArtista(artista);
            p.setPrecio(precio);
            p.setCategoria(categoria);
            p.setColorBase(colorBase);
            p.setFormato(formato);
            p.setAmenaza(amenaza);
            p.setRutaImagen(dbImagePath);

            // 4. Ejecución del Comando de Inserción
            OperacionesProducto operaciones = new OperacionesProducto();
            boolean exito = operaciones.registrarProducto(p);

            // 5. Enrutamiento del Estado del Sistema
            if (exito) {
                response.sendRedirect("inventario.jsp?status=success");
            } else {
                response.sendRedirect("inventario.jsp?status=error");
            }

        } catch (Exception e) {
            System.out.println("SYS.CRIT // Excepción en InventarioServlet: " + e.getMessage());
            response.sendRedirect("inventario.jsp?status=critical");
        }
    }

    // Extractor del nombre original del payload multipart
    private String extraerNombreArchivo(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }
}