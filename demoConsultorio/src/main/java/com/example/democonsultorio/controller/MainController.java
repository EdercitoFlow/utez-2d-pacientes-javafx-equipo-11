package com.example.democonsultorio;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class MainController {

    @FXML private TableView<Paciente> tabla;
    @FXML private TableColumn<Paciente, String> colCurp;
    @FXML private TableColumn<Paciente, String> colNombre;
    @FXML private TableColumn<Paciente, Integer> colEdad;
    @FXML private TableColumn<Paciente, String> colTelefono;
    @FXML private TableColumn<Paciente, String> colEstatus;

    @FXML private Label lblTotal, lblActivos, lblInactivos;

    private PacienteService service = new PacienteService();

    @FXML
    public void initialize() {

        service.cargarArchivo();

        colCurp.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCurp()));
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colEdad.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getEdad()));
        colTelefono.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));
        colEstatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEstatus()));

        tabla.setItems(FXCollections.observableArrayList(service.getLista()));

        actualizarResumen();
    }

    @FXML
    private void abrirFormulario() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/democonsultorio/form.fxml")
            );

            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Nuevo Paciente");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void inactivar() {
        int index = tabla.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            service.inactivar(index);
            tabla.setItems(FXCollections.observableArrayList(service.getLista()));
            actualizarResumen();
        }
    }

    private void actualizarResumen() {
        List<Paciente> lista = service.getLista();

        int total = lista.size();
        long activos = lista.stream().filter(p -> p.getEstatus().equals("ACTIVO")).count();
        long inactivos = total - activos;

        lblTotal.setText("Total: " + total);
        lblActivos.setText("Activos: " + activos);
        lblInactivos.setText("Inactivos: " + inactivos);
    }
}