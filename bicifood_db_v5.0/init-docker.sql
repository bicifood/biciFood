-- SCRIPT MYSQL ADAPTAT PER DOCKER BICIFOOD
-- Aquest script està optimitzat per funcionar dins de contenidors Docker

-- Configuració inicial
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

--
-- Base de dades: `bicifood_db`
-- Nota: La base de dades ja es crea automàticament via docker-compose
--

--
-- Estructura de taula per a `categoria`
--

DROP TABLE IF EXISTS `categoria`;
CREATE TABLE `categoria` (
  `id_categoria` int NOT NULL AUTO_INCREMENT,
  `nom_cat` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_categoria`),
  UNIQUE KEY `nom_cat` (`nom_cat`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcament de dades per a la taula `categoria`
--

INSERT INTO `categoria` VALUES 
(1,'CARNS'),
(2,'PEIXOS'),
(3,'AMANIDES I VEGETALS'),
(4,'ARRÒS I PASTA'),
(5,'BEGUDES'),
(6,'POSTRES');

--
-- Estructura de taula per a `estat_comanda`
--

DROP TABLE IF EXISTS `estat_comanda`;
CREATE TABLE `estat_comanda` (
  `id_estat` int NOT NULL AUTO_INCREMENT,
  `nom_estat` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_estat`),
  UNIQUE KEY `nom_estat` (`nom_estat`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcament de dades per a la taula `estat_comanda`
--

INSERT INTO `estat_comanda` VALUES 
(1,'PENDENT'),
(2,'PREPARANT'),
(3,'EN RUTA'),
(4,'LLIURADA');

--
-- Estructura de taula per a `producte`
--

DROP TABLE IF EXISTS `producte`;
CREATE TABLE `producte` (
  `id_producte` int NOT NULL AUTO_INCREMENT,
  `nom_prod` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcio` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `preu` decimal(6,2) NOT NULL,
  `id_categoria` int NOT NULL,
  `imatge_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actiu` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id_producte`),
  KEY `fk_producte_categoria` (`id_categoria`),
  CONSTRAINT `fk_producte_categoria` FOREIGN KEY (`id_categoria`) REFERENCES `categoria` (`id_categoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcament de dades per a la taula `producte` 
--

INSERT INTO `producte` VALUES 
(1,'Vedella amb arròs de l\'hort','Deliciosa vedella cuinada amb arròs fresc de l\'hort',13.95,1,'images/carns/vedella_arros.jpg',1),
(2,'Pollastre amb xampinyons','Pollastre tendre amb xampinyons frescos',12.95,1,'images/carns/pollastre_xampinyons.jpg',1),
(3,'Lluç amb verdures','Lluç fresc amb verdures de temporada',15.95,2,'images/peixos/lluc_verdures.jpg',1),
(4,'Peix a la sal','Peix fresc cuinat a la sal marina',18.50,2,'images/peixos/peix_sal.jpg',1),
(5,'Amanida Gourmet','Mescla selecta de fulles verdes amb ingredients premium',9.75,3,'images/amanides/amanida_gourmet.jpg',1),
(6,'Paella Valenciana','Autèntica paella valenciana amb ingredients tradicionals',16.50,4,'images/arros/paella_valenciana.jpg',1),
(7,'Coca-Cola','Beguda refrescant clàssica',2.50,5,'images/begudes/cocacola.jpg',1),
(8,'Aigua Mineral','Aigua mineral natural',1.50,5,'images/begudes/aigua.jpg',1),
(9,'Tiramisu Casolà','Postre italià tradicional fet a casa',4.95,6,'images/postres/tiramisu.jpg',1),
(10,'Brownie de Xocolata','Brownie casolà amb xocolata negra',3.95,6,'images/postres/brownie.jpg',1);

--
-- Estructura de taula per a `usuari`
--

DROP TABLE IF EXISTS `usuari`;
CREATE TABLE `usuari` (
  `id_usuari` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `cognoms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefon` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `adreca` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `codi_postal` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `data_registre` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `actiu` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id_usuari`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcament de dades per a la taula `usuari`
--

INSERT INTO `usuari` VALUES 
(1,'Admin','Sistema','admin@bicifood.com','$2a$10$N.zmdr9k7uOCQb96VdodL.JSK5xy2Te9cdj6xMj6xdL9HG02O8r7.','666000000','Carrer Principal 1','08001',NOW(),1),
(2,'Maria','Garcia López','maria@email.com','$2a$10$N.zmdr9k7uOCQb96VdodL.JSK5xy2Te9cdj6xMj6xdL9HG02O8r7.','666111111','Avinguda Catalunya 25','08002',NOW(),1),
(3,'Joan','Pérez Martín','joan@email.com','$2a$10$N.zmdr9k7uOCQb96VdodL.JSK5xy2Te9cdj6xMj6xdL9HG02O8r7.','666222222','Plaça Major 10','08003',NOW(),1);

--
-- Estructura de taula per a `comanda`
--

DROP TABLE IF EXISTS `comanda`;
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

--
-- Estructura de taula per a `linia_comanda`
--

DROP TABLE IF EXISTS `linia_comanda`;
CREATE TABLE `linia_comanda` (
  `id_linia` int NOT NULL AUTO_INCREMENT,
  `id_comanda` int NOT NULL,
  `id_producte` int NOT NULL,
  `quantitat` int NOT NULL,
  `preu_unitari` decimal(6,2) NOT NULL,
  PRIMARY KEY (`id_linia`),
  KEY `fk_linia_comanda` (`id_comanda`),
  KEY `fk_linia_producte` (`id_producte`),
  CONSTRAINT `fk_linia_comanda` FOREIGN KEY (`id_comanda`) REFERENCES `comanda` (`id_comanda`),
  CONSTRAINT `fk_linia_producte` FOREIGN KEY (`id_producte`) REFERENCES `producte` (`id_producte`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Restaurar configuració
SET FOREIGN_KEY_CHECKS = 1;

-- Informació final
SELECT 'Base de dades BiciFood carregada correctament!' as status;