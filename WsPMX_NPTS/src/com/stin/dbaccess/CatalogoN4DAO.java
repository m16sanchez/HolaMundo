package com.stin.dbaccess;

import java.util.ArrayList;
import java.util.List;

import com.stin.CatNptsN4;
import com.stin.DAO.GenericDao;

public class CatalogoN4DAO {

	public static List<CatNptsN4>getListNptnIntervencion(int idActividad,int idTipo){
        List<CatNptsN4>lista=new ArrayList<CatNptsN4>();
        String hql="SELECT N4 FROM CatNptsN4 N4 WHERE N4.idNptn4 NOT IN(SELECT NP.catNptsN4.idNptn4 FROM NptIntervencion NP WHERE NP.catActividades.idActividad="+idActividad+" AND NP.catIntervencion.idTipo="+idTipo+") ORDER BY N4.strNombre";        
        try {
            lista=GenericDao.GetAllGeneric(hql);
        } catch (Exception e) {
            System.out.println("ERROR CatalogoN4DAO.getListNptnIntervencion: " + e.getMessage());
        }
        return lista;
    }
    public static List<CatNptsN4>getListCatN4(boolean todo, String buscar){
        List<CatNptsN4>lista=new ArrayList<CatNptsN4>();
        String hql="SELECT N4 FROM CatNptsN4 N4 INNER JOIN FETCH N4.catNptsN3 N3 INNER JOIN FETCH N3.catNptsN2 N2 INNER JOIN FETCH N2.catNptsN1 ORDER BY N4.strNombre";
        if(!todo){
            hql="SELECT N4 FROM CatNptsN4 N4 INNER JOIN FETCH N4.catNptsN3 N3 INNER JOIN FETCH N3.catNptsN2 N2 INNER JOIN FETCH N2.catNptsN1 WHERE N4.strNombre LIKE '%" + buscar + "%'";
        }
        try {
            lista=GenericDao.GetAllGeneric(hql);
        } catch (Exception e) {
            System.out.println("ERROR CatalogoN4DAO.getListCatn4: " + e.getMessage());
        }
        return lista;
    }
    
    public static CatNptsN4 getCatN4(int id){
        CatNptsN4 catNptsN4=new CatNptsN4();
        String hql="SELECT N4 FROM CatNptsN4 N4 WHERE N4.idNptn4="+ id + "";
        try {
            catNptsN4=(CatNptsN4)GenericDao.searchBySpecific(hql);            
        } catch (Exception e) {
            System.out.println("ERROR CatalogoN4DAO.getCatN1(): " + e.getMessage());
        }
            
        return catNptsN4;
    }

    public static int getIdNpts (String busca) {
        int id=0;
        if(busca.equals(""))
            return 0;
            
        String hql="SELECT N4.idNptn4 FROM CatNptsN4 N4 WHERE N4.strNombre ='"+ busca.trim().toUpperCase() + "'" ;        
        try {
            id=(Integer)GenericDao.searchBySpecific(hql);
        } catch (Exception e) {
            System.out.println("ERROR CatalogoN4DAO.getIdNpts: " + e.getMessage());
        }
                
        return id;
    }
}
