package com.example.democonsultorio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // La ruta debe empezar con / y seguir la estructura de resources
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/democonsultorio/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Consultorio");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}