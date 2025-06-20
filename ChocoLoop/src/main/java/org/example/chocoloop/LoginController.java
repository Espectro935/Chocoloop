package org.example.chocoloop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.example.chocoloop.DB.conexion;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public class LoginController implements Initializable  {
    //------------------------JAVAFX-----------------------------//
    //-----------------------TEXT FIELD-----------------------------//
    @FXML
    private TextField TFUser, TFPassword;
    //------------------------PANE---------------------------------//
    @FXML
    private Pane PLogin;
    //------------------------LABEL---------------------------------//
    @FXML
    private Label lblConexion;

    //------------------------DECLARACIONES---------------------//
    Alert alert = new Alert(Alert.AlertType.ERROR);
    Usuario user = new Usuario();

    //--------------------------FUNCIONES------------------------//
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            if (isDatabaseConnected()) {
                lblConexion.setText("CONECTADA");
                lblConexion.setTextFill(Paint.valueOf("#008000"));
            } else {
                lblConexion.setText("DESCONECTADA");
                lblConexion.setTextFill(Paint.valueOf("#ef1111"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void validarUsuario() throws Exception {
        List<Usuario> lista = returnUser();
        String userName = TFUser.getText(), userPassword = TFPassword.getText();
        for (Usuario u : lista) {
            if(Objects.equals(userName, u.getUserName()) && Objects.equals(userPassword, u.getUserPassword())){
                abrirMidController();
            }else{
                alert.setTitle("ERROR");
                alert.setHeaderText("Credenciales invalidas");
                alert.showAndWait();
            }
        }
    }

    protected void abrirMidController() throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("mid-view.fxml"));
        Parent root = loader.load();
        Stage stageThis = (Stage) TFPassword.getScene().getWindow();
        stageThis.close();

        Stage stage = new Stage(); // Crea un nuevo Stage
        stage.setTitle("Sistema");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    //----------------------------BASE DE DATOS---------------------------//

    public boolean isDatabaseConnected() throws Exception {
        try (Connection connection = conexion.getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    protected List<Usuario> returnUser() throws Exception {
        List<Usuario> lista = new ArrayList<>();
        Connection con = conexion.getConnection();
        String sql = "select * from Usuario";
        ResultSet rs = con.createStatement().executeQuery(sql);
        while(rs.next()){
            user = new Usuario(rs.getString("nombre"), rs.getString("userName"), rs.getString("userPassword"));
            lista.add(user);
        }
        con.close();
        return lista;
    }
}