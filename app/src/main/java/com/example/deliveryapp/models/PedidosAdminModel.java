package com.example.deliveryapp.models;

public class PedidosAdminModel {

    private int idPedido;
    private int idMenu;
    private String nombre;
    private String imagen;
    private String precioUnidad;
    private int idUsuario;
    private String totalPagar;
    private String fecha;
    private String cantidad;
    private Boolean enviado;
    private Boolean preparando;

    public PedidosAdminModel(int idPedido, int idMenu, String nombre, String imagen, String precioUnidad, int idUsuario, String totalPagar, String fecha, String cantidad, Boolean enviado, Boolean preparando) {
        this.idPedido = idPedido;
        this.idMenu = idMenu;
        this.nombre = nombre;
        this.imagen = imagen;
        this.precioUnidad = precioUnidad;
        this.idUsuario = idUsuario;
        this.totalPagar = totalPagar;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.enviado = enviado;
        this.preparando = preparando;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(String precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(String totalPagar) {
        this.totalPagar = totalPagar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
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
}
