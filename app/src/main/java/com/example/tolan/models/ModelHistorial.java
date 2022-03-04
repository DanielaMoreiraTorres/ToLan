package com.example.tolan.models;

public class ModelHistorial {
    private Integer id;
    private Integer idactividad;
    private String nombre;
    private String descripcion;
    private String fecha;
    private Integer recompensa;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ModelHistorial(String descripcion, String fecha, Integer recompensa) {
        this.descripcion = descripcion;
        this.fecha = fecha;
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
