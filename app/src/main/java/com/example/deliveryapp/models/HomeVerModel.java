package com.example.deliveryapp.models;

public class HomeVerModel {
    private int id;
    private String image;
    private String name;
    private String description;
    private String timing;
    private String rating;
    private String precio;
    private String categoria;

    public HomeVerModel(int id, String image, String name, String description, String timing, String rating, String precio,String categoria) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.timing = timing;
        this.rating = rating;
        this.precio = precio;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String  image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
