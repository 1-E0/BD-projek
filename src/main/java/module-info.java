module com.example.bd {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    requires java.sql;


    requires de.jensd.fx.glyphs.fontawesome;


    opens com.example.bd.controller to javafx.fxml;


    opens com.example.bd.model to javafx.base;

    exports com.example.bd;
}