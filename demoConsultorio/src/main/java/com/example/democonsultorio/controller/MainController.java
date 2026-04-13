package com.example.democonsultorio.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.List;
import java.util.Optional;
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
    @FXML private Button btnEliminar;

    private static PacienteService service = new PacienteService();

    @FXML
    public void initialize() {
        service.cargarArchivo();
        colCurp.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("curp"));
        colNombre.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nombre"));
        colEdad.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("edad"));
        colTelefono.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("telefono"));
        colEstatus.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("estatus"));

        btnEliminar.disableProperty().bind(tabla.getSelectionModel().selectedItemProperty().isNull());
        actualizarTabla();
    }

    @FXML
    private void abrirFormulario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/democonsultorio/form.fxml"));
            Stage stage = new Stage();
            // Tamaño ajustado para que luzca mejor desde el inicio
            stage.setScene(new Scene(loader.load(), 400, 450));
            stage.setTitle("Registro de Nuevo Paciente");
            stage.setResizable(false); // Evita que se deforme el diseño
            stage.showAndWait();
            actualizarTabla();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cambiarEstatus() {
        Paciente seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            if (seleccionado.getEstatus().equalsIgnoreCase("ACTIVO")) {
                seleccionado.setEstatus("INACTIVO");
            } else {
                seleccionado.setEstatus("ACTIVO");
            }
            service.guardarEnArchivo();
            actualizarTabla();
        }
    }

    @FXML
    private void eliminar() {
        Paciente seleccionado = tabla.getSelectionModel().getSelectedItem();
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Desea borrar permanentemente a: " + seleccionado.getNombre() + "?");
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            service.getLista().remove(seleccionado);
            service.guardarEnArchivo();
            actualizarTabla();
        }
    }

    @FXML
    private void consultar() {
        TextInputDialog buscar = new TextInputDialog();
        buscar.setTitle("Consultar");
        buscar.setHeaderText("Búsqueda de Paciente");
        buscar.setContentText("Ingrese el nombre:");
        buscar.showAndWait().ifPresent(nombre -> {
            for (Paciente p : tabla.getItems()) {
                if (p.getNombre().equalsIgnoreCase(nombre.trim())) {
                    tabla.getSelectionModel().select(p);
                    tabla.scrollTo(p);
                    return;
                }
            }
        });
    }

    private void actualizarTabla() {
        service.cargarArchivo();
        tabla.setItems(FXCollections.observableArrayList(service.getLista()));
        actualizarResumen();
    }

    private void actualizarResumen() {
        List<Paciente> lista = service.getLista();
        int total = lista.size();
        int activos = 0;
        for (Paciente p : lista) {
            if (p.getEstatus().equalsIgnoreCase("ACTIVO")) activos++;
        }
        lblTotal.setText("Total: " + total);
        lblActivos.setText("Activos: " + activos);
        lblInactivos.setText("Inactivos: " + (total - activos));
    }
}