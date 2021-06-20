package edu.pucmm.eitc.encapsulaciones;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VentasProductos {
    private long id;
    private Date fechaCompra;
    private String nombreCliente;
    private ArrayList<Producto> listaProductos;

    public VentasProductos(String nombre, ArrayList<Producto> productos) {
        this.nombreCliente = nombre;
        this.listaProductos = productos;
    }

    public VentasProductos() {

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

    public void setId(long id) {
        this.id = id;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }
}
