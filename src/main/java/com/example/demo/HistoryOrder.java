package com.example.demo;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HistoryOrder {

    DB db = null;

    @FXML
    private ListView<String> data_time; // Список заказов клиента

    @FXML
    private Label order_status; // Статус заказа

    @FXML
    private ListView<String> spisok; // Список услуг

    @FXML
    private Label total_amount; // Стоимость за заказ

    int idClient; // Переменная id клиента
    int idOrder; // Переменная id заказа


    /*
      Метод переданное id клиента в качестве аргумента
    */
    public void setIdClient(int id){
        this.idClient = id;
    }

    /*
      Метод инициализации базы данных
    */
    @FXML
    void initialize()  {
        db = new DB();
    }

    public void loadDateTime() throws SQLException, ClassNotFoundException {
        data_time.setItems(FXCollections.observableArrayList(db.getDateTime(this.idClient)));
        data_time.setCellFactory(stringListView -> {
            /*
              Функция позволяет записаться на определенное время
            */
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu1 = new ContextMenu();
            MenuItem addItem = new MenuItem("Выбрать время");
            addItem.setOnAction(event -> {
                String item = cell.getItem(); // Товар
                try {
                    idOrder = db.getIdOrder(item);
                    spisok.setItems(FXCollections.observableArrayList(db.getListServices(idOrder)));
                    spisok.getItems().addAll(FXCollections.observableArrayList(db.getListUslug(idOrder)));
                    sumPrice();
                    order_status.setText(db.getStatis(idOrder));
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
    }

    private void sumPrice() {
        /*
          Функция позволяет увидеть стоимость услуг в истории заказов
        */
        int sum = 0;
        for (int index = 0 ;index < spisok.getItems().size(); index++) {
            sum += Integer.parseInt(
                    spisok.getItems().get(index).substring(
                            spisok.getItems().get(index).indexOf("|")+1,  spisok.getItems().get(index).length()
                    )
            );
        }
        total_amount.setText(Integer.toString(sum));
    }
    /*
      Переход на окно клиента
    */
    @FXML
    void exit() throws IOException, SQLException, ClassNotFoundException {
        Stage current = (Stage) data_time.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(current.getTitle());
        stage.setScene(scene);
        stage.show();
        stage.getIcons().add(new Image("C:/Users/Александр/up/imges/logo.jpg"));
        stage.setResizable(false);
        Client controller = fxmlLoader.getController();
        controller.setId(this.idClient);
        controller.loadPoducts();
        current.close();
    }
}
