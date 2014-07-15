package com.stin.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.stin.Siop;
import com.stin.dbaccess.SiopDAO;
import com.stin.model.Informacion;
import com.stin.model.InformacionPozos;
import com.stin.util.SiopUtils;

public class LeerExcel {
	
	private String fileName;

    public LeerExcel(String fileName) {
        this.fileName = fileName;
    }

    public String excutedSIOPReader(int idIntervencion) {
        //logger.info("Iniciando metodo excutedSIOPReader") ;        
        String res = "";
        //Variables necesarias para el cambio de etapa (TR)
        String proxTrd = "iniciar";
        String proxTrv = "iniciar";
        int contadorTR = 0;

        //Verificar si el archivo se leyo previamente
        try {
            //SiopUtils myUtils;
            String[] splitArray;

            ReadFile obj = new ReadFile(fileName);
            ArrayList array = new ArrayList();
            array = obj.getAllInfoFile();
            ArrayList array1 = new ArrayList();
            array1 = obj.getResults(array);
            ArrayList array2 = new ArrayList();
            array2 = obj.getFinalResults(array1);
            Iterator<InformacionPozos> i = array2.iterator();

            //Contiene toda la informacion del pozo
            InformacionPozos infP = null;
            StringBuilder plataforma=new StringBuilder("");
            int cont = 1;
            if (i.hasNext()) {
                while (i.hasNext()) {
                    infP = i.next();
                    //System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                    //*****************************INSERTA INFORMACION DE CABEZERA***********************************                
                    //Compañia
                    String compañia = SiopUtils.validateInfo(SiopUtils.splitFcn(infP.getCia(), ":", false));
                    //Complemento
                    String Complemento = infP.getCia_Complement().toUpperCase().trim();
                    //Fecha de Impresion del SIOP
                    splitArray = SiopUtils.splitFcn(infP.getCia_Fecha().trim(), " ", false);
                    String fechaSiop = splitArray[1];
                    //Fecha del OPERACION
                    String fechaOperacion = infP.getReportediario_Fecha();
                    //Proyecto:
                    String proyecto = infP.getReportediario_proyecto();
                    //Complemento
                    String complementoDiaro = infP.getReportediario_complemento();
                    //Equipo
                    splitArray = SiopUtils.splitFcn(infP.getEquipo(), " ", false);
                    String equipo = splitArray[1].trim();
                   
                    if (equipo.charAt(0) == '4') {
                        equipo = equipo + " - PEMEX";
                    } else {
                        
                        plataforma.delete(0,plataforma.length());
                        String arr[] = Complemento.split("\\) -");
                        if (arr.length > 1) {// VERIFICA SI EXISTE CUMPLE CON LA NOMENCLATURA
                            
                            for(int ii=1;ii<arr.length;ii++){
                                plataforma.append(arr[ii]+"");
                            }
                            equipo = equipo + " - " +arr[0] + ")";
                            
                            Complemento = plataforma.toString();//substring(3).replaceAll(" ","");
                        }
                    }    
                    //System.out.println("PLATAFORMA: " + Complemento + " ,equipo: " + equipo + " ,DIA: " + fechaOperacion );
                  
                    
                    //Equipo-Conductor
                    splitArray = SiopUtils.splitFcn(infP.getEquipo_conductor().trim(), " ", false);
                    ArrayList cleanList = SiopUtils.RemoveEmptyElements(splitArray, false);
                    ArrayList elements = SiopUtils.findListElement(cleanList);
                    String conductor = elements.get(0).toString().trim();
                    String espRotNM = elements.get(1).toString().trim();
                    String actividad = elements.get(2).toString().trim();
                    //Equipo-Inicio
                    splitArray = SiopUtils.splitFcn(infP.getEquipo_inicio(), ":", false);
                    String equipoFechaInicio = splitArray[1].toString().trim();
                    //Pozo
                    splitArray = SiopUtils.splitFcn(infP.getPozo(), ":", false);                    
                    //Insert SIOP Header

                    String resumenAnt = infP.getResumenDiaAnterior().toUpperCase().trim().replace("'", "-");
                    String resumen = infP.getResumenfin().toUpperCase().trim().replace("'", "-");
                    String dias = infP.getDiasProg();

                    double dd = Double.parseDouble(dias);
                    int diasProg = (int) dd;
                    String suptte =infP.getSuptte();

                    //System.out.println("SUPPER: " + infP.getSuptte() + " Opera " + fechaOperacion + " diasPro: " + diasProg);

                    //System.out.println(infP.getCia() + " " + infP.getCia_Fecha() + " " + infP.getFecha() +" " +infP.getReportediario_Fecha() + " " + infP.getDiasProg());                

                    Siop lastSiopID = SiopDAO.insertSiopCabezera(compañia, Complemento, fechaSiop, fechaOperacion, proyecto, complementoDiaro, equipo, conductor, espRotNM, actividad, equipoFechaInicio, idIntervencion, resumenAnt, resumen, cont, diasProg, suptte);
                    //myDAO.commit();
                    //*****************************INSERTA INFORMACION DE CABEZERA***********************************
                    //*****************************Retrive last inserted id***********************************             

                    //int lastSiopID = myDAO.getLastSiopId();

                    //System.out.println("Ultimo registro: " + lastSiopID.getIntSiopConsec());
                    //*****************************Retrive last inserted id***********************************
                    //*****************************INSERTA INFORMACION DE PROFUNDIDAD*************************
                    Iterator<HashMap<String, String>> iprof = infP.getProfundidadPorHoja().iterator();
                    while (iprof.hasNext()) {
                        HashMap<String, String> mp = iprof.next();
                        SiopDAO.insertSiopProfundidad(lastSiopID, "DESARROLLO", Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mp.get("profund_00_desarrollo"))), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mp.get("profund_24_desarrollo"))), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mp.get("profund_05_desarrollo"))), mp.get("profund_ava_ind_desarrollo").trim(), SiopUtils.verificaVacio(mp.get("profund_ult_cont_desarrollo")).trim(), SiopUtils.verificaVacio(mp.get("profund_prox_count_desarrollo")).trim());
                        SiopDAO.insertSiopProfundidad(lastSiopID, "VERTICAL", Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mp.get("profund_00_vertical"))), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mp.get("profund_24_vertical"))), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mp.get("profund_05_vertical"))), mp.get("profund_ava_ind_desarrollo").trim(), SiopUtils.verificaVacio(mp.get("profund_ult_cont_vertical")).trim(), SiopUtils.verificaProfundidadDesarrollo(mp.get("profund_prox_count_vertical")).trim());
                        //*****************************INSERTA INFORMACION TR *************************
                        if (cont == 1) {
                            proxTrd = mp.get("profund_ultima_tr_desarrollo").toString().trim();
                            proxTrv = mp.get("profund_ultima_tr_vertical").toString().trim();
                            contadorTR = 0;
                        }
                        if (SiopUtils.verificaTR(proxTrd, proxTrv, mp.get("profund_ultima_tr_desarrollo").toString(), mp.get("profund_ultima_tr_vertical").toString())) {
                            contadorTR++;
                            //La misma TR

                        } else {
                            contadorTR = 1;
                            //Cambio de Etapa(Presumiblemente)
                            proxTrd = mp.get("profund_ultima_tr_desarrollo").toString().trim();
                            proxTrv = mp.get("profund_ultima_tr_vertical").toString().trim();
                        }

                        SiopDAO.insertSiopTR(lastSiopID, SiopUtils.verificaVacio(mp.get("profund_ultima_tr_desarrollo")), SiopUtils.verificaVacio(mp.get("profund_ultima_tr_vertical")), SiopUtils.verificaVacio(mp.get("profund_prox_tr_desarrollo")), SiopUtils.verificaVacio(mp.get("profund_prox_tr_vertical")), SiopUtils.verificaVacio(mp.get("profund_b_l_desarrollo")), SiopUtils.verificaVacio(mp.get("profund_b_l_vertical")), SiopUtils.verificaVacio(mp.get("profund_c2_desarrollo")), contadorTR);
                        //*****************************INSERTA INFORMACION TR*************************
                    }
                    //*****************************INSERTA INFORMACION DE PROFUNDIDAD*************************
                    //*****************************INSERTA INFORMACION DE BARRENAS****************************
                    iprof = infP.getBarrenasPorHoja().iterator();
                    while (iprof.hasNext()) {
                        HashMap<String, String> mb = iprof.next();
                        SiopDAO.insertSiopBarrena(lastSiopID, "ACTUAL", SiopUtils.verificaVacio(mb.get("barrena_marca_actual")), SiopUtils.verificaVacio(mb.get("barrena_serie_actual")), Float.valueOf(SiopUtils.verificaVacio(mb.get("barrena_diametro_actual"))), SiopUtils.verificaVacio(mb.get("barrena_tipo_actual")), SiopUtils.verificaVacio(mb.get("barrena_toberas_actual")), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mb.get("barrena_mts_perf_actual"))), SiopUtils.verificaHorasOP(mb.get("barrena_hors_operacion_actual")), Float.valueOf(SiopUtils.verificaVacio(mb.get("barrena_prom_x_m_actual"))), SiopUtils.eliminaApostrofes(mb.get("barrena_3_ult_mts_actual")), Float.valueOf(SiopUtils.verificaVacio(mb.get("barrena_costo_x_m_actual"))), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mb.get("barrena_amp_inic_actual"))), SiopUtils.verificaVacio(mb.get("barrena_des_AIDC_actual")));
                    }
                    //*****************************INSERTA INFORMACION DE BARRENAS****************************  
                    //****************************INSERTA INFORMACION DE HERRAMIENTAS************************
                    iprof = infP.getHerramientasPorHoja().iterator();
                    while (iprof.hasNext()) {
                        HashMap<String, String> mh = iprof.next();
                        try {
                            if (SiopUtils.verificaHerramienta(mh.get("herramienta_reng_1"), mh.get("herramienta_cant_reng_1"), mh.get("herramienta_long_reng_1"), mh.get("herramienta_obs_reng_1"))) {
                                SiopDAO.insertSiopHerramienta(lastSiopID, mh.get("herramienta_reng_1").trim(), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mh.get("herramienta_cant_reng_1"))), Float.valueOf(mh.get("herramienta_long_reng_1")), mh.get("herramienta_obs_reng_1").trim());
                            }
                            if (SiopUtils.verificaHerramienta(mh.get("herramienta_reng_2"), mh.get("herramienta_cant_reng_2"), mh.get("herramienta_long_reng_2"), mh.get("herramienta_obs_reng_2"))) {
                                SiopDAO.insertSiopHerramienta(lastSiopID, mh.get("herramienta_reng_2").trim(), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mh.get("herramienta_cant_reng_2"))), Float.valueOf(mh.get("herramienta_long_reng_2")), mh.get("herramienta_obs_reng_2").trim());
                            }
                            if (SiopUtils.verificaHerramienta(mh.get("herramienta_reng_3"), mh.get("herramienta_cant_reng_3"), mh.get("herramienta_long_reng_3"), mh.get("herramienta_obs_reng_3"))) {
                                SiopDAO.insertSiopHerramienta(lastSiopID, mh.get("herramienta_reng_3").trim(), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mh.get("herramienta_cant_reng_3"))), Float.valueOf(mh.get("herramienta_long_reng_3")), mh.get("herramienta_obs_reng_3").trim());
                            }
                            if (SiopUtils.verificaHerramienta(mh.get("herramienta_reng_4"), mh.get("herramienta_cant_reng_4"), mh.get("herramienta_long_reng_4"), mh.get("herramienta_obs_reng_4"))) {
                                SiopDAO.insertSiopHerramienta(lastSiopID, mh.get("herramienta_reng_4").trim(), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mh.get("herramienta_cant_reng_4"))), Float.valueOf(mh.get("herramienta_long_reng_4")), mh.get("herramienta_obs_reng_4").trim());
                            }
                            if (SiopUtils.verificaHerramienta(mh.get("herramienta_reng_5"), mh.get("herramienta_cant_reng_5"), mh.get("herramienta_long_reng_5"), mh.get("herramienta_obs_reng_5"))) {
                                SiopDAO.insertSiopHerramienta(lastSiopID, mh.get("herramienta_reng_5").trim(), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mh.get("herramienta_cant_reng_5"))), Float.valueOf(mh.get("herramienta_long_reng_5")), mh.get("herramienta_obs_reng_5").trim());
                            }
                            if (SiopUtils.verificaHerramienta(mh.get("herramienta_reng_6"), mh.get("herramienta_cant_reng_6"), mh.get("herramienta_long_reng_6"), mh.get("herramienta_obs_reng_6"))) {
                                SiopDAO.insertSiopHerramienta(lastSiopID, mh.get("herramienta_reng_6").trim(), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mh.get("herramienta_cant_reng_6"))), Float.valueOf(mh.get("herramienta_long_reng_6")), mh.get("herramienta_obs_reng_6").trim());
                            }
                            if (SiopUtils.verificaHerramienta(mh.get("herramienta_reng_7"), mh.get("herramienta_cant_reng_7"), mh.get("herramienta_long_reng_7"), mh.get("herramienta_obs_reng_7"))) {
                                SiopDAO.insertSiopHerramienta(lastSiopID, mh.get("herramienta_reng_7").trim(), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mh.get("herramienta_cant_reng_7"))), Float.valueOf(mh.get("herramienta_long_reng_7")), mh.get("herramienta_obs_reng_7").trim());
                            }
                            if (SiopUtils.verificaHerramienta(mh.get("herramienta_reng_8"), mh.get("herramienta_cant_reng_8"), mh.get("herramienta_long_reng_8"), mh.get("herramienta_obs_reng_8"))) {
                                SiopDAO.insertSiopHerramienta(lastSiopID, mh.get("herramienta_reng_8").trim(), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mh.get("herramienta_cant_reng_8"))), Float.valueOf(mh.get("herramienta_long_reng_8")), mh.get("herramienta_obs_reng_8").trim());
                            }
                            if (SiopUtils.verificaHerramienta(mh.get("herramienta_reng_9"), mh.get("herramienta_cant_reng_9"), mh.get("herramienta_long_reng_9"), mh.get("herramienta_obs_reng_9"))) {
                                SiopDAO.insertSiopHerramienta(lastSiopID, mh.get("herramienta_reng_9").trim(), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mh.get("herramienta_cant_reng_9"))), Float.valueOf(mh.get("herramienta_long_reng_9")), mh.get("herramienta_obs_reng_9").trim());
                            }
                            if (SiopUtils.verificaHerramienta(mh.get("herramienta_reng_10"), mh.get("herramienta_cant_reng_10"), mh.get("herramienta_long_reng_10"), mh.get("herramienta_obs_reng_10"))) {
                                SiopDAO.insertSiopHerramienta(lastSiopID, mh.get("herramienta_reng_10").trim(), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mh.get("herramienta_cant_reng_10"))), Float.valueOf(mh.get("herramienta_long_reng_10")), mh.get("herramienta_obs_reng_10").trim());
                            }
                            if (SiopUtils.verificaHerramienta(mh.get("herramienta_reng_11"), mh.get("herramienta_cant_reng_11"), mh.get("herramienta_long_reng_11"), mh.get("herramienta_obs_reng_11"))) {
                                SiopDAO.insertSiopHerramienta(lastSiopID, mh.get("herramienta_reng_11").trim(), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mh.get("herramienta_cant_reng_11"))), Float.valueOf(mh.get("herramienta_long_reng_11")), mh.get("herramienta_obs_reng_11").trim());
                            }
                            if (SiopUtils.verificaHerramienta(mh.get("herramienta_reng_12"), mh.get("herramienta_cant_reng_12"), mh.get("herramienta_long_reng_12"), mh.get("herramienta_obs_reng_12"))) {
                                SiopDAO.insertSiopHerramienta(lastSiopID, mh.get("herramienta_reng_12").trim(), Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(mh.get("herramienta_cant_reng_12"))), Float.valueOf(mh.get("herramienta_long_reng_12")), mh.get("herramienta_obs_reng_12").trim());
                            }
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    }
                    //****************************INSERTA INFORMACION DE HERRAMIENTAS************************
                    //****************************INSERTA INFORMACION DE NUCLEO************************
                    iprof = infP.getNucleosPorHoja().iterator();
                    while (iprof.hasNext()) {
                        HashMap<String, String> mn = iprof.next();
                        if (SiopUtils.verificaNucleo(mn.get("nucleos_r_row_0_col_0"), mn.get("int_prog_row_0_col_1"), mn.get("int_disp_row_0_col_2"))) {
                            SiopDAO.insertNucleo(lastSiopID, mn.get("nucleos_r_row_0_col_0").trim(), mn.get("int_prog_row_0_col_1"), mn.get("int_disp_row_0_col_2"));
                        }
                        if (SiopUtils.verificaNucleo(mn.get("nucleos_r_row_1_col_0"), mn.get("int_prog_row_1_col_1"), mn.get("int_disp_row_1_col_2"))) {
                            SiopDAO.insertNucleo(lastSiopID, SiopUtils.verificaVacio(mn.get("nucleos_r_row_1_col_0")), SiopUtils.verificaVacio(mn.get("int_prog_row_1_col_1")), SiopUtils.verificaVacio(mn.get("int_disp_row_1_col_2")));
                        }
                        if (SiopUtils.verificaNucleo(mn.get("nucleos_r_row_2_col_0"), mn.get("int_prog_row_2_col_1"), mn.get("int_disp_row_2_col_2"))) {
                            SiopDAO.insertNucleo(lastSiopID, SiopUtils.verificaVacio(mn.get("nucleos_r_row_2_col_0")), SiopUtils.verificaVacio(mn.get("int_prog_row_2_col_1")), SiopUtils.verificaVacio(mn.get("int_disp_row_2_col_2")));
                        }
                        if (SiopUtils.verificaNucleo(mn.get("nucleos_r_row_3_col_0"), mn.get("int_prog_row_3_col_1"), mn.get("int_disp_row_3_col_2"))) {
                            SiopDAO.insertNucleo(lastSiopID, SiopUtils.verificaVacio(mn.get("nucleos_r_row_3_col_0")), SiopUtils.verificaVacio(mn.get("int_prog_row_3_col_1")), SiopUtils.verificaVacio(mn.get("int_disp_row_3_col_2")));
                        }
                        if (SiopUtils.verificaNucleo(mn.get("nucleos_r_row_4_col_0"), mn.get("int_prog_row_4_col_1"), mn.get("int_disp_row_4_col_2"))) {
                            SiopDAO.insertNucleo(lastSiopID, SiopUtils.verificaVacio(mn.get("nucleos_r_row_4_col_0")), SiopUtils.verificaVacio(mn.get("int_prog_row_4_col_1")), SiopUtils.verificaVacio(mn.get("int_disp_row_4_col_2")));
                        }
                    }
                    //****************************INSERTA INFORMACION DE NUCLEO************************
                    //****************************INSERTA INFORMACION DE LODO************************
                    iprof = infP.getLodoPorHoja().iterator();
                    while (iprof.hasNext()) {
                        HashMap<String, String> ml = iprof.next();
                        if (SiopUtils.verificaLodo(ml.get("lodo_row_0_col_23").trim())) {

//                         if(SiopUtils.verificaLodo(ml.get("lodo_row_0_col_23").trim())){
                            int alc = 0;
                            if (ml.get("Alc. ") == null) {
                                //System.out.println("SIN ALCALINIDAD");
                            } else {
                                //System.out.println("CON ALCALINIDAD");
                                alc = Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(ml.get("Alc. ").trim()));
                            }
//                        System.out.println("Lodo     |" + SiopUtils.verificaVacio(ml.get("lodo_row_0_col_23").toString().trim())+ "|");
//                        System.out.println("Dens     |" + SiopUtils.verificaVacio(ml.get("Dens").toString().trim()) + "|");
//                        System.out.println("Visc     |" + SiopUtils.verificaVacio(ml.get("Visc").toString().trim()) + "|");
//                        System.out.println("Temp     |" + SiopUtils.verificaVacio(ml.get("Temp °C").toString().trim()) + "|");
//                        System.out.println("Arena    |" + SiopUtils.verificaVacio(ml.get("%Arena").toString().trim()) + "|");
//                        System.out.println("Filtrado |" + SiopUtils.verificaVacio(ml.get("Filtrado").toString().trim()) + "|");
//                        System.out.println("Calcio   |" + SiopUtils.verificaVacio(ml.get("Calcio").toString().trim()) + "|");
//                        System.out.println("Enjarre  |" + SiopUtils.verificaVacio(ml.get("Enjarre").toString().trim()) + "|");
//                        //System.out.println("Alc      |" + SiopUtils.verificaVacio(ml.get("Alc. ").toString().trim()) + "|");
//                        System.out.println("Alc      |" + alc + "|");
//                        System.out.println("Gel 0    |" + SiopUtils.verificaVacio(ml.get("Gel 0 ").toString().trim()) + "|");
//                        System.out.println("Gel 10   |" + SiopUtils.verificaVacio(ml.get("Gel 10 ").toString().trim()) + "|");
//                        System.out.println("Cloruros |" + SiopUtils.verificaVacio(ml.get("Cloruros").toString().trim()) + "|");
//                        System.out.println("PH       |" + SiopUtils.verificaVacio(ml.get("PH").toString().trim()) + "|");
//                        System.out.println("Solidos  |" + SiopUtils.verificaVacio(ml.get("%Solidos").toString().trim()) + "|");
//                        System.out.println("Aceite   |" + SiopUtils.verificaVacio(ml.get("%Aceite").toString().trim()) + "|");
//                        System.out.println("Agua     |" + SiopUtils.verificaVacio(ml.get("%Agua").toString().trim()) + "|");
//                        System.out.println("VA       |" + SiopUtils.verificaVacio(ml.get("VA").toString().trim()) + "|");
//                        System.out.println("VP       |" + SiopUtils.verificaVacio(ml.get("VP").toString().trim()) + "|");
//                        System.out.println("YP       |" + SiopUtils.verificaVacio(ml.get("YP").toString().trim()) + "|");
//                        System.out.println("Emul     |" + SiopUtils.verificaVacio(ml.get("Emul").toString().trim()) + "|");
//                        System.out.println("R.A.A.   |" + SiopUtils.verificaVacio(ml.get("R.A.A.").toString().trim()) + "|");
//                        System.out.println("MBT      |" + SiopUtils.verificaVacio(ml.get("MBT").toString().trim()) + "|");

                            SiopDAO.insertSiopLodo(lastSiopID,
                                    SiopUtils.verificaVacio(ml.get("lodo_row_0_col_23").toString().trim()),
                                    Float.valueOf(SiopUtils.verificaVacio(ml.get("Dens").toString().trim())),
                                    Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(ml.get("Visc").toString().trim())),
                                    Float.valueOf(SiopUtils.verificaVacio(ml.get("Temp °C").toString().trim())),
                                    Float.valueOf(SiopUtils.verificaVacio(ml.get("%Arena").toString().trim())),
                                    Float.valueOf(SiopUtils.verificaVacio(ml.get("Filtrado").toString().trim())),
                                    Float.valueOf(SiopUtils.verificaVacio(ml.get("Calcio").toString().trim())),
                                    Float.valueOf(SiopUtils.verificaVacio(ml.get("Enjarre").toString().trim())),
                                    alc,
                                    Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(ml.get("Gel 0 ").toString().trim())),
                                    Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(ml.get("Gel 10 ").toString().trim())),
                                    Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(ml.get("Cloruros").toString().trim())),
                                    Float.valueOf(SiopUtils.verificaVacio(ml.get("PH").toString().trim())),
                                    Float.valueOf(SiopUtils.verificaVacio(ml.get("%Solidos").toString().trim())),
                                    Float.valueOf(SiopUtils.verificaVacio(ml.get("%Aceite").toString().trim())),
                                    Float.valueOf(SiopUtils.verificaVacio(ml.get("%Agua").toString().trim())),
                                    Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(ml.get("VA").toString().trim())),
                                    Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(ml.get("VP").toString().trim())),
                                    Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(ml.get("YP").toString().trim())),
                                    Integer.valueOf(SiopUtils.verificaProfundidadDesarrollo(ml.get("Emul").toString().trim())),
                                    SiopUtils.verificaVacio(ml.get("R.A.A.").toString().trim()),
                                    SiopUtils.verificaVacio(ml.get("MBT").toString().trim()));

                        }
                    }
                    //****************************INSERTA INFORMACION DE LODO************************
                    //*****************************DETALLE DE OPERACION***************************************
                    //Regresa la informacion concatenada de los comentarios
                    Iterator<Informacion> it = infP.getAllInfo().iterator();
                    Informacion info = null;

                    while (it.hasNext()) {
                        info = it.next();

                        String comment = info.getActividad().replace("'", "-");

                        SiopDAO.insertSiopOperacion(lastSiopID, info.getFechaInicio(), info.getFechaFinal(), comment.trim().toUpperCase());
//                    myDAO.commit();
                    }

                    lastSiopID = null;
                    //res = "Archivo " + infP.getNomFile() + " leido correctamente";
                    res="";

                    //Insertar nombre de archivo y fecha
                    cont++;
                }// WHILE
                                //QUITAR LA LLAVE DE ABAJO AL QUITAR ESTE COMENTARIO
 
            }else{// EL IF PARA VERIFICAR SI TIENE INFORMACIÓN
                res="Verifique que su archivo sea un SIOP Correcto";
            }
            //*****************************DETALLE DE OPERACION***************************************
            //myDAO.close();

                                  
        } catch (Exception e) {
            System.out.println("ERROR SIOP READER: " + e.getMessage());
            res = "Error: " + e.toString();
        }
//        JOptionPane.showMessageDialog(null, res, Menu.nombreSistema, JOptionPane.INFORMATION_MESSAGE);
        return res;
    }
    
}
