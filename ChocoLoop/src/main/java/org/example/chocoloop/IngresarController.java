package org.example.chocoloop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.example.chocoloop.DB.conexion;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class IngresarController implements Initializable {
    //--------------------------------JAVAFX-----------------------//
    //---------------------------(-CHOICE BOX----------------------//
    @FXML
    private ChoiceBox<String> CBBebida;
    //--------------------------SPINNERS--------------------------//
    @FXML
    private Spinner<Integer> SPCasa, SPChocobanano, SPChocolate, SPBebida;
    //------------------------TEXT FIELD--------------------------//
    @FXML
    private TextField TFLetra, TFCluster;
    //--------------------------LABEL-----------------------------//
    @FXML
    private Label lblTotal;
    //---------------------------BUTTON--------------------------//
    @FXML
    private Button btnSave;

    //--------------------------DECLARACIONES-------------------------//
    private String casa_letra;
    double chocobananoPrecio = 0.5, chocolatePrecio = 0.5, total, bebidaPrecio;
    Ingreso i = new Ingreso();
    LocalDate date = LocalDate.now();

    //--------------------------FUNCIONES----------------------------//
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            CBBebida.getItems().addAll(getBebidaNombre());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        asignarCluster();
    }

    public double getBebidaPrecio() {
        return bebidaPrecio;
    }

    public void setBebidaPrecio(double bebidaPrecio) {
        this.bebidaPrecio = bebidaPrecio;
    }

    public String getCasa_letra() {
        return casa_letra;
    }

    public void setCasa_letra(String casa_letra) {
        this.casa_letra = casa_letra;
    }

    @FXML
    protected void asignarCluster() {
        TFLetra.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            String letraActual = TFLetra.getText();
            String letraNueva = keyEvent.getCharacter();
            String letraFinal = (letraActual + letraNueva).toUpperCase();

            setCasa_letra(letraFinal);
            List<String> cluster;
            try {
                cluster = getCluster(0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            for (String item : cluster) {
                String[] partes = item.split("--");

                if (Objects.equals(letraFinal, partes[0])) {
                    TFCluster.setText(partes[1]);
                    System.out.println(Objects.equals(letraFinal, "y"));
                    if(Objects.equals(letraFinal, "y")){
                        SPCasa.getValueFactory().setValue(450);
                        System.out.println(SPCasa.getValue());
                    }
                    break;
                }else {
                    TFCluster.setText(null);
                }
            }
        });

    }

    @FXML
    protected void calcular() throws Exception {
        double
                chocobananoTotal = chocobananoPrecio * (double) SPChocobanano.getValue(),
                chocolateTotal = chocolatePrecio * (double) SPChocolate.getValue();
        total = chocobananoTotal + chocolateTotal;
        List<String> bebida = getBebida();
        for (String item : bebida) {
            String[] partes = item.split("--");
            String bebidaName = partes[1];
            double precio = Double.parseDouble(partes[2]);
            if (Objects.equals(CBBebida.getValue(), bebidaName)) {
                System.out.println(bebidaName + " $" + precio);
                total += precio * SPBebida.getValue();
                setBebidaPrecio(precio * SPBebida.getValue());
            }
        }

        i.setTotal(total);
        lblTotal.setText("$" + total);
        btnSave.setDisable(false);
    }

    @FXML
    protected void volverMid() throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("mid-view.fxml"));
        Parent root = loader.load();
        Stage stageThis = (Stage) TFLetra.getScene().getWindow();
        stageThis.close();

        Stage stage = new Stage(); // Crea un nuevo Stage
        stage.setTitle("Sistema");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    @FXML
    protected void reiniciarValores() {
        SPCasa.getValueFactory().setValue(0);
        SPBebida.getValueFactory().setValue(0);
        SPChocolate.getValueFactory().setValue(0);
        SPChocobanano.getValueFactory().setValue(0);

        btnSave.setDisable(true);

        TFLetra.clear();
        TFCluster.clear();

        CBBebida.setValue("");
    }

    @FXML
    protected void guardar() throws Exception {
        saveCasa();
        saveIngreso();
    }

    //------------------------BASE DE DATOS-------------------------//

    protected void saveCasa() throws Exception {
        int numCasa = SPCasa.getValue(), id_clusterLocal = 0;
        String letraCasa = getCasa_letra(), clusterCasa = TFCluster.getText();
        Casa c = new Casa(numCasa, letraCasa, clusterCasa);
        String cluster = c.getCasaLetra() + "--" + c.getCasaCluster();

        List<String> casaList = getCasa(0); // casas guardadas
        List<String> clusterList = getCluster(1); // clusters existentes

        for (String item : clusterList) {
            String[] partes = item.split("--");
            String parteCluster = partes[1] + "--" + partes[2];

            if (parteCluster.equals(cluster)) {
                id_clusterLocal = Integer.parseInt(partes[0]);

                // Verificar si ya existe esa casa con ese cluster
                boolean Existe = false;
                for (String item2 : casaList) {
                    String[] partes2 = item2.split("--");
                    int numExistente = Integer.parseInt(partes2[1]);
                    int idClusterExistente = Integer.parseInt(partes2[2]);

                    if (numExistente == numCasa && idClusterExistente == id_clusterLocal) {
                        Existe = true;
                        break;
                    }
                }

                if (!Existe) {
                    saveCasa(c.getCasaNum(), id_clusterLocal);
                }
                break;
            }
        }
    }


    protected void saveCasa(int numero, int cluster) throws Exception {
        Connection con = conexion.getConnection();
        System.out.println(numero + "--" + cluster);
        String query = "insert into casa (casa_numero, id_cluster) values (?,?)";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, numero);
        stmt.setInt(2, cluster);
        stmt.executeUpdate();
        con.close();
    }

    protected void saveIngreso() throws Exception {
        String letra = getCasa_letra(), cluster = TFCluster.getText();
        int casaNum = SPCasa.getValue();
        int id_bebida = getBebidaName(CBBebida.getValue()),
                chocobananoCant = SPChocobanano.getValue(),
                chocolateCant = SPChocolate.getValue(),
                bebidaCant = SPBebida.getValue();

        double chocobananoTotal = chocobananoPrecio * chocobananoCant,
                chocolateTotal = chocolatePrecio * chocolateCant,
                bebidaTotal = getBebidaPrecio(),
                total = i.getTotal();

        String fecha = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<String> casaList = getCasa(1); // Obtener lista de casas

        boolean ingresoGuardado = false;

        for (String item : casaList) {
            String[] partes = item.split("--");
            int id_casa = Integer.parseInt(partes[0]), casa_num = Integer.parseInt(partes[1]);
            String parteLetra = partes[2], parteCluster = partes[3];

            if (Objects.equals(parteLetra, letra) && Objects.equals(parteCluster, cluster)
                && Objects.equals(casa_num, casaNum)) {
                System.out.println(id_casa+ "||"+ casa_num + "||" +parteLetra + "||" + parteCluster);
                i = new Ingreso(id_casa, id_bebida, chocobananoCant, chocolateCant, bebidaCant,
                        chocobananoTotal, chocolateTotal, bebidaTotal, total, fecha);
                i.saveIngreso();
                System.out.println("Se guardo con exito");
                ingresoGuardado = true;
                break; // Salir después de guardar
            }
        }

        if (!ingresoGuardado) {
            System.err.println("No se encontró la casa para guardar el ingreso.");
        }
    }


    protected List<String> getBebidaNombre() throws Exception {
        List<String> bebida = new ArrayList<>();
        Connection con = conexion.getConnection();
        String query = "select bebida from bebida order by bebida asc";
        ResultSet rs = con.createStatement().executeQuery(query);
        while (rs.next()) {
            String item = rs.getString("bebida");
            bebida.add(item);
        }
        con.close();
        return bebida;
    }

    protected List<String> getBebida() throws Exception {
        List<String> bebida = new ArrayList<>();
        Connection con = conexion.getConnection();
        String query = "select * from bebida order by bebida asc";
        ResultSet rs = con.createStatement().executeQuery(query);
        while (rs.next()) {
            String item = rs.getInt("id") + "--" +
                    rs.getString("bebida") + "--" +
                    rs.getString("precio");
            bebida.add(item);
        }
        con.close();
        return bebida;
    }

    protected int getBebidaName(String name) throws Exception {
        int id = 0;
        if (name != null) {
            Connection con = conexion.getConnection();
            String query = "select id from bebida where bebida = ?";
            PreparedStatement psmt = con.prepareStatement(query);
            psmt.setString(1, name);
            ResultSet rs = psmt.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
            }
        } else {
            id = 1;
        }

        return id;
    }

    protected List<String> getCluster(int i) throws Exception {
        List<String> colonia = new ArrayList<>();
        Connection con = conexion.getConnection();
        String query = "select * from cluster order by letra asc";
        ResultSet rs = con.createStatement().executeQuery(query);
        if (i == 0) { // retorna solo letra y cluster
            while (rs.next()) {
                String item = rs.getString("letra") + "--" + rs.getString("cluster");
                colonia.add(item);
            }
        } else if (i == 1) { // retorna toda la tabla
            while (rs.next()) {
                String item = rs.getInt("id") + "--" +
                        rs.getString("letra") + "--" +
                        rs.getString("cluster");
                colonia.add(item);
            }
        }
        con.close();
        return colonia;
    }

    protected List<String> getCasa(int i) throws Exception {
        List<String> casa = new ArrayList<>();
        Connection con = conexion.getConnection();
        String query1 = "select * from casa";
        String query2 = "select c.id, c.casa_numero, cl.letra, cl.cluster from casa c inner join cluster cl \n" +
                "where c.id_cluster = cl.id  ";
        ResultSet rs;
        if (i == 0) {
            rs = con.createStatement().executeQuery(query1);
            while (rs.next()) {
                String item = rs.getInt("id") + "--" +
                        rs.getInt("casa_numero") + "--" +
                        rs.getInt("id_cluster");
                casa.add(item);
            }
        } else if (i == 1) {
            rs = con.createStatement().executeQuery(query2);
            while (rs.next()) {
                String item = rs.getInt("id") + "--" +
                        rs.getInt("casa_numero") + "--" +
                        rs.getString("letra") + "--" +
                        rs.getString("cluster");
                casa.add(item);
            }
        }

        con.close();
        return casa;
    }
}
