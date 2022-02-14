package com.example.tolan.models;

import java.io.Serializable;
import java.util.List;

public class ModelGroup {
    private Integer id;
    private Integer iddocente;
    private String docente;
    private List<ModelEstudent> estudiantes= null;

    public ModelGroup() {
    }

    public ModelGroup(Integer id, Integer iddocente, String docente, List<ModelEstudent> estudiantes) {
        this.id = id;
        this.iddocente = iddocente;
        this.docente = docente;
        this.estudiantes = estudiantes;
    }

    public List<ModelEstudent> getEstudiantes() {
        return estudiantes;
    }

    public String getEstudiantesNombre() {
        String Nombres="Alumnos"+"\n";
        for(int i=0; i<estudiantes.size();i++)
            Nombres+=estudiantes.get(i).getEstudiante()+"\n";
        return Nombres;
    }
    public String getEstudiantesT() {
        return String.valueOf(estudiantes.size());
    }
    public Boolean getEstudiantesActivo(Integer pos) {
        return estudiantes.get(pos).getActivo();
    }

    public void setEstudiantes(List<ModelEstudent> estudiantes) {
        this.estudiantes = estudiantes;
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


}
