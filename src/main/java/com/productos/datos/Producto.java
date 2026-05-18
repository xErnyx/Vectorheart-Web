package com.productos.datos;

public class Producto {
    private int idProducto;
    private String titulo;
    private String artista;
    private double precio;
    private String categoria;
    private String colorBase;
    private String formato;
    private int amenaza;
    private String rutaImagen;
    private String estadoObra;

    // Constructor Vacío
    public Producto() {}

    // Getters y Setters
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getArtista() { return artista; }
    public void setArtista(String artista) { this.artista = artista; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getColorBase() { return colorBase; }
    public void setColorBase(String colorBase) { this.colorBase = colorBase; }

    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

    public int getAmenaza() { return amenaza; }
    public void setAmenaza(int amenaza) { this.amenaza = amenaza; }

    public String getRutaImagen() { return rutaImagen; }
    public void setRutaImagen(String rutaImagen) { this.rutaImagen = rutaImagen; }

    public String getEstadoObra() { return estadoObra; }
    public void setEstadoObra(String estadoObra) { this.estadoObra = estadoObra; }
}