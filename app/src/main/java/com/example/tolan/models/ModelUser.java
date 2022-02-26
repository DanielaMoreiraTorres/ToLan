package com.example.tolan.models;

import org.json.JSONArray;
import org.json.JSONObject;

public class ModelUser {

    int id;
    String usuario;
    String clave;
    String tipousuario;
    JSONObject estudiante;
    JSONObject docente;
    String nombres;
    String apellidos;
    String fechanacimiento;
    String correo;
    String telefono;
    public static int stockcaritas=0;
    JSONArray grupo;
    boolean activo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getTipousuario() {
        return tipousuario;
    }

    public void setTipousuario(String tipousuario) {
        this.tipousuario = tipousuario;
    }

    public JSONObject getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(JSONObject estudiante) {
        this.estudiante = estudiante;
    }

    public JSONObject getDocente() {
        return docente;
    }

    public void setDocente(JSONObject docente) {
        this.docente = docente;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(String fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /*public int getStockcaritas() {
        return stockcaritas;
    }

    public void setStockcaritas(int stockcaritas) {
        this.stockcaritas = stockcaritas;
    }*/

    public JSONArray getGrupo() {
        return grupo;
    }

    public void setGrupo(JSONArray grupo) {
        this.grupo = grupo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
