package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;

public class Sotrudnic {

    @FXML
    private ListView<String> list_basket; // Список приобретённых товаров

    @FXML
    private ListView<Integer> list_count; // Количество заказа

    @FXML
    private TextArea list_dg;// Список замечаний

    @FXML
    private ListView<String> list_price; // Список цен на заказы

    @FXML
    private ListView<String> list_product; // Список товаров

    @FXML
    private ListView<String> list_selected_zap; // Выбранные запчасти
    @FXML
    private ListView<String> list_zap; // Список запчастей
    @FXML
    private ListView<String> list_usl; // Список услуг

    @FXML
    private ChoiceBox<String> mech; // Выбор механика

    @FXML
    private Label status; // Статус заказа

    @FXML
    private Label sum_price; // Сумма товаров в корзине
    DB db = null;
    int id_order = 0; // id заказа
    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        db = new DB();// Инициируем объект
        loadPoducts(); // Загружаем список услуг

        // Добавляем механиков
        ArrayList<String> list_products = db.getMech();
        ObservableList<String> langs_products = FXCollections.observableArrayList();
        for (int i = 0; i < list_products.size(); i++) {
            langs_products.add(list_products.get(i) + "");
        }
        mech.setItems(langs_products);

        // Добавляем цены на заказы
        ArrayList<String> list_prices = db.getCost();
        ObservableList<String> langs_prices = FXCollections.observableArrayList();
        langs_prices.addAll(list_prices);
        list_price.setItems(langs_prices);
    }

    void loadPoducts() throws SQLException, ClassNotFoundException {
        // Заполяем список заказов
        list_product.setItems(FXCollections.observableArrayList(db.getName()));
        // Заполяем список запчастей
        list_zap.setItems(FXCollections.observableArrayList(db.getzap()));
        // Заполяем список услуг
        list_usl.setItems(FXCollections.observableArrayList(db.getusl()));
        // Добавление в заказ услуг
        list_usl.setCellFactory(stringListView -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem addItem = new MenuItem("Выбрать");
            addItem.setOnAction(event -> {
                String item = cell.getItem(); // Товар
                boolean check = false;
                for (int index = 0; index < list_basket.getItems().size(); index++) {
                    if (list_basket.getItems().get(index).equals(item)) {
                        check = true;
                    }
                }
                if (!check) {
                    list_basket.getItems().add(item);
                }
                addedPrice();
            });
            contextMenu.getItems().addAll(addItem);
            cell.textProperty().bind(cell.itemProperty());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell;
        });
        // Добавление в заказ запчастей
        list_zap.setCellFactory(stringListView -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem addItem = new MenuItem("Выбрать");
            addItem.setOnAction(event -> {
                String item = cell.getItem(); // Товар
                boolean check = false;
                for (int index = 0; index < list_basket.getItems().size(); index++) {
                    if (list_basket.getItems().get(index).equals(item)) {
                        list_count.getItems().set(index, list_count.getItems().get(index) + 1);
                        check = true;
                    }
                }
                if (!check) {
                    if (list_basket.getItems().size() > list_selected_zap.getItems().size()) {
                        list_selected_zap.getItems().add(item);
                        list_count.getItems().add(1);
                    } else {
                            showMessage("Ошибка!", "Выберите сначала услугу");

                    }
                }
                addedPrice();
            });
            contextMenu.getItems().addAll(addItem);
            cell.textProperty().bind(cell.itemProperty());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }


            });
            return cell;
        });
        // Добавление услуг в заказ
        list_product.setCellFactory(stringListView -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu1 = new ContextMenu();
            MenuItem addItem = new MenuItem("Выбрать");
            addItem.setOnAction(event -> {
                String item = cell.getItem(); // Товар
                try {
                    id_order = db.getIdOrder(item);
                    list_basket.setItems(FXCollections.observableArrayList(db.getListUslug(id_order)));
                    list_selected_zap.setItems(FXCollections.observableArrayList(db.getListServices(id_order)));
                    list_count.setItems(FXCollections.observableArrayList(db.getCountServices(id_order)));
                    list_dg.setText(db.getZamechanie(id_order));
                    status.setText(db.getStatis(id_order));
                    addedPrice();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
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
            // Удаление услуги
            MenuItem deleteOneItem = new MenuItem("Удалить");
            // Добавление услуг
            deleteOneItem.setOnAction(event -> {
                String item = cell.getItem(); // Товар
                for (int index = 0; index < list_basket.getItems().size(); index++) {
                    if (list_basket.getItems().get(index).equals(item)) {
                        list_basket.getItems().remove(index);
                        if (list_selected_zap.getItems().size() > list_basket.getItems().size()) {
                            list_count.getItems().remove(index);
                            list_selected_zap.getItems().remove(index);
                        }
                        break;
                    }
                }
            });

            contextMenu2.getItems().addAll(deleteOneItem);
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
        // Удаление и добавление запчастей в заказ
        list_selected_zap.setCellFactory(stringListView -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu2 = new ContextMenu();
            // Удаление запчастей
            MenuItem deleteOneItem = new MenuItem("Удалить");
            // Добавление запчастей
            MenuItem addOneItem = new MenuItem("Добавить");
            addOneItem.setOnAction(event -> {
                String item = cell.getItem(); // Товар
                for (int index = 0; index < list_selected_zap.getItems().size(); index++) {
                    if (list_selected_zap.getItems().get(index).equals(item)) {
                        list_count.getItems().set(index, list_count.getItems().get(index) + 1);
                        break;
                    }
                }
            });
            deleteOneItem.setOnAction(event -> {
                String item = cell.getItem(); // Товар
                for (int index = 0; index < list_selected_zap.getItems().size(); index++) {
                    if (list_selected_zap.getItems().get(index).equals(item)) {
                        list_count.getItems().set(index, list_count.getItems().get(index) - 1);
                        if (list_count.getItems().get(index) < 1) {
                            list_selected_zap.getItems().remove(index);
                            list_count.getItems().remove(index);
                        }
                    }
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

    }
    private void addedPrice() {
        /*
          Функция добавляет цены выбранных услуг в заказ и в выбранный список услуг.
        */
        int sum = 0;
        for (int index = 0; index < list_basket.getItems().size(); index++) {
            sum += Integer.parseInt(list_basket.getItems().get(index).substring(
                    list_basket.getItems().get(index).indexOf("|") + 1,
                    list_basket.getItems().get(index).length()
            ));
        }
        for (int index = 0; index < list_selected_zap.getItems().size(); index++) {
            sum += Integer.parseInt(list_selected_zap.getItems().get(index).substring(
                    list_selected_zap.getItems().get(index).indexOf("|") + 1,
                    list_selected_zap.getItems().get(index).length()
            )) * list_count.getItems().get(index);
        }
        sum_price.setText(Integer.toString(sum));
    }

    @FXML
    private void Otkaz_zakaz() throws SQLException, ClassNotFoundException {
        /*
          Функция отказа товара
        */
        db.otmenaOrder(id_order);
        status.setText(db.getStatis(id_order));
    }

    @FXML
    private void AddZakaz() throws SQLException, ClassNotFoundException {
        /*
          Функция добавления товара
        */
        if (mech.getValue() != null && list_basket.getItems().size() != 0) {
            db.deleteOrderHasSpace(id_order);
            for (int index = 0; index < list_basket.getItems().size(); index++) {
                db.takeOrder(id_order, list_basket.getItems().get(index).substring(0, list_basket.getItems().get(index).indexOf("|")),
                        list_selected_zap.getItems().size() > index ?
                                list_selected_zap.getItems().get(index).substring(0, list_selected_zap.getItems().get(index).indexOf("|")) : null,
                        list_selected_zap.getItems().size() > index ? list_count.getItems().get(index) : null, mech.getValue(),
                        list_dg.getText());
            }
        }
        else {
            showMessage("Ошибка!", "Заполните данные");
        }

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