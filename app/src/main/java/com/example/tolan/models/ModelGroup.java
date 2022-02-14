package com.example.tolan.models;

public class ModelGroup {
    private Integer id;
    private Integer iddocente;
    private String docente;
    private Integer idestudiante;
    private String estudiante;
    private String fecha;
    private Boolean activo;

    public ModelGroup() {
    }

    public ModelGroup(Integer id, Integer iddocente, String docente, Integer idestudiante, String estudiante, String fecha, Boolean activo) {
        this.id = id;
        this.iddocente = iddocente;
        this.docente = docente;
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

    public Integer getIddocente() {
        return iddocente;
    }

    public void setIddocente(Integer iddocente) {
        this.iddocente = iddocente;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
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

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
