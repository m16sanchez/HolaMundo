package com.stin.implementor;



import javax.jws.WebService;

import com.stin.WsInterface;
import com.stin.DAO.GenericDao;
import com.stin.dbaccess.IntervencionDAO;
import com.stin.dbaccess.UsuariosDBAccess;
import com.stin.reader.LeerExcel;
import com.stin.reader.LeerNpts;

@WebService(endpointInterface="com.stin.WsInterface")
public class WsInterfaceImpl implements WsInterface {
	
	@Override
	public String ping() {
		// TODO Auto-generated method stub
		return "Conecction successful";
	}

	@Override
	public int getLogin(String usuario, String contrasena) {
		// TODO Auto-generated method stub
		return UsuariosDBAccess.getUsuarios(usuario, contrasena);
	}

	@Override
	public boolean newObjet(Object objeto) {
		// TODO Auto-generated method stub
		try {
			return GenericDao.newGeneric(objeto);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public boolean modifyObject(Object objeto) {
		// TODO Auto-generated method stub
		try {
			return GenericDao.setUpdate(objeto);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public boolean readNpts(String archivoDestino, int idPozo,
			int intervencion, int tipoIntervencion, String interBuscar,
			boolean visualiza, int ultimoConse) {
		// TODO Auto-generated method stub
		LeerNpts.getListNpts(archivoDestino, idPozo, interBuscar, intervencion, visualiza, tipoIntervencion);
		return false;
	}

	@Override
	public boolean readOldSiop(int pozoId, int intervencionId, int catSubInterId,String ruta) {
		// TODO Auto-generated method stub
		LeerExcel leerSiop=new LeerExcel(ruta);
		leerSiop.excutedSIOPReader(IntervencionDAO.searchIntervencionByTipoAndPozo(pozoId, intervencionId, catSubInterId));
		return false;
	}




}
