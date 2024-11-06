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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Car {

    @FXML
    private TextField marca; // Марка автомобиля

    @FXML
    private TextField model; // Модель автомобиля

    @FXML
    private Button registr; // Кнопка регистрации

    @FXML
    private TextField vin; // ВИН автомобиля

    @FXML
    private DatePicker year; // Год выпуска автомобиля
    /*
      Метод для вставки данных о новом автомобиле в базу данных
    */
    @FXML
    void initialize() {

        Registration registration=new Registration();

        year.getEditor().focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                try {
                    String dateValue = year.getEditor().getText();
                    // Проверка, что поле у datapicker не пустое
                    if (dateValue != null && !dateValue.isEmpty()) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        LocalDate date = LocalDate.parse(dateValue, formatter);
                        year.setValue(date);
                    }
                } catch (Exception e) {
                    showMessage("Ошибка!", "Введены некорректные значения(dd.mm.yyyy)!");

                    year.getEditor().clear();
                }
            }
        });
        // Минимальная дата
        LocalDate minDate = LocalDate.of(1950, 1, 1);
        // Максимальная дата
        LocalDate maxDate = LocalDate.now();

        year.setDayCellFactory(picker -> new DateCell() {
            @Override

            public void updateItem(LocalDate item, boolean empty) {
                // Проверка, является ли ячейка пустой
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(minDate) || item.isAfter(maxDate));
            }
        });
        year.valueProperty().addListener((obs, oldDate, newDate) -> {
            // Проверяем, если новая дата не является нулевой и находится до minDate или после maxDate
            if (newDate != null && (newDate.isBefore(minDate) || newDate.isAfter(maxDate))) {
                showMessage("Ошибка!", "Вы не можете выбрать дату до 1950 года или в будущем!");
                year.setValue(oldDate);
            }
        });
        /*
          В методе initialize() определена обработка события клика по кнопке регистрации
        */
        registr.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!marca.getText().trim().isEmpty() && !model.getText().trim().isEmpty() && !vin.getText().trim().isEmpty()) {
                    // Проверка заполнены ли поля
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("cabinet.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(fxmlLoader.load(), 455, 315);
                        stage.setTitle("Регистрация");
                        stage.setScene(scene);
                        stage.show();
                        stage.setResizable(false);
                        stage.getIcons().add(new Image("C:/Users/Александр/up/imges/logo.jpg"));
                        Stage stage1 = (Stage) registr.getScene().getWindow();
                        stage1.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    showMessage("Ошибка!", "Заполни все поля");
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
