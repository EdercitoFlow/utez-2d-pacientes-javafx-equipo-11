package com.example.democonsultorio.controller;

public class FormController {
}

package com.example.democonsultorio;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FormController {

    @FXML private TextField txtCurp, txtNombre, txtEdad, txtTelefono, txtAlergias;

    private PacienteService service = new PacienteService();

    @FXML
    private void guardar() {
        try {

            String curp = txtCurp.getText();
            String nombre = txtNombre.getText();
            int edad = Integer.parseInt(txtEdad.getText());
            String telefono = txtTelefono.getText();
            String alergias = txtAlergias.getText();

            if (nombre.length() < 5) throw new Exception("Nombre inválido");
            if (edad < 0 || edad > 120) throw new Exception("Edad inválida");
            if (!telefono.matches("\\d{10}")) throw new Exception("Teléfono inválido");

            Paciente p = new Paciente(curp, nombre, edad, telefono, alergias, "ACTIVO");

            service.cargarArchivo();
            service.agregar(p);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Guardado correctamente");
            alert.show();

            ((Stage) txtCurp.getScene().getWindow()).close();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }
}
