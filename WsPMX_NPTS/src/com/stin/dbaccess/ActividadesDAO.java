package com.stin.dbaccess;

import java.util.ArrayList;
import java.util.List;

import com.stin.CatActividades;
import com.stin.DAO.GenericDao;

public class ActividadesDAO {
	public static List<CatActividades>getListActividades(boolean todo,String buscar,int idTipo){
        List<CatActividades> lista=new ArrayList<CatActividades>();
        
        String hql="SELECT AC FROM CatActividades AC WHERE AC.catIntervencion.idTipo="+idTipo+" AND AC.strNombre !='NA' ORDER BY AC.strNombre ";
        //String hql="SELECT AC FROM CatActividades AC WHERE AC.strNombre !='NORMAL' ORDER BY AC.strNombre ";
        if(!todo){
            hql="SELECT AC FROM CatActividades AC WHERE AC.strNombre LIKE '%" + buscar + "%'";
        }
        try {
            lista=GenericDao.GetAllGeneric(hql);
        } catch (Exception e) {
            System.out.println("ERROR ActividadesDAO.getListActividades: " + e.getMessage());
        }                
        return lista;
    }
    
    public static List<CatActividades>getListActividadesInter(boolean todo, String buscar){
        List<CatActividades>lista=new ArrayList<CatActividades>();
        String hql="SELECT CatAct FROM CatActividades CatAct INNER JOIN FETCH CatAct.catIntervencion order by CatAct.strNombre";
        if(!todo){
            hql="SELECT CatAct FROM CatActividades CatAct INNER JOIN FETCH CatAct.catIntervencion WHERE CatAct.strNombre LIKE '%"+buscar+"%'";
        }
        try {
            lista=GenericDao.GetAllGeneric(hql);
        } catch (Exception e) {
            System.out.println("ERROR ActividadesDAO.getListActividadesInter: "+ e.getMessage());
        }
        return lista;
    }
    
    public static int getIdActividades(String actividad,int tipoIntervencion){        
        if(actividad.equals("") && (!(tipoIntervencion==1)))
            return 0;
                
        int id=0;
        
        try {
            String hql="";
            if(!(tipoIntervencion==1)){//Actividad diferente 
                hql="SELECT ACT.idActividad FROM CatActividades ACT WHERE ACT.strNombre ='" + actividad.trim().toUpperCase() + "'";// AND ACT.catIntervencion.idTipo="+ tipoIntervencion ;
            }else{
                hql="SELECT ACT.idActividad FROM CatActividades ACT WHERE ACT.strNombre ='Perforacion'";
            }                       
            //System.out.println("Actividades HQL->" + hql);
            id=(Integer)GenericDao.searchBySpecific(hql);
        } catch (Exception e) {
            System.out.println("ERROR ActividadesDAO.getIdActividades: " + e.getMessage());
        }
        return id;
    }
    
}
