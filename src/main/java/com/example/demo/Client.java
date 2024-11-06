package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private int idClient; // Переменная id клиента
    @FXML
    private DatePicker dataa; // Выбор даты на запись

    @FXML
    private ListView<String> list_basket; // Список заказов

    @FXML
    private ListView<Integer> list_count; // Количество заказа

    @FXML
    private ListView<String> list_price; // Список цен на товары

    @FXML
    private ListView<String> list_product; // Список услуг

    @FXML
    private ListView<String> list_time; // Список доступного времени для записи

    @FXML
    private Label sis; // Окно вывода информацию о заказе

    @FXML
    private Label status; // Статус заказа

    @FXML
    private Label sum_price; // Сумма товаров в корзине

    @FXML
    private TextField zamechanie; // Замечание клиента о заказе

    @FXML
    private Label selectedTime;  // Выбранное время пользователем

    DB db = null;
    /*
       Метод переданное id клиента в качестве аргумента
    */
    public void setId(int id){
        this.idClient = id;
    }

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        db = new DB();// Инициируем объект
        loadPoducts(); // Загружаем список товаров

        // Добавляем цены на товары
        ArrayList<String> list_prices = db.getPrice();
        ObservableList<String> langs_prices = FXCollections.observableArrayList();
        langs_prices.addAll(list_prices);
        list_price.setItems(langs_prices);

    }
    void loadPoducts() throws SQLException, ClassNotFoundException {
        // Заполяем список услуг
        ArrayList<String> list_products = db.getProducts();
        ObservableList<String> langs = FXCollections.observableArrayList();
        langs.addAll(list_products);
        list_product.setItems(FXCollections.observableArrayList());
        list_product.setItems(langs);
        // Добавление в заказ
        list_product.setCellFactory(stringListView -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu1 = new ContextMenu();
            MenuItem addItem = new MenuItem("Добавить");
            addItem.setOnAction(event -> {
                String item = cell.getItem(); // Услуга
                addedcount(item);
                try {
                    sumPrice();
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
            contextMenu1.getItems().addAll(addItem);
            cell.textProperty().bind(cell.itemProperty());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu1);
                }
            });
            return cell;
        });
        // Удаление и добавление услуг из списка заказа
        list_basket.setCellFactory(stringListView -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu2 = new ContextMenu();
            // Удаление одного услугу
            MenuItem deleteOneItem = new MenuItem("Удалить услугу");
            // Добавление одного услугу
            MenuItem addOneItem = new MenuItem("Добавить усгулу");
            addOneItem.setOnAction(event -> {
                String item = cell.getItem(); // Товар
                for (int index = 0; index < list_basket.getItems().size(); index++) {
                    if (list_basket.getItems().get(index).equals(item)) {
                        list_count.getItems().set(index, list_count.getItems().get(index) + 1);
                        if (list_count.getItems().get(index) < 1) {
                            list_basket.getItems().add(String.valueOf(index));
                            list_count.getItems().add(index);
                        }
                        break;
                    }
                }
            });
            deleteOneItem.setOnAction(event -> {
                String item = cell.getItem(); // Товар
                for (int index = 0; index < list_basket.getItems().size(); index++) {
                    if (list_basket.getItems().get(index).equals(item)) {
                        list_count.getItems().set(index, list_count.getItems().get(index) - 1);
                        if (list_count.getItems().get(index) < 1) {
                            list_basket.getItems().remove(index);
                            list_count.getItems().remove(index);
                        }
                        break;
                    }
                }
                try {
                    sumPrice();
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });

            contextMenu2.getItems().addAll(deleteOneItem);
            contextMenu2.getItems().addAll(addOneItem);
            cell.textProperty().bind(cell.itemProperty());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu2);
                }
            });
            return cell;
        });
        list_time.setCellFactory(stringListView -> {
            /*
              Функция позволяет записаться на определенное время
            */
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu1 = new ContextMenu();
            MenuItem addItem = new MenuItem("Выбрать время");
            addItem.setOnAction(event -> {
                String item = cell.getItem(); // Товар
                selectedTime.setText(item);
                try {
                    sumPrice();
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
            contextMenu1.getItems().addAll(addItem);
            cell.textProperty().bind(cell.itemProperty());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu1);
                }
            });
            return cell;
        });

    }
    private void sumPrice() throws SQLException, ClassNotFoundException {
        /*
          Функция позволяет подсчитать предварительную стоимость заказа
        */
        int sum = 0;
        for (Map.Entry<String, Integer> entry : parseListBasket().entrySet()) {
            sum += db.getSumPrice(entry.getKey(), entry.getValue());
        }
        sum_price.setText(Integer.toString(sum));
    }
    /*
      Функция на добавление заказа
    */
    @FXML
    void addedZakaz() throws SQLException, ClassNotFoundException {
        // Проверки на незаполненные значения
        if(list_basket.getItems() != null && list_count.getItems() != null && dataa.getValue() != null && zamechanie.getText() != null && list_time.getItems() != null && selectedTime.getText() != null) {
            if (list_basket.getItems().toArray().length != 0) {
                list_basket.setItems(list_basket.getItems().sorted());
                int count = 1;
                int idCar = db.getIdCar(this.idClient);
                int idOrder = db.addZakaz(dataa.getValue().toString(), selectedTime.getText(), zamechanie.getText(),
                        Integer.parseInt(sum_price.getText()), status.getText(), idCar);
                // Добавляем в смежную таблицы данные
                for (int i=0;i<list_basket.getItems().size();i++){
                    db.addElemntsZakaz(idOrder, list_basket.getItems().get(i),list_count.getItems().get(i));
                }
                // Очистика корзины и выбранного пункта выдачи
                list_basket.setItems(FXCollections.observableArrayList());
                sis.setText("Заказ принят!");
                sum_price.setText("0");
                list_count.setItems(FXCollections.observableArrayList());
                zamechanie.setText("");
                list_time.setItems(FXCollections.observableArrayList());
                selectedTime.setText("");

            } else {
                sis.setText("Добавьте товар в список покупок");
            }
        }else {
            sis.setText("Заполните все поля");
        }
    }

    private Map<String, Integer> parseListBasket() {
        /*
          Функция позволяет подсчитать количество каждого товара в заказе
        */
        Map<String, Integer> countMap = new HashMap<>();
        for (String item : list_basket.getItems()) {
            countMap.put(item, countMap.getOrDefault(item, 0) + 1);
        }
        return countMap;
    }

    private void addedcount(String item) {
        /*
          Функция позволяет подсчитать количество услуг
        */
        boolean fk = false;
        for (int index = 0; index < list_basket.getItems().size(); index++) {
            if (list_basket.getItems().get(index).equals(item)) {
                fk = true;
                list_count.getItems().set(index, list_count.getItems().get(index) + 1);
            }
        }
        if (!fk) {
            list_basket.getItems().add(item);
            list_count.getItems().add((1));
        }
    }
    @FXML
    void getTime() throws SQLException, ClassNotFoundException {
        /*
          Функция позволяет загрузить свободное время на определённую дату
        */
        if (LocalDate.now().isBefore(dataa.getValue())){
            list_time.setItems(FXCollections.observableArrayList(db.getOccupiedTime(dataa.getValue().toString())));}
        else{
            list_time.setItems(FXCollections.observableArrayList());
            selectedTime.setText("");
            showMessage("Ошибка!", "Прошлое уже не изменить :)");
        }
    }
    /*
      Переход на окно истории заказа
    */
    @FXML
    void loadHistory() throws IOException, SQLException, ClassNotFoundException {
        Stage current = (Stage) selectedTime.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("history-order.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(current.getTitle());
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.getIcons().add(new Image("C:/Users/Александр/up/imges/logo.jpg"));
        HistoryOrder controller = fxmlLoader.getController();
        controller.setIdClient(this.idClient);
        controller.loadDateTime();
        current.close();
    }
    /*
      Метод отображает сообщение на экране с заданным заголовком и текстом
    */
    void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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