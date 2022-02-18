package com.example.tolan.models;

import java.util.List;

public class ModelRecyclerItemSubnivel {

    private int id;

    private String titulo;

    private String image ;

    //private List<ModelRecyclerItemActividad> modelRecyclerItemActividades;

    public ModelRecyclerItemSubnivel(String titulo, String image, int id) {
        this.titulo = titulo;
        this.image = image;
        this.id=id;
        //this.modelRecyclerItemActividades = modelRecyclerItemActividades;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
