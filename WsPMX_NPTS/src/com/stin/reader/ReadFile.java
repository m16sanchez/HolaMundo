package com.stin.reader;

import java.io.FileInputStream;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.stin.model.Informacion;
import com.stin.model.InformacionPozos;

public class ReadFile {

	private String fileName;
    private FileInputStream myInput;
    private POIFSFileSystem myFileSystem;
    private HSSFWorkbook myWorkBook;
    private int numPags;

    /**
     * Constructor : inicializa los flujos
     *
     * @param fileName
     */
    public ReadFile(String fileName) {
        try {
            this.fileName = fileName;
            this.myInput = new FileInputStream(this.fileName);
            this.myFileSystem = new POIFSFileSystem(this.myInput);
            this.myWorkBook = new HSSFWorkbook(this.myFileSystem);
            this.numPags = myWorkBook.getNumberOfSheets();
        } catch (Exception exp) {
            System.out.println(exp);
        }
    }

    /**
     * getAllInfoFile : Lee del archivo excel toda la informacion y la pasa a un
     * arraylist
     *
     * @return ArrayList : resultado de la lectura linea por linea
     */
    public ArrayList getAllInfoFile() {
        ArrayList<ArrayList> alValues = new ArrayList<ArrayList>();
        int ColsNumberByRow = 0;
        int RowsNumberByFile = 0;
        int countX = 0;
        String namePage = "";
        String nameFile = "";
        try {
            //*********************NUMERO DE PAGINAS A LEER*********************
            for (int nump = 0; nump < 1; nump++) {
                countX = 1;

                HSSFSheet mySheet = myWorkBook.getSheetAt(nump);

                namePage = mySheet.getSheetName();
                RowsNumberByFile = mySheet.getLastRowNum();
                nameFile = this.getNameFile(this.fileName);

                Iterator rowIter = mySheet.rowIterator();
                while (rowIter.hasNext()) {
                    HSSFRow myRow = (HSSFRow) rowIter.next();
                    ColsNumberByRow = myRow.getLastCellNum();
                    Iterator cellIter = myRow.cellIterator();
                    ArrayList alValuesForY = new ArrayList();
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        alValuesForY.add(myCell.toString());                        
                    }

                    ArrayList infoToRow = new ArrayList();
                    infoToRow.add(0, countX); //numero de renglon
                    infoToRow.add(1, ColsNumberByRow);//cuantas columnas contiene
                    infoToRow.add(2, alValuesForY); //array con todos los valores leidos
                    infoToRow.add(3, RowsNumberByFile);//numero de renglones del archivo
                    infoToRow.add(4, namePage);//nombre de la pagina
                    infoToRow.add(5, nameFile);//nombre del archivo
                    alValues.add(infoToRow);

                    countX++;
                    ColsNumberByRow = 0;
                }
            }


        } catch (Exception exp) {
            System.out.print("error");
        }     
        return alValues;
    }

    public ArrayList getResults(ArrayList aValues) {
        ArrayList results = new ArrayList();
        ArrayList relementsAux = null;
        ArrayList values = new ArrayList();
        String diasProg="";
        String suptte="";
        int cY = 0;
        boolean isIRead = false;
        boolean isFRead = false;
        boolean isICRead = false;
        boolean isFCRead = false;
        boolean banderaSuper=false;
        
        int countProg=0;
        int length = 0;
        int rowIni = 0;
        int rowFin = 0;
        int rowIniC = 0;
        int rowFinC = 0;
        String pozoN = "";
        String array1[] = new String[3];
        String array2[] = new String[3];
        String array3[] = new String[3];
        String array4[]=new String[3];
        Iterator iterValues = aValues.iterator();
        array1 = null;
        array2 = null;
        array3 = null;
        array4 = null;
        String dateInF[] = new String[2];



        ArrayList<HashMap<String, String>> contenedor = new ArrayList();
        String aResumeDF[] = new String[2];
        String auxResume = "";
        boolean is7data = false;
        int countRow = 0;
        ArrayList<HashMap<String, String>> allElements = new ArrayList();

        
        InformacionPozos pozo= new InformacionPozos("", "");
        while (iterValues.hasNext()) {
            ArrayList infoAtCol = new ArrayList();
            infoAtCol = (ArrayList) iterValues.next();


            
            //System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            //System.out.println("Numero de renglon :" + infoAtCol.get(0));    
            
            //System.out.println("Archivo :" + infoAtCol.get(5)); 
            //System.out.println("pagina :" + infoAtCol.get(4)); 
            //System.out.println("-->: " + infoAtCol);               
            //System.out.println("Numero de lineas: " + infoAtCol.get(3));            
            //System.out.println("Numero de Columnas:" + infoAtCol.get(1));                                                                         
            ArrayList contentByColum = new ArrayList();
            contentByColum = (ArrayList) infoAtCol.get(2);
            //System.out.println("VALOR: " + contentByColum );


            if (isProfund(contentByColum, Integer.parseInt(infoAtCol.get(0).toString()))) {
                HashMap<String, String> profundidad;
                HashMap<String, String> barrenas;
                HashMap<String, String> herramienta;
                HashMap<String, String> nucleos;
                HashMap<String, String> lodo;
                is7data = true;
                countRow = 0;
                profundidad = new HashMap();
                barrenas = new HashMap();
                herramienta = new HashMap();
                nucleos = new HashMap();
                lodo = new HashMap();
                contenedor = new ArrayList();
                contenedor.add(profundidad);
                contenedor.add(barrenas);
                contenedor.add(herramienta);
                contenedor.add(nucleos);
                contenedor.add(lodo);
            }
            if (is7data) {
                countRow++;
                contenedor = getValuesForRowSevenDatas(contentByColum, countRow, contenedor);
                
            }
            if (countRow == 22) {
                is7data = false;
                countRow = 0;                

            }            

            if(countProg==62){
                countProg=0;
            }
            
            if (countProg==4){
                diasProg=isDiasProg(contentByColum, Integer.parseInt(infoAtCol.get(0).toString()));
                //System.out.println("___________________ meCOUNT: " + countProg + " = " + diasProg);
            }
                
            if (countProg==55){
                if(banderaSuper){
                    suptte=isSuppte(contentByColum, Integer.parseInt(infoAtCol.get(0).toString()));
                    //System.out.println("EL SUPER::: " + suptte);
                    pozo.setSuptte(suptte);
                    banderaSuper=false;
                }
                
                
            }
            
            countProg++;
            
            if ((auxResume = isResumeDay(contentByColum, Integer.parseInt(infoAtCol.get(0).toString()))) != null) {
                aResumeDF[0] = auxResume;
            }

            if ((auxResume = isResumeFinal(contentByColum, Integer.parseInt(infoAtCol.get(0).toString()))) != null) {
                aResumeDF[1] = auxResume;
            }

                                                             
            if (array1 == null) {
                array1 = isCia(contentByColum, Integer.parseInt(infoAtCol.get(0).toString()));

                //System.out.println("--> Array1 Null ");
            }
            if (array2 == null) {
                array2 = isFechaOperacion(contentByColum, Integer.parseInt(infoAtCol.get(0).toString()));

                // System.out.println("--> Array2 Null ");
            }
            if (array3 == null) {
                array3 = isEquipo(contentByColum, Integer.parseInt(infoAtCol.get(0).toString()));

                //System.out.println("--> Array3 Null ");
            }
           
          

            rowIni = isHeadOfRead(contentByColum, Integer.parseInt(infoAtCol.get(0).toString()));
            if (rowIni != -1) {
                //System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                //System.out.println("Numero de renglon :" + infoAtCol.get(0));
                //System.out.println("ini-->" +  rowIni);
                isIRead = true;
            }
            contentByColum = (ArrayList) infoAtCol.get(2);
            rowFin = isFootOfRead(contentByColum, Integer.parseInt(infoAtCol.get(0).toString()));
            if (rowFin != -1) {
                //System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                //System.out.println("Numero de renglon :" + infoAtCol.get(0));
                //System.out.println("fin-->>" +  rowFin);
                isFRead = true;
            }
            contentByColum = (ArrayList) infoAtCol.get(2);            
            rowIniC = isContinueOfRead(contentByColum, Integer.parseInt(infoAtCol.get(0).toString()));
            if (rowIniC != -1) {
                //System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                //System.out.println("Numero de renglon :" + infoAtCol.get(0));
                //System.out.println("iniConti-->>" +  rowIniC);
                isICRead = true;
            }
            contentByColum = (ArrayList) infoAtCol.get(2);
            rowFinC = isEndContinueOfRead(contentByColum, Integer.parseInt(infoAtCol.get(0).toString()));

            if (rowFinC != -1) {

                if (isICRead == true) {
                    isFCRead = true;
                    //System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                    //System.out.println("Numero de renglon :" + infoAtCol.get(0));
                    //System.out.println("finconti-->>" +  rowFinC);
                } else {
                    isFCRead = false;
                }
            }

            //System.out.println("-i->" + isIRead +"-f->" + isFRead+"-ic->"+isICRead+"-fc->"+ isFCRead);


            if ((isIRead == true) && (isFRead == false) && (isICRead == false) && (isFCRead == false)) {

                if (Integer.parseInt(infoAtCol.get(0).toString()) < rowIni) {
                } else if (Integer.parseInt(infoAtCol.get(0).toString()) == rowIni) {
                    ArrayList pz = (ArrayList) infoAtCol.get(2);
                    pozoN = (String) pz.get(2);
                    //System.out.println("Pozo" + pz.get(2)  );
                    relementsAux = new ArrayList();
                } else {
                    if (Integer.parseInt(infoAtCol.get(0).toString()) > rowIni) {
                        values = (ArrayList) infoAtCol.get(2);
                        dateInF = this.getDate((String) values.get(0));
                        relementsAux.add(new Informacion(dateInF[0], dateInF[1], (String) values.get(1)));
                        //System.out.println("date: " +values.get(0) + "  contenido : " + values.get(1));
                    }
                }
            } else if ((isIRead == true) && (isFRead == true) && (isICRead == false) && (isFCRead == false)) {

                //InformacionPozos pozo = new InformacionPozos("", "");
                pozo = new InformacionPozos("", "");
                pozo.setPozo(pozoN);
                pozo.setNumRow(Integer.parseInt(infoAtCol.get(0).toString()));
                pozo.setNumCols(Integer.parseInt(infoAtCol.get(1).toString()));
                pozo.setNomPag((String) infoAtCol.get(4));
                pozo.setNomFile((String) infoAtCol.get(5));
                pozo.setAllInfo(relementsAux);
                              
                
                /*
                 * Nuevo codigo
                 */                          
                 
                if (array1 == null) {
                    pozo.setCia("");
                    pozo.setCia_Complement("");
                    pozo.setCia_Fecha("");                    
                } else {
                    pozo.setCia(array1[0]);
                    pozo.setCia_Complement(array1[1]);
                    pozo.setCia_Fecha(array1[2]);      
                    pozo.setDiasProg(diasProg);
                    banderaSuper=true;
                }
                if (array2 == null) {
                    pozo.setReportediario_Fecha("");                    
                    pozo.setReportediario_proyecto("");
                    pozo.setReportediario_complemento("");                    
                } else {
                
                    pozo.setReportediario_Fecha(array2[0]);
                    //System.out.println("F1: " + array2[0].toString());
                    pozo.setReportediario_proyecto(array2[1]);
                    pozo.setReportediario_complemento(array2[2]);                    
                    
                }
                if (array3 == null) {
                    pozo.setEquipo("");
                    pozo.setEquipo_conductor("");
                    pozo.setEquipo_inicio("");                    
                } else {
                    pozo.setEquipo(array3[0]);
                    pozo.setEquipo_conductor(array3[1]);
                    pozo.setEquipo_inicio(array3[2]);                    
                }
                
                //System.out.println("1) "+pozo.getCia() + " 2)" + pozo.getReportediario_Fecha() + " 3)" + pozo.getEquipo_conductor() + " 4)" + pozo.getDiasProg());
               
                

                array1 = null;
                array2 = null;
                array3 = null;                
                
                
                isIRead = false;
                isFRead = false;
                relementsAux = null;
                //System.out.println("pzo: "  + pozo.getNumRow());



                /*
                 * Agregamos el nuevo nodo completo ya leido o lo que ya
                 * contiene
                 */


                //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  1  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                pozo.addHojaProfundidad(contenedor.get(0));
                //System.out.println("Profucndida");
                HashMap<String, String> mp = contenedor.get(0);
                //System.out.println("|" + mp.get("profund_00_desarrollo") + " | " + mp.get("profund_00_vertical") + "|");
                //System.out.println("|" + mp.get("profund_24_desarrollo") + " | " + mp.get("profund_24_vertical") + "|");
                //System.out.println("|" + mp.get("profund_05_desarrollo") + " | " + mp.get("profund_05_vertical") + "|");
                //System.out.println("|" + mp.get("profund_ava_ind_desarrollo") + " | " + mp.get("profund_ava_ind_vertical") + "|");
                //System.out.println("|" + mp.get("profund_ult_cont_desarrollo") + " | " + mp.get("profund_ult_cont_vertical") + "|");
                //System.out.println("|" + mp.get("profund_prox_count_desarrollo") + " | " + mp.get("profund_prox_count_vertical") + "|");

                //System.out.println("|" + mp.get("profund_ultima_tr_desarrollo") + " | " + mp.get("profund_ultima_tr_vertical") + "|");
                //System.out.println("|" + mp.get("profund_prox_tr_desarrollo") + " | " + mp.get("profund_prox_tr_vertical") + "|");
                //System.out.println("|" + mp.get("profund_b_l_desarrollo") + " | " + mp.get("profund_b_l_vertical") + "|");
                //System.out.println("|" + mp.get("profund_c2_desarrollo") + " | " + mp.get("profund_c2_vertical") + "|");

                pozo.addHojaBarrenas(contenedor.get(1));
                //System.out.println("barrenas");
                HashMap<String, String> mb = contenedor.get(1);
                //System.out.println("|" + mb.get("barrena_marca_actual") + " | " + mb.get("barrena_marca_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_serie_actual") + " | " + mb.get("barrena_serie_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_diametro_actual") + " | " + mb.get("barrena_diametro_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_tipo_actual") + " | " + mb.get("barrena_tipo_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_toberas_actual") + " | " + mb.get("barrena_toberas_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_mts_perf_actual") + " | " + mb.get("barrena_mts_perf_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_hors_operacion_actual") + " | " + mb.get("barrena_hors_operacion_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_prom_x_m_actual") + " | " + mb.get("barrena_prom_x_m_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_3_ult_mts_actual") + " | " + mb.get("barrena_3_ult_mts_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_costo_x_m_actual") + " | " + mb.get("barrena_costo_x_m_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_amp_inic_actual") + " | " + mb.get("barrena_amp_inic_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_des_AIDC_actual") + " | " + mb.get("barrena_des_AIDC_anterior") + "|");


                pozo.addHojaHerramientas(contenedor.get(2));
                //System.out.println("herramientas");
                HashMap<String, String> mh = contenedor.get(2);
                //System.out.println("|" + mh.get("herramienta_reng_1") + " | " + mh.get("herramienta_cant_reng_1") + "|" + "|" + mh.get("herramienta_long_reng_1") + " | " + mh.get("herramienta_obs_reng_1") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_2") + " | " + mh.get("herramienta_cant_reng_2") + "|" + "|" + mh.get("herramienta_long_reng_2") + " | " + mh.get("herramienta_obs_reng_2") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_3") + " | " + mh.get("herramienta_cant_reng_3") + "|" + "|" + mh.get("herramienta_long_reng_3") + " | " + mh.get("herramienta_obs_reng_3") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_4") + " | " + mh.get("herramienta_cant_reng_4") + "|" + "|" + mh.get("herramienta_long_reng_4") + " | " + mh.get("herramienta_obs_reng_4") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_5") + " | " + mh.get("herramienta_cant_reng_5") + "|" + "|" + mh.get("herramienta_long_reng_5") + " | " + mh.get("herramienta_obs_reng_5") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_6") + " | " + mh.get("herramienta_cant_reng_6") + "|" + "|" + mh.get("herramienta_long_reng_6") + " | " + mh.get("herramienta_obs_reng_6") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_7") + " | " + mh.get("herramienta_cant_reng_7") + "|" + "|" + mh.get("herramienta_long_reng_7") + " | " + mh.get("herramienta_obs_reng_7") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_8") + " | " + mh.get("herramienta_cant_reng_8") + "|" + "|" + mh.get("herramienta_long_reng_8") + " | " + mh.get("herramienta_obs_reng_8") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_9") + " | " + mh.get("herramienta_cant_reng_9") + "|" + "|" + mh.get("herramienta_long_reng_9") + " | " + mh.get("herramienta_obs_reng_9") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_10") + " | " + mh.get("herramienta_cant_reng_10") + "|" + "|" + mh.get("herramienta_long_reng_10") + " | " + mh.get("herramienta_obs_reng_10") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_11") + " | " + mh.get("herramienta_cant_reng_11") + "|" + "|" + mh.get("herramienta_long_reng_11") + " | " + mh.get("herramienta_obs_reng_11") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_12") + " | " + mh.get("herramienta_cant_reng_12") + "|" + "|" + mh.get("herramienta_long_reng_12") + " | " + mh.get("herramienta_obs_reng_12") + "|");

                pozo.addHojaNucleos(contenedor.get(3));
                //System.out.println("nucleos");
                HashMap<String, String> mn = contenedor.get(3);
                //System.out.println("|" + mn.get("nucleos_r_row_0_col_0") + " | " + mn.get("int_prog_row_0_col_1") + "|" + " | " + mn.get("int_disp_row_0_col_2") + "|");
                //System.out.println("|" + mn.get("nucleos_r_row_1_col_0") + " | " + mn.get("int_prog_row_1_col_1") + "|" + " | " + mn.get("int_disp_row_1_col_2") + "|");
                //System.out.println("|" + mn.get("nucleos_r_row_2_col_0") + " | " + mn.get("int_prog_row_2_col_1") + "|" + " | " + mn.get("int_disp_row_2_col_2") + "|");
                //System.out.println("|" + mn.get("nucleos_r_row_3_col_0") + " | " + mn.get("int_prog_row_3_col_1") + "|" + " | " + mn.get("int_disp_row_3_col_2") + "|");
                //System.out.println("|" + mn.get("nucleos_r_row_4_col_0") + " | " + mn.get("int_prog_row_4_col_1") + "|" + " | " + mn.get("int_disp_row_4_col_2") + "|");

                pozo.addHojaLodo(contenedor.get(4));
                //System.out.println("lodo");
                HashMap<String, String> ml = contenedor.get(4);
                //System.out.println("|" + ml.get("lodo_row_0_col_0") + " | " + ml.get("lodo_row_0_col_23") + "|");
                //System.out.println("|" + ml.get("lodo_row_2") + " | ");
                //System.out.println("|" + ml.get("lodo_row_3") + " | ");
                //System.out.println("|" + ml.get("lodo_row_4") + " | ");
                //System.out.println("|" + ml.get("lodo_row_5") + " | ");



//                
//                System.out.println(" KORN" );   
//                Iterator iterator = ml.keySet().iterator();       
//while (iterator.hasNext()) {   
//   String key = iterator.next().toString();   
//   String value = ml.get(key).toString();   
//    
//   System.out.println(key + " " + value);   
//}  
//System.out.println(" /KORN" );   
                //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");                                                               
                pozo.setResumenDiaAnterior(aResumeDF[0]);
                pozo.setResumenfin(aResumeDF[1]);
                aResumeDF[0] = "";
                aResumeDF[1] = "";


                results.add(pozo);
                //System.out.println("Tamaño results 1: " + results.size());
                //System.out.println("Finaliza lectura normal " );


            } else if ((isIRead == false) && (isFRead == false) && (isICRead == true) && (isFCRead == false)) {

                if (Integer.parseInt(infoAtCol.get(0).toString()) < rowIniC) {
                } else if (Integer.parseInt(infoAtCol.get(0).toString()) == rowIniC) { //System.out.println("inicia el relement");
                    relementsAux = new ArrayList();
                } else {
                    if (Integer.parseInt(infoAtCol.get(0).toString()) > rowIniC) {
                        values = (ArrayList) infoAtCol.get(2);
                        dateInF = this.getDate((String) values.get(0));
                        relementsAux.add(new Informacion(dateInF[0], dateInF[1], (String) values.get(1)));
                        //relementsAux.add( new Informacion((String)values.get(0), (String)values.get(1)));
                        //System.out.println("cdate: " +values.get(0) + " ccontenido : " + values.get(1));
                    }
                }
            } else if ((isIRead == false) && (isFRead == false) && (isICRead == true) && (isFCRead == true)) {
                
                InformacionPozos pozo2 = null;
                //System.out.println("tamaño de results :  " + results.size());
                pozo2 = (InformacionPozos) results.get((results.size() - 1));
                pozo2.addElementToPozo(relementsAux);
                isICRead = false;
                isFCRead = false;
                relementsAux = null;
                //System.out.println("pzo: "  + pozo.getNumRow());


                /*
                 * Agregamos el nuevo nodo completo ya leido o lo que ya
                 * contiene
                 */
                //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  2  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                //pozo.addHojaProfundidad(contenedor.get(0));
                //System.out.println("Profucndida");
                HashMap<String, String> mp = contenedor.get(0);
                //System.out.println("|" + mp.get("profund_00_desarrollo") + " | " + mp.get("profund_00_vertical") + "|");
                //System.out.println("|" + mp.get("profund_24_desarrollo") + " | " + mp.get("profund_24_vertical") + "|");
                //System.out.println("|" + mp.get("profund_05_desarrollo") + " | " + mp.get("profund_05_vertical") + "|");
                //System.out.println("|" + mp.get("profund_ava_ind_desarrollo") + " | " + mp.get("profund_ava_ind_vertical") + "|");
                //System.out.println("|" + mp.get("profund_ult_cont_desarrollo") + " | " + mp.get("profund_ult_cont_vertical") + "|");
                //System.out.println("|" + mp.get("profund_prox_count_desarrollo") + " | " + mp.get("profund_prox_count_vertical") + "|");

                //System.out.println("|" + mp.get("profund_ultima_tr_desarrollo") + " | " + mp.get("profund_ultima_tr_vertical") + "|");
                //System.out.println("|" + mp.get("profund_prox_tr_desarrollo") + " | " + mp.get("profund_prox_tr_vertical") + "|");
                //System.out.println("|" + mp.get("profund_b_l_desarrollo") + " | " + mp.get("profund_b_l_vertical") + "|");
                //System.out.println("|" + mp.get("profund_c2_desarrollo") + " | " + mp.get("profund_c2_vertical") + "|");


                //pozo.addHojaBarrenas(contenedor.get(1));
                //System.out.println("barrenas");
                HashMap<String, String> mb = contenedor.get(1);
                //System.out.println("|" + mb.get("barrena_marca_actual") + " | " + mb.get("barrena_marca_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_serie_actual") + " | " + mb.get("barrena_serie_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_diametro_actual") + " | " + mb.get("barrena_diametro_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_tipo_actual") + " | " + mb.get("barrena_tipo_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_toberas_actual") + " | " + mb.get("barrena_toberas_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_mts_perf_actual") + " | " + mb.get("barrena_mts_perf_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_hors_operacion_actual") + " | " + mb.get("barrena_hors_operacion_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_prom_x_m_actual") + " | " + mb.get("barrena_prom_x_m_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_3_ult_mts_actual") + " | " + mb.get("barrena_3_ult_mts_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_costo_x_m_actual") + " | " + mb.get("barrena_costo_x_m_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_amp_inic_actual") + " | " + mb.get("barrena_amp_inic_anterior") + "|");
                //System.out.println("|" + mb.get("barrena_des_AIDC_actual") + " | " + mb.get("barrena_des_AIDC_anterior") + "|");


               if(isFullOfData(contenedor.get(2))){
                    pozo2.addHojaHerramientas(contenedor.get(2));
                }
                //System.out.println("herramientas");
                HashMap<String, String> mh = contenedor.get(2);
                //System.out.println("|" + mh.get("herramienta_reng_1") + " | " + mh.get("herramienta_cant_reng_1") + "|" + "|" + mh.get("herramienta_long_reng_1") + " | " + mh.get("herramienta_obs_reng_1") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_2") + " | " + mh.get("herramienta_cant_reng_2") + "|" + "|" + mh.get("herramienta_long_reng_2") + " | " + mh.get("herramienta_obs_reng_2") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_3") + " | " + mh.get("herramienta_cant_reng_3") + "|" + "|" + mh.get("herramienta_long_reng_3") + " | " + mh.get("herramienta_obs_reng_3") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_4") + " | " + mh.get("herramienta_cant_reng_4") + "|" + "|" + mh.get("herramienta_long_reng_4") + " | " + mh.get("herramienta_obs_reng_4") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_5") + " | " + mh.get("herramienta_cant_reng_5") + "|" + "|" + mh.get("herramienta_long_reng_5") + " | " + mh.get("herramienta_obs_reng_5") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_6") + " | " + mh.get("herramienta_cant_reng_6") + "|" + "|" + mh.get("herramienta_long_reng_6") + " | " + mh.get("herramienta_obs_reng_6") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_7") + " | " + mh.get("herramienta_cant_reng_7") + "|" + "|" + mh.get("herramienta_long_reng_7") + " | " + mh.get("herramienta_obs_reng_7") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_8") + " | " + mh.get("herramienta_cant_reng_8") + "|" + "|" + mh.get("herramienta_long_reng_8") + " | " + mh.get("herramienta_obs_reng_8") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_9") + " | " + mh.get("herramienta_cant_reng_9") + "|" + "|" + mh.get("herramienta_long_reng_9") + " | " + mh.get("herramienta_obs_reng_9") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_10") + " | " + mh.get("herramienta_cant_reng_10") + "|" + "|" + mh.get("herramienta_long_reng_10") + " | " + mh.get("herramienta_obs_reng_10") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_11") + " | " + mh.get("herramienta_cant_reng_11") + "|" + "|" + mh.get("herramienta_long_reng_11") + " | " + mh.get("herramienta_obs_reng_11") + "|");
                //System.out.println("|" + mh.get("herramienta_reng_12") + " | " + mh.get("herramienta_cant_reng_12") + "|" + "|" + mh.get("herramienta_long_reng_12") + " | " + mh.get("herramienta_obs_reng_12") + "|");

                //pozo.addHojaNucleos(contenedor.get(3));
                //System.out.println("nucleos");
                HashMap<String, String> mn = contenedor.get(3);
                //System.out.println("|" + mn.get("nucleos_r_row_0_col_0") + " | " + mn.get("int_prog_row_0_col_1") + "|" + " | " + mn.get("int_disp_row_0_col_2") + "|");
                //System.out.println("|" + mn.get("nucleos_r_row_1_col_0") + " | " + mn.get("int_prog_row_1_col_1") + "|" + " | " + mn.get("int_disp_row_1_col_2") + "|");
                //System.out.println("|" + mn.get("nucleos_r_row_2_col_0") + " | " + mn.get("int_prog_row_2_col_1") + "|" + " | " + mn.get("int_disp_row_2_col_2") + "|");
                //System.out.println("|" + mn.get("nucleos_r_row_3_col_0") + " | " + mn.get("int_prog_row_3_col_1") + "|" + " | " + mn.get("int_disp_row_3_col_2") + "|");
                //System.out.println("|" + mn.get("nucleos_r_row_4_col_0") + " | " + mn.get("int_prog_row_4_col_1") + "|" + " | " + mn.get("int_disp_row_4_col_2") + "|");


                //pozo.addHojaLodo(contenedor.get(4));
                //System.out.println("lodo");
                HashMap<String, String> ml = contenedor.get(4);
                //System.out.println("|" + ml.get("lodo_row_0_col_0") + " | " + ml.get("lodo_row_0_col_23") + "|");
                //System.out.println("|" + ml.get("lodo_row_2") + " | ");
                //System.out.println("|" + ml.get("lodo_row_3") + " | ");
                //System.out.println("|" + ml.get("lodo_row_4") + " | ");
                //System.out.println("|" + ml.get("lodo_row_5") + " | ");

                //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");





                results.set((results.size() - 1), pozo2);
                //System.out.println("Tamaño results 2: " + results.size());
                //System.out.println("tamaño de results 2 :  " + results.size());
                //InformacionPozos otro = null;
                //otro = (InformacionPozos)results.get((results.size()-1));
                //Iterator io = otro.getAllInfo().iterator();
                //System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
                //while(io.hasNext()){
                //    Informacion iop = (Informacion)io.next();
                //System.out.println("XXXXXXXXXx-->" + iop.getFecha() +"XXXXXXXXXXXXXX-Z"+ iop.getActividad());

                //}
                //System.out.println("Finaliza lectura continuacion " );
                                
                array1 = null;
                array2 = null;
                array3 = null;
                array4=null;
            } else {
            }

            ////System.out.println("------------------------------------------------");

        }

//        System.out.println("ArrayList final -{-> " + results.size());

        return results;
    }

    public ArrayList<InformacionPozos> getFinalResults(ArrayList<InformacionPozos> array2) {
        boolean isFist = false;
        boolean isDateActive = false;
        boolean isAdded = false;
        String data1 = "";
        String data2 = "";
        String data3 = "";
        String aux = "";
        ArrayList<InformacionPozos> results = new ArrayList<InformacionPozos>();
        ArrayList newArrayList = new ArrayList();
        Iterator<InformacionPozos> i = array2.iterator();
        InformacionPozos infP = null;

        int cont = 0;
        int cHojas = 0;

        while (i.hasNext()) {
            infP = i.next();
            // System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            //System.out.println("numero de renglon"+infP.getNumRow());
            // System.out.println("columnas"+infP.getNumCols());
            // System.out.println("nombredeopag"+infP.getNomPag());
            // System.out.println("nombredearchivo"+infP.getNomFile());
            // System.out.println("informacion");
            ++cont;
            //System.out.println("<--------- Informacion archivo " + cont + " --------->");

            //System.out.println("Resumen dia anterior: " + infP.getResumenDiaAnterior());
            //System.out.println("Resumen final: " + infP.getResumenfin());


            //System.out.println("PROFUNDIDAD");
            Iterator<HashMap<String, String>> iprof = infP.getProfundidadPorHoja().iterator();
            cHojas = 0;
            while (iprof.hasNext()) {
//                System.out.println("Hoja: " + ++cHojas);
                HashMap<String, String> mp = iprof.next();
//                System.out.println("00:00 " + "|" + mp.get("profund_00_desarrollo") + " | " + mp.get("profund_00_vertical") + "|");
//                System.out.println("24:00 " + "|" + mp.get("profund_24_desarrollo") + " | " + mp.get("profund_24_vertical") + "|");
//                System.out.println("05:00 " + "|" + mp.get("profund_05_desarrollo") + " | " + mp.get("profund_05_vertical") + "|");
//                System.out.println("Ava/Ind    " + "|" + mp.get("profund_ava_ind_desarrollo") + " | " + mp.get("profund_ava_ind_vertical") + "|");
//                System.out.println("Ult.Cont   " + "|" + mp.get("profund_ult_cont_desarrollo") + " | " + mp.get("profund_ult_cont_vertical") + "|");
//                System.out.println("Prox. Cont " + "|" + mp.get("profund_prox_count_desarrollo") + " | " + mp.get("profund_prox_count_vertical") + "|");
//
//                System.out.println("Ultima TR  " + "|" + mp.get("profund_ultima_tr_desarrollo") + " | " + mp.get("profund_ultima_tr_vertical") + "|");
//                System.out.println("Prox. TR   " + "|" + mp.get("profund_prox_tr_desarrollo") + " | " + mp.get("profund_prox_tr_vertical") + "|");
//                System.out.println("B.L        " + "|" + mp.get("profund_b_l_desarrollo") + " | " + mp.get("profund_b_l_vertical") + "|");
//                System.out.println("C2         " + "|" + mp.get("profund_c2_desarrollo") + " | " + mp.get("profund_c2_vertical") + "|");

            }

            //System.out.println("BARRENAS");
            iprof = infP.getBarrenasPorHoja().iterator();
            cHojas = 0;
            while (iprof.hasNext()) {
                HashMap<String, String> mb = iprof.next();
//                System.out.println("Hoja: " + ++cHojas);
//                System.out.println("Marca      " + "|" + mb.get("barrena_marca_actual") + " | " + mb.get("barrena_marca_anterior") + "|");
//                System.out.println("Serie      " + "|" + mb.get("barrena_serie_actual") + " | " + mb.get("barrena_serie_anterior") + "|");
//                System.out.println("Diametro   " + "|" + mb.get("barrena_diametro_actual") + " | " + mb.get("barrena_diametro_anterior") + "|");
//                System.out.println("Tipo       " + "|" + mb.get("barrena_tipo_actual") + " | " + mb.get("barrena_tipo_anterior") + "|");
//                System.out.println("Toberas    " + "|" + mb.get("barrena_toberas_actual") + " | " + mb.get("barrena_toberas_anterior") + "|");
//                System.out.println("Mts. Perf  " + "|" + mb.get("barrena_mts_perf_actual") + " | " + mb.get("barrena_mts_perf_anterior") + "|");
//                System.out.println("Hrs. Op    " + "|" + mb.get("barrena_hors_operacion_actual") + " | " + mb.get("barrena_hors_operacion_anterior") + "|");
//                System.out.println("Prom x m   " + "|" + mb.get("barrena_prom_x_m_actual") + " | " + mb.get("barrena_prom_x_m_anterior") + "|");
//                System.out.println("3 Ult. Mts " + "|" + mb.get("barrena_3_ult_mts_actual") + " | " + mb.get("barrena_3_ult_mts_anterior") + "|");
//                System.out.println("Costo x m  " + "|" + mb.get("barrena_costo_x_m_actual") + " | " + mb.get("barrena_costo_x_m_anterior") + "|");
//                System.out.println("Amp. Inic  " + "|" + mb.get("barrena_amp_inic_actual") + " | " + mb.get("barrena_amp_inic_anterior") + "|");
//                System.out.println("Des. IADC  " + "|" + mb.get("barrena_des_AIDC_actual") + " | " + mb.get("barrena_des_AIDC_anterior") + "|");
            }

            //System.out.println("HERRAMIENTAS");
            iprof = infP.getHerramientasPorHoja().iterator();
            cHojas = 0;
            while (iprof.hasNext()) {
                HashMap<String, String> mh = iprof.next();
//                System.out.println("Hoja: " + ++cHojas);
//                System.out.println("|" + mh.get("herramienta_reng_1") + " | " + mh.get("herramienta_cant_reng_1") + "|" + "|" + mh.get("herramienta_long_reng_1") + " | " + mh.get("herramienta_obs_reng_1") + "|");
//                System.out.println("|" + mh.get("herramienta_reng_2") + " | " + mh.get("herramienta_cant_reng_2") + "|" + "|" + mh.get("herramienta_long_reng_2") + " | " + mh.get("herramienta_obs_reng_2") + "|");
//                System.out.println("|" + mh.get("herramienta_reng_3") + " | " + mh.get("herramienta_cant_reng_3") + "|" + "|" + mh.get("herramienta_long_reng_3") + " | " + mh.get("herramienta_obs_reng_3") + "|");
//                System.out.println("|" + mh.get("herramienta_reng_4") + " | " + mh.get("herramienta_cant_reng_4") + "|" + "|" + mh.get("herramienta_long_reng_4") + " | " + mh.get("herramienta_obs_reng_4") + "|");
//                System.out.println("|" + mh.get("herramienta_reng_5") + " | " + mh.get("herramienta_cant_reng_5") + "|" + "|" + mh.get("herramienta_long_reng_5") + " | " + mh.get("herramienta_obs_reng_5") + "|");
//                System.out.println("|" + mh.get("herramienta_reng_6") + " | " + mh.get("herramienta_cant_reng_6") + "|" + "|" + mh.get("herramienta_long_reng_6") + " | " + mh.get("herramienta_obs_reng_6") + "|");
//                System.out.println("|" + mh.get("herramienta_reng_7") + " | " + mh.get("herramienta_cant_reng_7") + "|" + "|" + mh.get("herramienta_long_reng_7") + " | " + mh.get("herramienta_obs_reng_7") + "|");
//                System.out.println("|" + mh.get("herramienta_reng_8") + " | " + mh.get("herramienta_cant_reng_8") + "|" + "|" + mh.get("herramienta_long_reng_8") + " | " + mh.get("herramienta_obs_reng_8") + "|");
//                System.out.println("|" + mh.get("herramienta_reng_9") + " | " + mh.get("herramienta_cant_reng_9") + "|" + "|" + mh.get("herramienta_long_reng_9") + " | " + mh.get("herramienta_obs_reng_9") + "|");
//                System.out.println("|" + mh.get("herramienta_reng_10") + " | " + mh.get("herramienta_cant_reng_10") + "|" + "|" + mh.get("herramienta_long_reng_10") + " | " + mh.get("herramienta_obs_reng_10") + "|");
//                System.out.println("|" + mh.get("herramienta_reng_11") + " | " + mh.get("herramienta_cant_reng_11") + "|" + "|" + mh.get("herramienta_long_reng_11") + " | " + mh.get("herramienta_obs_reng_11") + "|");
//                System.out.println("|" + mh.get("herramienta_reng_12") + " | " + mh.get("herramienta_cant_reng_12") + "|" + "|" + mh.get("herramienta_long_reng_12") + " | " + mh.get("herramienta_obs_reng_12") + "|");

            }

            //System.out.println("NUCLEOS");
            iprof = infP.getNucleosPorHoja().iterator();
            cHojas = 0;
            while (iprof.hasNext()) {
                HashMap<String, String> mn = iprof.next();
//                System.out.println("Hoja: " + ++cHojas);
//                System.out.println("|" + mn.get("nucleos_r_row_0_col_0") + " | " + mn.get("int_prog_row_0_col_1") + "|" + " | " + mn.get("int_disp_row_0_col_2") + "|");
//                System.out.println("|" + mn.get("nucleos_r_row_1_col_0") + " | " + mn.get("int_prog_row_1_col_1") + "|" + " | " + mn.get("int_disp_row_1_col_2") + "|");
//                System.out.println("|" + mn.get("nucleos_r_row_2_col_0") + " | " + mn.get("int_prog_row_2_col_1") + "|" + " | " + mn.get("int_disp_row_2_col_2") + "|");
//                System.out.println("|" + mn.get("nucleos_r_row_3_col_0") + " | " + mn.get("int_prog_row_3_col_1") + "|" + " | " + mn.get("int_disp_row_3_col_2") + "|");
//                System.out.println("|" + mn.get("nucleos_r_row_4_col_0") + " | " + mn.get("int_prog_row_4_col_1") + "|" + " | " + mn.get("int_disp_row_4_col_2") + "|");

            }


            //System.out.println("LODO");
            iprof = infP.getLodoPorHoja().iterator();
            cHojas = 0;
            while (iprof.hasNext()) {
                HashMap<String, String> ml = iprof.next();
                //System.out.println("Hoja: " + ++cHojas);
//                System.out.println("|" + ml.get("lodo_row_0_col_0") + " | " + ml.get("lodo_row_0_col_23") + "|");
//                System.out.println("|" + ml.get("lodo_row_2") + " | ");
//                System.out.println("|" + ml.get("lodo_row_3") + " | ");
//                System.out.println("|" + ml.get("lodo_row_4") + " | ");
//                System.out.println("|" + ml.get("lodo_row_5") + " | ");
//                
//                System.out.println("Dens     |" + ml.get("Dens") + "|");
//                System.out.println("Visc     |" + ml.get("Visc") + "|");
//                System.out.println("Temp     |" + ml.get("Temp °C") + "|");
//                System.out.println("Arena    |" + ml.get("%Arena") + "|");
//                System.out.println("Filtrado |" + ml.get("Filtrado") + "|");
//                System.out.println("Calcio   |" + ml.get("Calcio") + "|");
//                System.out.println("Enjarre  |" + ml.get("Enjarre") + "|");
//                System.out.println("Alc      |" + ml.get("Alc. ") + "|");
//                System.out.println("Gel 0    |" + ml.get("Gel 0 ") + "|");
//                System.out.println("Gel 10   |" + ml.get("Gel 10 ") + "|");
//                System.out.println("Cloruros |" + ml.get("Cloruros") + "|");
//                System.out.println("PH       |" + ml.get("PH") + "|");
//                System.out.println("Solidos  |" + ml.get("%Solidos") + "|");
//                System.out.println("Aceite   |" + ml.get("%Aceite") + "|");
//                System.out.println("Agua     |" + ml.get("%Agua") + "|");
//                System.out.println("VA       |" + ml.get("VA") + "|");
//                System.out.println("VP       |" + ml.get("VP") + "|");
//                System.out.println("YP       |" + ml.get("YP") + "|");
//                System.out.println("Emul     |" + ml.get("Emul") + "|");
//                System.out.println("R.A.A.   |" + ml.get("R.A.A.") + "|");
//                System.out.println("MBT      |" + ml.get("MBT") + "|");





            }




            //System.out.println("<------------------------------->");




            Iterator<Informacion> it = infP.getAllInfo().iterator();
            Informacion info = null;
            while (it.hasNext()) {
                info = it.next();

                //System.out.println("1:" +info.getFecha() + " 2:" + info.getActividad()) ;

                if ((isDateActive == true) && (isAdded == true) && (isZeroOrNull(info.getFechaInicio()) == false)) {
                    isDateActive = false;
                    isAdded = false;
                    //System.out.println("1:" +data1 + " 2:" + data2) ;
                    newArrayList.add(new Informacion(data1, data3, data2));
                    //System.out.println( " --> 1 <--") ;
                }
                if ((isZeroOrNull(info.getFechaInicio()) == false) && (isDateActive == false)) {
                    //System.out.println( " --> 2 <--") ;
                    isDateActive = true;
                    data1 = info.getFechaInicio();
                    data3 = info.getFechaFinal();
                    data2 = (isZeroOrNull(info.getActividad())) ? "" : info.getActividad();
                } else {

                    if (isZeroOrNull(info.getFechaInicio()) == true && isDateActive == true) {
                        // System.out.println( " --> 3 <--") ;
                        aux = (isZeroOrNull(info.getActividad())) ? "" : " " + info.getActividad();
                        data2 = data2 + aux;
                        //System.out.println("add: " +data2 ) ;
                        isAdded = true;
                    } else {

                        //*********DEVUELVE LA ACTIVIDAD SI SOLO TIENE UN RENGLON**********
                        if (isZeroOrNull(info.getFechaInicio()) == false && isDateActive == true && isAdded == false) {

                            newArrayList.add(new Informacion(data1, data3, data2));
                            isDateActive = true;
                            data1 = info.getFechaInicio();
                            data3 = info.getFechaFinal();
                            data2 = (isZeroOrNull(info.getActividad())) ? "" : info.getActividad();

                        } else {
                        }
                        // System.out.println( " --> 4 <--") ;
                        //*****************************************************************
                    }
                }

            }
            if (isAdded == true && isDateActive == true) {
                isDateActive = false;
                isAdded = false;
                //System.out.println("1:" +data1 + " 2:" + data2) ;
                newArrayList.add(new Informacion(data1, data3, data2));
                //System.out.println( " --> 1 <--") ;
            }
            //System.out.println("Que valores mantiene : " + isAdded + "...-"+ isDateActive );

            infP.setAllInfo(newArrayList);
            newArrayList = new ArrayList();
            results.add(infP);
            //System.out.println("xxjejexxxxxxxxxxxxxxxxxxxxxxxxxxxx>>xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        }


        return results;
    }

    private int isHeadOfRead(ArrayList contentByColum, int numRow) {
        int cY = 0;
        int isIni = 0;
        int row = -1;
        Iterator iterCols = contentByColum.iterator();
        while (iterCols.hasNext()) {
            String content = new String();
            content = (String) iterCols.next();

            if ((cY == 0) || (cY == 2) || (cY == 8)) {
                if (cY == 0) {
                    if ((content.indexOf("Operaci") != -1) || (content.indexOf("operaci") != -1)) {
                        isIni++;
                        //System.out.println( "0x["+ numRow+"]["+ cY+"] :" + content  );
                    }

                }
                if (cY == 2) {
                    if ((content.indexOf("Pozo:") != -1) || (content.indexOf("pozo:") != -1)) {
                        isIni++;
                        //System.out.println( "2["+ numRow +"]["+ cY+"] :" + content  );
                    }
                }
                if (cY == 8) {
                    if ((content.indexOf("MOVIMIENTO") != -1) || (content.indexOf("movimiento") != -1)
                            || (content.indexOf("TERMINACION") != -1) || (content.indexOf("terminacion") != -1)
                            || (content.indexOf("MANTENIMIENTO") != -1) || (content.indexOf("mantenimiento") != -1)
                            || (content.indexOf("PERFORACION") != -1) || (content.indexOf("perforacion") != -1)
                            || (content.indexOf("SUSPENSION") != -1) || (content.indexOf("suspension") != -1)) {
                        isIni++;
                        //System.out.println( "8["+ numRow +"]["+ cY+"] :" + content  );
                    }
                }
            }
            cY++;
        }
        if (isIni >= 2) {
            row = numRow;
        } else {
            row = -1;
        }

        return row;
    }

    private int isContinueOfRead(ArrayList contentByColum, int numRow) {
        int cY = 0;
        int isIni = 0;
        int row = -1;
        Iterator iterCols = contentByColum.iterator();
        while (iterCols.hasNext()) {
            String content = new String();
            content = (String) iterCols.next();

            if ((cY == 0) || (cY == 2) || (cY == 8)) {
                if (cY == 0) {

                    if ((content.indexOf("Continúa Operación") != -1) || (content.indexOf("Continua Operacion") != -1)
                            || (content.indexOf("CONTINÚA OPERACIÓN") != -1) || (content.indexOf("CONTINUA OPERACION") != -1)) {
                        isIni++;
                        //System.out.println( "8["+ numRow +"]["+ cY+"] :" + content  );
                    }

                }
                if (cY == 3) {
                    if ((content.indexOf("Pozo:") != -1) || (content.indexOf("pozo:") != -1)
                            || (content.indexOf("POZO:") != -1) || (content.indexOf("pozo:") != -1)) {
                        isIni++;

                    }
                }

            }
            cY++;
        }
        if (isIni >= 1) {
            row = numRow;
        } else {
            row = -1;
        }

        return row;
    }

    private int isFootOfRead(ArrayList contentByColum, int numRow) {
        int cY = 0;
        int isFin = 0;

        int row = -1;
        cY = 0;
        Iterator iterCols = contentByColum.iterator();
        while (iterCols.hasNext()) {
            String content = new String();
            content = (String) iterCols.next();

            if ((cY == 0) || (cY == 10)) {
                if (cY == 0) {

                    if ((content.indexOf("Resumen:") != -1) || (content.indexOf("resumen:") != -1)) {
                        isFin++;
                        //System.out.println( "0["+ numRow +"]["+ cY+"] :" + content  );
                    }
                }
                if (cY == 10) {
                    if ((content.indexOf("KIOSCO") != -1) || (content.indexOf("kiosco") != -1)) {
                        isFin++;
                        //System.out.println( "10["+ numRow +"]["+ cY+"] :" + content  );
                    }
                }
            }
            cY++;
        }
        if ((isFin > 1)) {
            row = numRow;
        } else {
            row = -1;
        }
        return row;
    }

    private int isEndContinueOfRead(ArrayList contentByColum, int numRow) {
        int cY = 0;
        int isFin = 0;

        int row = -1;
        cY = 0;
        Iterator iterCols = contentByColum.iterator();
        while (iterCols.hasNext()) {
            String content = new String();
            content = (String) iterCols.next();

            if ((cY == 0) || (cY == 10)) {
                if (cY == 0) {
                    if ((content.indexOf("Observ.:") != -1) || (content.indexOf("OBSERV.:") != -1)) {
                        isFin++;
                        //System.out.println( "0z["+ numRow +"]["+ cY+"] :" + content  );
                    }
                }
            }
            cY++;
        }
        if ((isFin >= 1)) {
            row = numRow;
        } else {
            row = -1;
        }
        return row;
    }

    private ArrayList<ArrayList> getValuesWithInformation(ArrayList aValues) {

        return null;
    }

    private ArrayList getInfoFinal(ArrayList aValuesTo, int beg, int end) {
        ArrayList<ArrayList> results = new ArrayList<ArrayList>();
        ArrayList avalues = new ArrayList();
        Iterator it = aValuesTo.iterator();
        boolean isDate = false;
        boolean isAdded = false;
        boolean isFist = false;
        int cloop = beg;
        int numFirst = 0;
        String data1 = new String();
        String data2 = new String();
        String date = new String();
        String description = new String();
        String aux = new String();
        while (it.hasNext()) {
            ArrayList values = new ArrayList();
            values = (ArrayList) it.next();
            if ((Integer.parseInt(values.get(0).toString()) > beg) && (Integer.parseInt(values.get(0).toString()) <= end)) {
                avalues = (ArrayList) values.get(2);
                date = (String) avalues.get(0);
                description = (String) avalues.get(1);
                //Para saber cual fue la primera fila
                if (isFist == false && isZeroOrNull(date) == false) {
                    numFirst = cloop;
                    isFist = true;
                }
                if ((isDate == true) && (isAdded == true) && (isZeroOrNull(date) == false)) {
                    isDate = false;
                    //System.out.println("1:" +data1 + " 2:" + data2) ;
                    ArrayList<String> dataTwo = new ArrayList<String>();
                    dataTwo.add(data1);
                    dataTwo.add(data2);
                    results.add(dataTwo);
                }
                if ((isZeroOrNull(date) == false) && (isDate == false)) {
                    isDate = true;
                    data1 = date;
                    data2 = (isZeroOrNull(description)) ? "" : description;
                } else {
                    if (isZeroOrNull(date) && isDate == true) {
                        aux = (isZeroOrNull(description)) ? "" : " " + description;
                        data2 = data2 + aux;
                        isAdded = true;
                    }
                }
            }
            cloop++;
        }

        return results;
    }

    private static boolean isZeroOrNull(String date) {
        if ((date.equals("0.0")) || (date.equals("0")) || (date.equals("")) || (date == null)) {
            return true;
        } else {
            return false;
        }
    }

    private static ArrayList<Integer> getNumBegRow(ArrayList aValues) {
        int cY = 0;
        ArrayList<Integer> allData = new ArrayList<Integer>();
        int isIni = 0;
        int isIni2 = 0;
        int numRowBeg = 0;
        Iterator iterValues = aValues.iterator();
        while (iterValues.hasNext()) {
            ArrayList infoAtCol = new ArrayList();
            infoAtCol = (ArrayList) iterValues.next();
            //System.out.println("Numero de renglon: " + infoAtCol.get(0)  );
            //System.out.println("Numero de columnas: " + infoAtCol.get(1)  );
            ArrayList contentByColum = new ArrayList();
            contentByColum = (ArrayList) infoAtCol.get(2);
            Iterator iterCols = contentByColum.iterator();
            cY = 0;

            while (iterCols.hasNext()) {
                String content = new String();
                content = (String) iterCols.next();

                if ((cY == 0) || (cY == 2) || (cY == 8)) {
                    if (cY == 0) {
                        if ((content.indexOf("Operaci") != -1) || (content.indexOf("operaci") != -1)) {
                            isIni++;
                            //System.out.println( "0["+ infoAtCol.get(0) +"]["+ cY+"] :" + content  );
                        }

                        if ((content.indexOf("Continúa Operación") != -1)
                                || (content.indexOf("continúa operación") != -1)
                                || (content.indexOf("continua operacion") != -1)
                                || (content.indexOf("Continua Operacion") != -1)) {
                            isIni2++;
                            //System.out.println( "0ini2["+ infoAtCol.get(0) +"]["+ cY+"] :" + content  );
                        }

                    }
                    if (cY == 2) {
                        if ((content.indexOf("Pozo:") != -1) || (content.indexOf("pozo:") != -1)) {
                            isIni++;
                            //System.out.println( "2["+ infoAtCol.get(0) +"]["+ cY+"] :" + content  );
                        }
                    }
                    if (cY == 3) {
                        if ((content.indexOf("Pozo:") != -1) || (content.indexOf("pozo:") != -1)) {
                            isIni++;
                            //System.out.println( "2Init2["+ infoAtCol.get(0) +"]["+ cY+"] :" + content  );
                        }
                    }

                    if (cY == 8) {
                        if ((content.indexOf("MOVIMIENTO") != -1) || (content.indexOf("movimiento") != -1)) {
                            isIni++;
                            //System.out.println( "8["+ infoAtCol.get(0) +"]["+ cY+"] :" + content  );
                        }
                    }
                }

                cY++;
            }


            //System.out.println("1-->" +isIni+"  2-->"+isIni2 );
            if ((isIni > 2) && (isIni2 == 0)) {
                allData.add(Integer.parseInt(infoAtCol.get(0).toString()));
                isIni = 0;
            }
            if ((isIni2 > 1) && (isIni == 0)) {
                allData.add(Integer.parseInt(infoAtCol.get(0).toString()));
                isIni2 = 0;
            }
        }
        //System.out.println("Renglon de inicio: " + numRowBeg );
        return allData;
    }

    private static ArrayList<Integer> getNumEndRow(ArrayList aValues) {
        int cY = 0;
        ArrayList<Integer> allData = new ArrayList<Integer>();
        int isFin = 0;
        int isFin2 = 0;
        int numRowEnd = 0;
        Iterator iterValues = aValues.iterator();
        while (iterValues.hasNext()) {
            ArrayList infoAtCol = new ArrayList();
            infoAtCol = (ArrayList) iterValues.next();
            //System.out.println("Numero de renglon: " + infoAtCol.get(0)  );
            //System.out.println("Numero de columnas: " + infoAtCol.get(1)  );
            ArrayList contentByColum = new ArrayList();
            contentByColum = (ArrayList) infoAtCol.get(2);
            Iterator iterCols = contentByColum.iterator();
            cY = 0;

            while (iterCols.hasNext()) {
                String content = new String();
                content = (String) iterCols.next();

                if ((cY == 0) || (cY == 10)) {
                    if (cY == 0) {

                        if ((content.indexOf("Resumen:") != -1) || (content.indexOf("resumen:") != -1)) {
                            isFin++;
                            //System.out.println( "0e["+ infoAtCol.get(0) +"]["+ cY+"] :" + content  );
                        }
                        if ((content.indexOf("Observ.:") != -1) || (content.indexOf("observ.:") != -1)) {
                            isFin++;
                            //System.out.println( "0end2["+ infoAtCol.get(0) +"]["+ cY+"] :" + content  );
                        }
                    }

                    if (cY == 10) {
                        if ((content.indexOf("KIOSCO") != -1) || (content.indexOf("kiosco") != -1)) {
                            isFin++;
                            //System.out.println( "10["+ infoAtCol.get(0) +"]["+ cY+"] :" + content  );
                        }
                    }
                }

                cY++;
            }

            if ((isFin > 1) && (isFin2 == 0)) {
                //numRowEnd = (int)infoAtCol.get(0);
                allData.add(Integer.parseInt(infoAtCol.get(0).toString()));
                isFin = 0;
            }
            if ((isFin2 == 1) && (isFin == 0)) {

                allData.add(Integer.parseInt(infoAtCol.get(0).toString()));
                isFin2 = 0;
            }

        }
        //System.out.println("Renglon de fin: " + numRowEnd );
        return allData;
    }

    private String getNameFile(String chair) {
        return chair.substring(((chair.lastIndexOf("\\")) + 1), chair.length());
    }

    private String[] isCia(ArrayList contentByColum, int numRow) {
        String values[] = new String[3];
        int cY = 0;
        int isIni = 0;
        Iterator iterCols = contentByColum.iterator();        
        while (iterCols.hasNext()) {
            String content = new String();
            content = (String) iterCols.next();            
            if ((cY == 0) || (cY == 4) || (cY == 8)) {
                if (cY == 0) {
                    if (((content.indexOf("Cia:") != -1)
                            || (content.indexOf("CIA:") != -1)
                            || (content.indexOf("Cía") != -1)
                            || (content.indexOf("Compañia:") != -1)
                            || (content.indexOf("Compañía:") != -1)
                            || (content.indexOf("Compania::") != -1)
                            || (content.indexOf("Companía::") != -1)
                            || (content.indexOf("COMPANIA::") != -1)
                            || (content.indexOf("COMPANÍA::") != -1)
                            || (content.indexOf("COMPAÑÍA::") != -1)
                            || (content.indexOf("COMPAÑIA:") != -1) || (content.equals("") == true))) {
                        isIni++;
                        //System.out.println( "cia["+ numRow+"]["+ cY+"] :" + content  );
                        values[0] = content;
                    }

                }
                if ((cY == 4)) {
                    if (!(content.equals(""))) {
                        isIni++;
                        //System.out.println( "cia["+ numRow +"]["+ cY+"] :" + content  );
                        values[1] = content;
                    } else {
                        values[1] = "";
                    }
                }
                if ((cY == 8) && (isIni >= 1)) {
                    if ((content.indexOf("Fecha") != -1) || (content.indexOf("FECHA") != -1)) {
                        isIni++;
                        //System.out.println( "cia["+ numRow +"]["+ cY+"] :" + content  );
                        values[2] = content;
                    }
                }
            }
            cY++;
        }
        if (isIni < 2) {
            values = null;
        }

        return values;
    }

    
    private String isDiasProg(ArrayList contentByColum, int numRow){
        String values ="";
        int cY = 0;
        int isIni = 0;
        Iterator iterCols = contentByColum.iterator();        
        while(iterCols.hasNext()){
            String content=new String();
            content=(String) iterCols.next();                       
           
            if(content.indexOf("Dias Prog:")!=-1){
                 isIni++;
                //System.out.println(content.indexOf("Dias Prog:") + " " + content);
            }
            if (cY==6 && isIni==1){  
                //System.out.println("DIAS DEL CONTENT :D " + content + " CY: " + cY);
                values =content;                
            }
            
            cY++;
        }
        return values;
    }

    
     private String isSuppte(ArrayList contentByColum, int numRow){
        String values ="";
        int cY = 0;
        int isIni = 0;
        Iterator iterCols = contentByColum.iterator();        
        while(iterCols.hasNext()){
            String content=new String();
            content=(String) iterCols.next();
            if(content.indexOf("Suptte.")!=-1){
                 isIni++;
            }
            if (cY==9 && isIni==1){
                if (content!=null){
                    values =content;
                }
                 //System.out.println("cONTINUA: " + content.indexOf("Suptte.") + " " + content + " cy: " + cY);
            }
            
            cY++;
        }
        return values;
    }
    
    

    private String[] isFechaOperacion(ArrayList contentByColum, int numRow) {
        String values[] = new String[3];
        int cY = 0;
        int isIni = 0;
        Iterator iterCols = contentByColum.iterator();        
        while (iterCols.hasNext()) {
            String content = new String();
            content = (String) iterCols.next();                
            if ((cY == 0) || (cY == 5) || (cY == 6) || (cY == 7) || (cY == 8)) {
                if (cY == 0) {
                    if ((content.indexOf("REPORTE DIARIO DE OPERACIONES") != -1)
                            || (content.indexOf("reporte diario de operaciones") != -1)
                            || (content.indexOf("Reporte diario de operaciones") != -1)
                            || (content.indexOf("Reporte Diario de Operaciones") != -1)) {
                        isIni++;
                        //System.out.println( "reportediario["+ numRow+"]["+ cY+"] :" + content  );
                    }

                }
                if (cY == 5 && isIni == 1) {
                    if (!(content.equals(""))) {

                        //System.out.println( "reportediario["+ numRow +"]["+ cY+"] :" + content  );
                        values[0] = content;
                    } else {
                        values[0] = "";
                    }

                }

                if (cY == 6) {
                    if ((content.indexOf("Proyecto:") != -1)
                            || (content.indexOf("proyecto") != -1)
                            || (content.indexOf("PROYECTO") != -1)
                            || (content.indexOf("Proyecto") != -1)) {
                        isIni++;
                        //System.out.println( "reportediario["+ numRow+"]["+ cY+"] :" + content  );

                    }

                }
                if (cY == 7 && isIni >= 2) {
                    if (!(content.equals(""))) {
                        //System.out.println( "reportediario["+ numRow +"]["+ cY+"] :" + content  );
                        values[1] = content;
                    } else {
                        values[1] = "";
                    }

                }
                if (cY == 8 && isIni >= 2) {
                    if (!(content.equals(""))) {
                        isIni++;
                        //System.out.println( "reportediario["+ numRow +"]["+ cY+"] :" + content  );
                        values[2] = content;
                    } else {
                        values[2] = "";
                    }
                }
            }
            cY++;
        }
        if (isIni < 2) {
            values = null;
        }

        return values;
    }

    private String[] isEquipo(ArrayList contentByColum, int numRow) {
        String values[] = new String[3];
        int cY = 0;
        int isIni = 0;
        Iterator iterCols = contentByColum.iterator();        
        while (iterCols.hasNext()) {
            String content = new String();
            content = (String) iterCols.next();            
            if ((cY == 0) || (cY == 9) || (cY == 10)) {
                if (cY == 0) {
                    if ((content.indexOf("Equipo:") != -1)
                            || (content.indexOf("EQUIPO:") != -1)) {
                        isIni++;
                        //System.out.println( "equipo["+ numRow+"]["+ cY+"] :" + content  );
                        values[0] = content;
                    }

                }
                if (cY == 9) {
                    if ((content.indexOf("Conductor:") != -1)
                            || (content.indexOf("CONDUCTOR:") != -1)) {
                        isIni++;
                        //System.out.println( "equipo["+ numRow+"]["+ cY+"] :" + content  );
                        values[1] = content;
                    }

                }
                if (cY == 10) {
                    if ((content.indexOf("Inicio:") != -1)
                            || (content.indexOf("INICIO:") != -1)
                            || (content.indexOf("Inicío:") != -1)
                            || (content.indexOf("Inició:") != -1)
                            || (content.indexOf("INICIÓ:") != -1)) {
                        isIni++;
                        //System.out.println( "equipo["+ numRow+"]["+ cY+"] :" + content  );
                        values[2] = content;
                    }

                }

            }
            cY++;
        }
        if (isIni < 2) {
            values = null;
        }

        return values;
    }

    /**
     * getDate
     *
     * @param date | String
     * @return Array String | {[12:00][14:00]} fecha incial y fecha final en ese
     * orden
     */
    public String[] getDate(String date) {
        //System.out.println("Recibe-->" + date);
        String result[] = new String[2];
        int indice = 0;
        indice = date.indexOf("-");
        //System.out.println("indice-->" + indice);
        if (indice == -1) {
            result[0] = date;
            result[1] = date;
            return result;
        }
        result[0] = date.substring(0, indice);
        result[1] = date.substring((indice + 1), date.length());
        return result;
    }

    private boolean isProfund(ArrayList contentByColum, int numRow) {

        String values[] = new String[3];
        int cY = 0;
        int isIni = 0;
        Iterator iterCols = contentByColum.iterator();
        while (iterCols.hasNext()) {

            String content = new String();
            content = (String) iterCols.next();


            if (cY == 0) {
                if (content.compareToIgnoreCase("Profund.") == 0) {

                    isIni++;
                    // System.out.println("Profund[" + numRow + "][" + cY + "] :" + content);

                }

            }
            //							

            if (cY == 1) {
                if (content.compareToIgnoreCase("Desarr.") == 0) {
                    isIni++;
                    //System.out.println("Desarr[" + numRow + "][" + cY + "] :" + content);

                }

            }
            if (cY == 2) {
                if (content.compareToIgnoreCase("Vertical") == 0) {
                    isIni++;
                    //System.out.println("Vertical[" + numRow + "][" + cY + "] :" + content);

                }

            }
            if (cY == 3) {
                if (content.compareToIgnoreCase("Barrenas") == 0) {
                    isIni++;
                    //System.out.println("Barrenas[" + numRow + "][" + cY + "] :" + content);

                }

            }
            if (cY == 4) {
                if (content.compareToIgnoreCase("Actual") == 0) {
                    isIni++;
                    //System.out.println("Actual[" + numRow + "][" + cY + "] :" + content);

                }

            }
            if (cY == 5) {
                if (content.compareToIgnoreCase("Anterior") == 0) {
                    isIni++;
                    //System.out.println("Anterior[" + numRow + "][" + cY + "] :" + content);

                }

            }
            if (cY == 6) {
                if (content.compareToIgnoreCase("Herramienta") == 0 || content.compareToIgnoreCase("Aparejo") == 0) {
                    isIni++;
                    //System.out.println("Herramienta[" + numRow + "][" + cY + "] :" + content);

                }

            }
            if (cY == 8) {
                if (content.compareToIgnoreCase("Cant.") == 0 || content.compareToIgnoreCase("Prof. Cima") == 0) {
                    isIni++;
                    //System.out.println("Cant[" + numRow + "][" + cY + "] :" + content);

                }

            }
            if (cY == 9) {
                if (content.compareToIgnoreCase("Long.") == 0 || content.compareToIgnoreCase("Prof. Base") == 0) {
                    isIni++;
                    //System.out.println("Long[" + numRow + "][" + cY + "] :" + content);

                }

            }
            if (cY == 10) {
                if (content.compareToIgnoreCase("Obs.") == 0) {
                    isIni++;
                    //System.out.println("Obs[" + numRow + "][" + cY + "] :" + content);

                }

            }





            cY++;
        }//CHECA SI VIENEN AL MENOS 5 ETIQUETAS
        if (isIni > 5) {
            return true;
        }

        return false;
    }

    private String isResumeDay(ArrayList contentByColum, int row) {
        String values[] = new String[3];
        int cY = 0;
        int isIni = 0;
        Iterator iterCols = contentByColum.iterator();
        boolean isReader = false;
        String resume = "";
        while (iterCols.hasNext()) {
            String content = new String();
            content = (String) iterCols.next();


            if (cY == 0) {
                if (content.equalsIgnoreCase("Resumen Dia Anterior")) {
                    isReader = true;
                } else {
                    return null;
                }
            }
            if ((isReader) && (cY == 2 || cY == 3 || cY == 4 || cY == 5 || cY == 6 || cY == 7 || cY == 8 || cY == 9 || cY == 10)) {
                resume += content;
            }
            cY++;
        }
//        System.out.println("Day : " + resume);
        return resume;
    }

    private String isResumeFinal(ArrayList contentByColum, int row) {
        String values[] = new String[3];
        int cY = 0;
        int isIni = 0;
        Iterator iterCols = contentByColum.iterator();
        boolean isReader = false;
        String resume = "";
        while (iterCols.hasNext()) {
            String content = new String();
            content = (String) iterCols.next();


            if (cY == 0) {
                if (content.equalsIgnoreCase("Resumen:")) {
                    isReader = true;
                } else {
                    return null;
                }
            }
            if ((isReader) && (cY == 1 || cY == 2 || cY == 3 || cY == 4 || cY == 5 || cY == 6 || cY == 7 || cY == 8 || cY == 9)) {
                resume += content;
            }
            cY++;
        }
//        System.out.println("Final : " + resume);
        return resume;
    }

    private ArrayList<HashMap<String, String>> getValuesForRowSevenDatas(ArrayList contentByColum, int row, ArrayList<HashMap<String, String>> contenedor) {
        String content;
        int cY = 0;

        Iterator iterCols = contentByColum.iterator();
        HashMap<String, String> mapProfund = contenedor.get(0);
        HashMap<String, String> mapBarrenas = contenedor.get(1);
        HashMap<String, String> mapHerramientas = contenedor.get(2);
        HashMap<String, String> mapnucleos = contenedor.get(3);
        HashMap<String, String> maplodo = contenedor.get(4);
        String aux = "";
        String aux1 = "";
        String aux2 = "";
        String aux3 = "";
        String aux4 = "";
        ArrayList<String> labels = new ArrayList();
        labels.add("Dens");
        labels.add("Visc");
        labels.add("Temp °C");
        labels.add("%Arena");
        labels.add("Filtrado");
        labels.add("Calcio");
        labels.add("Enjarre");
        labels.add("Alc. ");
        labels.add("Gel 0 ");
        labels.add("Gel 10 ");
        labels.add("Cloruros");
        labels.add("PH");
        labels.add("%Solidos");
        labels.add("%Aceite");
        labels.add("%Agua");
        labels.add("VA");
        labels.add("VP");
        labels.add("YP");
        labels.add("Emul");
        labels.add("R.A.A.");
        labels.add("MBT");



        while (iterCols.hasNext()) {
            content = (String) iterCols.next();

            //System.out.println(" ROW " + row + " cY " + cY );

            if (row > 1) {
                switch (row) {
                    case 2:
                        if (cY == 0) {
                            /*
                             * 00:00
                             */
                        }
                        if (cY == 1) {
                            mapProfund.put("profund_00_desarrollo", content);
                        }
                        if (cY == 2) {
                            mapProfund.put("profund_00_vertical", content);
                        }
                        if (cY == 3) {
                            /*
                             * marca
                             */
                        }
                        if (cY == 4) {
                            mapBarrenas.put("barrena_marca_actual", content);
                        }
                        if (cY == 5) {
                            mapBarrenas.put("barrena_marca_anterior", content);
                        }
                        if (cY == 6) {
                            mapHerramientas.put("herramienta_reng_1", content);
                        }
                        if (cY == 8) {
                            mapHerramientas.put("herramienta_cant_reng_1", content);
                        }
                        if (cY == 9) {
                            mapHerramientas.put("herramienta_long_reng_1", content);
                        }
                        if (cY == 10) {
                            mapHerramientas.put("herramienta_obs_reng_1", content);
                        }
                        break;
                    case 3:
                        if (cY == 0) {
                        }
                        if (cY == 1) {
                            mapProfund.put("profund_24_desarrollo", content);
                        }
                        if (cY == 2) {
                            mapProfund.put("profund_24_vertical", content);
                        }
                        if (cY == 3) {
                        }
                        //SE LEE LA SERIE DE LA BARRENA
                        if (cY == 4) {
                            //System.out.println("VALOR: " + content);
                            mapBarrenas.put("barrena_serie_actual", content);
                        }
                        if (cY == 5) {
                            mapBarrenas.put("barrena_serie_anterior", content);
                            //System.out.println("VALOR: " + content);
                        }
                        if (cY == 6) {
                            mapHerramientas.put("herramienta_reng_2", content);
                        }
                        if (cY == 8) {
                            mapHerramientas.put("herramienta_cant_reng_2", content);
                        }
                        if (cY == 9) {
                            mapHerramientas.put("herramienta_long_reng_2", content);
                        }
                        if (cY == 10) {
                            mapHerramientas.put("herramienta_obs_reng_2", content);
                        }
                        break;
                    case 4:
                        if (cY == 0) {
                        }
                        if (cY == 1) {
                            mapProfund.put("profund_05_desarrollo", content);
                        }
                        if (cY == 2) {
                            mapProfund.put("profund_05_vertical", content);
                        }
                        if (cY == 3) {
                        }
                        if (cY == 4) {
                            mapBarrenas.put("barrena_diametro_actual", content);
                        }
                        if (cY == 5) {
                            mapBarrenas.put("barrena_diametro_anterior", content);
                        }
                        if (cY == 6) {
                            mapHerramientas.put("herramienta_reng_3", content);
                        }
                        if (cY == 8) {
                            mapHerramientas.put("herramienta_cant_reng_3", content);
                        }
                        if (cY == 9) {
                            mapHerramientas.put("herramienta_long_reng_3", content);
                        }
                        if (cY == 10) {
                            mapHerramientas.put("herramienta_obs_reng_3", content);
                        }

                        break;
                    case 5:
                        if (cY == 0) {
                        }
                        if (cY == 1) {
                            mapProfund.put("profund_ava_ind_desarrollo", content);
                        }
                        if (cY == 2) {
                            mapProfund.put("profund_ava_ind_vertical", content);
                        }
                        if (cY == 3) {
                        }
                        if (cY == 4) {
                            mapBarrenas.put("barrena_tipo_actual", content);
                        }
                        if (cY == 5) {
                            mapBarrenas.put("barrena_tipo_anterior", content);
                        }
                        if (cY == 6) {
                            mapHerramientas.put("herramienta_reng_4", content);
                        }
                        if (cY == 8) {
                            mapHerramientas.put("herramienta_cant_reng_4", content);
                        }
                        if (cY == 9) {
                            mapHerramientas.put("herramienta_long_reng_4", content);
                        }
                        if (cY == 10) {
                            mapHerramientas.put("herramienta_obs_reng_4", content);
                        }
                        break;
                    case 6:
                        if (cY == 0) {
                            /*
                             * ava/ind
                             */
                        }
                        if (cY == 1) {
                            mapProfund.put("profund_ult_cont_desarrollo", content);
                        }
                        if (cY == 2) {
                            mapProfund.put("profund_ult_cont_vertical", content);
                        }
                        if (cY == 3) {
                            /*
                             * toberas
                             */
                        }
                        if (cY == 4) {
                            mapBarrenas.put("barrena_toberas_actual", content);
                        }
                        if (cY == 5) {
                            mapBarrenas.put("barrena_toberas_anterior", content);
                        }
                        if (cY == 6) {
                            mapHerramientas.put("herramienta_reng_5", content);
                        }
                        if (cY == 8) {
                            mapHerramientas.put("herramienta_cant_reng_5", content);
                        }
                        if (cY == 9) {
                            mapHerramientas.put("herramienta_long_reng_5", content);
                        }
                        if (cY == 10) {
                            mapHerramientas.put("herramienta_obs_reng_5", content);
                        }
                        break;
                    case 7:
                        if (cY == 0) {
                            /*
                             * prox.cont
                             */
                        }
                        if (cY == 1) {
                            mapProfund.put("profund_prox_count_desarrollo", content);
                        }
                        if (cY == 2) {
                            mapProfund.put("profund_prox_count_vertical", content);
                        }
                        if (cY == 3) {
                            /*
                             * marca
                             */
                        }
                        if (cY == 4) {
                            mapBarrenas.put("barrena_mts_perf_actual", content);
                        }
                        if (cY == 5) {
                            mapBarrenas.put("barrena_mts_perf_anterior", content);
                        }
                        if (cY == 6) {
                            mapHerramientas.put("herramienta_reng_6", content);
                        }
                        if (cY == 8) {
                            mapHerramientas.put("herramienta_cant_reng_6", content);
                        }
                        if (cY == 9) {
                            mapHerramientas.put("herramienta_long_reng_6", content);
                        }
                        if (cY == 10) {
                            mapHerramientas.put("herramienta_obs_reng_6", content);
                        }
                        break;
                    case 8:
                        if (cY == 0) {
                            /*
                             * Litologia no esta marcado
                             */
                        }
                        if (cY == 1) {
                        }
                        if (cY == 2) {
                        }
                        if (cY == 3) {
                        }
                        if (cY == 4) {
                            mapBarrenas.put("barrena_hors_operacion_actual", content);

                        }
                        if (cY == 5) {
                            mapBarrenas.put("barrena_hors_operacion_anterior", content);

                        }
                        if (cY == 6) {
                            mapHerramientas.put("herramienta_reng_7", content);
                        }
                        if (cY == 8) {
                            mapHerramientas.put("herramienta_cant_reng_7", content);
                        }
                        if (cY == 9) {
                            mapHerramientas.put("herramienta_long_reng_7", content);
                        }
                        if (cY == 10) {
                            mapHerramientas.put("herramienta_obs_reng_7", content);
                        }
                        break;
                    case 9:
                        if (cY == 0) {
                        }
                        if (cY == 1) {
                        }
                        if (cY == 2) {
                        }
                        if (cY == 3) {
                            /*
                             * prom x m
                             */
                        }
                        if (cY == 4) {
                            mapBarrenas.put("barrena_prom_x_m_actual", content);
                        }
                        if (cY == 5) {
                            mapBarrenas.put("barrena_prom_x_m_anterior", content);
                        }
                        if (cY == 6) {
                            mapHerramientas.put("herramienta_reng_8", content);
                        }
                        if (cY == 8) {
                            mapHerramientas.put("herramienta_cant_reng_8", content);
                        }
                        if (cY == 9) {
                            mapHerramientas.put("herramienta_long_reng_8", content);
                        }
                        if (cY == 10) {
                            mapHerramientas.put("herramienta_obs_reng_8", content);
                        }
                        break;
                    case 10:
                        if (cY == 0) {
                            /*
                             * 00:00
                             */
                        }
                        if (cY == 1) {
                        }
                        if (cY == 2) {
                        }
                        if (cY == 3) {
                            /*
                             * marca
                             */
                        }
                        if (cY == 4) {
                            mapBarrenas.put("barrena_3_ult_mts_actual", content);
                        }
                        if (cY == 5) {
                            mapBarrenas.put("barrena_3_ult_mts_anterior", content);
                        }
                        if (cY == 6) {
                            mapHerramientas.put("herramienta_reng_9", content);
                        }
                        if (cY == 8) {
                            mapHerramientas.put("herramienta_cant_reng_9", content);
                        }
                        if (cY == 9) {
                            mapHerramientas.put("herramienta_long_reng_9", content);
                        }
                        if (cY == 10) {
                            mapHerramientas.put("herramienta_obs_reng_9", content);
                        }
                        break;
                    case 11:
                        if (cY == 0) {
                        }
                        if (cY == 1) {
                        }
                        if (cY == 2) {
                        }
                        if (cY == 3) {
                            /*
                             * costo x m
                             */
                        }
                        if (cY == 4) {
                            mapBarrenas.put("barrena_costo_x_m_actual", content);
                        }
                        if (cY == 5) {
                            mapBarrenas.put("barrena_costo_x_m_anterior", content);
                        }
                        if (cY == 6) {
                            mapHerramientas.put("herramienta_reng_10", content);
                        }
                        if (cY == 8) {
                            mapHerramientas.put("herramienta_cant_reng_10", content);
                        }
                        if (cY == 9) {
                            mapHerramientas.put("herramienta_long_reng_10", content);
                        }
                        if (cY == 10) {
                            mapHerramientas.put("herramienta_obs_reng_10", content);
                        }
                        break;
                    case 12:
                        if (cY == 0) {
                        }
                        if (cY == 1) {
                        }
                        if (cY == 2) {
                        }
                        if (cY == 3) {
                            /*
                             * amp inic
                             */
                        }
                        if (cY == 4) {
                            mapBarrenas.put("barrena_amp_inic_actual", content);
                        }
                        if (cY == 5) {
                            mapBarrenas.put("barrena_amp_inic_anterior", content);
                        }
                        if (cY == 6) {
                            mapHerramientas.put("herramienta_reng_11", content);
                        }
                        if (cY == 8) {
                            mapHerramientas.put("herramienta_cant_reng_11", content);
                        }
                        if (cY == 9) {
                            mapHerramientas.put("herramienta_long_reng_11", content);
                        }
                        if (cY == 10) {
                            mapHerramientas.put("herramienta_obs_reng_11", content);
                        }
                        break;
                    case 13:
                        boolean isOkToRead = true;
                        if (cY == 0) {
                        }
                        if (cY == 1) {
                            mapProfund.put("profund_ultima_tr_desarrollo", content);
                        }
                        if (cY == 2) {
                            mapProfund.put("profund_ultima_tr_vertical", content);
                        }
                        if (cY == 3) {
                        }
                        if (cY == 4) {
                            mapBarrenas.put("barrena_des_AIDC_actual", content);
                        }
                        if (cY == 5) {
                            mapBarrenas.put("barrena_des_AIDC_anterior", content);
                        }
                        if (cY == 6) {
                            if (content.equalsIgnoreCase("Ult. Reg.") || content.equalsIgnoreCase("Ult.Reg.") || content.equalsIgnoreCase("Ult.  Reg.")) {
                                isOkToRead = false;
                            }
                            if (isOkToRead) {
                                mapHerramientas.put("herramienta_reng_12", content);
                            }

                        }
                        if (cY == 8) {
                            if (isOkToRead) {
                                 //herramienta_long_reng_
                                mapHerramientas.put("herramienta_cant_reng_12", content);
                            }

                        }
                        if (cY == 9) {
                            if (isOkToRead) {
                                mapHerramientas.put("herramienta_long_reng_12", content);
                            }
                        }
                        if (cY == 10) {
                            if (isOkToRead) {
                                mapHerramientas.put("herramienta_obs_reng_12", content);
                            }
                        }
                        break;
                    case 14:
                        if (cY == 0) {
                        }
                        if (cY == 1) {
                            mapProfund.put("profund_prox_tr_desarrollo", content);
                        }
                        if (cY == 2) {
                            mapProfund.put("profund_prox_tr_vertical", content);
                        }
                        if (cY == 3) {
                            if (content.equalsIgnoreCase("Nucleos R.") || content.equalsIgnoreCase("Nucleos  R.") || content.equalsIgnoreCase("NucleosR.")) {
                                mapnucleos.put("nucleos_r_row_0_col_0", content);
                            }
                        }
                        if (cY == 4) {
                            if (content.equalsIgnoreCase("Int. Prog.") || content.equalsIgnoreCase("Int.  Prog.") || content.equalsIgnoreCase("Int.Prog.")) {
                                mapnucleos.put("int_prog_row_0_col_1", content);
                            }
                        }
                        if (cY == 5) {
                            if (content.equalsIgnoreCase("Int.Disp.") || content.equalsIgnoreCase("Int. Disp.") || content.equalsIgnoreCase("Int.  Disp.")) {
                                mapnucleos.put("int_disp_row_0_col_2", content);
                            }
                        }
                        if (cY == 6) {
                        }
                        if (cY == 8) {
                        }
                        if (cY == 9) {
                        }
                        if (cY == 10) {
                        }

                        break;
                    case 15:
                        if (cY == 0) {
                        }
                        if (cY == 1) {
                            mapProfund.put("profund_b_l_desarrollo", content);
                        }
                        if (cY == 2) {
                            mapProfund.put("profund_b_l_vertical", content);
                        }
                        if (cY == 3) {
                            mapnucleos.put("nucleos_r_row_1_col_0", content);
                        }
                        if (cY == 4) {
                            mapnucleos.put("int_prog_row_1_col_1", content);
                        }
                        if (cY == 5) {
                            mapnucleos.put("int_disp_row_1_col_2", content);
                        }
                        if (cY == 6) {
                        }
                        if (cY == 8) {
                        }
                        if (cY == 9) {
                        }
                        if (cY == 10) {
                        }
                        break;
                    case 16:
                        if (cY == 0) {
                        }
                        if (cY == 1) {
                        }
                        if (cY == 2) {
                        }
                        if (cY == 3) {
                            mapnucleos.put("nucleos_r_row_2_col_0", content);
                        }
                        if (cY == 4) {
                            mapnucleos.put("int_prog_row_2_col_1", content);
                        }
                        if (cY == 5) {
                            mapnucleos.put("int_disp_row_2_col_2", content);
                        }
                        if (cY == 6) {
                        }
                        if (cY == 8) {
                        }
                        if (cY == 9) {
                        }
                        if (cY == 10) {
                        }
                        break;
                    case 17:
                        if (cY == 0) {
                        }
                        if (cY == 1) {
                            mapProfund.put("profund_c2_desarrollo", content);
                        }
                        if (cY == 2) {
                            mapProfund.put("profund_c2_vertical", content);
                        }
                        if (cY == 3) {
                            mapnucleos.put("nucleos_r_row_3_col_0", content);
                        }
                        if (cY == 4) {
                            mapnucleos.put("int_prog_row_3_col_1", content);
                        }
                        if (cY == 5) {
                            mapnucleos.put("int_disp_row_3_col_2", content);
                        }
                        if (cY == 6) {
                        }
                        if (cY == 8) {
                        }
                        if (cY == 9) {
                        }
                        if (cY == 10) {
                        }
                        break;
                    case 18:

                        if (cY == 0) {
                            maplodo.put("lodo_row_0_col_0", content);
                        }
                        if (cY == 1) {
                            aux += content;
                        }
                        if (cY == 2) {
                            aux += content;
                            maplodo.put("lodo_row_0_col_23", aux);


                        }
                        if (cY == 3) {
                            mapnucleos.put("nucleos_r_row_4_col_0", content);
                        }
                        if (cY == 4) {
                            mapnucleos.put("int_prog_row_4_col_1", content);
                        }
                        if (cY == 5) {
                            mapnucleos.put("int_disp_row_4_col_2", content);
                        }
                        if (cY == 6) {
                        }
                        if (cY == 8) {
                        }
                        if (cY == 9) {
                        }
                        if (cY == 10) {
                        }
                        break;
                    case 19:
                        if (cY == 0) {
                            aux1 += content;
                        }
                        if (cY == 1) {
                            aux1 += content;
                        }
                        if (cY == 2) {
                            aux1 += content;
                            maplodo.put("lodo_row_2", aux1);
                            String label = "";
                            String value = "";
                            ArrayList<String> twoValues = null;
                            String[] result = aux1.split(":");

                            for (int x = 0; x < result.length; x++) {
                                if (x == 0) {
                                    label = result[x].trim();
                                } else {
                                    twoValues = isListLabelsPosition(labels, result[x]);
                                    if (x == result.length - 1) {
                                        /*ultimo*/
                                        if (label.length() > 0) {
                                            if (!twoValues.isEmpty()) {
                                                value = twoValues.get(0);
                                                //System.out.println( label + ": " + value);
                                                maplodo.put(label, value);
                                                label = twoValues.get(1);
                                                //System.out.println( label + ": N/I" );  
                                                maplodo.put(label, "");
                                            } else {
                                                //System.out.println( label + ": " + result[x]);  
                                                maplodo.put(label, result[x]);
                                            }
                                        } else {
                                            /*No ahi informacion entendible*/
                                        }
                                    } else {

                                        if (label.length() > 0) {
                                            if (!twoValues.isEmpty()) {
                                                value = twoValues.get(0);
                                                //System.out.println(label + ": " + value);
                                                maplodo.put(label, value);
                                                label = twoValues.get(1);
                                            } else {
                                                //System.out.println(label + ": " + result[x]);
                                                maplodo.put(label, result[x]);
                                            }
                                        } else {
                                        }
                                    }
                                }

                            }



                        }
                        if (cY == 3) {
                            mapnucleos.put("nucleos_r_row_4_col_0", content);
                        }
                        if (cY == 4) {
                            mapnucleos.put("int_prog_row_4_col_1", content);
                        }
                        if (cY == 5) {
                            mapnucleos.put("int_disp_row_4_col_2", content);
                        }
                        if (cY == 6) {
                        }
                        if (cY == 8) {
                        }
                        if (cY == 9) {
                        }
                        if (cY == 10) {
                        }
                        break;
                    case 20:

                        if (cY == 0) {
                            aux2 += content;

                        }
                        if (cY == 1) {
                            aux2 += content;

                        }
                        if (cY == 2) {
                            aux2 += content;
                            maplodo.put("lodo_row_3", aux2);
                            String label = "";
                            String value = "";
                            ArrayList<String> twoValues = null;
                            String[] result = aux2.split(":");

                            for (int x = 0; x < result.length; x++) {
                                if (x == 0) {
                                    label = result[x].trim();
                                } else {
                                    twoValues = isListLabelsPosition(labels, result[x]);
                                    if (x == result.length - 1) {
                                        /*ultimo*/
                                        if (label.length() > 0) {
                                            if (!twoValues.isEmpty()) {
                                                value = twoValues.get(0);
//                                               System.out.println( label + ": " + value);
                                                maplodo.put(label, value);
                                                label = twoValues.get(1);
//                                               System.out.println( label + ": N/I" );  
                                                maplodo.put(label, "");
                                            } else {
//                                                 System.out.println( label + ": " + result[x]);  
                                                maplodo.put(label, result[x]);
                                            }
                                        } else {
                                            /*No ahi informacion entendible*/
                                        }
                                    } else {

                                        if (label.length() > 0) {
                                            if (!twoValues.isEmpty()) {
                                                value = twoValues.get(0);
//                                                System.out.println(label+":2 " + value);
                                                maplodo.put(label, value);
                                                label = twoValues.get(1);
                                            } else {
//                                                 System.out.println(label + "3: " + result[x]);
                                                maplodo.put(label, result[x]);
                                            }
                                        } else {
                                        }
                                    }
                                }

                            }


                        }
                        if (cY == 3) {
                            mapnucleos.put("nucleos_r_row_4_col_0", content);
                        }
                        if (cY == 4) {
                            mapnucleos.put("int_prog_row_4_col_1", content);
                        }
                        if (cY == 5) {
                            mapnucleos.put("int_disp_row_4_col_2", content);
                        }
                        if (cY == 6) {
                        }
                        if (cY == 8) {
                        }
                        if (cY == 9) {
                        }
                        if (cY == 10) {
                        }
                        break;
                    case 21:

                        if (cY == 0) {
                            aux3 += content;

                        }
                        if (cY == 1) {
                            aux3 += content;

                        }
                        if (cY == 2) {
                            aux3 += content;
                            maplodo.put("lodo_row_4", aux3);

                            String label = "";
                            String value = "";
                            ArrayList<String> twoValues = null;
                            String[] result = aux3.split(":");
                            for (int x = 0; x < result.length; x++) {
                                if (x == 0) {
                                    label = result[x].trim();
                                } else {
                                    twoValues = isListLabelsPosition(labels, result[x]);
                                    if (x == result.length - 1) {
                                        /*ultimo*/
                                        if (label.length() > 0) {
                                            if (!twoValues.isEmpty()) {
                                                value = twoValues.get(0);
                                                //System.out.println( label + ": " + value);
                                                maplodo.put(label, value);
                                                label = twoValues.get(1);
                                                //System.out.println( label + ": N/I" );  
                                                maplodo.put(label, "");
                                            } else {
                                                //System.out.println( label + ": " + result[x]);  
                                                maplodo.put(label, result[x]);
                                            }
                                        } else {
                                            /*No ahi informacion entendible*/
                                        }
                                    } else {

                                        if (label.length() > 0) {
                                            if (!twoValues.isEmpty()) {
                                                value = twoValues.get(0);
                                                //System.out.println(label + ": " + value);
                                                maplodo.put(label, value);
                                                label = twoValues.get(1);
                                            } else {
                                                //System.out.println(label + ": " + result[x]);
                                                maplodo.put(label, result[x]);
                                            }
                                        } else {
                                        }
                                    }
                                }

                            }

                        }
                        if (cY == 3) {
                            mapnucleos.put("nucleos_r_row_4_col_0", content);
                        }
                        if (cY == 4) {
                            mapnucleos.put("int_prog_row_4_col_1", content);
                        }
                        if (cY == 5) {
                            mapnucleos.put("int_disp_row_4_col_2", content);
                        }
                        if (cY == 6) {
                        }
                        if (cY == 8) {
                        }
                        if (cY == 9) {
                        }
                        if (cY == 10) {
                        }
                        break;
                    case 22:

                        if (cY == 0) {
                            aux4 += content;

                        }
                        if (cY == 1) {
                            aux4 += content;

                        }
                        if (cY == 2) {
                            aux4 += content;
                            maplodo.put("lodo_row_5", aux4);

                            String label = "";
                            String value = "";
                            ArrayList<String> twoValues = null;
                            String[] result = aux4.split(":");
                            for (int x = 0; x < result.length; x++) {
                                if (x == 0) {
                                    label = result[x].trim();
                                } else {
                                    twoValues = isListLabelsPosition(labels, result[x]);
                                    if (x == result.length - 1) {
                                        /*ultimo*/
                                        if (label.length() > 0) {
                                            if (!twoValues.isEmpty()) {
                                                value = twoValues.get(0);
//                                               System.out.println( label + ":1 " + value);
                                                maplodo.put(label, value);
                                                label = twoValues.get(1).trim();
//                                               System.out.println( label + ":2 N/I" );  
                                                maplodo.put(label, "");
                                            } else {
//                                                 System.out.println( label + ":3 " + result[x]);  
                                                maplodo.put(label, result[x]);
                                            }
                                        } else {
                                            /*No ahi informacion entendible*/
                                        }
                                    } else {

                                        if (label.length() > 0) {
                                            if (!twoValues.isEmpty()) {
                                                value = twoValues.get(0);
//                                                System.out.println(label + ":4 " + value);
                                                maplodo.put(label, value);
                                                label = twoValues.get(1).trim();
                                            } else {
//                                                 System.out.println(label + ":5 " + result[x]);
                                                maplodo.put(label, result[x]);
                                            }
                                        } else {
                                        }
                                    }
                                }

                            }


                        }
                        if (cY == 3) {
                            mapnucleos.put("nucleos_r_row_4_col_0", content);
                        }
                        if (cY == 4) {
                            mapnucleos.put("int_prog_row_4_col_1", content);
                        }
                        if (cY == 5) {
                            mapnucleos.put("int_disp_row_4_col_2", content);
                        }
                        if (cY == 6) {
                        }
                        if (cY == 8) {
                        }
                        if (cY == 9) {
                        }
                        if (cY == 10) {
                        }
                        break;
                    default:
                        break;


                }





            }
            cY++;
        }
        contenedor.add(0, mapProfund);
        contenedor.add(1, mapBarrenas);
        contenedor.add(2, mapHerramientas);
        contenedor.add(3, mapnucleos);
        contenedor.add(4, maplodo);

        return contenedor;
    }

    private boolean isListLabels(ArrayList<String> labels, String labelToSearch) {
        Iterator<String> iter = labels.iterator();
        boolean isHere = false;
        String element = "";
        while (iter.hasNext()) {
            element = iter.next();
            //System.out.println(element + " com:pa " + labelToSearch);
            if (element.equalsIgnoreCase(labelToSearch)) {
                isHere = true;
                break;
            }
        }
        return isHere;
    }

    private ArrayList isListLabelsPosition(ArrayList<String> labels, String labelToSearch) {
        Iterator<String> iter = labels.iterator();
        ArrayList results = new ArrayList();
        String element = "";
        int position = -1;
        while (iter.hasNext()) {
            element = iter.next();
//           System.out.println(element + " <> " + labelToSearch);
            if ((position = labelToSearch.indexOf(element)) > -1) {
//                System.out.println(" atratar ---> " + labelToSearch);
//                System.out.println(" Position ---> " + position);
//                System.out.println(" substroi ---> " + labelToSearch.substring(0, position-1));
//                System.out.println(" element ---> " + element);
                results.add(0, labelToSearch.substring(0, position - 1));
                results.add(1, element);
                break;
            }
        }
        return results;
    }
    private boolean isFullOfData(HashMap<String, String> map) {        
        String aLabels[] = new String[12];
        aLabels[0] = "herramienta_reng_1";
        aLabels[1] = "herramienta_reng_2";
        aLabels[2] = "herramienta_reng_3";
        aLabels[3] = "herramienta_reng_4";
        aLabels[4] = "herramienta_reng_5";
        aLabels[5] = "herramienta_reng_6";
        aLabels[6] = "herramienta_reng_7";
        aLabels[7] = "herramienta_reng_8";
        aLabels[8] = "herramienta_reng_9";
        aLabels[9] = "herramienta_reng_10";
        aLabels[10] = "herramienta_reng_11";
        aLabels[11] = "herramienta_reng_12";        
        boolean isFull =  false;
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            if( aLabels[0].equalsIgnoreCase(key) || 
                aLabels[1].equalsIgnoreCase(key) ||
                aLabels[2].equalsIgnoreCase(key) ||
                aLabels[3].equalsIgnoreCase(key) ||
                aLabels[4].equalsIgnoreCase(key) ||
                aLabels[5].equalsIgnoreCase(key) ||
                aLabels[6].equalsIgnoreCase(key) ||
                aLabels[7].equalsIgnoreCase(key) ||
                aLabels[8].equalsIgnoreCase(key) ||
                aLabels[9].equalsIgnoreCase(key) ||
                aLabels[10].equalsIgnoreCase(key) ||
                aLabels[11].equalsIgnoreCase(key)){                      
                if( map.get(key).toString().length() > 0  ){
                    isFull = true;
                }
            }                         
        }
        return isFull;
    }
}
