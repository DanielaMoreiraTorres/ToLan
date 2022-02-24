package com.example.tolan.models;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public void MapContenido(JSONArray contenido, List<ModelContent> modelContentsEnun,
                             ArrayList<ModelContent> modelContentsOp, ArrayList<ModelContent> respuestas){
        try {
            ModelContent modelContent = null;
            for (int i=0;i<contenido.length();i++){
                modelContent = new ModelContent();
                modelContent.setId(contenido.getJSONObject(i).getInt("id"));
                modelContent.setDescripcion(contenido.getJSONObject(i).getString("descripcion"));
                modelContent.setEnunciado(contenido.getJSONObject(i).getBoolean("enunciado"));
                modelContent.setRespuesta(contenido.getJSONObject(i).getBoolean("respuesta"));
                modelContent.setActivo(contenido.getJSONObject(i).getBoolean("activo"));
                modelContent.setMultimedia((JSONArray) contenido.getJSONObject(i).get("multimedia"));
                if(contenido.getJSONObject(i).get("enunciado").equals(true))
                    modelContentsEnun.add(modelContent);
                else{
                    modelContentsOp.add(modelContent);
                    if(contenido.getJSONObject(i).get("respuesta").equals(true))
                        respuestas.add(modelContent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
