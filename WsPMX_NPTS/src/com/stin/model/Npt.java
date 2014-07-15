package com.stin.model;

public class Npt {
	private int pozo;
    private int intervencion;
    private int consecutivo;
    private String fecha;
    private int profundidad;
    private float tiempo;
    private int idNptIntervencion;
    private String resumen;
    private String actividad;
    private String n4;
    private String strDetalle;
    private int etapa;
    private int tr;
    public Npt() {
    }

    public Npt(int pozo, int intervencion, int consecutivo, String fecha, int profundidad, float tiempo, int idNptIntervencion,
            String resumen,String strDetalle, int etapa, int tr) {
        this.pozo = pozo;
        this.intervencion = intervencion;
        this.consecutivo = consecutivo;
        this.fecha = fecha;
        this.profundidad = profundidad;
        this.tiempo = tiempo;
        this.idNptIntervencion = idNptIntervencion;
        this.resumen = resumen;        
        this.strDetalle=strDetalle;
        this.etapa=etapa;
        this.tr=tr;
    }

     public Npt(int pozo, int intervencion, int consecutivo, String fecha, int profundidad, float tiempo, String resumen,String actividad,String n4) {
        this.pozo = pozo;
        this.intervencion = intervencion;
        this.consecutivo = consecutivo;
        this.fecha = fecha;
        this.profundidad = profundidad;
        this.tiempo = tiempo;        
        this.resumen = resumen;        
        this.actividad=actividad;                
        this.n4=n4;
    }

    
    
    /**
     * @return the pozo
     */
    public int getPozo() {
        return pozo;
    }

    /**
     * @param pozo the pozo to set
     */
    public void setPozo(int pozo) {
        this.pozo = pozo;
    }

    /**
     * @return the tiempo
     */
    public float getTiempo() {
        return tiempo;
    }

    /**
     * @param tiempo the tiempo to set
     */
    public void setTiempo(float tiempo) {
        this.tiempo = tiempo;
    }

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

     /**
     * @return the idNpt
     */
   
    public int getIdNptIntervencion() {
        return idNptIntervencion;
    }

    public void setIdNptIntervencion(int idNptIntervencion) {
        this.idNptIntervencion = idNptIntervencion;
    }

   
    public int getConsecutivo() {
        return consecutivo;
    }

    public int getProfundidad() {
        return profundidad;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }

    public void setProfundidad(int profundidad) {
        this.profundidad = profundidad;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    /**
     * @return the intervencion
     */
    public int getIntervencion() {
        return intervencion;
    }

    /**
     * @param intervencion the intervencion to set
     */
    public void setIntervencion(int intervencion) {
        this.intervencion = intervencion;
    }

    public String getActividad() {
        return actividad;
    }

    public String getN4() {
        return n4;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public void setN4(String n4) {
        this.n4 = n4;
    }

    public String getStrDetalle() {
        return strDetalle;
    }

    public void setStrDetalle(String strDetalle) {
        this.strDetalle = strDetalle;
    }

    public int getEtapa() {
        return etapa;
    }

    public int getTr() {
        return tr;
    }

    public void setEtapa(int etapa) {
        this.etapa = etapa;
    }

    public void setTr(int tr) {
        this.tr = tr;
    }
    
    


}
