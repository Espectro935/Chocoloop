module org.example.chocoloop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens org.example.chocoloop to javafx.fxml;
    exports org.example.chocoloop;
}