package com.stin.dbaccess;

import com.stin.DAO.GenericDao;

public class PerforacionDAO {

	public static int searchPerfConseByInter(int idIntervencion,int tipoIntervencion){
        int id=0;        
        String hql="";
        
        if (tipoIntervencion==1){
            hql="SELECT MAX(p.intCon) FROM Perforacion p WHERE p.intervencion.idIntervencion="+ idIntervencion + "";            
        }else if(tipoIntervencion==2){
            hql="SELECT MAX(p.intCon) FROM Terminacion p WHERE p.intervencion.idIntervencion="+ idIntervencion + "";            
        }else{
            hql="SELECT MAX(p.intCon) FROM Rma p WHERE p.intervencion.idIntervencion="+ idIntervencion + "";
        }
        //System.out.println("-------->"+hql);
        try {                               
            id=(Integer) GenericDao.searchBySpecific(hql);
        } catch (Exception e) {
            System.out.println("ERROR PerforacionDAO.searchPerfConseByInter " + e.getMessage());
        }
        return id;
    }
}
