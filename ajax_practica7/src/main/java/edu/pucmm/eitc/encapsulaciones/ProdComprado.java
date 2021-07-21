package edu.pucmm.eitc.encapsulaciones;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(ProdID.class)
public class ProdComprado implements Serializable {
    @Id
    private long ventaID;
    @Id
    private int productId;
    private int cantidad;
    private int precio;
    private String nombre;

    public ProdComprado(int productId, long ventaID, int cantidad, int precio, String nombre) {
        this.productId = productId;
        this.ventaID = ventaID;
        this.cantidad = cantidad;
        this.precio = precio;
        this.nombre = nombre;
    }

    public ProdComprado() {

    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public long getVentaID() {
        return ventaID;
    }

    public void setVentaID(long ventaID) {
        this.ventaID = ventaID;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public int total(){
        return cantidad * precio;
    }
}
class ProdID implements Serializable {
    @Id
    private long ventaID;
    @Id
    private int productId;

    public ProdID() {}

    public ProdID(long ventaID, int productId) {
        this.ventaID = ventaID;
        this.productId = productId;
    }
}
