package com.stin.dbaccess;

import com.stin.DAO.GenericDao;

public class IntervencionDAO {
	  public static int searchIntervencionByTipoAndPozo(int idPozo,int tipo , int idSubInter){
	        int intervencion=0;
	        String hql="SELECT MAX(I.idIntervencion) FROM Intervencion I  WHERE I.pozo.idPozo="+idPozo+" AND I.catIntervencion.idTipo="+tipo +
	                " AND I.catSubIntervencion.idCatSubIntervencion=" +idSubInter;
	        try {
	            intervencion=(Integer) GenericDao.searchBySpecific(hql);
	        } catch (Exception e) {
	            System.out.println("ERROR IntervencionDAO.searchIntervencionByTipoAndPozo: " + e.getMessage());
	        }
	        return intervencion;    
	    }

}
