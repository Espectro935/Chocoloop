package org.example.chocoloop.DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class conexion {
    private static final String url = "jdbc:mysql://localhost:3306/ChocoLoop";
    private static final String user = "root";
    private static final String password = "";

    public static Connection getConnection() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url,user,password);
    }
}
