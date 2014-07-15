package com.stin;


import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;



@WebService
public interface WsInterface {	
	@WebResult(name="String")
	public String ping();

	@WebResult(name="int")
	public int getLogin(@WebParam(name="usuario") String usuario,@WebParam(name="contrasena")String contrasena);
	
	@WebResult(name="boolean")
	public boolean newObjet(@WebParam(name="objeto")Object objeto);
	
	@WebResult(name="boolean")
	public boolean modifyObject(@WebParam(name="objeto") Object objeto);
	
	@WebResult(name="boolean")
	public boolean readNpts(@WebParam(name="archivoDestino")String archivoDestino,
			@WebParam(name="idPozo")int idPozo, 
			@WebParam(name="intervencion")int intervencion,
			@WebParam(name="tipoIntervencion")int tipoIntervencion,
			@WebParam(name="interBuscar")String interBuscar,
			@WebParam(name="visualia")boolean visualiza,
			@WebParam(name="ultimoConse")int ultimoConse);
	
	@WebResult(name="boolean")
	public boolean readOldSiop(@WebParam(name="pozoId")int pozoId,@WebParam(name="intervencionId")int intervencionId,@WebParam(name="catSubInterId") int catSubInterId,@WebParam(name="ruta")String ruta);
	
}
