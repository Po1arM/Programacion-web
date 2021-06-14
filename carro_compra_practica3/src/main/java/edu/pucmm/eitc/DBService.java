package edu.pucmm.eitc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBService {

    private static DBService instance;
    private String URL = "jdbc:h2:tcp://localhost/~/webmarket"; //Modo Server...

    private DBService(){
        regDriver();
    }

    public static DBService getInstance() {
        if(instance == null){
            instance = new DBService();
        }
        return instance;
    }

    private void regDriver() {
        try{
            Class.forName("org.h2.Driver");
        }catch (ClassNotFoundException exception){
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, exception);

        }
    }

     public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, "admin","admin");
        } catch (SQLException exception){
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, exception);
        }
        return connection;
    }

    public void testConnection() {
        try {
            getConnection().close();
            System.out.println("Conexión realizado con exito...");
        } catch (SQLException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
