package edu.pucmm.eitc.servicios;

import edu.pucmm.eitc.DBService;
import edu.pucmm.eitc.encapsulaciones.Producto;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ServiceProduct extends DBService<Producto> {
    private static ServiceProduct instance;

    private ServiceProduct(){
        super(Producto.class);
    }

    public static ServiceProduct getInstance(){
        if(instance == null){
            instance = new ServiceProduct();
        }
        return instance;
    }

    public List<Producto> getProductos(){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT * FROM producto", Producto.class);
        List<Producto> productos = query.getResultList();
        return  productos;
    }
}
