package edu.pucmm.eitc.servicios;

import edu.pucmm.eitc.DBService;
import edu.pucmm.eitc.encapsulaciones.ProdComprado;
import edu.pucmm.eitc.encapsulaciones.Producto;
import edu.pucmm.eitc.encapsulaciones.VentasProductos;

import java.util.ArrayList;
import java.util.List;

public class ServiceProdComprado extends DBService<ProdComprado> {
    private static ServiceProdComprado instance;

    private ServiceProdComprado(){ super(ProdComprado.class);}

    public static ServiceProdComprado getInstance(){
        if(instance == null){
            instance = new ServiceProdComprado();
        }
        return instance;
    }

    public List<ProdComprado> convertProd(List<Producto> productos, long venta){
        List<ProdComprado> list = new ArrayList<ProdComprado>();
        for (Producto prod:productos) {
            ProdComprado temp = new ProdComprado(prod.getId(),venta,prod.getCantidad(),prod.getPrecio(),prod.getNombre());
            getInstance().create(temp);
            list.add(temp);
        }
        return list;
    }
}
