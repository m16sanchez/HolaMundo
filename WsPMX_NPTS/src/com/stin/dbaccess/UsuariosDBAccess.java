package com.stin.dbaccess;

import com.stin.DAO.GenericDao;



public class UsuariosDBAccess {
	
	public static int getUsuarios(String usuario,String contrasena){
		String hql="SELECT U.intPermiso FROM Usuarios U WHERE U.strUser='" + usuario + "' AND strPassword='" + contrasena + "'";
		int permiso=0;
		try {
			permiso=(Integer) GenericDao.searchBySpecific(hql);
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		return permiso;
	}
}
