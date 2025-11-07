-- ============================================
-- BiciFood - Dades REALS de bicifood_db_v5.0.sql
-- ============================================

-- Inserir categories REALS
INSERT INTO categoria (nom_cat) VALUES 
('CARNS'),
('PEIXOS'),
('AMANIDES I VEGETALS'),
('ARRÒS I PASTA'),
('BEGUDES'),
('POSTRES');

-- Inserir productes REALS segons bicifood_db_v5.0.sql
-- CARNS (categoria 1)
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Vedella amb arròs de l''hort',13.95,'images/Vedella Arròs.png',50,1,'Suculenta carn de vedella, feta al forn de pedra, amb arròs Basmati, acompanyat amb espàrrecs i tomàquets.');
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Vedella amb verdures',14.50,'images/Vedella Verdures.png',45,1,'Tendra vedella a la planxa servida amb verdures de temporada saltejades.');
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Pollastre amb xampinyons',12.95,'images/Pollastre Xampi.png',60,1,'Pit de pollastre a la planxa amb xampinyons salvatger i salsa de fines herbes.');
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Pollastre amb verdures',11.95,'images/Pollatre amb verdures.png',55,1,'Pollastre rostit acompanyat de verdures fresques de l''hort.');

-- PEIXOS (categoria 2)
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Lluç amb verdures',15.95,'images/Lluç Verdures.png',30,2,'Filet de lluç fresc a la planxa amb verdures mediterrànies.');
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Peix a la sal',18.50,'images/Peix Sal.png',25,2,'Peix del dia cuit a la sal marina amb guarnició de verdures.');

-- AMANIDES I VEGETALS (categoria 3)
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Amanida Fresca',8.95,'images/Amanida Fresca.png',80,3,'Mix de fulles verdes fresques, tomàquet, cogombre i vinagreta de la casa.');
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Amanida Gourmet',9.75,'images/Amanida Gourmet.png',75,3,'Amanida selecta amb fruits secs, formatge de cabra i vinagreta balsàmica.');
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Saltejat de verdures',9.50,'images/Saltejat de Verdures.png',90,3,'Verdures fresques saltejades al wok amb oli d''oliva verge extra.');
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Verdures rostides',8.95,'images/Verdures Rustides.png',85,3,'Verdures de temporada rostides al forn amb fines herbes.');

-- ARRÒS I PASTA (categoria 4)
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Paella Valenciana',16.50,'images/Paella.png',40,4,'Autèntica paella valenciana amb pollastre, garrofó, bajoqueta i safrà.');
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Espaguetis Carbonara',11.50,'images/Espaguetis carbonara.png',70,4,'Pasta fresca amb salsa carbonara tradicional italiana.');
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Espaguetis al Pesto',10.95,'images/Espaguetis Pesto.png',65,4,'Espaguetis amb salsa pesto casolana i parmesà.');
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Risotto',13.95,'images/Risotto.png',35,4,'Risotto cremós amb bolets i parmesà envellit.');

-- BEGUDES (categoria 5) - Inclou refrescos i vins
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Coca-Cola',2.50,'images/Coca-cola.jpg',200,5,'Refrescant Coca-Cola en llauna de 33cl.');
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Fanta Taronja',2.50,'images/Fanta.jpg',180,5,'Fanta taronja en llauna de 33cl.');
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Aigua mineral',1.50,'images/Aigua.jpg',300,5,'Aigua mineral natural 50cl.');
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Vi Torremadrina',15.95,'images/Vi Torremadrina.jpeg',25,5,'Vi negre D.O. amb notes afrutades i cos mitjà.');
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Vi Viña Esmeralda',12.95,'images/Vi Viña Esmeralda.jpeg',30,5,'Vi blanc aromàtic amb tocs florals i frescs.');

-- POSTRES (categoria 6)
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Coulant de xocolata',6.50,'images/Coulant.png',100,6,'Coulant de xocolata negra amb cor líquid i gelat de vainilla.');
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
('Fruites del bosc',5.95,'images/Fruites del bosc.png',120,6,'Selecció de fruites del bosc fresques amb crema xantilly.');