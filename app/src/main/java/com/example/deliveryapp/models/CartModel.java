package com.example.deliveryapp.models;

public class CartModel {
    private int id;
    private String total;
    private String fecha;
    private Boolean enviado;
    private Boolean preparando;
    private String cantidad;
    private String image;
    private String nombre;
    private String precio;

    public CartModel(int id, String total, String fecha, Boolean enviado, Boolean preparando, String cantidad, String image, String nombre, String precio) {
        this.id = id;
        this.total = total;
        this.fecha = fecha;
        this.enviado = enviado;
        this.preparando = preparando;
        this.cantidad = cantidad;
        this.image = image;
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }

    public Boolean getPreparando() {
        return preparando;
    }

    public void setPreparando(Boolean preparando) {
        this.preparando = preparando;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
