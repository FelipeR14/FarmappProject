package com.example.proyectofarmapp;

public class Product {
    String id;
    String nombre;
    String ingrediente;
    String contenido;
    String precio;
    String imgUrl;
    String existencia;
    String cantidad;

    public Product() {
    }

    public Product(String id, String nombre, String ingrediente, String contenido, String precio, String imgUrl, String existencia, String cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.ingrediente = ingrediente;
        this.contenido = contenido;
        this.precio = precio;
        this.imgUrl = imgUrl;
        this.existencia = existencia;
        this.cantidad = cantidad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(String ingrediente) {
        this.ingrediente = ingrediente;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getExistencia() {
        return existencia;
    }

    public void setExistencia(String existencia) {
        this.existencia = existencia;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
