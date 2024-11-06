package com.example.demo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {
    // Данные для подключения к локальной базе данных


    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:13306/kursavaya", "javafxTest", "changeme");
            return con;

    }

    public int getUser(String log, String pass) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить роль пользователя
         */
        String sql = "SELECT role_idrole,count(*) as n FROM user where login=? and password=? group by role_idrole";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, log);
        statement.setString(2, pass);
        ResultSet res = statement.executeQuery();
        int role = 0;
        while (res.next()) {

            role = res.getInt("role_idrole");
        }
        return role;
    }

    public void insertuser(String LastName, String FirstName, String Phone, String Address) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет добавить данные клиента
         */
        String sql = "insert into clients(LastName,FirstName, Phone, Address) values(?,?,?,?)";
        PreparedStatement prSt = getDbConnection().prepareStatement(sql);
        prSt.setString(1, LastName);
        prSt.setString(2, FirstName);
        prSt.setString(3, Phone);
        prSt.setString(4, Address);
        prSt.executeUpdate();
    }

    public ArrayList<String> getProducts() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить название каждой услуги
         */
        String sql = "SELECT Name_services FROM services;";
        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery(sql);
        ArrayList<String> products = new ArrayList<>();
        while (res.next()) {
            products.add(res.getString("Name_services"));
        }
        return products;
    }

    public int getuserid(String login) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить номер телефона клиента
         */
        String sql = "select user_idUser from clients where phone='" + login + "'";
        PreparedStatement prsr = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = prsr.executeQuery();
        int id = 0;
        while (resultSet.next()) {
            id = resultSet.getInt("user_idUser");

        }
        return id;
    }

    public String getuserlog(Integer id) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить логин по идентификатору пользователю
         */
        String sql = "select Login from user where idUser='" + id + "'";
        PreparedStatement prsr = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = prsr.executeQuery();
        String id_log = "";
        while (resultSet.next()) {
            id_log = resultSet.getString("Login");

        }
        return id_log;

    }

    public String getuserpas(Integer id) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить пароль по идентификатору пользователю
         */
        String sql = "select Password from user where idUser='" + id + "'";
        PreparedStatement prsr = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = prsr.executeQuery();
        String id_pas = "";
        while (resultSet.next()) {
            id_pas = resultSet.getString("Password");

        }
        return id_pas;

    }

    public ArrayList<String> getPrice() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить цены на услуги
         */
        String sql = "SELECT Price_services FROM services;";
        Statement statement_code = getDbConnection().createStatement();
        ResultSet res = statement_code.executeQuery(sql);
        ArrayList<String> prices = new ArrayList<>();
        while (res.next()) {
            prices.add(res.getString("Price_services"));
        }
        return prices;
    }

    public void insertcar(String Brand, String Model, String Year, String VIN, Integer client) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет добавить данные о машине
         */
        String sql = "insert cars (Brand,Model,Year,VIN,Clients_ClientID) values(?,?,?,?,?)";
        PreparedStatement prSt = getDbConnection().prepareStatement(sql);
        prSt.setString(1, Brand);
        prSt.setString(2, Model);
        prSt.setString(3, Year);
        prSt.setString(4, VIN);
        prSt.setInt(5, client);
        prSt.executeUpdate();
    }
    public Integer getidkl(Integer id) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить id клиента
         */
        String sql = "SELECT ClientID FROM clients  WHERE user_idUser = '" + id + "'";
        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery(sql);
        Integer point = -1;
        while (res.next()) {
            point = res.getInt("ClientID");
        }
        return point;
    }

    public int getSumPrice(String name_product, Integer count_product) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить список цен на услуги
         */
        String sql = "SELECT Price_services FROM services where Name_services ='" + name_product + "';";
        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery(sql);
        int sum = 0;
        while (res.next()) {
            sum = res.getInt("Price_services");
        }
        return sum * count_product;
    }

    public ArrayList<String> getMech() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить название механика
         */
        String sql = "SELECT Name_Mechanic FROM mechanic";
        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery(sql);
        ArrayList<String> points = new ArrayList<String>();
        while (res.next()) {
            points.add(res.getString("Name_Mechanic"));
        }
        return points;
    }

    public ArrayList<String> getCost() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить цену заказа
         */
        String sql = "SELECT cost FROM orders;";
        Statement statement_code = getDbConnection().createStatement();
        ResultSet res = statement_code.executeQuery(sql);
        ArrayList<String> prices = new ArrayList<>();
        while (res.next()) {
            prices.add(res.getString("cost"));
        }
        return prices;
    }

    public ArrayList<String> getzap() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить название и цены на запчасти
         */
        String sql = "SELECT name_spare_parts, Price  FROM spare_parts;";
        Statement statement_code = getDbConnection().createStatement();
        ResultSet res = statement_code.executeQuery(sql);
        ArrayList<String> prices = new ArrayList<>();
        while (res.next()) {
            prices.add(res.getString("name_spare_parts") + "|" + res.getString("Price"));
        }
        return prices;
    }

    public ArrayList<String> getusl() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить название и цены на услуги
         */
        String sql = "SELECT Name_services,Price_services FROM services;";
        Statement statement_code = getDbConnection().createStatement();
        ResultSet res = statement_code.executeQuery(sql);
        ArrayList<String> prices = new ArrayList<>();
        while (res.next()) {
            prices.add(res.getString("Name_services") + "|" + res.getString("Price_services"));
        }
        return prices;
    }

    public ArrayList<String> getOccupiedTime(String date) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить список свободное время для записи
         */
        ArrayList<String> time = new ArrayList<String>();
        time.addAll(List.of(new String[]{"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"}));
        String sql = "SELECT Time_or FROM orders WHERE Date_or = ?";
        PreparedStatement statement = getDbConnection().prepareCall(sql);
        statement.setString(1, date);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String occupiedTime = resultSet.getString("Time_or");
            time.remove(occupiedTime.substring(0, occupiedTime.length() - 3));
        }
        return time;
    }

    public int addZakaz(String Date, String Time, String Zamechanie, Integer Cost, String Status, Integer Cars_CarID) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет добавить заказ
         */
        String sql = "INSERT INTO orders (Date_or, Time_or, Zamechanie, Cost, Status, Cars_CarID)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, Date);
        statement.setString(2, Time + ":00");
        statement.setString(3, Zamechanie.length() > 3 ? Zamechanie : "");
        statement.setInt(4, Cost);
        statement.setString(5, Status.length() > 1 ? Status : "Оплачен");
        statement.setInt(6, Cars_CarID);
        statement.executeUpdate();
        sql = "SELECT OrderID FROM orders WHERE Date_or = ? AND Time_or = ?";
        statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, Date);
        statement.setString(2, Time + ":00");
        ResultSet res = statement.executeQuery();
        int id_zakaz = -1;
        while (res.next()) {
            id_zakaz = res.getInt("OrderID");
        }
        return id_zakaz;

    }

    public int getIdCar(int idClient) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить id машины
         */
        int idCar = 0;
        String sql = "SELECT CarID FROM cars WHERE Clients_ClientID = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idClient);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) idCar = resultSet.getInt("CarID");
        return idCar;
    }

    public void addElemntsZakaz(int idOrder, String name_services, Integer count) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет добавить в смежную таблицу данные
         */
        int idServices = 0;
        String sql = "SELECT idservices FROM services WHERE Name_services = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, name_services);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) idServices = resultSet.getInt("idservices");
        sql = "INSERT INTO orders_has_spare_parts (Orders_OrderID,services_idservices, quantity)  " +
                "VALUES (?, ?, ?)";
        statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idOrder);
        statement.setInt(2, idServices);
        statement.setInt(3, count);
        statement.executeUpdate();
    }

    public ArrayList<String> getName() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить дату и время
         */
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT Date_or, Time_or FROM orders;";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        ResultSet res = statement.executeQuery();
        while (res.next()) {
            list.add(res.getString("Date_or") + " " + res.getString("Time_or"));
        }
        return list;
    }

    public ArrayList<String> getListUslug(int idOrder) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить название и цену услуг по идентификатору доступа
         */
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT Name_services, Price_services FROM orders_has_spare_parts" +
                " AS ohsp LEFT JOIN services AS s ON ohsp.services_idservices = s.idservices " +
                "LEFT JOIN spare_parts AS sp ON ohsp.Spare_parts_idSpare_parts = sp.idSpare_parts WHERE Orders_OrderID = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idOrder);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            list.add(resultSet.getString("Name_services") + "|" + resultSet.getString("Price_services"));
        }
        return list;
    }

    public ArrayList<String> getListServices(int idOrder) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить название и цену запчастей по идентификатору доступа
         */
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT name_spare_parts, Price FROM orders_has_spare_parts" +
                " AS ohsp LEFT JOIN services AS s ON ohsp.services_idservices = s.idservices " +
                "LEFT JOIN spare_parts AS sp ON ohsp.Spare_parts_idSpare_parts = sp.idSpare_parts WHERE Orders_OrderID = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idOrder);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            if (resultSet.getString("name_spare_parts") != null) {
                list.add(resultSet.getString("name_spare_parts") + "|" + resultSet.getString("Price"));
            }
        }
        return list;
    }

    public int getIdOrder(String item) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить id заказа по дате и времени
         */
        int idOrder = 0;
        String sql = "SELECT OrderID FROM orders WHERE Date_or = ? AND Time_or = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, item.substring(0, item.indexOf(" ")));
        statement.setString(2, item.substring(item.indexOf(" ") + 1, item.length()));
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            idOrder = resultSet.getInt("OrderID");
        }
        return idOrder;
    }

    public ArrayList<Integer> getCountServices(int idOrder) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить количество выбранных запчастей
         */
        ArrayList<Integer> list = new ArrayList<>();
        String sql = "SELECT quantity FROM orders_has_spare_parts" +
                " AS ohsp LEFT JOIN services AS s ON ohsp.services_idservices = s.idservices " +
                "LEFT JOIN spare_parts AS sp ON ohsp.Spare_parts_idSpare_parts = sp.idSpare_parts WHERE Orders_OrderID = ?" +
                " AND Spare_parts_idSpare_parts is not null";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idOrder);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            if (resultSet.getInt("quantity") != 0) {
                list.add(resultSet.getInt("quantity"));
            }
        }
        return list;
    }

    public String getZamechanie(int idOrder) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить пожелание по заказу
         */
        String zamechanie = "";
        String sql = "SELECT Zamechanie FROM orders WHERE OrderID = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idOrder);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) zamechanie = resultSet.getString("Zamechanie");
        return zamechanie;
    }

    public String getStatis(int idOrder) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить статус заказа
         */
        String zamechanie = "";
        String sql = "SELECT Status FROM orders WHERE OrderID = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idOrder);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) zamechanie = resultSet.getString("Status");
        return zamechanie;
    }

    public void otmenaOrder(int idOrder) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет изменить заказу статус отменен
         */
        String sql = "UPDATE orders SET `Status` = 'Отменён' WHERE OrderID = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idOrder);
        statement.executeUpdate();
    }

    public void deleteOrderHasSpace(int idOrder) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет удалять все строки в таблице "orders_has_spare_parts", где значение столбца "Orders_OrderID" равно заданному значению.
         */
        String sql = "DELETE FROM orders_has_spare_parts WHERE (Orders_OrderID = ?)";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idOrder);
        statement.executeUpdate();
    }

    public void takeOrder(int idOrder, String name_uslug, String name_servise, Integer count, String name_mehanik,
                          String zamech) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет принять заказ
         */
        int idUslug = -1;
        int idServise = -1;
        String sql = "SELECT idservices FROM services WHERE Name_services = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, name_uslug);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) idUslug = resultSet.getInt("idservices");
        sql = "INSERT INTO orders_has_spare_parts (Orders_OrderID, services_idservices) " +
                "VALUES (?, ?)";
        statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idOrder);
        statement.setInt(2, idUslug);
        statement.executeUpdate();
        if (name_servise != null) {
            sql = "SELECT idSpare_parts FROM spare_parts WHERE name_spare_parts = ?";
            statement = getDbConnection().prepareStatement(sql);
            statement.setString(1, name_servise);
            resultSet = statement.executeQuery();
            while (resultSet.next()) idServise = resultSet.getInt("idSpare_parts");
            sql = "UPDATE orders_has_spare_parts SET Spare_parts_idSpare_parts = ?, quantity = ?" +
                    " WHERE (Orders_OrderID = ?) and (services_idservices = ?)";
            statement = getDbConnection().prepareStatement(sql);
            statement.setInt(1, idServise);
            statement.setInt(2, count);
            statement.setInt(3, idOrder);
            statement.setInt(4, idUslug);
            statement.executeUpdate();
        }
        CallableStatement callableStatement = getDbConnection().prepareCall("{call calculate_order_price(?)}");
        callableStatement.setInt(1, idOrder);
        callableStatement.executeLargeUpdate();
        sql = "UPDATE orders SET Status = 'Выполнен', Zamechanie = ? WHERE (OrderID = ?)";
        statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, zamech);
        statement.setInt(2, idOrder);
        statement.executeUpdate();
        sql = "UPDATE mechanic SET orders_OrderID = ? WHERE (Name_Mechanic = ?)";
        statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idOrder);
        statement.setString(2, name_mehanik);
        statement.executeUpdate();
    }

    public ArrayList<String> getDateTime(int idClient) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет выбирать дату и время заказов для автомобилей
         */
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT Date_or, Time_or FROM orders WHERE Cars_CarID = (SELECT CarID FROM cars WHERE Clients_ClientID = ?)";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idClient);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) list.add(resultSet.getString("Date_or") + " "
                + resultSet.getString("Time_or").substring(0, resultSet.getString("Time_or").length() - 3));
        return list;
    }

    public int getIdUser(String login, String password) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить id клиента соответствующий заданному пользователю по его логину и паролю
         */
        int id = 0;
        String sql = "SELECT ClientID FROM clients AS c JOIN user AS u ON u.idUser = c.user_idUser WHERE Login = ? AND Password = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, login);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) id = resultSet.getInt("ClientID");
        return id;
    }
    public boolean isPhoneNumberExists(String phoneNumber) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет проверить существует ли номер телефона
         */
        String query = "SELECT * FROM clients WHERE phone = ?";
        PreparedStatement pstmt = getDbConnection().prepareStatement(query);
        pstmt.setString(1, phoneNumber);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }
}