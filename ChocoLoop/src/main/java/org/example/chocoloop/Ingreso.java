package org.example.chocoloop;

import org.example.chocoloop.DB.conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Ingreso {
    private int id_casa,
            id_bebida,
            chocobanano_cant,
            chocolate_cant,
            bebida_cant;
    private double chocobanano_total,
            chocolate_total,
            bebida_total,
            total;
    private String fecha;

    public Ingreso() {
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Ingreso(int id_casa, int id_bebida, int chocobanano_cant, int chocolate_cant, int bebida_cant, double chocobanano_total, double chocolate_total, double bebida_total, double total, String fecha) {
        this.id_casa = id_casa;
        this.id_bebida = id_bebida;
        this.chocobanano_cant = chocobanano_cant;
        this.chocolate_cant = chocolate_cant;
        this.bebida_cant = bebida_cant;
        this.chocobanano_total = chocobanano_total;
        this.chocolate_total = chocolate_total;
        this.bebida_total = bebida_total;
        this.total = total;
        this.fecha = fecha;
    }

    public int getId_casa() {
        return id_casa;
    }

    public int getId_bebida() {
        return id_bebida;
    }

    public int getChocobanano_cant() {
        return chocobanano_cant;
    }

    public int getChocolate_cant() {
        return chocolate_cant;
    }

    public int getBebida_cant() {
        return bebida_cant;
    }

    public double getChocobanano_total() {
        return chocobanano_total;
    }

    public double getChocolate_total() {
        return chocolate_total;
    }

    public double getBebida_total() {
        return bebida_total;
    }

    public double getTotal() {
        return total;
    }

    public String getFecha() {
        return fecha;
    }

    //----------------------BASE DE DATOS--------------------//
    protected void saveIngreso() throws Exception {
        int id_casa = getId_casa(), id_bebida = getId_bebida(),
                chocobananoCant = getChocobanano_cant(), chocolateCant = getChocolate_cant(), bebidaCant = getBebida_cant();
        double chocobananoTotal = getChocobanano_total(), chocolateTotal = getChocolate_total(), bebidaTotal = getBebida_total(),
                total = getTotal();
        String fecha = getFecha();

        Connection con = conexion.getConnection();
        String query =
                "insert into ingreso(id_casa, id_bebida, " +
                        "chocobanano_cant, chocolate_cant, bebida_cant, " +
                        "chocobanano_total, chocolate_total, bebida_total, " +
                        "total, fecha) " +
                        "values (?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, id_casa);
        pstmt.setInt(2, id_bebida);
        pstmt.setInt(3, chocobananoCant);
        pstmt.setInt(4, chocolateCant);
        pstmt.setInt(5, bebidaCant);
        pstmt.setDouble(6, chocobananoTotal);
        pstmt.setDouble(7, chocolateTotal);
        pstmt.setDouble(8, bebidaTotal);
        pstmt.setDouble(9, total);
        pstmt.setString(10, fecha);

        pstmt.executeUpdate();
        con.close();
    }

    protected List<String> getIngreso() throws Exception {
        List<String> ingreso = new ArrayList<>();
        Connection con = conexion.getConnection();
        String query = "select * from ingreso";
        ResultSet rs = con.createStatement().executeQuery(query);
        while(rs.next()){
            String item = rs.getInt("id_casa") + "--" +
            rs.getInt("id_bebida") + "--" +
            rs.getInt("chocobanano_Cant") + "--" +
            rs.getInt("chocolate_Cant") + "--" +
            rs.getInt("bebida_Cant") + "--" +
            rs.getDouble("chocobanano_Total") + "--" +
            rs.getDouble("chocolate_Total") + "--" +
            rs.getDouble("bebida_Total") + "--" +
            rs.getDouble("total") + "--" +
            rs.getString("fecha");

            ingreso.add(item);
        }
        con.close();
        return ingreso;
    }
}
