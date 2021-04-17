-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: meetme
-- ------------------------------------------------------
-- Server version	8.0.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `meetme` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `meetme`;

--
-- Table structure for table `meetings`
--

DROP TABLE IF EXISTS `meetings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meetings` (
  `meeting_id` int NOT NULL AUTO_INCREMENT,
  `start_date` varchar(45) NOT NULL,
  `end_date` varchar(45) NOT NULL,
  `start_time` varchar(45) NOT NULL,
  `end_time` varchar(45) NOT NULL,
  `title` varchar(50) NOT NULL,
  `description` varchar(120) NOT NULL,
  `location` varchar(120) NOT NULL,
  `chairperson` varchar(60) NOT NULL,
  `attendees` json NOT NULL,
  PRIMARY KEY (`meeting_id`),
  KEY `chairperson` (`chairperson`),
  CONSTRAINT `meetings_ibfk_1` FOREIGN KEY (`chairperson`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meetings`
--

LOCK TABLES `meetings` WRITE;
/*!40000 ALTER TABLE `meetings` DISABLE KEYS */;
INSERT INTO `meetings` VALUES (1,'2021-04-13','2021-04-13','14:00:00','15:00:00','Keynote','Apple Keynote','2 Ronald Regan Court','bmteasdale','{\"ids\": [\"bmteasdale\", \"btorrent3\", \"smarchington4\", \"sespasa\", \"rmottershead6\", \"rachhouse\"]}'),(2,'2021-04-19','2021-04-19','13:00:00','14:00:00','Team Building','Hosted in the MacKay Room','31 University Ave.','btorrent3','{\"ids\": [\"btorrent3\", \"bmteasdale\", \"rachhouse\", \"kedwins2\", \"smarchington4\", \"sespasa\", \"rmottershead6\"]}'),(3,'2021-04-14','2021-04-14','10:00:00','11:00:00','Board Meeting','Discuss company-wide policies and issues.','3090 Martha Dr','bmteasdale','{\"ids\": [\"btorrent3\", \"bmteasdale\", \"kedwins2\", \"smarchington4\", \"sespasa\", \"rmottershead6\"]}'),(5,'2021-04-13','2021-04-13','11:00','12:00','Team Meeting','Discuss next weeks agenda','11 Main St','rachhouse','{\"ids\": [\"rachhouse\", \"bmteasdale\"]}'),(6,'2021-04-19','2021-04-19','14:00','15:00','Interview','Interview for x job','Virtual','rachhouse','{\"ids\": [\"rachhouse\", \"bmteasdale\"]}'),(18,'2021-04-16','2021-04-16','12:00','13:00','Company Update','Update team on company changes','Virtual','rachhouse','{\"ids\": [\"rachhouse\", \"bmteasdale\"]}'),(24,'2021-04-13','2021-04-13','13:00','14:00','Important Meeting','Charles V. Keating Centre, Room 3032','1100 Convocation Blvd','rachhouse','{\"ids\": [\"rachhouse\", \"bmteasdale\"]}');
/*!40000 ALTER TABLE `meetings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `bio` varchar(120) DEFAULT NULL,
  `meeting_ids` json DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('bmteasdale','teasdale','Brendan','Teasdale','bmteasdale3@gmail.com','99% Caffeine','{\"ids\": [1, 2, 5, 6, 18, 24, 25, 26, 28, 30, 31, 32, 33, 34]}'),('btorrent3','ArkfpnnIAhg','Bennie','Torrent','btorrent3@multiply.com','Visionary exuding conglomeration','{\"ids\": [1]}'),('janedoe','pass','jane','doe','jdoe@email.com',NULL,NULL),('kedwins2','XqnQZi','Kristo','Edwins','kedwins2@ted.com','Multi-lateral homogeneous installation','{\"ids\": [2]}'),('rachhouse','house','Rachel','House','rachhouse103@gmail.com','Hello World!','{\"ids\": [1, 2, 5, 6, 18, 24, 25, 26, 28, 30, 31, 32, 33, 34]}'),('rmottershead6','fjj7Nt1rS9S','Rolfe','Mottershead','rmottershead6@mysql.com','Progressive didactic hardware','{\"ids\": [1, 2]}'),('sespasa','cboNwax','Shandra','Espasa','sespasa5@wired.com','Virtual logistical success','{\"ids\": [1, 2]}'),('smarchington4','boxJVT9f9c','Sheeree','Marchington','smarchington4@baidu.com','Multi-lateral scalable initiative','{\"ids\": [1, 2]}');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-16 11:36:40
