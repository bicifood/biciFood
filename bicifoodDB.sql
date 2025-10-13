-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: bici_food_db
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `adreca`
--

DROP TABLE IF EXISTS `adreca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `adreca` (
  `id_adreca` int NOT NULL AUTO_INCREMENT,
  `id_usuari` int NOT NULL,
  `carrer` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `codi_postal` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `localitat` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_adreca`),
  KEY `fk_adreca_usuari` (`id_usuari`),
  CONSTRAINT `fk_adreca_usuari` FOREIGN KEY (`id_usuari`) REFERENCES `usuari` (`id_usuari`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adreca`
--

LOCK TABLES `adreca` WRITE;
/*!40000 ALTER TABLE `adreca` DISABLE KEYS */;
/*!40000 ALTER TABLE `adreca` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `allergen`
--

DROP TABLE IF EXISTS `allergen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `allergen` (
  `id_allergen` int NOT NULL AUTO_INCREMENT,
  `nom_allergen` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_allergen`),
  UNIQUE KEY `nom_allergen` (`nom_allergen`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `allergen`
--

LOCK TABLES `allergen` WRITE;
/*!40000 ALTER TABLE `allergen` DISABLE KEYS */;
/*!40000 ALTER TABLE `allergen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `beguda`
--

DROP TABLE IF EXISTS `beguda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `beguda` (
  `id_producte` int NOT NULL,
  `volum_ml` int DEFAULT NULL,
  `te_alcohol` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id_producte`),
  CONSTRAINT `fk_beguda_producte` FOREIGN KEY (`id_producte`) REFERENCES `producte` (`id_producte`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `beguda`
--

LOCK TABLES `beguda` WRITE;
/*!40000 ALTER TABLE `beguda` DISABLE KEYS */;
/*!40000 ALTER TABLE `beguda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `id_categoria` int NOT NULL AUTO_INCREMENT,
  `nom_cat` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_categoria`),
  UNIQUE KEY `nom_cat` (`nom_cat`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (2,'Begudes'),(1,'Plats Principals'),(3,'Postres');
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
  `nom_estat` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_estat`),
  UNIQUE KEY `nom_estat` (`nom_estat`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estat_comanda`
--

LOCK TABLES `estat_comanda` WRITE;
/*!40000 ALTER TABLE `estat_comanda` DISABLE KEYS */;
INSERT INTO `estat_comanda` VALUES (5,'CANCEL·LADA'),(3,'EN RUTA'),(4,'LLIURADA'),(1,'PENDENT'),(2,'PREPARANT');
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
  `quantitat` int NOT NULL,
  `preu_unitari_moment` decimal(5,2) NOT NULL,
  PRIMARY KEY (`id_linia`),
  UNIQUE KEY `uk_comanda_producte` (`id_comanda`,`id_producte`),
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
  `hora_assignacio` datetime DEFAULT NULL,
  `hora_entrega` datetime DEFAULT NULL,
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
-- Table structure for table `plat`
--

DROP TABLE IF EXISTS `plat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `plat` (
  `id_producte` int NOT NULL,
  `temps_preparacio_minuts` int DEFAULT NULL,
  `es_vega` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id_producte`),
  CONSTRAINT `fk_plat_producte` FOREIGN KEY (`id_producte`) REFERENCES `producte` (`id_producte`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plat`
--

LOCK TABLES `plat` WRITE;
/*!40000 ALTER TABLE `plat` DISABLE KEYS */;
/*!40000 ALTER TABLE `plat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `postre`
--

DROP TABLE IF EXISTS `postre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `postre` (
  `id_producte` int NOT NULL,
  `necessita_refrigeracio` tinyint(1) DEFAULT '1',
  `es_sense_gluten` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id_producte`),
  CONSTRAINT `fk_postre_producte` FOREIGN KEY (`id_producte`) REFERENCES `producte` (`id_producte`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `postre`
--

LOCK TABLES `postre` WRITE;
/*!40000 ALTER TABLE `postre` DISABLE KEYS */;
/*!40000 ALTER TABLE `postre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producte`
--

DROP TABLE IF EXISTS `producte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producte` (
  `id_producte` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcio` text COLLATE utf8mb4_unicode_ci,
  `preu` decimal(5,2) NOT NULL,
  `url_foto` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `stock` int DEFAULT '0',
  `id_categoria` int NOT NULL,
  PRIMARY KEY (`id_producte`),
  KEY `fk_producte_categoria` (`id_categoria`),
  CONSTRAINT `fk_producte_categoria` FOREIGN KEY (`id_categoria`) REFERENCES `categoria` (`id_categoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producte`
--

LOCK TABLES `producte` WRITE;
/*!40000 ALTER TABLE `producte` DISABLE KEYS */;
/*!40000 ALTER TABLE `producte` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producte_allergen`
--

DROP TABLE IF EXISTS `producte_allergen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producte_allergen` (
  `id_producte` int NOT NULL,
  `id_allergen` int NOT NULL,
  PRIMARY KEY (`id_producte`,`id_allergen`),
  KEY `fk_pa_allergen` (`id_allergen`),
  CONSTRAINT `fk_pa_allergen` FOREIGN KEY (`id_allergen`) REFERENCES `allergen` (`id_allergen`),
  CONSTRAINT `fk_pa_producte` FOREIGN KEY (`id_producte`) REFERENCES `producte` (`id_producte`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producte_allergen`
--

LOCK TABLES `producte_allergen` WRITE;
/*!40000 ALTER TABLE `producte_allergen` DISABLE KEYS */;
/*!40000 ALTER TABLE `producte_allergen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol`
--

DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol` (
  `id_rol` int NOT NULL AUTO_INCREMENT,
  `nom_rol` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
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
  `nom` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cognoms` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefon` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_rol` int NOT NULL,
  `punts` int DEFAULT '0',
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
-- Dumping events for database 'bici_food_db'
--

--
-- Dumping routines for database 'bici_food_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-13 21:56:22
