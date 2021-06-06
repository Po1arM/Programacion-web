package edu.pucmm.eitc.encapsulaciones;

import kotlin.Triple;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class VentasProductos {
    private long id;
    private Date fechaCompra;
    private String nombreCliente;
    private ArrayList<Producto> listaProductos;

    public VentasProductos(long id, String nombreCliente, ArrayList<Producto> productos) {
        this.id = id;
        this.fechaCompra = new Date();
        this.nombreCliente = nombreCliente;
        this.listaProductos = productos;
    }

    public long getId() {
        return id;
    }

    public String getFechaCompra() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");
        String date = dateFormat.format(fechaCompra);
        return date;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public Integer getTotal(){
        Integer total = 0;
        for (Producto producto : listaProductos) {
            total += producto.getPrecio()*producto.getCantidad();
        }
        return total;
    }
}
