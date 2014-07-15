package com.stin;

import java.util.List;

import com.stin.DAO.GenericDao;
import com.stin.mapping.Participantes;

public class SayHello {

	public String ping(){
		return "connection successful";
		
	}
	public String Hi(String name){
		return "Hi " + name;
	}
	
	
	public boolean getList(){
		try {
			String hql="SELECT P FROM Participantes P";
			List<Participantes> lista=GenericDao.GetAllGeneric(hql);
			for(Participantes p: lista){
				System.out.println(p.getNombre());
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ERROR: " + e.getMessage());
		}
		return true;
	}
}
