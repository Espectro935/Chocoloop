package org.example.chocoloop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    //------------------TABLE VIEW-------------------------//
    @FXML
    private TableView<Ingreso> TVIngreso;
    @FXML
    private TableColumn<Ingreso, String> colIdCasa, colIdBebida,
            colChocobananoCant, colChocolateCant, colBebidaCant,
            colChocobananoTotal, colChocolateTotal, colBebidaTotal, colTotal,
            colFecha;
    //-----------------DECLARACIONES------------------------//
    String[] items = {"ingreso", "Casa", "bebida", "Usuario"};
    Alert alertError = new Alert(Alert.AlertType.ERROR);
    Ingreso ingreso = new Ingreso();
    //Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);

    //-----------------FUNCIONES----------------------------//
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            colIdCasa.setCellValueFactory(new PropertyValueFactory<>("id_casa"));
            colIdBebida.setCellValueFactory(new PropertyValueFactory<>("id_bebida"));
            colChocobananoCant.setCellValueFactory(new PropertyValueFactory<>("chocobanano_cant"));
            colChocolateCant.setCellValueFactory(new PropertyValueFactory<>("chocolate_cant"));
            colBebidaCant.setCellValueFactory(new PropertyValueFactory<>("bebida_cant"));
            colChocobananoTotal.setCellValueFactory(new PropertyValueFactory<>("chocobanano_total"));
            colChocolateTotal.setCellValueFactory(new PropertyValueFactory<>("chocolate_total"));
            colBebidaTotal.setCellValueFactory(new PropertyValueFactory<>("bebida_total"));
            colTotal.setCellValueFactory(new PropertyValueFactory<>("Total"));
            colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

            ObservableList<Ingreso> listaIngreso = FXCollections.observableArrayList();

            for (String item : ingreso.getIngreso()) {
                String[] parts = item.split("--");
                int idCasa = Integer.parseInt(parts[0]);
                int idBebida = Integer.parseInt(parts[1]);
                int chocobananoCant = (int) Double.parseDouble(parts[2]);
                int chocolateCant = (int) Double.parseDouble(parts[3]);
                int bebidaCant = (int) Double.parseDouble(parts[4]);
                double chocobananoTotal = Double.parseDouble(parts[5]);
                double chocolateTotal = Double.parseDouble(parts[6]);
                double bebidaTotal = Double.parseDouble(parts[7]);
                double total = Double.parseDouble(parts[8]);
                String fecha = parts[9];

                listaIngreso.add(new Ingreso(idCasa, idBebida, chocobananoCant, chocolateCant,
                        bebidaCant, chocobananoTotal, chocolateTotal, bebidaTotal, total, fecha));
            }

            TVIngreso.setItems(listaIngreso);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
