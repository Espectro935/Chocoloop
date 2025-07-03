package org.example.chocoloop.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class tablas {
    protected List<String> getTablas() throws Exception {
        List<String> tabla = new ArrayList<>();
        Connection con = conexion.getConnection();
        String query = "show tables";
        ResultSet rs = con.createStatement().executeQuery(query);
        while (rs.next()) {
            String item = rs.getString("Tables_in_chocoloop");
            tabla.add(item);
        }
        tabla.remove("ingreso");
        con.close();
        return tabla;
    }

    protected List<String> getTablaBebida() throws Exception {
        List<String> bebida = new ArrayList<>();
        Connection con = conexion.getConnection();
        String query = "select * from bebida";
        ResultSet rs = con.createStatement().executeQuery(query);
        while (rs.next()) {
            String item = rs.getInt("id") + "--" +
                    rs.getString("bebida") + "--" +
                    rs.getDouble("precio");
            bebida.add(item);
        }
        con.close();
        return bebida;
    }

    protected List<String> getTablaCluster() throws Exception {
        List<String> colonia = new ArrayList<>();
        Connection con = conexion.getConnection();
        String query = "select * from cluster order by letra asc";
        ResultSet rs = con.createStatement().executeQuery(query);
        while (rs.next()) {
            String item = rs.getInt("id") + "--" +
                    rs.getString("letra") + "--" +
                    rs.getString("cluster");
            colonia.add(item);
        }
        con.close();
        return colonia;
    }

    protected List<String> getTablaCasa() throws Exception {
        List<String> casa = new ArrayList<>();
        Connection con = conexion.getConnection();
        String query2 = "select c.id, c.casa_numero, cl.letra, cl.cluster from casa c inner join cluster cl \n" +
                "where c.id_cluster = cl.id  ";
        ResultSet rs;
        rs = con.createStatement().executeQuery(query2);
        while (rs.next()) {
            String item = rs.getInt("id") + "--" +
                    rs.getInt("casa_numero") + "--" +
                    rs.getString("letra") + "--" +
                    rs.getString("cluster");
            casa.add(item);
        }
        con.close();
        return casa;
    }

    protected List<String> getTablaUsuario() throws Exception {
        List<String> usuario = new ArrayList<>();
        Connection con = conexion.getConnection();
        String query = "select * from usuario";
        ResultSet rs = con.createStatement().executeQuery(query);
        while (rs.next()) {
            String item = rs.getInt("id") + "--" +
                    rs.getString("nombre") + "--" +
                    rs.getString("userName") + "--" +
                    rs.getString("userPassword");
            usuario.add(item);
        }
        con.close();
        return usuario;
    }


}
