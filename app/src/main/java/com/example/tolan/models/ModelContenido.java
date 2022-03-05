package com.example.tolan.models;

public class ModelContenido {

    Integer idContenido;
    String descripcion;
    boolean isEnunciado, isRespuesta;

    public ModelContenido(Integer idContenido, String descripcion, boolean isEnunciado, boolean isRespuesta) {
        this.idContenido = idContenido;
        this.descripcion = descripcion;
        this.isEnunciado = isEnunciado;
        this.isRespuesta = isRespuesta;
    }

    public Integer getIdContenido() {
        return idContenido;
    }

    public void setIdContenido(Integer idContenido) {
        this.idContenido = idContenido;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEnunciado() {
        return isEnunciado;
    }

    public void setEnunciado(boolean enunciado) {
        isEnunciado = enunciado;
    }

    public boolean isRespuesta() {
        return isRespuesta;
    }

    public void setRespuesta(boolean respuesta) {
        isRespuesta = respuesta;
    }
}
