package edu.pucmm.eitc;

import java.util.ArrayList;
import java.util.Date;

public class VentasProductos {
    private long id;
    private Date fechaCompra;
    private String nombreCliente;
    private ArrayList<Producto> listaProductos;

    public VentasProductos(long id, Date fechaCompra, String nombreCliente, ArrayList<Producto> productos) {
        this.id = id;
        this.fechaCompra = new Date();
        this.nombreCliente = nombreCliente;
        this.listaProductos = productos;
    }

    public long getId() {
        return id;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }
}
