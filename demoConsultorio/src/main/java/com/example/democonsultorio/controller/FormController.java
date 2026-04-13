package com.example.democonsultorio.controller;

import com.example.democonsultorio.model.Paciente;
import com.example.democonsultorio.service.PacienteService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FormController {

    @FXML private TextField txtCurp, txtNombre, txtEdad, txtTelefono, txtAlergias;
    @FXML private Button btnGuardar;

    private static PacienteService service = new PacienteService();

    @FXML
    public void initialize() {

        btnGuardar.setStyle(
                "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 8;" +
                        "-fx-font-size: 13px;"
        );

        btnGuardar.setOnMouseEntered(e ->
                btnGuardar.setStyle(
                        "-fx-background-color: #45a049;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 8;" +
                                "-fx-font-size: 13px;"
                )
        );

        btnGuardar.setOnMouseExited(e ->
                btnGuardar.setStyle(
                        "-fx-background-color: #4CAF50;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 8;" +
                                "-fx-font-size: 13px;"
                )
        );
    }

    @FXML
    private void guardar() {
        try {
            String curp = txtCurp.getText();
            String nombre = txtNombre.getText();
            int edad = Integer.parseInt(txtEdad.getText());
            String telefono = txtTelefono.getText();
            String alergias = txtAlergias.getText();

            if (curp.isEmpty()) throw new Exception("CURP obligatorio");
            if (edad < 0 || edad > 120) throw new Exception("Edad inválida");
            if (!telefono.matches("\\d{10}")) throw new Exception("Teléfono inválido");

            Paciente p = new Paciente(curp, nombre, edad, telefono, alergias, "ACTIVO");

            service.cargarArchivo();
            service.agregar(p);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Guardado correctamente");
            alert.showAndWait();

            ((Stage) txtCurp.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("La edad debe ser un número");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }
}