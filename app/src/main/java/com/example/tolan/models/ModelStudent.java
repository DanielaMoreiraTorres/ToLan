package com.example.tolan.models;


public class ModelStudent {
    private Integer id;
    private Integer idestudiante;
    private String estudiante;
    private String fecha;
    private Boolean activo;

    public ModelStudent() {
    }

    public ModelStudent(Integer id, Integer idestudiante, String estudiante, String fecha, Boolean activo) {
        this.id = id;
        this.idestudiante = idestudiante;
        this.estudiante = estudiante;
        this.fecha = fecha;
        this.activo = activo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdestudiante() {
        return idestudiante;
    }

    public void setIdestudiante(Integer idestudiante) {
        this.idestudiante = idestudiante;
    }

    public String getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(String estudiante) {
        this.estudiante = estudiante;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Boolean getActivo() {
        return activo;
    }
    public String getActivoS() {
        if(String.valueOf(activo).equals("true"))
        return "Activo";
        else
            return "Inactivo";
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }




}
