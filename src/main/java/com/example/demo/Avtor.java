package com.example.demo;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Avtor {
    @FXML
    private TextField idLogin; // Логин

    @FXML
    private PasswordField idPassword; // Пароль
    @FXML
    private TextField idpas2; // Пароль на видимой части
    @FXML
    private CheckBox pokaz; // Кнопка для показа пароля
    @FXML
    private Button registr; // Кнопка регистрации
    @FXML
    private Button vxod; // Кнопка входа

    DB db = new DB();
        /*
          Метод changeVisibility() отвечает за изменение видимости полей ввода пароля.
        */
    @FXML
    void changeVisibility() {
        if (pokaz.isSelected()) {
            idpas2.setText(idPassword.getText());
            idpas2.setVisible(true);
            idPassword.setVisible(false);
            return;
        }
        idPassword.setText(idpas2.getText());
        idPassword.setVisible(true);
        idpas2.setVisible(false);
    }

    @FXML
    void initialize() {
        idpas2.setVisible(false);
        // Обработчик события. Сработает при нажатии на кнопку
        vxod.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            // Метод, что будет срабатывать
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    if (pokaz.isSelected()) {
                        idPassword.setText(idpas2.getText());
                    }
                    // Проверяем является ли поле заполненным
                    if (!idLogin.getText().trim().equals("") & !idPassword.getText().trim().equals("")) {
                        // Вызываем метод из класса DB
                        // через этот метод будет добавлено новое задание
                        int a = db.getUser(idLogin.getText(), idPassword.getText());
                        if (a == 2) {
                            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sotrudnic.fxml"));
                            Stage stage = new Stage();
                            Scene scene = new Scene(fxmlLoader.load(), 1300, 655);
                            stage.setTitle("Личный кабинет: Сотрудника");
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.show();
                            stage.getIcons().add(new Image("C:/Users/Александр/up/imges/logo.jpg"));
                        }
                        if (a == 0) {
                            showMessage("Ошибка!", "Неверные данные");
                        }
                        if (a == 1) {
                            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
                            Stage stage = new Stage();
                            Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
                            stage.setTitle("Личный кабинет: Клиента");
                            stage.setScene(scene);
                            stage.show();
                            stage.setResizable(false);
                            stage.getIcons().add(new Image("C:/Users/Александр/up/imges/logo.jpg"));
                            Client controller = fxmlLoader.getController();
                            controller.setId(db.getIdUser(idLogin.getText(), idPassword.getText()));
                            controller.loadPoducts();
                        }
                    } else {
                        showMessage("Ошибка!", "Неверные данные");
                    }
                } catch (SQLException e) { // Отслеживаем ошибки
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // Обработчик события. Сработает при нажатии на кнопку
        registr.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            /*
              Переход на окно регистрации
            */
            @Override
            public void handle(MouseEvent mouseEvent) {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("registration.fxml"));
                Stage stage = new Stage();
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 450, 400);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setTitle("Регистрация");
                stage.setScene(scene);
                stage.show();
                stage.getIcons().add(new Image("C:/Users/Александр/up/imges/logo.jpg"));
                stage.setResizable(false);
                Stage currentStage = (Stage) vxod.getScene().getWindow();
                currentStage.close();
            }
        });
    }
    /*
      Метод отображает сообщение на экране с заданным заголовком и текстом
    */
    void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        ImageView imageView = new ImageView(new Image("C:/Users/Александр/up/imges/showe.jpg"));
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);
        alert.setGraphic(imageView);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("C:/Users/Александр/up/imges/logo.jpg"));

        alert.showAndWait();
    }
}