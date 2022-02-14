package com.example.tolan.models;

import java.util.List;

public class ModelGroupByIdDocente {
    private Integer iddocente;
    private String docente;
    private List<ModelEstudent> estudiantes = null;

    public ModelGroupByIdDocente() {
    }
    public ModelGroupByIdDocente(Integer iddocente, String docente, List<ModelEstudent> estudiantes) {
        this.iddocente = iddocente;
        this.docente = docente;
        this.estudiantes = estudiantes;
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

    public List<ModelEstudent> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<ModelEstudent> estudiantes) {
        this.estudiantes = estudiantes;
    }







}
