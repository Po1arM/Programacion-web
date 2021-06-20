package edu.pucmm.eitc.servicios;

import edu.pucmm.eitc.DBService;
import edu.pucmm.eitc.encapsulaciones.VentasProductos;

public class ServiceVentas extends DBService<VentasProductos> {

    private static ServiceVentas instance;

    private ServiceVentas(){ super(VentasProductos.class);}

    public static ServiceVentas getInstance(){
        if(instance == null){
            instance = new ServiceVentas();
        }
        return instance;
    }
}
