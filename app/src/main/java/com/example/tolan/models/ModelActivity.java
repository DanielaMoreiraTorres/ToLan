package com.example.tolan.models;

import org.json.JSONArray;

import java.io.Serializable;

public class ModelActivity implements Serializable {

    private int id;
    private String nombre;
    private String descripcion;
    private int recompensavalor;
    private String tipo;
    private Boolean activo;
    private JSONArray historial;
    private JSONArray contenido;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getRecompensavalor() {
        return recompensavalor;
    }

    public void setRecompensavalor(int recompensavalor) {
        this.recompensavalor = recompensavalor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public JSONArray getHistorial() {
        return historial;
    }

    public void setHistorial(JSONArray historial) {
        this.historial = historial;
    }

    public JSONArray getContenido() {
        return contenido;
    }

    public void setContenido(JSONArray contenido) {
        this.contenido = contenido;
    }
}
