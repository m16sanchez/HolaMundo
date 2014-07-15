package com.stin.model;

public class Informacion {

	private String fechaInicio;
    private String fechaFinal;
    private String actividad;

    /*public void Datos(){ 
    this.fecha="";
    this.actividad="";
    }*/
    public Informacion(String date, String fin, String act) {
        this.fechaInicio = initvariable(date);
        this.fechaFinal = initvariable(fin);
        this.actividad = initvariable(act);
    }

    public void setFechaInicio(String date) {
        this.fechaInicio = initvariable(date);
    }

    public String getFechaInicio() {
        return this.fechaInicio;
    }

    public void setActividad(String act) {
        this.actividad = initvariable(act);
    }

    public String getActividad() {
        return this.actividad;
    }

    public void setFechaFinal(String date) {
        this.fechaFinal = initvariable(date);
    }

    public String getFechaFinal() {
        return this.fechaFinal;
    }

    private String initvariable(String value) {
        if (value == null) {
            return "";
        }
        if (value.equals("")) {
            return "";
        }
        if (!(value instanceof String)) {
            return "";
        }
        return value;
    }
}
