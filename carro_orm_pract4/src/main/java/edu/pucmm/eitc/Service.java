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
        BootStrapServices.startDB();

        DBService.getInstance().testConnection();
        try {
            BootStrapServices.crearTablas();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Service getInstance(){
        if(instancia == null){
            instancia = new Service();
        }
        return instancia;
    }



    public List<Producto> getProductos() {
        List<Producto> productos= new ArrayList<Producto>();
        Connection connection = null;
        try{
            String query = "SELECT * FROM PRODUCTO WHERE ESTADO = 'ACT'";
            connection = DBService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()){
                Producto producto = new Producto();
                producto.setId(result.getInt("id"));
                producto.setNombre(result.getString("nombre"));
                producto.setPrecio(result.getInt("precio"));
                productos.add(producto);
            }
        }catch (SQLException exception){
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                connection.close();
            }catch (SQLException exception){
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return productos;
    }

    public List<VentasProductos> getVentas() {
        List<VentasProductos> ventas= new ArrayList<VentasProductos>();
        Connection connection = null;
        try{
            String query = "SELECT * FROM VENTA";
            connection = DBService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()){
                VentasProductos venta = new VentasProductos();
                venta.setId(result.getInt("id"));
                venta.setNombreCliente(result.getString("nombre"));
                venta.setFechaCompra(result.getDate("fecha"));
                venta.setListaProductos(getProductosPorVenta(venta.getId()));
                ventas.add(venta);
            }
        }catch (SQLException exception){
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                connection.close();
            }catch (SQLException exception){
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return ventas;
    }

    private ArrayList<Producto> getProductosPorVenta(long id) {
        List<Producto> productos= new ArrayList<Producto>();
        Connection connection = null;
        try{
            String query = "SELECT PRODUCTO.NOMBRE AS NOMBRE, PRODUCTO.PRECIO AS PRECIO,VENTAPRODUCTOS.CANTIDAD AS CANTIDAD\n" +
                    "FROM VENTAPRODUCTOS INNER JOIN PRODUCTO ON PRODUCTO.ID = VENTAPRODUCTOS.PRODUCTOID;";
            connection = DBService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()){
                Producto producto = new Producto();
                producto.setNombre(result.getString("nombre"));
                producto.setPrecio(result.getInt("precio"));
                producto.setCantidad(result.getInt("cantidad"));
                productos.add(producto);
            }
        }catch (SQLException exception){
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                connection.close();
            }catch (SQLException exception){
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return (ArrayList<Producto>) productos;
    }

    public Usuario autentificarUsuario(String usuario, String nombre, String password){
        return new Usuario(usuario,password);
    }

    public Producto registrarProducto(Producto producto){
        boolean ok =false;

        Connection connection = null;
        try {
            String query = "INSERT INTO PRODUCTO(NOMBRE,PRECIO) values(?,?)";
            connection = DBService.getInstance().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, producto.getNombre());
            preparedStatement.setInt(2,producto.getPrecio());
            int fila = preparedStatement.executeUpdate();
        } catch (SQLException ex){
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try{
                connection.close();
            } catch (SQLException ex){
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return producto;
    }

    public Producto actualizarProducto(Producto producto){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "update producto set nombre=?, precio=? where id = ?";
            con = DBService.getInstance().getConnection();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, producto.getNombre());
            prepareStatement.setInt(2, producto.getPrecio());
            //Indica el where...
            prepareStatement.setInt(3, producto.getId());
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return producto;
    }
    public boolean eliminarProducto(int id){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "update producto set estado='ELI' where id = ?";
            con = DBService.getInstance().getConnection();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setInt(1, id);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }
    public Producto getProductosPorID(int id) {
        Producto aux = null;
        Connection con = null;
        try {
            //utilizando los comodines (?)...
            String query = "select * from producto where id = ?";
            con = DBService.getInstance().getConnection();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setInt(1, id);
            //Ejecuto...
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                aux = new Producto();
                aux.setId(id);
                aux.setNombre(rs.getString("nombre"));
                aux.setPrecio(rs.getInt("precio"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return aux;
    }
    public boolean addVentas(VentasProductos venta) {
        boolean ok =false;

        Connection con = null;
        try {
            String query = "insert into venta(fecha, nombre) values(CURRENT_DATE,?)";
            con = DBService.getInstance().getConnection();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, venta.getNombreCliente());

            int fila = prepareStatement.executeUpdate();
            addProductosVenta(fila, venta.getListaProductos());
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ok;
    }

    public boolean addProductosVenta(int fila, ArrayList<Producto> listaProductos){
        boolean ok =false;

        Connection con = null;
        for (Producto producto : listaProductos) {
            try {
                String query = "insert into ventaproductos(ventaid, productoid,cantidad) values(?,?,?)";
                con = DBService.getInstance().getConnection();
                PreparedStatement prepareStatement = con.prepareStatement(query);
                prepareStatement.setInt(1, fila);
                prepareStatement.setInt(2,producto.getId());
                prepareStatement.setInt(3,producto.getCantidad());


                int fil = prepareStatement.executeUpdate();
                ok = fil > 0 ;

            } catch (SQLException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            } finally{
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return ok;

    }
    public long getCarrito(){
        return carrito++;
    }

    public String autentificarUsuario(Usuario user) {
        boolean ok = false;
        Connection connection = null;
        try {
            String query = "SELECT count(*) as cantidad FROM USUARIO WHERE user = '"+ user.getUsuario()+"' and password = '" + user.getPassword()+"'";
            connection = DBService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            int resultado = result.getInt("cantidad");
            if (resultado > 0) {
                preparedStatement = connection.prepareStatement("SELECT tipo FROM USUARIO WHERE user = '"+ user.getUsuario()+"' and password = '" + user.getPassword()+"'");
                ResultSet rs = preparedStatement.executeQuery();
                rs.next();

                String tipo = rs.getString("tipo");
                return tipo;
            }else{
                registrarUsuario(user);
            }
        }catch (SQLException exception){
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                connection.close();
            }catch (SQLException exception){
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return "";
    }

    private void registrarUsuario(Usuario user) throws SQLException {
        String query = "If Not Exists(select * from usuario where usuario='admin' and password ='admin')\n" +
                "Begin\n" +
                "insert into usuario (usuario,password,tipo) values ('"+user.getUsuario()+"','CLI','"+user.getPassword()+"')\n" +
                "End";
        BootStrapServices.ejecutarQuery(query);
    }

}

