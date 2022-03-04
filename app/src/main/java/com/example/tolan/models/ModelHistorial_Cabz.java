package com.example.tolan.models;

import java.util.List;

public class ModelHistorial_Cabz {
    private Integer idestudiante;
    private String estudiante;
    private List<ModelHistorial> historial= null;

    public ModelHistorial_Cabz(Integer idestudiante, String estudiante, List<ModelHistorial> historial) {
        this.idestudiante = idestudiante;
        this.estudiante = estudiante;
        this.historial = historial;
    }

    public String getActividades() {
        String Nombres="Actividades"+"\n";
        String Alumnos="";
        for(int i=0; i<historial.size();i++)
            Alumnos+="\n"+historial.get(i).getDescripcion()+
                    " \nFecha Realizada: "+historial.get(i).getFecha()+
                    "\nRecompensa Obtenida: "+historial.get(i).getRecompensa()+"\n";
        Nombres=Nombres+Alumnos;
        return Nombres;
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

    public List<ModelHistorial> getHistorial() {
        return historial;
    }

    public void setHistorial(List<ModelHistorial> historial) {
        this.historial = historial;
    }
}
