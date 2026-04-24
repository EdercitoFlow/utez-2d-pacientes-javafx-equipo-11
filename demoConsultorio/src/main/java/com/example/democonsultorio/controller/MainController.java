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
    @FXML private TableColumn<Paciente, String> colAlergias;
    @FXML private TableColumn<Paciente, String> colEstatus;

    @FXML private Label lblTotal, lblActivos, lblInactivos;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;

    private static PacienteService service = new PacienteService();

    @FXML
    public void initialize() { //Carga datos iniciales
        service.cargarArchivo();
        colCurp.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("curp"));
        colNombre.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nombre"));
        colEdad.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("edad"));
        colTelefono.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("telefono"));

        colEstatus.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("estatus"));

        btnEliminar.disableProperty().bind(tabla.getSelectionModel().selectedItemProperty().isNull());
        btnEditar.disableProperty().bind(tabla.getSelectionModel().selectedItemProperty().isNull());

        actualizarTabla(); //Aparezcan los pacientes visualmente
        colAlergias.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("alergias"));
    }

    @FXML
    private void abrirFormulario() {
        try { //Medida de seguridad
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/democonsultorio/form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load(), 400, 450));
            stage.setTitle("Registro de Nuevo Paciente");
            stage.setResizable(false);
            stage.showAndWait();
            actualizarTabla();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editar() {
        Paciente seleccionado = tabla.getSelectionModel().getSelectedItem(); //Se guarda el paciente seleccionado
        if (seleccionado != null) { //Validación
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/democonsultorio/form.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load(), 400, 450));

                FormController controller = loader.getController(); //Ahora Main tiene el control de FormController
                controller.prepararEdicion(seleccionado);   //Pasa paciente de la tabla al otro controlador y se vuelve !=null

                stage.setTitle("Editar Paciente");
                stage.setResizable(false);
                stage.showAndWait();

                service.guardarEnArchivo(); //Asegurar cambios en DD
                actualizarTabla();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void cambiarEstatus() {
        Paciente seleccionado = tabla.getSelectionModel().getSelectedItem(); //Revisa que linea esta seleccionada
        if (seleccionado != null) {
            if (seleccionado.getEstatus().equalsIgnoreCase("ACTIVO")) { //equals ignora las mayusculas o minusculas
                seleccionado.setEstatus("INACTIVO");
            } else {
                seleccionado.setEstatus("ACTIVO");
            }
            service.guardarEnArchivo(); //Manda cambios al DD

            tabla.refresh(); //Mas rapido que actualizartabla porque solo cambiamos un dato y no más
            actualizarResumen(); //Vuelve a contar los estatus
        }
    }

    @FXML
    private void eliminar() {
        Paciente seleccionado = tabla.getSelectionModel().getSelectedItem(); //Verifica linea seleccionada
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Desea borrar permanentemente a: " + seleccionado.getNombre() + "?");
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) { //Solo si se selecciono Aceptar
            service.getLista().remove(seleccionado); //Borrado local
            service.guardarEnArchivo(); //Reescribe el archivo
            actualizarTabla(); //Muestra nuevamente visualmente
        }
    }

    @FXML
    private void consultar() {
        TextInputDialog buscar = new TextInputDialog();
        buscar.setTitle("Consultar");
        buscar.setHeaderText("Búsqueda de Paciente");
        buscar.setContentText("Ingrese el nombre:");
        buscar.showAndWait().ifPresent(nombre -> { //Se ejecuta si se escribio algo y "Aceptar"
            for (Paciente p : tabla.getItems()) { //Revisa lo que hay en la tabla
                if (p.getNombre().equalsIgnoreCase(nombre.trim())) {    //Ignora mayus y elimina espacios mal usados
                    tabla.getSelectionModel().select(p); //Señala la persona seleccionada
                    tabla.scrollTo(p);
                    return;
                }
            }
        });
    }

    private void actualizarTabla() {
        service.cargarArchivo(); //Trae la nueva info
        tabla.setItems(FXCollections.observableArrayList(service.getLista())); //Refresh tabla
        actualizarResumen();
    }

    private void actualizarResumen() {
        List<Paciente> lista = service.getLista(); //Trae lista
        int total = lista.size();
        int activos = 0;
        for (Paciente p : lista) { //Revisa uno por uno
            if (p.getEstatus().equalsIgnoreCase("ACTIVO")) activos++;
        }//Sumatoria
        lblTotal.setText("Total: " + total);
        lblActivos.setText("Activos: " + activos);
        lblInactivos.setText("Inactivos: " + (total - activos));
    }
}