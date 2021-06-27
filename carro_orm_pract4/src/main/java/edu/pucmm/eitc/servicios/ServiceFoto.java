package edu.pucmm.eitc.servicios;

import edu.pucmm.eitc.DBService;
import edu.pucmm.eitc.encapsulaciones.Foto;

public class ServiceFoto extends DBService<Foto> {

    private static ServiceFoto instance;

    private ServiceFoto(){
        super(Foto.class);
    }

    public static ServiceFoto getInstancia(){
        if(instance==null){
            instance = new ServiceFoto();
        }
        return instance;
    }
}

