package edu.pucmm.eitc;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BootStrapServices {
    private static Server server;

    public static void startDB() {
        try {
            server = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-ifNotExists").start();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void stopDB() throws SQLException {
        if(server != null){
            server.stop();
        }
    }

    //Scripts para crear las tablas
    //Por hacer todas las tablas
    public static void crearTablas() throws SQLException{
        crearTablaUsuarios();
        crearTablaVentas();
        crearTablaProductos();
        crearTablaVentasProductos();
        crearAdmin();
    }
    public static void crearTablaProductos() throws SQLException {

        String query = "CREATE TABLE IF NOT EXISTS PRODUCTO\n" +
                "(\n" +
                "  ID IDENTITY  PRIMARY KEY  NOT NULL ,\n" +
                "  NOMBRE VARCHAR(100) NOT NULL,\n" +
                "  ESTADO VARCHAR(3) NOT NULL DEFAULT 'ACT',\n" +
                "  PRECIO INTEGER NOT NULL \n" +
                ");";

        ejecutarQuery(query);
    }
    public static void crearTablaUsuarios() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS USUARIO\n" +
                "(\n" +
                "  USER VARCHAR(20) PRIMARY KEY NOT NULL,\n" +
                "   PASSWORD VARCHAR(25) NOT NULL, \n" +
                "   TIPO VARCHAR(3) NOT NULL\n" +
                ");";
        ejecutarQuery(query);

    }
    public static void crearTablaVentas() throws SQLException {

        String query = "CREATE TABLE IF NOT EXISTS VENTA\n"+
                "(\n" +
                " ID IDENTITY PRIMARY KEY NOT NULL  ,\n"+
                " FECHA DATE NOT NULL,\n"+
                " NOMBRE VARCHAR(25) NOT NULL \n" +
                ");";

        ejecutarQuery(query);

    }
    public static void crearTablaVentasProductos() throws SQLException {

        String query = "CREATE TABLE IF NOT EXISTS VENTAPRODUCTOS\n"+
                "(\n" +
                " VENTAID INTEGER NOT NULL,\n"+
                " PRODUCTOID INTEGER NOT NULL,\n"+
                " CANTIDAD INTEGER NOT NULL, \n" +
                "FOREIGN KEY (VENTAID) REFERENCES VENTA(ID), \n" +
                "FOREIGN KEY (PRODUCTOID) REFERENCES PRODUCTO(ID), \n"+
                "CONSTRAINT PK_ID PRIMARY KEY (VENTAID,PRODUCTOID) \n" +
                ");";

        ejecutarQuery(query);

    }
    public static void crearAdmin() throws SQLException {
        String query = "insert into usuario (user,password,tipo) values ('admin','admin','ADM')";
        Connection connection = null;
        try {
            connection = DBService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
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
    }
    public static void ejecutarQuery(String query) throws SQLException{
        Connection connection = DBService.getInstance().getConnection();
        Statement statement = connection.createStatement();
        statement.execute(query);
        statement.close();
        connection.close();
    }
}
