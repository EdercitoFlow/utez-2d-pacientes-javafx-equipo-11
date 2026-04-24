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
    private Paciente pacienteEnEdicion; // Variable que se declara en null

    @FXML
    public void initialize() {
        btnGuardar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 13px;");
        btnGuardar.setOnMouseEntered(e -> btnGuardar.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 13px;"));
        btnGuardar.setOnMouseExited(e -> btnGuardar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 13px;"));
    }

    //Llama los datos de este objeto en cuadros de texto
    public void prepararEdicion(Paciente p) {
        //Si existe trae la información de ese paciente
        this.pacienteEnEdicion = p;
        txtCurp.setText(p.getCurp());
        txtNombre.setText(p.getNombre());
        txtEdad.setText(String.valueOf(p.getEdad()));
        txtTelefono.setText(p.getTelefono());

        txtCurp.setEditable(false);
    }

    @FXML
    private void guardar() {
        //Medida de seguridad para saltar error amigable
        try {
            String curp = txtCurp.getText();
            String nombre = txtNombre.getText();
            int edad = Integer.parseInt(txtEdad.getText());
            String telefono = txtTelefono.getText();
            String alergias = txtAlergias.getText();

            // Restricciones en variables
            if (curp.isEmpty()) throw new Exception("CURP obligatorio");
            if (edad < 0 || edad > 120) throw new Exception("Edad inválida");
            if (!telefono.matches("\\d{10}")) throw new Exception("Teléfono inválido");

            service.cargarArchivo();
            //Editable, se usan: setter objeto cambiar valores internos
            if (pacienteEnEdicion != null) {
                pacienteEnEdicion.setNombre(nombre);
                pacienteEnEdicion.setEdad(edad);
                pacienteEnEdicion.setTelefono(telefono);
                pacienteEnEdicion.setAlergias(alergias);
            } else {
                //Nuevo, por qué:
                Paciente nuevo = new Paciente(curp, nombre, edad, telefono, alergias, "ACTIVO");
                service.agregar(nuevo); //ArrayList
            }

            service.guardarEnArchivo();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(pacienteEnEdicion != null ? "Actualizado correctamente" : "Guardado correctamente");
            alert.showAndWait();

            ((Stage) txtCurp.getScene().getWindow()).close(); //Cerrar ventana con campo CURP

        } catch (NumberFormatException e) {
            mostrarError("La edad debe ser un número");
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}