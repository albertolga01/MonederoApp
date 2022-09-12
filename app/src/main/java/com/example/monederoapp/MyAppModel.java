package com.example.monederoapp;

public class MyAppModel {

    private String nombre, precio, stock, img;
    private String name, imageurl, description, IdTarjeta;


    // Campo nombre
    public String getName() {
        return name;
    }
    // Seteo el nombre
    public void setName(String name) {
        this.name = name;
    }


    // Campo descripcion
    public String getdescription() {
        return description;
    }
    // Seteo el decripcion
    public void setdescription(String description) {
        this.description = description;
    }

    // Campo Tarjeta
    public String getIdTarjeta() {
        return IdTarjeta;
    }
    // Seteo el Tarjeta
    public void setIdTarjeta(String IdTarjeta) {
        this.IdTarjeta = IdTarjeta;
    }

    // Campo imagen
    public String getimageurl(){
        return imageurl;
    }
    // Seteo la imagen
    public void setImageurl(String imageurl){
        this.imageurl = imageurl;
    }

}
