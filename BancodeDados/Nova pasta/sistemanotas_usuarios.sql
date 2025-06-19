-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: sistemanotas
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

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

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `cpf` char(11) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `tipo` enum('professor','aluno','secretaria') NOT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `rua` varchar(100) DEFAULT NULL,
  `numero` varchar(10) DEFAULT NULL,
  `complemento` varchar(50) DEFAULT NULL,
  `bairro` varchar(100) DEFAULT NULL,
  `cidade` varchar(100) DEFAULT NULL,
  `estado` varchar(2) DEFAULT NULL,
  `cep` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`cpf`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES ('08715208508','luiz carlos rocha','mafaxc@email.com','jjfkkmnviu8','aluno','77988217142','Rua das cafas','154','Ap 12','Centro','São Paulo','SP','01000-000'),('12345678900','Jota','jo345o@email.com','senha123','aluno','11999999999','Rua das Flores','123','Apt 45','Centro','São Paulo','SP','01234-567'),('12345678901','João da Silva','joao@email.com','senha123','secretaria','11999999999','Rua das Flores','123','Apt 45','Centro','São Paulo','SP','01000-000'),('3784920289','gustavo','gustavinho@email.com','3784920289','professor','7799087463','casa','89','casa amarela','nova olinda','guanambi','BA','46430000'),('4678249290','Matheus','karlusg@email.com','4678249290','aluno','432654232','casa','53','casa','nova esperança','gaunambi','ba','46430000'),('47532327','Maria 341Oliveira','maria@email.com','123','professor','11988887777','Avenida Brasil','456','Bloco B','Jardins','Rio de Janeiro','RJ','22000-000'),('84932049843','luiza','luizinhadojob@email.com','84932049843','aluno','4543754346542','casa','867','cas','ali','cracolandia','SP','0949403'),('938892993','admin','admin@email.com','admin','secretaria','558883235315','rua do embalo','243','sdsgdfgdfsg','ggddfsg','guanambi','df','3426464221'),('98327376234','pedro','pedro2019@gmail.com','98327376234','professor','77998265435','rua 4','343','perto do sacolao','centro','candiba','BA','46432000'),('98763782789','carlos alves moreira','carlosalves38@email.com','98763782789','aluno','7789929877','cxasa','3','casa','nova olinda','guanambi','ba','46430000'),('98983872','vitor gabriel','vitor@email.com','123','aluno','98798743','seila no cafunde dos jurs','45','444ffds','nova olinda','guanambi','ab','46430000');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-17 22:20:05
