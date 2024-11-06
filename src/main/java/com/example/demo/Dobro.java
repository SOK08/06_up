package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class Dobro {
    @FXML
    private Button nachal; // Кнопка входа
    @FXML
    void nachalo() throws IOException {
        /*
          Переход на окно авторизации
        */
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("avtor.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Автосервис");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.getIcons().add(new Image("C:/Users/Александр/up/imges/logo.jpg"));
        Stage stage1=(Stage) nachal.getScene().getWindow();
        stage1.close();
    }
}
