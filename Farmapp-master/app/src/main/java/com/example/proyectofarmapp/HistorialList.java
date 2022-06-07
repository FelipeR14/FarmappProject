package com.example.proyectofarmapp;

public class HistorialList {
    String Fecha;
    String TotalPagado;
    String Productos;
    String TotalPiezas;

    public HistorialList() {
    }

    public HistorialList(String fecha, String totalPagado, String productos, String totalPiezas) {
        Fecha = fecha;
        TotalPagado = totalPagado;
        Productos = productos;
        TotalPiezas = totalPiezas;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getTotalPagado() {
        return TotalPagado;
    }

    public void setTotalPagado(String totalPagado) {
        TotalPagado = totalPagado;
    }

    public String getProductos() {
        return Productos;
    }

    public void setProductos(String productos) {
        Productos = productos;
    }

    public String getTotalPiezas() {
        return TotalPiezas;
    }

    public void setTotalPiezas(String totalPiezas) {
        TotalPiezas = totalPiezas;
    }
}
