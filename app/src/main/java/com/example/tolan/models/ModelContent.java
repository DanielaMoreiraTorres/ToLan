package com.example.tolan.models;

import org.json.JSONArray;

import java.io.Serializable;

public class ModelContent implements Serializable {

    private int id;
    private String descripcion;
    private Boolean enunciado;
    private Boolean respuesta;
    private Boolean activo;
    private JSONArray multimedia;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(Boolean enunciado) {
        this.enunciado = enunciado;
    }

    public Boolean getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Boolean respuesta) {
        this.respuesta = respuesta;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public JSONArray getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(JSONArray multimedia) {
        this.multimedia = multimedia;
    }
}
