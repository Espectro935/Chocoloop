package org.example.chocoloop;

public class Usuario {
    private String nombre, userName, userPassword;

    public Usuario() {
    }

    public Usuario(String nombre, String userName, String userPassword) {
        this.nombre = nombre;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

}
