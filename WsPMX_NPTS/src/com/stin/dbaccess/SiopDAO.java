package com.stin.dbaccess;

import com.stin.Barrena;
import com.stin.Herramienta;
import com.stin.Intervencion;
import com.stin.Lodo;
import com.stin.Nucleo;
import com.stin.Operacion;
import com.stin.Profundidad;
import com.stin.Siop;
import com.stin.Tr;
import com.stin.DAO.GenericDao;
import com.stin.util.SiopUtils;

public class SiopDAO {


    public static Siop insertSiopCabezera(String cia, String estructura, String fechaSiop, String fechaOperacion, String proyecto, String activo, String equipo, String conductor, String espRotNM, String actividad, String fechaInicio, int idIntervencion, String resumenAnterior, String resumen, int consecutivo, int diasProg, String suptte) throws Exception {
        Siop siop=new Siop();        
        //System.out.println("INSERTA SIOP");
        try { //REVISADO
            siop.setStrCia(cia);
            siop.setStrEstructura(estructura);
            siop.setDateReporte(SiopUtils.dateFechaSiop(fechaSiop));
            siop.setDateOperacion(SiopUtils.dateFechaOperacion(fechaOperacion));
            siop.setStrProyecto(proyecto);
            siop.setStrActivo(activo);
            siop.setStrEquipo(equipo);
            siop.setStrConductor(conductor);
            siop.setStrEspRotNm(espRotNM);
            siop.setStrActividad(actividad);
            siop.setDateActividadInic(fechaInicio);              
            siop.setDiasProg(diasProg);           
            siop.setStrSuptte(suptte);
            
            Intervencion intervencion=new Intervencion();
            intervencion.setIdIntervencion(idIntervencion);
            
            siop.setIntervencion(intervencion);
            siop.setStrResumenAnterior(resumenAnterior);
            siop.setStrResumen(resumen);
            siop.setIntSiopConsec(consecutivo);  
            
            GenericDao.newGeneric(siop);
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
        return siop;
    }
    public static void insertSiopProfundidad(Siop idSiop, String tipo, int prof00, int prof24, int prof05, String avaInd, String ultCont, String proxCont) throws Exception {
        Profundidad profundidad=new Profundidad();
        //System.out.println("INSERTA PROFUNDIDAD");
        try {//REVIZADO
            profundidad.setSiop(idSiop);
            profundidad.setStrTipoProf(tipo);
            profundidad.setInt00(prof00);
            profundidad.setInt24(prof24);
            profundidad.setInt05(prof05);
            profundidad.setStrAvaInd(avaInd);
            profundidad.setStrUltCont(ultCont);
            profundidad.setStrProxCont(proxCont);
            
            GenericDao.newGeneric(profundidad);
            profundidad=null;
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }
    public static void insertSiopBarrena(Siop idSiop, String actualAnterior, String marca, String serie, float diametro, String tipo, String tobera, int metrosPerforados, String horasOperecion, float promedioPorMetro, String tresUltimosMts, float costoPorMetro, int ampInic, String desIADC) throws Exception {
        Barrena barrena=new Barrena();
        //System.out.println("INSERTA BARRENA");
        try {//REVIZADO
            barrena.setSiop(idSiop);
            barrena.setStrActAnt(actualAnterior);
            barrena.setStrMarca(marca);
            barrena.setStrSerie(serie);
            barrena.setStrDiametro((double)diametro);
            barrena.setStrTipo(tipo);
            barrena.setStrTobera(tobera);
            barrena.setIntMtsPerf(metrosPerforados);
            barrena.setStrHrsOp(horasOperecion);
            barrena.setFloatPromPorMt((double)promedioPorMetro);
            barrena.setStr3ultMts(tresUltimosMts);
            barrena.setFloatCostoPorMt((double)costoPorMetro);
            barrena.setIntAmpInic((short)ampInic);
            barrena.setStrDesAidc(desIADC);
            
            GenericDao.newGeneric(barrena);
            barrena=null;
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }
    public static void insertSiopHerramienta(Siop idSiop, String herramienta, int cantidad, float longitud, String observaciones) throws Exception {
        Herramienta herra=new Herramienta();
        //System.out.println("INSERTA HERRAMIENTA");
        try {//REVIZADO
            herra.setSiop(idSiop);
            herra.setStrHerramienta(herramienta);
            herra.setIntCant(cantidad);
            herra.setFloatLong((double)longitud);
            herra.setStrObservaciones(observaciones);
            
            GenericDao.newGeneric(herra);
            herra=null;
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }
    public static void insertSiopTR(Siop idSiop, String ultimaTRd, String ultimaTRv, String proxTRd, String proxTRv, String bLd, String bLv, String c2, int consecutivo) throws Exception {
        Tr tr=new Tr();
        //System.out.println("INSERTA TR");
        try {//REVIZADO
            tr.setSiop(idSiop);
            tr.setStrUltimaTrd(ultimaTRd);
            tr.setStrUltimaTrv(ultimaTRv);
            tr.setStrProxTrd(proxTRd);
            tr.setStrProxTrv(proxTRv);
            tr.setStrBld(bLd);
            tr.setStrBlv(bLv);
            tr.setStrC2(c2);
            tr.setIntTrconsec(consecutivo);
            
            GenericDao.newGeneric(tr);
            tr=null;
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
        
    }
    public static void insertNucleo(Siop idSiop, String nucleoR, String intProg, String intDisp) throws Exception {
        Nucleo nucleo=new Nucleo();
        //System.out.println("INSERTA NUCLEO");
        try {//REVISADO
            nucleo.setSiop(idSiop);
            nucleo.setStrNucleoR(nucleoR);
            nucleo.setStrIntProg(intProg);
            nucleo.setStrIntDisp(intDisp);
            
            GenericDao.newGeneric(nucleo);
            nucleo=null;
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }
    public static void insertSiopLodo(Siop idSiop, String lodo, float densidad, int viscosidad, float temperatura, float arena, float filtrado, float calcio, float enjarre, int alcalinidad, int gelCero, int gelDiez, int cloruros, float ph, float solidos, float aceite, float agua, int va, int vp, int yp, int emul, String raa, String mbt) throws Exception {
        Lodo lod=new Lodo();
        //System.out.println("INSERTA LODO");
        try {//REVISADO
            lod.setSiop(idSiop);
            lod.setStrLodo(lodo);
            lod.setFloatDens((double)densidad);
            lod.setIntVisc(viscosidad);
            lod.setFloatTemp((double)temperatura);
            lod.setFloatArena((double)arena);
            lod.setFloatFiltrado((double)filtrado);
            lod.setFloatCalcio((double)calcio);
            lod.setFloatEnjarre((double)enjarre);
            lod.setIntAlc(alcalinidad);
            lod.setIntGel0(gelCero);
            lod.setIntGel10(gelDiez);
            lod.setIntCloruros(cloruros);
            lod.setFloatPh((double)ph);
            lod.setFloatSolidos((double)solidos);
            lod.setFloatAceite((double)aceite);
            lod.setFloatAgua((double)agua);
            lod.setIntVa(va);
            lod.setIntVp(vp);
            lod.setIntYp(yp);
            lod.setIntEmul(emul);
            lod.setStrRaa(raa);
            lod.setStrMbt(mbt);
            
            GenericDao.newGeneric(lod);
            lod=null;
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }
    public static void insertSiopOperacion(Siop idSiop, String fechaIncio, String fechaFin, String actividad) throws Exception {
        Operacion operacion=new Operacion();
        //System.out.println("INSERTA OPERACION");
        try {
            operacion.setSiop(idSiop);
            operacion.setDateIni(fechaIncio);
            operacion.setDateEnd(fechaFin);
            operacion.setStrResume(actividad);
            
            GenericDao.newGeneric(operacion);
            operacion=null;
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }
}
