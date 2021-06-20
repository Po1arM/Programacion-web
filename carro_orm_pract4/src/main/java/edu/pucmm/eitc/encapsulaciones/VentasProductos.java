package edu.pucmm.eitc.encapsulaciones;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class VentasProductos implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    @Temporal(TemporalType.DATE)
    private Date fechaCompra;
    private String nombreCliente;
    @OneToMany
    private List<Producto> listaProductos;

    public VentasProductos() {

    }

    public VentasProductos(String nombre, ArrayList<Producto> productos) {
        this.nombreCliente = nombre;
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

    public List<Producto> getListaProductos() {
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
