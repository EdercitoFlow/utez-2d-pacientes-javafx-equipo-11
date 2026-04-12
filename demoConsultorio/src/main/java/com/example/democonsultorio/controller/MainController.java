package com.example.democonsultorio.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.List;
import com.example.democonsultorio.service.PacienteService;
import com.example.democonsultorio.model.Paciente;

public class MainController {

    @FXML private TableView<Paciente> tabla;
    @FXML private TableColumn<Paciente, String> colCurp;
    @FXML private TableColumn<Paciente, String> colNombre;
    @FXML private TableColumn<Paciente, Integer> colEdad;
    @FXML private TableColumn<Paciente, String> colTelefono;
    @FXML private TableColumn<Paciente, String> colEstatus;

    @FXML private Label lblTotal, lblActivos, lblInactivos;

    private static PacienteService service = new PacienteService();

    @FXML
    public void initialize() {
        service.cargarArchivo();
        colCurp.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("curp"));
        colNombre.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nombre"));
        colEdad.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("edad"));
        colTelefono.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("telefono"));
        colEstatus.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("estatus"));

        actualizarTabla();
    }

    @FXML
    private void abrirFormulario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/democonsultorio/nuevo-paciente.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Nuevo Paciente");
            stage.showAndWait();

            actualizarTabla();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void consultar() {
        Paciente seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            mostrarAlerta("Consulta de Paciente",
                    "Nombre: " + seleccionado.getNombre() + "\n" +
                            "CURP: " + seleccionado.getCurp() + "\n" +
                            "Estatus: " + seleccionado.getEstatus());
        } else {
            mostrarAlerta("Atención", "Selecciona un paciente para consultar.");
        }
    }

    @FXML
    private void actualizar() {
        Paciente seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("Editando a: " + seleccionado.getNombre());
        } else {
            mostrarAlerta("Atención", "Selecciona un paciente para actualizar.");
        }
    }

    private void actualizarTabla() {
        service.cargarArchivo();
        tabla.setItems(FXCollections.observableArrayList(service.getLista()));
        actualizarResumen();
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

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}