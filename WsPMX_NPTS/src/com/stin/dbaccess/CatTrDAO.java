package com.stin.dbaccess;

import java.util.ArrayList;
import java.util.List;

import com.stin.CatTr;
import com.stin.DAO.GenericDao;

public class CatTrDAO {
	public static List<CatTr> getListCatTr(boolean todo, String buscar){
        List<CatTr> lista=new ArrayList<CatTr>();
        String hql="SELECT C FROM CatTr C ORDER BY C.idCatTr";
        if (!todo){
            hql="SELECT C FROM CatTr C WHERE C.strNombre LIKE '%" + buscar + "%'";
        }
        try {
            lista=GenericDao.GetAllGeneric(hql);
        } catch (Exception e) {
            System.out.println("ERROR CatTrDAO.getListCatTr: " + e.getMessage());
        }
        return lista;
    }
    public static boolean setUpdateCatTr(CatTr catTr){
        try {
            GenericDao.setUpdate(catTr);
            return true;
        } catch (Exception e) {
            System.out.println("ERROR CatTrDAO.setUpdateCatTr: " + e.getMessage());
        }
        return false;
    }
       public static int searchIdTr(String tr){
        int idInter=0;
        String hql="SELECT C.idCatTr FROM CatTr C WHERE C.strNombre='"+ tr +"'";
        try {
            idInter=(Integer) GenericDao.searchBySpecific(hql);
        } catch (Exception e) {
            System.out.println("ERROR IntervencionDAO.searchIntervencionByTipoAndPozo: " + e.getMessage());
        }
        return idInter;    
    }
       public static CatTr getCatTr(String strNombre){
        CatTr catTr=null;
        try {
            
            String hql="SELECT Cat FROM CatTr Cat WHERE Cat.strNombre='" + strNombre + "'";
            catTr=(CatTr)GenericDao.searchBySpecific(hql);
        } catch (Exception e) {
            System.out.println("ERROR CatEtapaDAO.getCatTr " + e.getMessage());
        }
        return catTr;
    }
}
