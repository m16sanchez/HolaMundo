package com.stin.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class InformacionPozos {

	//System.out.println("Numero de renglon: " + infoAtCol.get(0)  );
    //System.out.println("Numero de columnas: " + infoAtCol.get(1)  );
    //System.out.println("Numero de renglones del archivo: " + infoAtCol.get(3)  );
    //System.out.println("Numero de pagina: " + infoAtCol.get(4)  );
    //System.out.println("Numero de archivo: " + infoAtCol.get(5)  );

    private ArrayList<Informacion> allInfo;
    private String fecha;
    private String pozo;
    private int numRow;
    private int numCols;
    private String nomPag;
    private String nomFile;
    private String cia;
    private String cia_Complement;
    private String cia_Fecha;
    private String reportediario_Fecha;
    private String reportediario_proyecto;
    private String reportediario_complemento;
    private String equipo;
    private String equipo_conductor;
    private String equipo_inicio;
    private String diasProg;
    private String suptte;
    
    /*     
     * Segundo Bloque
     */

    private ArrayList<HashMap<String, String>> HerramientasPorHoja = new ArrayList();
    private ArrayList<HashMap<String, String>> BarrenasPorHoja = new ArrayList();
    private ArrayList<HashMap<String, String>> ProfundidadPorHoja = new ArrayList();
    private ArrayList<HashMap<String, String>> NucleosPorHoja = new ArrayList();
    private ArrayList<HashMap<String, String>> LodoPorHoja = new ArrayList();

    private String resumenDiaAnterior;
    private String resumenfin;    
	
    
    
    
    public InformacionPozos() {
        this.allInfo = new ArrayList<Informacion>();
        this.fecha = "";
        this.pozo = "";
    }

    public InformacionPozos(String pozo, String fecha) {
        this.allInfo = new ArrayList<Informacion>();
        this.pozo = initvariable(pozo);
        this.fecha = initvariable(fecha);
    }

    public void setPozo(String name) {
        this.pozo = initvariable(name);
    }

    public String getPozo() {
        return this.pozo;
    }

    public void setFecha(String fecha) {
        this.fecha = initvariable(fecha);
    }

    public String getFecha() {
        return this.fecha;
    }

    public void addElementToPozo(ArrayList<Informacion> values) {
        Informacion info = null;

        Informacion obj = null;
        Iterator<Informacion> it = values.iterator();

        while (it.hasNext()) {
            obj = it.next();

            this.allInfo.add(obj);
        }

    }

    public void setAllInfo(ArrayList<Informacion> values) {
        this.allInfo = values;
    }

    public ArrayList<Informacion> getAllInfo() {
        return this.allInfo;
    }

    public void setNumRow(int value) {
        this.numRow = value;
    }

    public int getNumRow() {
        return this.numRow;
    }

    public void setNumCols(int value) {
        this.numCols = value;
    }

    public int getNumCols() {
        return this.numCols;
    }

    public void setNomPag(String value) {
        this.nomPag = initvariable(value);
    }

    public String getNomPag() {
        return this.nomPag;
    }

    public void setNomFile(String value) {
        this.nomFile = initvariable(value);
    }

    public String getNomFile() {
        return this.nomFile;
    }

    public String getCia() {
        return cia;
    }

    public void setCia(String cia) {
        this.cia = initvariable(cia);
    }

    public String getCia_Complement() {
        return cia_Complement;
    }

    public void setCia_Complement(String cia_Complement) {
        this.cia_Complement = initvariable(cia_Complement);
    }

    public String getCia_Fecha() {
        return cia_Fecha;
    }

    public void setCia_Fecha(String cia_Fecha) {
        this.cia_Fecha = initvariable(cia_Fecha);
    }

    public String getReportediario_Fecha() {
        return reportediario_Fecha;
    }

    public void setReportediario_Fecha(String reportediario_Fecha) {
        this.reportediario_Fecha = initvariable(reportediario_Fecha);
    }

    public String getReportediario_proyecto() {
        return reportediario_proyecto;
    }

    public void setReportediario_proyecto(String reportediario_proyecto) {
        this.reportediario_proyecto = initvariable(reportediario_proyecto);
    }

    public String getReportediario_complemento() {
        return reportediario_complemento;
    }

    public void setReportediario_complemento(String reportediario_complemento) {
        this.reportediario_complemento = initvariable(reportediario_complemento);
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = initvariable(equipo);
    }

    public String getEquipo_conductor() {
        return equipo_conductor;
    }

    public void setEquipo_conductor(String equipo_conductor) {
        this.equipo_conductor = initvariable(equipo_conductor);
    }

    public String getEquipo_inicio() {
        return equipo_inicio;
    }

    public void setEquipo_inicio(String equipo_inicio) {
        this.equipo_inicio = initvariable(equipo_inicio);
    }

    private String initvariable(String value) {
        if (value == null) {
            return "";
        }
        if (value.equals("")) {
            return "";
        }
        if (!(value instanceof String)) {
            return "";
        }
        return value;
    }

    public ArrayList<HashMap<String, String>> getHerramientasPorHoja() {
        return HerramientasPorHoja;
    }

    public void setHerramientasPorHoja(
            ArrayList<HashMap<String, String>> herramientasPorHoja) {
        HerramientasPorHoja = herramientasPorHoja;
    }
    
    
    public void addHojaHerramientas(  HashMap<String, String> hojaHerramientas  ){        
        HerramientasPorHoja.add(hojaHerramientas);        
    }
    
    

    public ArrayList<HashMap<String, String>> getBarrenasPorHoja() {
        return BarrenasPorHoja;
    }

    public void setBarrenasPorHoja(
            ArrayList<HashMap<String, String>> barrenasPorHoja) {
        BarrenasPorHoja = barrenasPorHoja;
    }
    
    
    public void addHojaBarrenas(  HashMap<String, String> hojaBarrenas ){    
        BarrenasPorHoja.add(hojaBarrenas);        
    }

    public ArrayList<HashMap<String, String>> getProfundidadPorHoja() {
        return ProfundidadPorHoja;
    }

    public void setProfundidadPorHoja(
            ArrayList<HashMap<String, String>> profundidadPorHoja) {
        ProfundidadPorHoja = profundidadPorHoja;
    }
    
     public void addHojaProfundidad(  HashMap<String, String> hojaProfundidad ){    
         ProfundidadPorHoja.add(hojaProfundidad);        
    }

    
    

    public ArrayList<HashMap<String, String>> getNucleosPorHoja() {
        return NucleosPorHoja;
    }

    public void setNucleosPorHoja(ArrayList<HashMap<String, String>> nucleosPorHoja) {
        NucleosPorHoja = nucleosPorHoja;
    }
    
    public void addHojaNucleos(  HashMap<String, String> hojaNucleos ){    
         NucleosPorHoja.add(hojaNucleos);
    }
    
    
    public ArrayList<HashMap<String, String>> getLodoPorHoja() {
		return LodoPorHoja;
	}

	public void setLodoPorHoja(ArrayList<HashMap<String, String>> lodoPorHoja) {
		LodoPorHoja = lodoPorHoja;
	}
    
     public void addHojaLodo(  HashMap<String, String> hojaLodo ){    
         LodoPorHoja.add(hojaLodo);
    }

    public String getDiasProg() {
        return diasProg;
    }

    public void setDiasProg(String diasProg) {
        this.diasProg = diasProg;
    }

    public String getSuptte() {
        return suptte;
    }

    public void setSuptte(String suptte) {
        this.suptte = suptte;
    }
     
     
     
    public String getResumenDiaAnterior() {
			return resumenDiaAnterior;
		}
		public void setResumenDiaAnterior(String resumenDiaAnterior) {
			this.resumenDiaAnterior = resumenDiaAnterior;
		}
		public String getResumenfin() {
			return resumenfin;
		}
		public void setResumenfin(String resumenfin) {
			this.resumenfin = resumenfin;
		}    

}
