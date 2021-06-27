package edu.pucmm.eitc.servicios;

import edu.pucmm.eitc.DBService;
import edu.pucmm.eitc.encapsulaciones.Producto;
import edu.pucmm.eitc.encapsulaciones.VentasProductos;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ServiceVentas extends DBService<VentasProductos> {

    private static ServiceVentas instance;

    private ServiceVentas(){ super(VentasProductos.class);}

    public static ServiceVentas getInstance(){
        if(instance == null){
            instance = new ServiceVentas();
        }
        return instance;
    }

    public List<VentasProductos> getVentas(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from VENTASPRODUCTOS ", VentasProductos.class);
        List<VentasProductos> lista = query.getResultList();
        return lista;
    }
}
