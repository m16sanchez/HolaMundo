package com.stin.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class SiopUtils {

	 static public String dateFechaOperacionNPTS(String fecha) { //UNO PARA SIOP Y OTRA PARA NPT
//       SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");         
       SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy"); 
       String strDate = fecha;
       String maquillado = "";
       Date varDate = null;

       try {            
           varDate = dateFormat.parse(strDate);            
           dateFormat = new SimpleDateFormat("yyyy-MM-dd");
           //System.out.println("Date FECHA OP:" + dateFormat.format(varDate));
           maquillado = dateFormat.format(varDate);            
           return maquillado;
       } catch (Exception e) {            
           // TODO: handle exception            
           e.printStackTrace();
           return maquillado;
       }
   }
	 static public String validateInfo(String[] splitArray) {
	        String res = null;
	        try {
	            if (splitArray.length > 1) {
	                res = splitArray[1].toString().trim();
	            } else {
	                res = "No existe informacion";
	            }

	        } catch (Exception e) {
	            System.out.println("Error validateData: " + e.getMessage());
	        }
	        return res;
	    }
	 //Return SIOP Cell array

	    static public String[] splitFcn(String string2Split, String pipe, boolean print) {
	        String res[];
	        res = string2Split.split(pipe);
	        if (print == true) {
	            printArray(res);
	        }
	        return res;
	    }
	    
	    //Print array in console

	    static public void printArray(String[] array) {
	        int count = 0;
	        if (array.length > 0) {
	            for (String item : array) {
	                System.out.println("ITEM " + count + ": " + item.toString());
	                count++;
	            }
	        }
	    }
	    
	  //Remove empty array spaces

	    static public ArrayList RemoveEmptyElements(String[] toCleanArray, boolean print) {

	        ArrayList cleanList = new ArrayList();

	        try {
	            for (int i = 0; i < toCleanArray.length; i++) {
	                if (!toCleanArray[i].isEmpty()) {
	                    cleanList.add(toCleanArray[i].toString());
	                }
	            }

	            if (print == true) {
	                printList(cleanList);
	            }

	        } catch (Exception e) {
	            System.out.println("RemoveEmptyElements Error: " + e.getMessage());
	        }

	        return cleanList;
	    }

	    static public void printList(ArrayList list) {

	        try {

	            Iterator it = list.iterator();
	            while (it.hasNext()) {
	                System.out.println("Elemento: " + it.next());
	            }

	        } catch (Exception e) {
	            System.out.println("Error printList" + e.getMessage());
	        }
	    }
	    
	    static public ArrayList findListElement(ArrayList range) {

	        ArrayList elements = new ArrayList();

	        String Conductor;
	        String EspRotNM;
	        String Actividad;
	        String nd = "Sin Información.";

	        int e1 = range.indexOf("Conductor:");
	        int e2 = range.indexOf("Esp.");
	        int e3 = range.indexOf("Rot-NM:");
	        int e4 = range.indexOf("Actividad:");

	        if (e2 - e1 - 1 > 0) {
	            Conductor = range.get(e2 - e1 - 1).toString();
	            elements.add(Conductor);
	            //System.out.println("HAY CONDUCTOR: " + Conductor);
	        } else {
	            elements.add(nd);
	            //System.out.println("NO HAY CONDUCTOR");
	        }
	        if (e4 - e3 > 1) {
	            EspRotNM = range.get(e3 + 1).toString();
	            elements.add(EspRotNM);
	            //System.out.println("HAY Rot-NM: " + EspRotNM);
	        } else {
	            elements.add(nd);
	            //System.out.println("NO HAY Rot-NM:");
	        }
	        if (e4 + 1 > e4) {
	            Actividad = range.get(e4 + 1).toString();
	            elements.add(Actividad);
	            //System.out.println("HAY Actividad: " + Actividad);
	        } else {
	            elements.add(nd);
	            //System.out.println("NO HAY Actividad");
	        }
	        return elements;
	    }
	    static public Date dateFechaSiop(String fecha) { // String retorna el maquillado
	        
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	        String strDate = fecha;
	        String maquillado = "";
	        Date varDate = null;

	        try {
	            varDate = dateFormat.parse(strDate);
	            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	            //System.out.println("Date SIOP:" + dateFormat.format(varDate));
	            maquillado = dateFormat.format(varDate);
	            //System.out.println("maquillado: " + maquillado);
	            return varDate;

	        } catch (Exception e) {
	            // TODO: handle exception
	            e.printStackTrace();
	            return varDate;
	        }
	    }

	    static public Date dateFechaOperacion(String fecha) { //UNO PARA SIOP Y OTRA PARA NPT
	        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy"); 
	        String strDate = fecha;
	        String maquillado = "";
	        Date varDate = null;

	        try {            
	            varDate = dateFormat.parse(strDate);            
	            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	            //System.out.println("Date FECHA OP:" + dateFormat.format(varDate));
	            maquillado = dateFormat.format(varDate);            
	            return varDate;
	        } catch (Exception e) {            
	            // TODO: handle exception            
	            e.printStackTrace();
	            return varDate;
	        }
	    }
	    static public boolean verificaTR(String temTRd, String tempTRv, String trd, String trv) {
	        if ((trd.equals(temTRd)) && (trv.equals(tempTRv))) {
	            return true;
	        }
	        return false;
	    }
	    static public String verificaProfundidadDesarrollo(String data) {
	        if ((data.equals("0.0")) || (data.equals("")) || (data == null)) {
	            return "0";
	        } else {
	            double value = Double.valueOf(data);
	            String res = String.valueOf(Math.round(value));

	            return String.valueOf(res);
	        }
	    }

	    static public String verificaVacio(String data) {
	        if ((data.equals("0.0")) || (data.equals("")) || (data == null)) {
	            return "0";
	        } else {
	            return String.valueOf(data.trim().toUpperCase());
	        }
	    }

	    static public String verificaHorasOP(String data) {
	        if (data.length() > 7) {
	            return "00:00";
	        } else {
	            return String.valueOf(data.trim().toUpperCase());
	        }
	    }
	    static public String eliminaApostrofes(String data) {
	        if ((data.equals("0.0")) || (data.equals("")) || (data == null)) {
	            return "0";
	        } else {
	            String info = data.replace("'", "");
	            return String.valueOf(info.trim().toUpperCase());
	        }
	    }
	    static public boolean verificaHerramienta(String herramienta, String cantidad, String longitud, String obs) {
	        boolean res = false;
	        try {  
	            if(herramienta==null){
	                return false;
	            }
	            if (herramienta.equals("") || herramienta.contains("null") && cantidad.equals("0.0") && longitud.equals("0.0") && obs.equals("0.0")) {
	                res = false;
	            } else {
	                res = true;
	            }
	            if (herramienta.isEmpty()) {
	                res = false;
	            } else {
	                res = true;
	            }
	        } catch (Exception e) {
	            System.out.println("ERROR HERRAMIENTA: " + e.getMessage());
	        }
	        return res;
	    }
	    static public boolean verificaNucleo(String nucleo, String prog, String disp) {
	        boolean res = true;
	        try {
	            if (nucleo.equals("0.0") && prog.equals("0.0") && disp.equals("0.0")) {
	                res = false;
	            }
	            if (nucleo.isEmpty() || nucleo.contains("Nucleos")) {
	                res = false;
	            }
	        } catch (Exception e) {
	            System.out.println("ERROR Nucleo: " + e.getMessage());
	        }
	        return res;
	    }
	    static public boolean verificaLodo(String lodo) {
	        boolean res = true;
	        try {
	            if (lodo.contains("0.0") || lodo.isEmpty() || lodo.contains("NO HAY")) {
	                res = false;
	            }
	        } catch (Exception e) {
	            System.out.println("ERROR LODO: " + e.getMessage());
	        }
	        return res;
	    }
}
