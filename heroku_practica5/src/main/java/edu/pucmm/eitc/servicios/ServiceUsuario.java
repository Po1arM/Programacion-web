package edu.pucmm.eitc.servicios;

import edu.pucmm.eitc.DBService;
import edu.pucmm.eitc.encapsulaciones.Comentario;
import edu.pucmm.eitc.encapsulaciones.Usuario;

public class ServiceUsuario extends DBService<Usuario>  {

    private static ServiceUsuario instance;

    private ServiceUsuario(){
        super(Usuario.class);
    }

    public static ServiceUsuario getInstancia(){
        if(instance==null){
            instance = new ServiceUsuario();
        }
        return instance;
    }

    public static String autentificarUsuario(Usuario aux) {
        return "ADM";
    }
}
