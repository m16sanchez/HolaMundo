package com.stin.server;

public class Multivalor {
	private int idPozo;
	private String nombre;
	
	public Multivalor(){}
	public Multivalor(int idPozo, String nombre){
		this.idPozo=idPozo;
		this.nombre=nombre;
	}
	public int getIdPozo() {
		return idPozo;
	}
	public void setIdPozo(int idPozo) {
		this.idPozo = idPozo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
