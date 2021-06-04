package edu.pucmm.eitc;

import java.util.ArrayList;

public class CarroCompra {
    private long id;
    private ArrayList<Producto> productos;

    public CarroCompra(long id) {
        this.id = id;
        this.productos = new ArrayList<Producto>();
    }

    public void addProducto(Producto nuevo){
        this.productos.add(nuevo);
    }

}
