module com.example.democonsultorio {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.democonsultorio to javafx.graphics, javafx.fxml;
    exports com.example.democonsultorio;


    opens com.example.democonsultorio.controller to javafx.fxml;
    exports com.example.democonsultorio.controller;


    opens com.example.democonsultorio.model to javafx.base;
    exports com.example.democonsultorio.model;


    opens com.example.democonsultorio.service to javafx.fxml;
    exports com.example.democonsultorio.service;
}