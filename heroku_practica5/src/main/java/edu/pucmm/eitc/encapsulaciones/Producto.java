package edu.pucmm.eitc.encapsulaciones;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Producto implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private String nombre;
    private String descri;
    private int precio;
    @Transient
    private int cantidad;
    @Column(columnDefinition = "boolean default true")
    private boolean estado;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Foto> fotos;

    public Producto(String nombre, int precio, String desc) {
        this.nombre = nombre;
        this.precio = precio;
        estado = true;
        descri = desc;
    }

    public Producto() {

    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void actualizar(Producto producto) {
        this.nombre = producto.nombre;
        this.precio = producto.precio;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCantidad(int cantidad){this.cantidad = cantidad;}

    public int getCantidad() {
        return cantidad;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int total(){
        return precio * cantidad;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }
}
