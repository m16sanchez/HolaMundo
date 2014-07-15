package com.stin.reader;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


import com.stin.CatEtapa;
import com.stin.CatTr;
import com.stin.Intervencion;
import com.stin.Programas;
import com.stin.dbaccess.ActividadesDAO;
import com.stin.dbaccess.CatTrDAO;
import com.stin.dbaccess.CatEtapaDAO;
import com.stin.dbaccess.CatalogoN4DAO;
import com.stin.dbaccess.IntervencionDAO;
import com.stin.dbaccess.NptsIntervencionDAO;
import com.stin.dbaccess.PerforacionDAO;
import com.stin.model.Npt;
import com.stin.util.SiopUtils;


public class LeerNpts {
	
	public static List<Npt> leerExcelNPT(String archivoDestino, int idPozo, int intervencion, int tipoIntervencion, String interBuscar, boolean visualiza, int ultimoConse) {

        List npts = new ArrayList();
        int error = 1;

        int reportFila = 0, reportColumna = 0;
        HSSFWorkbook myWorkBook;
        try {

            FileInputStream myInput = new FileInputStream(archivoDestino);
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            myWorkBook = new HSSFWorkbook(myFileSystem);
            boolean excelSinNpt = false, excelConNpt = true, continua = true;

            List<Integer> catNpts = new ArrayList();
            List<Integer> catActividades = new ArrayList<Integer>();
            List<String> catNptsString = new ArrayList<String>();
            List<String> catActividadesString = new ArrayList<String>();
            List<String> catDetalle = new ArrayList<String>();


            int verificaConsecu = 0, recorreFila = 0, veces = 0;
            double sumaHoras = 0;
            int recorreColumna=0;
            //System.out.println("numero de hojas: " + myWorkBook.getNumberOfSheets());
            error = 2;
            for (int i = 0; i < myWorkBook.getNumberOfSheets(); i++) {
                org.apache.poi.ss.usermodel.Sheet sheet = myWorkBook.getSheetAt(i);
                //System.out.println("NOMBRE DE LA HOJA: " + sheet.getSheetName() + " ****************************** ");
                if (sheet.getSheetName().toString().equals("Real")) {
                    //System.out.println("FILA) " + sheet.getLastRowNum());
                    //System.out.println("COL: " + sheet.getRow(1).getLastCellNum());

                    int filaBuscar = 0;
                    //System.out.println("3");
                    error = 3;
                     System.out.println("BUSCAR->:"+ interBuscar);
                    for (int fila = 0; fila < sheet.getLastRowNum(); fila++) {
                        if (!(sheet.getRow(fila).getCell(1) == null)) {
                            System.out.println("DENTRO->:"+sheet.getRow(fila).getCell(1).toString());
                            if (sheet.getRow(fila).getCell(1).toString().equalsIgnoreCase(interBuscar)) {
                                System.out.println("ENCONTRADO POS: " + fila);
                                filaBuscar = fila;
                                continua = false;
                                break;
                            }
                        }
                    } // 3)NO ENCONTRO EL TEXTO DE PERFORACION, TERMINACION, RMA
                    if (continua) {
                        throw new Exception("");
                    }
                    continua = true;
                    error = 4;


                    int numColumnas = sheet.getRow(filaBuscar + 1).getLastCellNum();
                    int numFilas = sheet.getLastRowNum();
                    System.out.println("COLUMNA= " + numColumnas);
                    System.out.println("FILAS= " + numFilas);

                    if (visualiza) {
                        for (int columna = 7; columna < numColumnas; columna++) { // Recorre cada las columnas                                

                          
                            if (!(sheet.getRow(filaBuscar).getCell(columna) == null) || !(sheet.getRow(filaBuscar + 1).getCell(columna) == null)
                                    || !(sheet.getRow(filaBuscar + 2).getCell(columna) == null)) {
                                //System.out.println("*********** COLUMNA " + columna + " ***************");
                                /*System.out.println(sheet.getRow(filaBuscar).getCell(columna).toString() == null);
                                System.out.println("ACTIVIDAD: " + sheet.getRow(filaBuscar).getCell(columna).toString()
                                + "\nNPT" + sheet.getRow(filaBuscar + 1).getCell(columna).toString());
                                System.out.println("DETALLE : " + sheet.getRow(filaBuscar + 2).getCell(columna).toString());*/


                                if (!(tipoIntervencion == 1)) {
                                    System.out.println("ACTIVIDAD: " + sheet.getRow(filaBuscar).getCell(columna).toString());
                                    catActividades.add(ActividadesDAO.getIdActividades(sheet.getRow(filaBuscar).getCell(columna).toString(), tipoIntervencion));//TENIA (COLUMNA,1)
                                } else {
                                    catActividades.add(ActividadesDAO.getIdActividades("", tipoIntervencion));//TENIA (COLUMNA,1)
                                }
                                catNpts.add(CatalogoN4DAO.getIdNpts(sheet.getRow(filaBuscar + 1).getCell(columna).toString()));//TENIA (COLUMNA,0)                                
                                catDetalle.add(sheet.getRow(filaBuscar + 2).getCell(columna).toString());
                            } else {
                                //System.out.println("A SALIR ");
                                numColumnas = columna;
                                break;
                            }
                        }
                    } else {
                        for (int columna = 7; columna < numColumnas; columna++) {    //                            
//                            if(!(sheet.getRow(filaBuscar + 1).getCell(1).toString().equals(""))){
//                                continua=false;
//                            }//           
                            //System.out.println("*********** COLUMNA " + columna + " ***************");
                              
                            System.out.println("*********** COLUMNA " + columna + " ***************");
                            System.out.println(sheet.getRow(filaBuscar).getCell(columna));
                            System.out.println(sheet.getRow(filaBuscar+1).getCell(columna));
                            System.out.println(sheet.getRow(filaBuscar+2).getCell(columna));
                            
                            if (!(sheet.getRow(filaBuscar).getCell(columna) == null) || !(sheet.getRow(filaBuscar + 1).getCell(columna) == null)
                                    || !(sheet.getRow(filaBuscar + 2).getCell(columna) == null)) {

                                if(CatalogoN4DAO.getIdNpts(sheet.getRow(filaBuscar + 1).getCell(columna).toString())!=0){
                                    catNptsString.add(sheet.getRow(filaBuscar + 1).getCell(columna).toString());
                                }else{
                                    catNptsString.add("Error: no existe " + sheet.getRow(filaBuscar + 1).getCell(columna).toString() + " en nivel 4");
                                }
                                
                                
                                
                                
                                catDetalle.add(sheet.getRow(filaBuscar + 2).getCell(columna).toString());
                                if (tipoIntervencion == 1) {
                                    catActividadesString.add("Perforación");
                                } else {
                                    if(ActividadesDAO.getIdActividades(sheet.getRow(filaBuscar).getCell(columna).toString(), tipoIntervencion)!=0){
                                        catActividadesString.add(sheet.getRow(filaBuscar).getCell(columna).toString());
                                    }else{
                                        catActividadesString.add("Error: no existe " + sheet.getRow(filaBuscar).getCell(columna).toString() + " en las actividades");
                                    }
                                }
                            } else {
                                //System.out.println("A SALIR ");
                                numColumnas = columna;
                                break;
                            }
                        }
                    }


                    int aux = 0;
                    filaBuscar = filaBuscar + 3;
                    //System.out.println("FILABUSCAR:" + filaBuscar);
                    //System.out.println("NUEVA COLUMNA: " + numColumnas);

                    //System.out.println("5");                     
                    error = 5;

                    recorreColumna=filaBuscar-2;
                    for (int fila = filaBuscar; fila < numFilas; fila++) { // Recorre cada fila de la hoja                        
                        aux = 0;
                        excelSinNpt = false;
                        excelConNpt = true;

                        //System.out.println("ULTIMO: " + ultimoConse);
                        //System.out.println("EXCEL: " + sheet.getRow(filaBuscar).getCell(1).toString());     
                        //System.out.println("VERIFICA=== " + sheet.getRow(fila).getCell(1));
                        // MARCO CODIGO PARA TIEMPO NORMAL CUANDO TENGA NPT                        
                        if (veces > 0 && visualiza) {
                            if (sumaHoras < 24) {
                                npts.add(new Npt(idPozo, intervencion,
                                        Integer.parseInt(sheet.getRow(fila - 1).getCell(1).toString().replace(".0", "")), SiopUtils.dateFechaOperacionNPTS(sheet.getRow(fila - 1).getCell(2).toString()),
                                        Integer.parseInt(sheet.getRow(fila - 1).getCell(3).toString().replace(".0", "")), 24 - ((float) sumaHoras),
                                        NptsIntervencionDAO.getIdNptIntervencionNA(tipoIntervencion), sheet.getRow(fila - 1).getCell(6).toString().replace("'", ""), "",
                                        CatEtapaDAO.searchIdEtapa(sheet.getRow(fila-1).getCell(4).toString()),
                                        CatTrDAO.searchIdTr(sheet.getRow(fila-1).getCell(5).toString()))); //24 debe ser el id NA 
                            } else if (sumaHoras != 24) {
                                throw new Exception("");
                            }
                        }

                        if (!(sheet.getRow(fila).getCell(1) == null) && !(sheet.getRow(fila).getCell(2) == null) && !(sheet.getRow(fila).getCell(3) == null)) {
                            if (sheet.getRow(fila).getCell(1).toString().isEmpty() || sheet.getRow(fila).getCell(2).toString().isEmpty() || sheet.getRow(fila).getCell(3).toString().isEmpty()) {
                                break;
                            }
                        } else {
                            break;
                        }
                        verificaConsecu = Integer.parseInt(sheet.getRow(fila).getCell(1).toString().replace(".0", ""));
                        recorreFila = fila;

                        sumaHoras = 0;
                        veces = 0;
                        //System.out.println("Ultimo Con: " + ultimoConse + " verfiCa: " + verificaConsecu + " valor: " +(ultimoConse < verificaConsecu) );
                        filaBuscar=filaBuscar-2;
                        if (ultimoConse < verificaConsecu) {
                            for (int columna = 7; columna < numColumnas; columna++) { // Recorre cada    fila de la columna                                                                  
                                reportFila = fila;
                                reportColumna = columna;
                                //if (!(sheet.getRow(1).getCell(columna).toString().equals(""))) {
                                
                                if (!(sheet.getRow(recorreColumna).getCell(columna) == null) && !(sheet.getRow(recorreColumna).getCell(columna).toString().isEmpty())) {
                                    //if (!(hoja.getColumnView(columna).isHidden())) { //VERIFICA COLUMNA OCULTA                                                                                
                                    if (!(sheet.getRow(fila).getCell(3) == null)) { // VERFICA QUE NO ESTE VACIO EL CONSECUTIVO
                                        //if (!(sheet.getRow(fila).getCell(3).toString().equals(""))) { // VERFICA QUE NO ESTE VACIO EL CONSECUTIVO
                                        //System.out.println("fila " + fila + " col= " + columna);

                                        if (!(sheet.getRow(fila).getCell(columna) == null)) { //VERIFICA QUE TENGA HORAS REPORTADAS
                                            //System.out.println("if 3");
                                            if (!(sheet.getRow(fila).getCell(columna).toString().equals(""))) {
                                                //System.out.println("if 4:"+sheet.getRow(fila).getCell(columna).toString()+"." + "fila: " + fila +"columna: " + columna);
                                                excelConNpt = false;
                                                if (visualiza) {

                                                    if (fila == recorreFila) {
                                                        veces++;
                                                        sumaHoras = sumaHoras + Double.parseDouble(sheet.getRow(fila).getCell(columna).toString());
                                                    }
                                                    ///
                                                    npts.add(new Npt(idPozo, intervencion,
                                                            Integer.parseInt(sheet.getRow(fila).getCell(1).toString().replace(".0", "")), SiopUtils.dateFechaOperacionNPTS(sheet.getRow(fila).getCell(2).toString()),
                                                            Integer.parseInt(sheet.getRow(fila).getCell(3).toString().replace(".0", "")), Float.parseFloat(sheet.getRow(fila).getCell(columna).toString().replace(".0", "")),
                                                            NptsIntervencionDAO.getIdNptIntervencion(catNpts.get(aux), catActividades.get(aux), tipoIntervencion), sheet.getRow(fila).getCell(6).toString().replace("'", ""),
                                                            catDetalle.get(aux),
                                                            CatEtapaDAO.searchIdEtapa(sheet.getRow(fila).getCell(4).toString()),
                                                            CatTrDAO.searchIdTr(sheet.getRow(fila).getCell(5).toString())));
                                                    //System.out.println("SALIO");
                                                    //System.out.println("VALOR: " +sheet.getRow(fila).getCell(columna).toString()+" aja" + sheet.getRow(fila).getCell(4).toString() + " ,,,  " + sheet.getRow(fila).getCell(5).toString()) ;
                                                } else {

                                                    npts.add(new Npt(idPozo, intervencion,
                                                            Integer.parseInt(sheet.getRow(fila).getCell(1).toString().replace(".0", "")), SiopUtils.dateFechaOperacionNPTS(sheet.getRow(fila).getCell(2).toString()),
                                                            Integer.parseInt(sheet.getRow(fila).getCell(3).toString().replace(".0", "")), Float.parseFloat(sheet.getRow(fila).getCell(columna).toString().replace(".0", "")),
                                                            sheet.getRow(fila).getCell(6).toString().replace("'", ""), catActividadesString.get(aux), catNptsString.get(aux)));
                                                }

                                            } else {
                                                excelSinNpt = true;  //Indica que no tiene horas de NPTS                                    
                                            }
                                        } else {
                                            excelSinNpt = true;  //Indica que no tiene horas de NPTS                                    
                                        }

                                    } else {
                                        numFilas = 0;
                                    }

                                    //}//IS HIDDEN
                                }
                                aux++;
                            }//FOR COLUMNAS
                        }//IF ULTIMO CONSECUTIVO
                        //aux++;
                        if (excelSinNpt && excelConNpt) {
                            try {
                                //System.out.println("6");
                                error = 5;
                                //if (!(sheet.getRow(fila).getCell(2).toString().equals(""))) {
                                //System.out.println("***** VACIO: " + sheet.getRow(fila).getCell(1).toString() + " , " + sheet.getRow(fila).getCell(2) + " ,  " + sheet.getRow(fila).getCell(3));
                                if (!(sheet.getRow(fila).getCell(1) == null) || !(sheet.getRow(fila).getCell(2) == null) || !(sheet.getRow(fila).getCell(3) == null)
                                        || !(sheet.getRow(fila).getCell(1).toString().equals("")) || !(sheet.getRow(fila).getCell(2).toString().equals("")) || !(sheet.getRow(fila).getCell(3).toString().equals(""))) {
                                                                     
                                    if (visualiza) {
                                        npts.add(new Npt(idPozo, intervencion,
                                                Integer.parseInt(sheet.getRow(fila).getCell(1).toString().replace(".0", "")), SiopUtils.dateFechaOperacionNPTS(sheet.getRow(fila).getCell(2).toString()),
                                                Integer.parseInt(sheet.getRow(fila).getCell(3).toString().replace(".0", "")), 24,
                                                NptsIntervencionDAO.getIdNptIntervencionNA(tipoIntervencion), sheet.getRow(fila).getCell(6).toString().replace("'", ""), "",
                                                CatEtapaDAO.searchIdEtapa(sheet.getRow(fila).getCell(4).toString()),
                                                CatTrDAO.searchIdTr(sheet.getRow(fila).getCell(5).toString()))); //24 debe ser el id NA 
                                    } else {
                                        //System.out.println("***** VACIO: " + sheet.getRow(fila).getCell(1).toString() + " , " + sheet.getRow(fila).getCell(2) + " ,  " + sheet.getRow(fila).getCell(3));
                                        npts.add(new Npt(idPozo, intervencion,
                                                Integer.parseInt(sheet.getRow(fila).getCell(1).toString().replace(".0", "")), SiopUtils.dateFechaOperacionNPTS(sheet.getRow(fila).getCell(2).toString()),
                                                Integer.parseInt(sheet.getRow(fila).getCell(3).toString().replace(".0", "")), 24,
                                                sheet.getRow(fila).getCell(6).toString().replace("'", ""), "NORMAL", "CO")); //24 debe ser el id NA 
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println("Verifica: " + e.getMessage());
                            }

                        }
                    }
                  
                    break; // para que no siga recorriendo las hojas de excel


                }//QUE SEA IGUAL A REAL LA HOJA

            }// RECORRIDO POR HOJA
        } catch (Exception e) {
            System.out.println(" ERROR AL LEER: " + e.getMessage());
            npts = new ArrayList();
            npts.add(new Npt(0, error, -100, "0", reportFila + 1, reportColumna + 1, "", "ERROR ", ""));
            System.out.println("ERROR: " + e.getLocalizedMessage() + " TAMAÑO " + npts.size());

        }
        
        if (error==2){
            npts = new ArrayList();
            npts.add(new Npt(0, error, -100, "0", reportFila + 1, reportColumna + 1, "", "ERROR ", ""));
        }
        
        for (int ii = 0; ii < npts.size(); ii++) {
            //if (((Npt) npts.get(i)).getIdNpt() == 0) {
            System.out.println("Consecutivo " + ((Npt) npts.get(ii)).getConsecutivo()
                    + " , FECHA " + ((Npt) npts.get(ii)).getFecha()
                    + " , PROFUNDIDAD " + ((Npt) npts.get(ii)).getProfundidad()
                    + " , IDNPTINTERVERNCION " + ((Npt) npts.get(ii)).getIdNptIntervencion()
                    + ", TIEMPO " + ((Npt) npts.get(ii)).getTiempo()
                    + ", INTERVENCION " + ((Npt) npts.get(ii)).getIntervencion()
                    + "detalle " + ((Npt) npts.get(ii)).getStrDetalle()
                    + "etapa " + ((Npt)npts.get(ii)).getEtapa()  
                    + "TR " + ((Npt)npts.get(ii)).getTr());
            //}
        }

        
        
        //System.exit(0); 
        return npts;
    }
    
    
	// METODOS PARA TRATAR LOS NPTS
	
	//int ultimoConse;
	public static List<Npt>getListNpts(String excelName, int index,String textBuscar,int tipo,boolean visualiza,int idSubInter){
        int intervencion =IntervencionDAO.searchIntervencionByTipoAndPozo(index,tipo,idSubInter);
        System.out.println("INTERVENCION: " + intervencion + " IDPOZO= " + index + " INTER: " +tipo);        
        try {
        	//ultimoConse = 0;//PerforacionDAO.searchPerfConseByInter(intervencion, tipo);//es el id de la intervencion
        } 
        catch (Exception e) {
        }
        System.out.println("LLAMADO::::");
        List<Npt>lista=leerExcelNPT(excelName,index,intervencion,tipo,textBuscar,visualiza,0/*ultimoConsecutivo*/); // Intervencion es el id                                
        return lista;
    }
	// FIN METODOS PARA TRATAR LOS NPTS
	
	
	
	
	
	
    public static List<Programas> getProgramaPerforacion(String excelName, int idIntervencion) {
        List<Programas> listaPrograma = new ArrayList<Programas>();
        HSSFWorkbook myWorkbook;
        int error=0;
        try {
            FileInputStream myInput = new FileInputStream(excelName);
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            myWorkbook = new HSSFWorkbook(myFileSystem);
            org.apache.poi.ss.usermodel.Sheet sheet = myWorkbook.getSheetAt(0);
            error=1;
            //Identifcar apartir de que columna buscar
            int filaBuscar = 0;
            for (int fila = 0; fila < sheet.getLastRowNum(); fila++) {
                if (!(sheet.getRow(fila).getCell(4) == null)) {
                    if (sheet.getRow(fila).getCell(4).toString().equalsIgnoreCase("Etapa")) {
                        filaBuscar = fila + 1;
                        break;
                    }
                }
            }
            error=2;
            //Tomar lectura del Programa
            Intervencion intervencion=new Intervencion();
            intervencion.setIdIntervencion(idIntervencion);
            error=3;
            for (int fila = filaBuscar; fila < sheet.getLastRowNum(); fila++) {
                //System.out.println("valor: " + sheet.getRow(fila).getCell(3)==null || sheet.getRow(fila).getCell(3).toString().equals(""));
                if (sheet.getRow(fila).getCell(4) == null || sheet.getRow(fila).getCell(4).toString().equals("")) {
                    break;
                }
                    listaPrograma.add(new Programas(
                            CatEtapaDAO.getCatEtapa(sheet.getRow(fila).getCell(4).toString()),
                            intervencion,
                            CatTrDAO.getCatTr(sheet.getRow(fila).getCell(5).toString()),
                            sheet.getRow(fila).getCell(6).getNumericCellValue(),
                            sheet.getRow(fila).getCell(11).getNumericCellValue()
                            ));
            }
        error=4;
        
        /*for( Programas p: listaPrograma){
            System.out.println(p.getCatEtapa().getStrNombre() + "\n" +p.getCatTr().getStrNombre() + "\n"+p.getFloatTiempo()+"\n"+p.getIntProf());
        }*/
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("ERROR getProgramaPerforacion: " + e.getMessage() + error);
        }
        return listaPrograma;

    }
    
     public static List<Programas> getProgramaTerminacion(String excelName, int idIntervencion) {
        List<Programas> listaPrograma = new ArrayList<Programas>();
        HSSFWorkbook myWorkbook;
        int error=0;
        try {
            FileInputStream myInput = new FileInputStream(excelName);
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            myWorkbook = new HSSFWorkbook(myFileSystem);
            org.apache.poi.ss.usermodel.Sheet sheet = myWorkbook.getSheetAt(0);
            error=1;
            //Identifcar apartir de que columna buscar
            int filaBuscar = 0;
            String etapa="",tr="";
            for (int fila = 0; fila < sheet.getLastRowNum(); fila++) {
                if (!(sheet.getRow(fila).getCell(5) == null)) {
                    if (sheet.getRow(fila).getCell(5).toString().equalsIgnoreCase("PROF")) {
                        filaBuscar = fila + 1;
                        break;
                    }
                    if (!(sheet.getRow(fila).getCell(4) == null) && !(sheet.getRow(fila).getCell(4).toString().equals(""))) {
                        etapa=sheet.getRow(fila).getCell(4).toString();
                        tr=sheet.getRow(fila).getCell(5).toString();
                    }
                }
            }
            System.out.println("ULTIMA ETAPA: " + etapa + " TR " + tr+ " FILA BUSCAR: " +filaBuscar);
            CatEtapa catEtapa=CatEtapaDAO.getCatEtapa(etapa);
            CatTr catTr= CatTrDAO.getCatTr(tr);
            error=2;
            //Tomar lectura del Programa
            Intervencion intervencion=new Intervencion();
            intervencion.setIdIntervencion(idIntervencion);
            error=3;
            for (int fila = filaBuscar; fila < sheet.getLastRowNum(); fila++) {
                //System.out.println("valor: " + sheet.getRow(fila).getCell(3)==null || sheet.getRow(fila).getCell(3).toString().equals(""));
                if (sheet.getRow(fila).getCell(4) == null || sheet.getRow(fila).getCell(4).toString().equals("")) {
                    break;
                }                
                    listaPrograma.add(new Programas(
                            catEtapa,
                            intervencion,
                            catTr,
                            sheet.getRow(fila).getCell(5).getNumericCellValue(),
                            sheet.getRow(fila).getCell(6).getNumericCellValue()
                            ));
            }
        error=4;
        
        /*for( Programas p: listaPrograma){
            System.out.println(p.getCatEtapa().getStrNombre() + "\n" +p.getCatTr().getStrNombre() + "\n"+p.getFloatTiempo()+"\n"+p.getIntProf());
        }*/
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("ERROR getProgramaTerminacion: " + e.getMessage() + error);
        }
        return listaPrograma;

    }
      public static List<Programas> getProgramaRMA(String excelName, int idIntervencion) {
        List<Programas> listaPrograma = new ArrayList<Programas>();
        HSSFWorkbook myWorkbook;
        int error=0;
        try {
            FileInputStream myInput = new FileInputStream(excelName);
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            myWorkbook = new HSSFWorkbook(myFileSystem);
            org.apache.poi.ss.usermodel.Sheet sheet = myWorkbook.getSheetAt(0);
            error=1;
            //Identifcar apartir de que columna buscar
            int filaBuscar = 0;
            for (int fila = 0; fila < sheet.getLastRowNum(); fila++) {
                if (!(sheet.getRow(fila).getCell(4) == null)) {
                    if (sheet.getRow(fila).getCell(4).toString().equalsIgnoreCase("ETAPA 1")) {
                        filaBuscar = fila + 1;
                        break;
                    }
                }
            }
            error=2;
            //Tomar lectura del Programa
            Intervencion intervencion=new Intervencion();
            intervencion.setIdIntervencion(idIntervencion);
            error=3;
            for (int fila = filaBuscar; fila < sheet.getLastRowNum(); fila++) {
                //System.out.println("valor: " + sheet.getRow(fila).getCell(3)==null || sheet.getRow(fila).getCell(3).toString().equals(""));
                if (sheet.getRow(fila).getCell(4) == null || sheet.getRow(fila).getCell(4).toString().equals("")) {
                    break;
                }

                //CatEtapa catEtapa, Intervencion intervencion, CatTr catTr, 
                //double intProf, double floatTiempo
                    listaPrograma.add(new Programas(
                            CatEtapaDAO.getCatEtapa(sheet.getRow(fila).getCell(4).toString()),
                            intervencion,
                            CatTrDAO.getCatTr(sheet.getRow(fila).getCell(5).toString()),
                            sheet.getRow(fila).getCell(8).getNumericCellValue(),
                            sheet.getRow(fila).getCell(7).getNumericCellValue()
                            ));
            }
        error=4;
        /*
        for( Programas p: listaPrograma){
            System.out.println(p.getCatEtapa().getStrNombre() + "\n"+p.getFloatTiempo()+"\n"+p.getIntProf());
        }
        */
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("ERROR getProgramaRMA: " + e.getMessage() + error);
        }
        return listaPrograma;

    }

      /*
    public static String insertaNpts(List<Npt> listaNpts, int tipoIntervencion) {
        String res = null;
        try {
            for (int j = 0; j < listaNpts.size(); j++) {
                Npt myNpt = (Npt) listaNpts.get(j);
                if (myNpt.getConsecutivo() != -100) {
                    switch (tipoIntervencion) {
                        case 1: //PERFORACION
                            System.out.println("INSERTA PERFORACION: " + listaNpts.get(j).getConsecutivo());
                            PerforacionDAO.insertPerforacion(myNpt);
                            break;//;
                        case 2: // TERMINACION
                            System.out.println("INSERTA TERMINACION" + listaNpts.get(j).getConsecutivo());
                            TerminacionDAO.insertTerminacion(myNpt);
                            break;
                        case 3: //RMA
                            System.out.println("INSERTA RMA" + listaNpts.get(j).getConsecutivo());
                            RmaDAO.insertRma(myNpt);
                            //myDAO.insertRMA(myNpt);
                            break;
                    }
                    res = "";
                }else{
                    res="Ocurrio un error al Cargar el Archivo, de clic en Visualizar para más detalle";
                    break;
                }

            }
            
        } catch (Exception e) {
            res = "Falló la carga del archivo, revise la estructura";
            System.out.println("insertaNpts: " + e.getMessage());
        }
        return res;
    }
    
    
    public static String insertaPrograma(List<Programas> listaProgramas) {
        String res = null;
        try {
            for (Programas programas:listaProgramas){
               ProgramasDAO.setNewProgramas(programas);
            }
            res="";
        } catch (Exception e) {
            res = "Fallo la carga de Programas, revise la estructura \n("+ e.getMessage()+")";
            System.out.println("insertaNpts: " + e.getMessage());
        }
        return res;
    }
    
    */
	

}
