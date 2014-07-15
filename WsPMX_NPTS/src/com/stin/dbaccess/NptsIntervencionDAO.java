package com.stin.dbaccess;

import java.util.ArrayList;
import java.util.List;

import com.stin.NptIntervencion;
import com.stin.DAO.GenericDao;

public class NptsIntervencionDAO {
	 
    public static List<NptIntervencion> getContentIntervencion(int idActividad, int idTipo){
    
        List<NptIntervencion>listaIntervencion=new ArrayList<NptIntervencion>();
        String hql="FROM NptIntervencion NPI INNER JOIN FETCH NPI.catNptsN4 N4 INNER JOIN FETCH NPI.catActividades ACT INNER JOIN FETCH NPI.catIntervencion I  WHERE ACT.idActividad="+idActividad +" AND I.idTipo="+idTipo+" ORDER BY N4.strNombre";        
        try {
           listaIntervencion=GenericDao.GetAllGeneric(hql); 
        } catch (Exception e) {
           System.out.println("ERROR  NptsActividadesDAO.getContentIntervencion: "+ e.getMessage() );
        }
        return listaIntervencion;
        
    }
    
    public static List<NptIntervencion>getAllIntervencion(int idActividad, int idTipo){
    
        List<NptIntervencion>listaIntervencion=new ArrayList<NptIntervencion>();
        String hql="SELECT N FROM NptIntervencion N WHERE N.catActividades.idActividad!="+ idActividad + " AND N.catIntervencion.idTipo!="+ idTipo+ " ORDER BY N.catNptsN4.strNombre";        
        try {
           listaIntervencion=GenericDao.GetAllGeneric(hql); 
        } catch (Exception e) {
           System.out.println("ERROR  NptsActividadesDAO.getContentIntervencion: "+ e.getMessage() );
        }
        return listaIntervencion;
        
    }
   
    public static int getIdNptIntervencion(int idNpt4,int idActividad,int idTipo){
        int id=0;
        String hql="";
        try {
            hql="SELECT Np.idNptIntervencion FROM NptIntervencion Np WHERE Np.catNptsN4.idNptn4="+idNpt4+" AND Np.catActividades.idActividad="+idActividad+" AND Np.catIntervencion.idTipo="+ idTipo;
            id=(Integer)GenericDao.searchBySpecific(hql);
        } catch (Exception e) {            
            System.out.println("ERROR NptsActividadesDAO.getIdNptIntervencion: "+ e.getMessage());
        }
        return id;
    }
    public static int getIdNptIntervencionNA(int idTipo){
        int id=0;
         NptIntervencion nptIntervencion=new NptIntervencion();
        try {
            String hql="";
             if(idTipo==1){
                 hql="SELECT I FROM NptIntervencion I INNER JOIN FETCH I.catNptsN4 N4 INNER JOIN FETCH I.catActividades ACT WHERE N4.strNombre='NORMAL' AND  ACT.idActividad=18 AND I.catIntervencion.idTipo="+idTipo+"";
             }else if(idTipo==2){
                 hql="SELECT I FROM NptIntervencion I INNER JOIN FETCH I.catNptsN4 N4 INNER JOIN FETCH I.catActividades ACT WHERE N4.strNombre='NORMAL' AND  ACT.strNombre='NORMAL' AND I.catIntervencion.idTipo="+ idTipo+"";
             }else{
                 hql="SELECT I FROM NptIntervencion I INNER JOIN FETCH I.catNptsN4 N4 INNER JOIN FETCH I.catActividades ACT WHERE N4.strNombre='NORMAL' AND  ACT.strNombre='NORMAL' AND I.catIntervencion.idTipo="+ idTipo+"";
             }             
            nptIntervencion=(NptIntervencion)GenericDao.searchBySpecific(hql);
        } catch (Exception e) {
            System.out.println("ERROR NptsActividadesDAO.getIdNptIntervencionNA: " + e.getMessage());
        }
        return nptIntervencion.getIdNptIntervencion();
        
    }    
    public static List<NptIntervencion>getListCatNpts(int idTipo,String buscar){
        List<NptIntervencion>lista=new ArrayList<NptIntervencion>();                
        String hql="SELECT N FROM NptIntervencion N INNER JOIN FETCH N.catActividades C INNER JOIN FETCH N.catNptsN4 N4 WHERE N.catIntervencion.idTipo="+ idTipo;
        
        if(!(buscar.equals(""))){
            hql="SELECT N FROM NptIntervencion N INNER JOIN FETCH N.catActividades C INNER JOIN FETCH N.catNptsN4 N4 WHERE N.catIntervencion.idTipo="+ idTipo +" AND N4.strNombre LIKE '%" + buscar + "%'";
        }
        
        try {
            
            lista=GenericDao.GetAllGeneric(hql);
        } catch (Exception e) {
            System.out.println("ERROR NptsActividadesDAO.getListCatNpts" +e.getMessage());
        }
        return lista;
    }
    
}
