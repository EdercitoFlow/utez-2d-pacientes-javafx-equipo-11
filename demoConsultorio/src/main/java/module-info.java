module com.example.democonsultorio {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.democonsultorio to javafx.fxml;
    exports com.example.democonsultorio;
}