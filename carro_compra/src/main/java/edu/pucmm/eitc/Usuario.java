package edu.pucmm.eitc;

public class Usuario {
    private String usuario;
    private String nombre;
    private String password;

    public Usuario(String usuario, String nombre, String password) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.password = password;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPassword() {
        return password;
    }
}
