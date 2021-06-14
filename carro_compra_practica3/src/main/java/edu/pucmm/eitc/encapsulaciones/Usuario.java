package edu.pucmm.eitc.encapsulaciones;

public class Usuario {
    private String usuario;
    private String password;

    public Usuario() {
        usuario ="";
        password = "";
    }
    public Usuario(String user, String pass) {
        usuario = user;
        password = pass;
    }

    public String getUsuario() {
        return usuario;
    }
    public String getPassword() {
        return password;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
