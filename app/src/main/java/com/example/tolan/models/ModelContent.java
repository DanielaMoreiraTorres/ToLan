package com.example.tolan.models;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelContent implements Serializable {

    private int id;
    private String descripcion;
    private Boolean enunciado;
    private Boolean respuesta;
    private Boolean activo;
    private JSONArray multimedia;

    public static String urlInicial = "", urlInicialOp = "";

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

    public void MapContenido(JSONArray contenido, ArrayList<String> listRutasMultimedia,
                             ArrayList<String> listItemsMultimedia, Map<String, List<String>> map_MultimediaExtra,
                             ArrayList<String> listRutasMultimediaOp, ArrayList<String> listItemsMultimediaOp,
                             Map<String, List<String>> map_MultimediaExtraOp,
                             List<ModelContent> modelContentsEnun, ArrayList<ModelContent> modelContentsOp,
                             ArrayList<ModelContent> modelContentsIni, ArrayList<ModelContent> respuestas){
        try {
            ModelContent modelContent = null;
            urlInicial = "";
            urlInicialOp = "";
            List<String> lstUrls_Ayuda = new ArrayList<>();
            List<String> lstUrls_AyudaOp = new ArrayList<>();
            for (int i=0;i<contenido.length();i++){
                modelContent = new ModelContent();
                modelContent.setId(contenido.getJSONObject(i).getInt("id"));
                modelContent.setDescripcion(contenido.getJSONObject(i).getString("descripcion"));
                modelContent.setEnunciado(contenido.getJSONObject(i).getBoolean("enunciado"));
                modelContent.setRespuesta(contenido.getJSONObject(i).getBoolean("respuesta"));
                modelContent.setActivo(contenido.getJSONObject(i).getBoolean("activo"));
                modelContent.setMultimedia((JSONArray) contenido.getJSONObject(i).get("multimedia"));
                if(contenido.getJSONObject(i).get("enunciado").equals(true)) {
                    if (((JSONArray) contenido.getJSONObject(i).get("multimedia")).length() > 0) {
                        for (int c = 0; c < modelContent.getMultimedia().length(); c++) {
                            if (modelContent.getMultimedia().getJSONObject(c).getBoolean("inicial")) {
                                listItemsMultimedia.add(modelContent.getMultimedia().getJSONObject(c).getString("descripcion"));
                                urlInicial = modelContent.getMultimedia().getJSONObject(c).getString("url");
                                listRutasMultimedia.add(urlInicial);
                            } else
                                lstUrls_Ayuda.add(modelContent.getMultimedia().getJSONObject(c).getString("url"));
                        }
                        map_MultimediaExtra.put(urlInicial, lstUrls_Ayuda);
                    } else
                        modelContentsEnun.add(modelContent);
                } else{
                    modelContentsOp.add(modelContent);
                    /* int contMulti = modelContent.getMultimedia().length();
                    for (int m = 0; m < contMulti; m++) {
                        if (modelContent.getMultimedia().getJSONObject(m).getBoolean("inicial"))
                            modelContentsIni.add(modelContent);
                    }*/
                    if (((JSONArray) contenido.getJSONObject(i).get("multimedia")).length() > 0) {
                        for (int c = 0; c < modelContent.getMultimedia().length(); c++) {
                            if (modelContent.getMultimedia().getJSONObject(c).getBoolean("inicial")) {
                                listItemsMultimediaOp.add(modelContent.getMultimedia().getJSONObject(c).getString("descripcion"));
                                urlInicialOp = modelContent.getMultimedia().getJSONObject(c).getString("url");
                                listRutasMultimediaOp.add(urlInicialOp);
                                modelContentsIni.add(modelContent);
                            } else
                                lstUrls_AyudaOp.add(modelContent.getMultimedia().getJSONObject(c).getString("url"));
                        }
                        map_MultimediaExtraOp.put(urlInicialOp, lstUrls_AyudaOp);
                    }
                    if(contenido.getJSONObject(i).get("respuesta").equals(true))
                        respuestas.add(modelContent);
                }
            }
            if(modelContentsEnun.size() == 0) {
                for (int i=0;i<contenido.length();i++) {
                    modelContent = new ModelContent();
                    modelContent.setId(contenido.getJSONObject(i).getInt("id"));
                    modelContent.setDescripcion(contenido.getJSONObject(i).getString("descripcion"));
                    modelContent.setEnunciado(contenido.getJSONObject(i).getBoolean("enunciado"));
                    modelContent.setRespuesta(contenido.getJSONObject(i).getBoolean("respuesta"));
                    modelContent.setActivo(contenido.getJSONObject(i).getBoolean("activo"));
                    modelContent.setMultimedia((JSONArray) contenido.getJSONObject(i).get("multimedia"));
                    if(contenido.getJSONObject(i).get("enunciado").equals(true)) {
                        modelContentsEnun.add(modelContent);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
