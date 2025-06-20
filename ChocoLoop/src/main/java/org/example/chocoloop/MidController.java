package org.example.chocoloop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.chocoloop.DB.conexion;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MidController implements Initializable {
    //-------------------JAVAFX-----------------------------//
    //------------------CHOICE BOX-------------------------//
    @FXML
    private ChoiceBox<String> CBItem;
    //-------------------LIST VIEW-------------------------//
    @FXML
    private ListView<String> LVDatos;
    //-----------------DECLARACIONES------------------------//
    String[] items = {"ingreso", "Casa", "bebida", "Usuario"};
    Alert alertError = new Alert(Alert.AlertType.ERROR);
    Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);

    //-----------------FUNCIONES----------------------------//
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        CBItem.getItems().addAll(items);
    }

    @FXML
    protected void abrirIngreso() throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("ingresar-view.fxml"));
        Parent root = loader.load();
        Stage stageThis = (Stage) CBItem.getScene().getWindow();
        stageThis.close();

        Stage stage = new Stage(); // Crea un nuevo Stage
        stage.setTitle("Ingresar");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
    //------------------BASE DE DATOS------------------------//
    protected List<String> returnCasa() throws Exception{
        List<String> casa = new ArrayList<>();
        Connection con = conexion.getConnection();
        String query = "select * from Casa";
        ResultSet rs = con.createStatement().executeQuery(query);
        while(rs.next()){
            String item = rs.getString("casa_numero") + "//" + rs.getString("casa_letra") + "//" +
                    rs.getString("casa_cluster");
            casa.add(item);
        }

        return casa;
    }

    protected List<String> returnUsuario() throws Exception{
        List<String> usuario = new ArrayList<>();
        Connection con = conexion.getConnection();
        String query = "select * from Usuario ";
        ResultSet rs = con.createStatement().executeQuery(query);
        while(rs.next()){
            String item = rs.getString("nombre") + "//" + rs.getString("userName") + "//" +
                    rs.getString("userPassword");
            usuario.add(item);
        }

        return usuario;
    }

    protected List<String> returnBebida() throws Exception{
        List<String> bebida = new ArrayList<>();
        Connection con = conexion.getConnection();
        String query = "select * from bebida order by bebida asc";
        ResultSet rs = con.createStatement().executeQuery(query);
        while(rs.next()){
            String item = rs.getString("bebida") + "// $" + rs.getString("precio");
            bebida.add(item);
        }
        con.close();
        return bebida;
    }

    @FXML
    protected void showData() throws Exception{
        if(CBItem.getValue() != null){
            String item = CBItem.getValue();

            LVDatos.getItems().clear();
            switch (item){
                case "ingreso":
                    break;
                case "Casa":
                    LVDatos.getItems().addAll(returnCasa());
                    break;
                case "bebida":
                    LVDatos.getItems().add("PRODUCTO//PRECIO");
                    LVDatos.getItems().addAll(returnBebida());
                    break;
                case "Usuario":
                    LVDatos.getItems().addAll(returnUsuario());
                    break;
                default:
                    alertError.setHeaderText("Seleccione una opcion valida");
                    break;
            }
        }else{
            System.err.println("Esta vacio");
        }
    }
}
