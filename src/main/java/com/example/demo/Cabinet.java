package com.example.demo;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Cabinet {

    @FXML
    private Label log; // Логин клиента

    @FXML
    private Label pas; // Пароль клиента

    @FXML
    private Button avtor; // Кнопка для возращения на окно авторизации
    DB db=new DB();
    /*
      Метод использоваться для получения данных о пользователе пароля и логина из базы данных
    */
    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
       Registration registration=new Registration();
       log.setText(db.getuserlog(registration.id));
       pas.setText(db.getuserpas(registration.id));
       /*
         В методе initialize() определена обработка события клика по кнопке регистрации
       */
       avtor.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
       @Override
       public void handle(MouseEvent mouseEvent) {
           /*
             Переход на окно авторизации
           */
               try {
                   FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("avtor.fxml"));
                   Stage stage = new Stage();
                   Scene scene = new Scene(fxmlLoader.load(), 568, 345);
                   stage.setTitle("Авторизация");
                   stage.setScene(scene);
                   stage.show();
                   stage.setResizable(false);
                   stage.getIcons().add(new Image("C:/Users/Александр/up/imges/logo.jpg"));
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
       }
       });
   }
}
