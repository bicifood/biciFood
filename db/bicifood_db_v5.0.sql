CREATE DATABASE  IF NOT EXISTS `bicifood_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bicifood_db`;
-- MySQL dump 10.13  Distrib 8.0.42, for macos15 (arm64)
--
-- Host: localhost    Database: bicifood_db
-- ------------------------------------------------------
-- Server version	9.4.0

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
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `id_categoria` int NOT NULL AUTO_INCREMENT,
  `nom_cat` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_categoria`),
  UNIQUE KEY `nom_cat` (`nom_cat`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES 
(1,'CARNS'),
(2,'PEIXOS'),
(3,'AMANIDES I VEGETALS'),
(4,'ARRÒS I PASTA'),
(5,'BEGUDES'),
(6,'POSTRES');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comanda`
--

DROP TABLE IF EXISTS `comanda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comanda` (
  `id_comanda` int NOT NULL AUTO_INCREMENT,
  `id_client` int NOT NULL,
  `data_hora_comanda` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `import_total` decimal(6,2) NOT NULL,
  `adreca_lliurament` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `cp_lliurament` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_estat` int NOT NULL,
  PRIMARY KEY (`id_comanda`),
  KEY `fk_comanda_client` (`id_client`),
  KEY `fk_comanda_estat` (`id_estat`),
  CONSTRAINT `fk_comanda_client` FOREIGN KEY (`id_client`) REFERENCES `usuari` (`id_usuari`),
  CONSTRAINT `fk_comanda_estat` FOREIGN KEY (`id_estat`) REFERENCES `estat_comanda` (`id_estat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comanda`
--

LOCK TABLES `comanda` WRITE;
/*!40000 ALTER TABLE `comanda` DISABLE KEYS */;
/*!40000 ALTER TABLE `comanda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estat_comanda`
--

DROP TABLE IF EXISTS `estat_comanda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estat_comanda` (
  `id_estat` int NOT NULL AUTO_INCREMENT,
  `nom_estat` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_estat`),
  UNIQUE KEY `nom_estat` (`nom_estat`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estat_comanda`
--

LOCK TABLES `estat_comanda` WRITE;
/*!40000 ALTER TABLE `estat_comanda` DISABLE KEYS */;
INSERT INTO `estat_comanda` VALUES (3,'EN RUTA'),(4,'LLIURADA'),(1,'PENDENT'),(2,'PREPARANT');
/*!40000 ALTER TABLE `estat_comanda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `linia_comanda`
--

DROP TABLE IF EXISTS `linia_comanda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `linia_comanda` (
  `id_linia` int NOT NULL AUTO_INCREMENT,
  `id_comanda` int NOT NULL,
  `id_producte` int NOT NULL,
  `preu_unitari` decimal(5,2) NOT NULL,
  `quantitat` int NOT NULL,
  `subtotal` decimal(6,2) NOT NULL,
  PRIMARY KEY (`id_linia`),
  KEY `fk_lc_comanda` (`id_comanda`),
  KEY `fk_lc_producte` (`id_producte`),
  CONSTRAINT `fk_lc_comanda` FOREIGN KEY (`id_comanda`) REFERENCES `comanda` (`id_comanda`),
  CONSTRAINT `fk_lc_producte` FOREIGN KEY (`id_producte`) REFERENCES `producte` (`id_producte`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `linia_comanda`
--

LOCK TABLES `linia_comanda` WRITE;
/*!40000 ALTER TABLE `linia_comanda` DISABLE KEYS */;
/*!40000 ALTER TABLE `linia_comanda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lliurament`
--

DROP TABLE IF EXISTS `lliurament`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lliurament` (
  `id_lliurament` int NOT NULL AUTO_INCREMENT,
  `id_comanda` int NOT NULL,
  `id_repartidor` int DEFAULT NULL,
  `data_hora_assignacio` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `data_hora_lliurament_real` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_lliurament`),
  UNIQUE KEY `id_comanda` (`id_comanda`),
  KEY `fk_lliurament_repartidor` (`id_repartidor`),
  CONSTRAINT `fk_lliurament_comanda` FOREIGN KEY (`id_comanda`) REFERENCES `comanda` (`id_comanda`),
  CONSTRAINT `fk_lliurament_repartidor` FOREIGN KEY (`id_repartidor`) REFERENCES `usuari` (`id_usuari`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lliurament`
--

LOCK TABLES `lliurament` WRITE;
/*!40000 ALTER TABLE `lliurament` DISABLE KEYS */;
/*!40000 ALTER TABLE `lliurament` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producte`
--

DROP TABLE IF EXISTS `producte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producte` (
  `id_producte` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `preu` decimal(5,2) NOT NULL,
  `imatge_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `stock` int NOT NULL DEFAULT '0',
  `id_categoria` int NOT NULL,
  `descripcio` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_producte`),
  UNIQUE KEY `nom_producte_uq` (`nom`),
  KEY `fk_producte_categoria` (`id_categoria`),
  CONSTRAINT `fk_producte_categoria` FOREIGN KEY (`id_categoria`) REFERENCES `categoria` (`id_categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producte`
--

LOCK TABLES `producte` WRITE;
/*!40000 ALTER TABLE `producte` DISABLE KEYS */;
INSERT INTO `producte` VALUES 
-- CARNS (categoria 1)
(1,'Vedella amb arròs de l\'hort',13.95,'images/Vedella Arròs.png',50,1,'Suculenta carn de vedella, feta al forn de pedra, amb arròs Basmati, acompanyat amb espàrrecs i tomàquets.'),
(2,'Vedella amb verdures',14.50,'images/Vedella Verdures.png',45,1,'Tendra vedella a la planxa servida amb verdures de temporada saltejades.'),
(3,'Pollastre amb xampinyons',12.95,'images/Pollastre Xampi.png',60,1,'Pit de pollastre a la planxa amb xampinyons salvatger i salsa de fines herbes.'),
(4,'Pollastre amb verdures',11.95,'images/Pollatre amb verdures.png',55,1,'Pollastre rostit acompanyat de verdures fresques de l\'hort.'),

-- PEIXOS (categoria 2)
(5,'Lluç amb verdures',15.95,'images/Lluç Verdures.png',30,2,'Filet de lluç fresc a la planxa amb verdures mediterrànies.'),
(6,'Peix a la sal',18.50,'images/Peix Sal.png',25,2,'Peix del dia cuit a la sal marina amb guarnició de verdures.'),

-- AMANIDES I VEGETALS (categoria 3)
(7,'Amanida Fresca',8.95,'images/Amanida Fresca.png',80,3,'Mix de fulles verdes fresques, tomàquet, cogombre i vinagreta de la casa.'),
(8,'Amanida Gourmet',9.75,'images/Amanida Gourmet.png',75,3,'Amanida selecta amb fruits secs, formatge de cabra i vinagreta balsàmica.'),
(9,'Saltejat de verdures',9.50,'images/Saltejat de Verdures.png',90,3,'Verdures fresques saltejades al wok amb oli d\'oliva verge extra.'),
(10,'Verdures rostides',8.95,'images/Verdures Rustides.png',85,3,'Verdures de temporada rostides al forn amb fines herbes.'),

-- ARRÒS I PASTA (categoria 4)
(11,'Paella Valenciana',16.50,'images/Paella.png',40,4,'Autèntica paella valenciana amb pollastre, garrofó, bajoqueta i safrà.'),
(12,'Espaguetis Carbonara',11.50,'images/Espaguetis carbonara.png',70,4,'Pasta fresca amb salsa carbonara tradicional italiana.'),
(13,'Espaguetis al Pesto',10.95,'images/Espaguetis Pesto.png',65,4,'Espaguetis amb salsa pesto casolana i parmesà.'),
(14,'Risotto',13.95,'images/Risotto.png',35,4,'Risotto cremós amb bolets i parmesà envellit.'),

-- BEGUDES (categoria 5) - Inclou refrescos i vins
(15,'Coca-Cola',2.50,'images/Coca-cola.jpg',200,5,'Refrescant Coca-Cola en llauna de 33cl.'),
(16,'Fanta Taronja',2.50,'images/Fanta.jpg',180,5,'Fanta taronja en llauna de 33cl.'),
(17,'Aigua mineral',1.50,'images/Aigua.jpg',300,5,'Aigua mineral natural 50cl.'),
(18,'Vi Torremadrina',15.95,'images/Vi Torremadrina.jpeg',25,5,'Vi negre D.O. amb notes afrutades i cos mitjà.'),
(19,'Vi Viña Esmeralda',12.95,'images/Vi Viña Esmeralda.jpeg',30,5,'Vi blanc aromàtic amb tocs florals i frescs.'),

-- POSTRES (categoria 6)
(20,'Coulant de xocolata',6.50,'images/Coulant.png',100,6,'Coulant de xocolata negra amb cor líquid i gelat de vainilla.'),
(21,'Fruites del bosc',5.95,'images/Fruites del bosc.png',120,6,'Selecció de fruites del bosc fresques amb crema xantilly.');
/*!40000 ALTER TABLE `producte` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol`
--

DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol` (
  `id_rol` int NOT NULL AUTO_INCREMENT,
  `nom_rol` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_rol`),
  UNIQUE KEY `nom_rol` (`nom_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol`
--

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` VALUES (1,'ADMIN'),(2,'CLIENT'),(3,'REPARTIDOR');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuari`
--

DROP TABLE IF EXISTS `usuari`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuari` (
  `id_usuari` int NOT NULL AUTO_INCREMENT,
  `email` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_rol` int NOT NULL,
  `punts` int DEFAULT '0',
  `Nom i cognoms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `adreca` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `codi_postal` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `poblacio` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_usuari`),
  UNIQUE KEY `email` (`email`),
  KEY `fk_usuari_rol` (`id_rol`),
  CONSTRAINT `fk_usuari_rol` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuari`
--

LOCK TABLES `usuari` WRITE;
/*!40000 ALTER TABLE `usuari` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuari` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'bicifood_db'
--

--
-- Dumping routines for database 'bicifood_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-17 14:03:49
