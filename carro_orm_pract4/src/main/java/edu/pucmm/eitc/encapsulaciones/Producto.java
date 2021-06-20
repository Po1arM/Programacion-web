package edu.pucmm.eitc.encapsulaciones;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Producto implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    private String nombre;
    private int precio;
    @Transient
    private int cantidad;
    @OneToMany
    private List<Comentario> comentarios;
    @OneToMany
    private List<Foto> fotos;
    public Producto() {
        id = 0;
        nombre = "";
        precio = 0;
    }
    public Producto(String nombre, int precio) {
        this.nombre = nombre;
        this.precio = precio;
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

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public int total(){
        return precio * cantidad;
    }
}
