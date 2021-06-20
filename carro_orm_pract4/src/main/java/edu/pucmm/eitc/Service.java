package edu.pucmm.eitc;

import edu.pucmm.eitc.encapsulaciones.Producto;
import edu.pucmm.eitc.encapsulaciones.Usuario;
import edu.pucmm.eitc.encapsulaciones.VentasProductos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Service {
    private static Service instancia;
    private List<Usuario> usuarios;
    private List<Producto> productos;
    private List<VentasProductos> ventas;
    private int cont;
    private long carrito;

   public Service() {

    }

    public static Service getInstance(){
        if(instancia == null){
            instancia = new Service();
        }
        return instancia;
    }


/*
    public List<Producto> getProductos() {

    }

    public List<VentasProductos> getVentas() {

    }

    private ArrayList<Producto> getProductosPorVenta(long id) {

    }

    public Usuario autentificarUsuario(String usuario, String nombre, String password){
    }

    public Producto registrarProducto(Producto producto){

    }

    public Producto actualizarProducto(Producto producto){

    }
    public boolean eliminarProducto(int id){

    }
    public Producto getProductosPorID(int id) {

    }
    public boolean addVentas(VentasProductos venta) {

    }

    public boolean addProductosVenta(int fila, ArrayList<Producto> listaProductos){


    }


    public String autentificarUsuario(Usuario user) {

    }

    private void registrarUsuario(Usuario user) throws SQLException {

    }*/

}

