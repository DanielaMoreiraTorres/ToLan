package com.example.tolan.models;

public class subnivel {
    private int idnivel;
    private String nombre;
    private String descripcion;
    private int numactividades;
    private boolean activo;

    public int getIdnivel() {
        return idnivel;
    }

    public void setIdnivel(int idnivel) {
        this.idnivel = idnivel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getNumactividades() {
        return numactividades;
    }

    public void setNumactividades(int numactividades) {
        this.numactividades = numactividades;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public subnivel(int idnivel, String nombre, String descripcion, int numactividades, boolean activo) {
        this.idnivel = idnivel;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.numactividades = numactividades;
        this.activo = activo;
    }


}
