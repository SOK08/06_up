create database if not exists kursavaya;
use kursavaya;
GRANT ALL PRIVILEGES ON kursavaya.* TO 'javafxTest'@'%' IDENTIFIED BY 'changeme';
FLUSH PRIVILEGES;

-- -----------------------------------------------------
-- Table `kursavaya`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kursavaya`.`role` (
  `idRole` INT NOT NULL AUTO_INCREMENT,
  `Name_role` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idRole`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `kursavaya`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kursavaya`.`user` (
  `idUser` INT NOT NULL AUTO_INCREMENT,
  `Password` VARCHAR(45) NOT NULL,
  `Login` VARCHAR(45) NOT NULL,
  `Role_idRole` INT NOT NULL,
  PRIMARY KEY (`idUser`),
  INDEX `fk_User_Role1` (`Role_idRole` ASC) VISIBLE,
  CONSTRAINT `fk_User_Role1`
    FOREIGN KEY (`Role_idRole`)
    REFERENCES `kursavaya`.`role` (`idRole`))
ENGINE = InnoDB
AUTO_INCREMENT = 62
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `kursavaya`.`clients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kursavaya`.`clients` (
  `ClientID` INT NOT NULL AUTO_INCREMENT,
  `LastName` VARCHAR(45) NOT NULL,
  `FirstName` VARCHAR(45) NOT NULL,
  `Phone` VARCHAR(45) NOT NULL,
  `Address` VARCHAR(100) NOT NULL,
  `user_idUser` INT NOT NULL,
  PRIMARY KEY (`ClientID`, `user_idUser`),
  INDEX `fk_clients_user1_idx` (`user_idUser` ASC) VISIBLE,
  CONSTRAINT `fk_clients_user1`
    FOREIGN KEY (`user_idUser`)
    REFERENCES `kursavaya`.`user` (`idUser`))
ENGINE = InnoDB
AUTO_INCREMENT = 60
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `kursavaya`.`cars`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kursavaya`.`cars` (
  `CarID` INT NOT NULL AUTO_INCREMENT,
  `Brand` VARCHAR(45) NOT NULL,
  `Model` VARCHAR(45) NOT NULL,
  `Year` DATE NOT NULL,
  `VIN` VARCHAR(45) NOT NULL,
  `Clients_ClientID` INT NOT NULL,
  PRIMARY KEY (`CarID`),
  INDEX `fk_Cars_Clients` (`Clients_ClientID` ASC) VISIBLE,
  CONSTRAINT `fk_Cars_Clients`
    FOREIGN KEY (`Clients_ClientID`)
    REFERENCES `kursavaya`.`clients` (`ClientID`))
ENGINE = InnoDB
AUTO_INCREMENT = 26
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `kursavaya`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kursavaya`.`orders` (
  `OrderID` INT NOT NULL AUTO_INCREMENT,
  `Date_or` DATE NOT NULL,
  `Time_or` TIME NOT NULL,
  `Zamechanie` VARCHAR(200) NULL DEFAULT NULL,
  `Cost` VARCHAR(45) NOT NULL,
  `Status` VARCHAR(45) NOT NULL,
  `Cars_CarID` INT NOT NULL,
  PRIMARY KEY (`OrderID`),
  INDEX `fk_Orders_Cars1` (`Cars_CarID` ASC) VISIBLE,
  CONSTRAINT `fk_Orders_Cars1`
    FOREIGN KEY (`Cars_CarID`)
    REFERENCES `kursavaya`.`cars` (`CarID`))
ENGINE = InnoDB
AUTO_INCREMENT = 24
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `kursavaya`.`mechanic`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kursavaya`.`mechanic` (
  `idMechanic` INT NOT NULL AUTO_INCREMENT,
  `Name_Mechanic` VARCHAR(45) NOT NULL,
  `orders_OrderID` INT NOT NULL,
  PRIMARY KEY (`idMechanic`),
  INDEX `fk_mechanic_orders1_idx` (`orders_OrderID` ASC) VISIBLE,
  CONSTRAINT `fk_mechanic_orders1`
    FOREIGN KEY (`orders_OrderID`)
    REFERENCES `kursavaya`.`orders` (`OrderID`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `kursavaya`.`services`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kursavaya`.`services` (
  `idservices` INT NOT NULL,
  `Price_services` VARCHAR(45) NOT NULL,
  `Status_services` VARCHAR(45) NOT NULL,
  `Name_services` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idservices`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `kursavaya`.`spare_parts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kursavaya`.`spare_parts` (
  `idSpare_parts` INT NOT NULL AUTO_INCREMENT,
  `name_spare_parts` VARCHAR(45) NOT NULL,
  `Price` VARCHAR(45) NOT NULL,
  `Description_spare` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`idSpare_parts`))
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `kursavaya`.`orders_has_spare_parts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kursavaya`.`orders_has_spare_parts` (
  `Orders_OrderID` INT NOT NULL AUTO_INCREMENT,
  `services_idservices` INT NOT NULL,
  `Spare_parts_idSpare_parts` INT NULL DEFAULT NULL,
  `quantity` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`Orders_OrderID`, `services_idservices`),
  INDEX `fk_orders_has_spare_parts_services1` (`services_idservices` ASC) VISIBLE,
  INDEX `fk_Orders_has_Spare_parts_Spare_parts1` (`Spare_parts_idSpare_parts` ASC) VISIBLE,
  CONSTRAINT `fk_Orders_has_Spare_parts_Orders1`
    FOREIGN KEY (`Orders_OrderID`)
    REFERENCES `kursavaya`.`orders` (`OrderID`),
  CONSTRAINT `fk_orders_has_spare_parts_services1`
    FOREIGN KEY (`services_idservices`)
    REFERENCES `kursavaya`.`services` (`idservices`),
  CONSTRAINT `fk_Orders_has_Spare_parts_Spare_parts1`
    FOREIGN KEY (`Spare_parts_idSpare_parts`)
    REFERENCES `kursavaya`.`spare_parts` (`idSpare_parts`))
