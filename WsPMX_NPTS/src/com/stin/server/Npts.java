package com.stin.server;

public class Npts {

	private double tiempo;
	private String descripcion;
	public Npts(){}
	
	public Npts(double tiempo,String descripcion){
		this.tiempo=tiempo;
		this.descripcion=descripcion;
	}
	public double getTiempo() {
		return tiempo;
	}
	public void setTiempo(double tiempo) {
		this.tiempo = tiempo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
