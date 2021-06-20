package edu.pucmm.eitc.servicios;

import edu.pucmm.eitc.DBService;
import edu.pucmm.eitc.encapsulaciones.Comentario;
import edu.pucmm.eitc.encapsulaciones.Foto;

public class ServiceComentario extends DBService<Comentario> {
    private static ServiceComentario instance;

    private ServiceComentario(){
        super(Comentario.class);
    }

    public static ServiceComentario getInstancia(){
        if(instance==null){
            instance = new ServiceComentario();
        }
        return instance;
    }
}