ENGINE = InnoDB
AUTO_INCREMENT = 24
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;

USE `kursavaya` ;

-- -----------------------------------------------------
-- procedure calculate_order_price
-- -----------------------------------------------------

DELIMITER $$
USE `kursavaya`$$
CREATE DEFINER=`root`@`127.0.0.1` PROCEDURE `calculate_order_price`(idOrder int)
BEGIN
DECLARE total_price DECIMAL(10, 2);
  DECLARE total_services_price DECIMAL(10, 2);
  DECLARE total_spare_parts_price DECIMAL(10, 2);

  SELECT IFNULL(SUM(Price_services), 0) INTO total_services_price FROM services;
  SELECT IFNULL(SUM(Price), 0) INTO total_spare_parts_price FROM spare_parts;

  SET total_price = total_services_price + total_spare_parts_price;

  UPDATE orders SET cost = (SELECT SUM(Price*quantity+Price_services) FROM orders_has_spare_parts AS ohsp
  JOIN services AS s ON s.idservices = ohsp.services_idservices
  JOIN spare_parts AS sp ON sp.idSpare_parts = ohsp.Spare_parts_idSpare_parts WHERE Orders_OrderID = idOrder) WHERE OrderID = idOrder;
END$$

DELIMITER ;
USE `kursavaya`;

DELIMITER $$
USE `kursavaya`$$
CREATE
DEFINER=`javafxTest`@`127.0.0.1`
TRIGGER `kursavaya`.`clients_BEFORE_INSERT`
BEFORE INSERT ON `kursavaya`.`clients`
FOR EACH ROW
BEGIN
DECLARE a VARCHAR(45);
    DECLARE d INT;
    SET a = CONCAT('client');
    INSERT INTO user VALUES (null,a, NEW.Phone,1);
    SELECT max(idUser) INTO d FROM user;
    SET NEW.user_idUser = d;
END$$


USE `kursavaya` ;
INSERT INTO `role` VALUES (1,'Сотрудник'),(2,'Клиент');
INSERT INTO `user` VALUES (1,'12','st1',1),(2,'123','st2',1),(3,'1234','st3',1),(4,'12345','cot',2),(37,'client','893456789',1),(60,'client','03456789',1),(61,'client','78904567',1);
INSERT INTO `clients` VALUES (1,'Орлов','Дмитрий','89528562356','г.Нижний Новгород, ул. Студенческая 6Б',1),(2,'Сухоруков','Кирилл','89545562356','г.Бор, ул. Степана Разина 9Б',2),(3,'Круглов','Кирилл','89528550356','г.Нижний Новгород, ул. Студенческая 6',3),(35,'Гутянская','Елена','893456789','НН',37),(58,'Дима','Орлов','03456789','НН',60),(59,'Слпл','ушощпидупь','78904567','Городец',61);
INSERT INTO `cars` VALUES (1,'Lada','Vesta','2015-10-10','sgre32543555ydwt53',1),(2,'Mazda','CX-5','2013-07-07','sgre3254355hjdwt53',2),(3,'Lada','Granta','2020-04-08','sgre325hr555ydwt53',3),(20,'LADA','Granta','2022-05-05','dftgyuh2345',35),(25,'Лада','Гранта','2010-12-12','FGHjk345678',58);
INSERT INTO `services` VALUES (0,'0','Свободна','Ещё запчасти'),(1,'3000','Свободна','Замена помпы'),(2,'1500','Не доступна','Замена воздушного фильтра'),(3,'2000','Свободна','Замена топливного фильтра'),(4,'1000','Свободна','Замена масла'),(5,'6000','Не доступна','Замена генератора'),(6,'4500','Свободна','Замена задних пружин'),(7,'10000','Свободна','Замена ходовой части'),(8,'3500','Свободна','Замена амотризаторов'),(9,'100','Свободна','Диагностика');
INSERT INTO `spare_parts` VALUES (1,'Генератор','15000','Имеется'),(2,'Помпа','5000','Имеется'),(3,'Топливный фильтр','3000','Имеется'),(4,'Масляный фильтр','1500','Имеется'),(5,'Воздушный фильтр','2500','Имеется');
INSERT INTO `orders` VALUES (1,'2023-05-05','14:00:00','','8000','Оплачен',1),(2,'2023-08-05','16:00:00','','14000','Выполнен',2),(3,'2023-06-05','17:30:00','','35000','Оплачен',3),(17,'2023-11-17','11:00:00','ЧЕНИТЕ МАШИНУ','4000','Выполнен',1),(18,'2023-11-24','12:00:00','Момыть машину','100','Отменён',20),(19,'2023-11-24','15:00:00','Мойка ','1600','Выполнен',20),(20,'2023-11-22','10:00:00','помыть машину','3100','Отменён',1),(22,'2023-11-24','14:00:00','','16000','Выполнен',25),(23,'2024-09-20','12:00:00','','2500','Выполнен',1);
INSERT INTO `orders_has_spare_parts` VALUES (1,1,1,'1'),(17,2,5,'1'),(17,6,NULL,NULL),(18,9,NULL,'1'),(19,7,NULL,NULL),(19,9,4,'1'),(20,1,NULL,'1'),(20,9,NULL,'1'),(22,4,1,'1'),(22,5,NULL,NULL),(23,4,4,'1');
INSERT INTO `mechanic` VALUES (1,'Ломов',17),(2,'Гранычев',23),(3,'Войтков',3);

DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
