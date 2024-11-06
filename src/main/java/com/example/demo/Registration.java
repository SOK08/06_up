package com.example.demo;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Registration {

    public static Integer id; // Статическая переменная id
    @FXML
    private TextField adress; // Адрес клиента
    public static String fam, imia, telethon, addresss; // Статические переменные фамилия, имя, телефон и адрес

    @FXML
    private TextField fio; // Фамилия клиента

    @FXML
    private TextField ima; // Имя клиента

    @FXML
    private TextField phone; // Телефон клиента
    @FXML
    private Button idcar; // Кнопка регистрации машины

    DB db = new DB();

    @FXML
    void initialize() {
        /*
          Метод устанавливает текст в текстовые поля
        */
        fio.setText(fam);
        ima.setText(telethon);
        phone.setText(addresss);
        adress.setText(imia);
        idcar.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    if (fio.getText() != null && ima.getText() != null && phone.getText() != null && adress.getText() != null) {
                        // Проверка заполнены ли поля
                        if (!fio.getText().trim().equals("") && !ima.getText().trim().equals("") && !phone.getText().trim().equals("") && !adress.getText().trim().equals("")) {
                            // Проверка заполнены ли поля
                            if (!db.isPhoneNumberExists(phone.getText())) {
                                db.insertuser(fio.getText(), ima.getText(), phone.getText(), adress.getText());
                                id = db.getuserid(phone.getText());
                                fam = fio.getText();
                                telethon = ima.getText();
                                addresss = phone.getText();
                                imia = adress.getText();
                                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("car.fxml"));
                                Stage stage = new Stage();
                                Scene scene = new Scene(fxmlLoader.load(), 570, 410);
                                stage.setTitle("Добавление автомобиля");
                                stage.setScene(scene);
                                stage.show();
                                stage.setResizable(false);
                                stage.getIcons().add(new Image("C:/Users/Александр/up/imges/logo.jpg"));
                                Stage stage1 = (Stage) idcar.getScene().getWindow();
                                stage1.close();
                            } else {
                                showMessage("Ошибка!", "Телефонный номер уже существует");
                            }
                        } else {
                            showMessage("Ошибка!", "Заполни все ячейки сначала");
                        }
                    } else {
                        showMessage("Ошибка!", "Заполни все ячейки сначала");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
