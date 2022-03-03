package com.example.tolan.models;

public class ModelHistorial {
    private Integer id;
    private Integer idactividad;
    private String nombre;
    private Integer recompensa;

    public ModelHistorial(Integer id, Integer idactividad, String nombre, Integer recompensa) {
        this.id = id;
        this.idactividad = idactividad;
        this.nombre = nombre;
        this.recompensa = recompensa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdactividad() {
        return idactividad;
    }

    public void setIdactividad(Integer idactividad) {
        this.idactividad = idactividad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getRecompensa() {
        return recompensa;
    }

    public void setRecompensa(Integer recompensa) {
        this.recompensa = recompensa;
    }
}
