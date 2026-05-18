package com.productos.datos;

import java.sql.Timestamp;

public class Factura {
    private int idFactura;
    private int idUsuario;
    private String metodoPago;
    private double totalPagado;
    private Timestamp fecha;

    // Constructores
    public Factura() {}

    // Getters y Setters
    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public double getTotalPagado() { return totalPagado; }
    public void setTotalPagado(double totalPagado) { this.totalPagado = totalPagado; }

    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }
}