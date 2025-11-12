-- Dades inicials per a BiciFood DB (H2)
-- ===========================================

-- Categories
INSERT INTO categoria (id_categoria, nom_cat) VALUES 
(1, 'CARNS'),
(2, 'PEIXOS'),
(3, 'AMANIDES I VEGETALS'),
(4, 'ARRÒS I PASTA'),
(5, 'BEGUDES'),
(6, 'POSTRES');

-- Estats de comanda
INSERT INTO estat_comanda (id_estat, nom_estat) VALUES 
(1, 'PENDENT'),
(2, 'EN PREPARACIÓ'),
(3, 'EN REPARTIMENT'),
(4, 'LLIURADA');

-- Rols d'usuari
INSERT INTO rol (id_rol, nom_rol) VALUES 
(1, 'CLIENT'),
(2, 'REPARTIDOR'),
(3, 'ADMINISTRADOR');

-- Productes
INSERT INTO producte (id_producte, nom, preu, imatge_path, stock, id_categoria, descripcio) VALUES 
-- CARNS (categoria 1)
(1, 'Vedella amb arròs de l''hort', 13.95, 'images/Vedella Arròs.png', 50, 1, 'Suculenta carn de vedella, feta al forn de pedra, amb arròs Basmati, acompanyat amb espàrrecs i tomàquets.'),
(2, 'Vedella amb verdures', 14.50, 'images/Vedella Verdures.png', 45, 1, 'Tendra vedella a la planxa servida amb verdures de temporada saltejades.'),
(3, 'Pollastre amb xampinyons', 12.95, 'images/Pollastre Xampi.png', 60, 1, 'Pit de pollastre a la planxa amb xampinyons salvatger i salsa de fines herbes.'),
(4, 'Pollastre amb verdures', 11.95, 'images/Pollatre amb verdures.png', 55, 1, 'Pollastre rostit acompanyat de verdures fresques de l''hort.'),

-- PEIXOS (categoria 2)
(5, 'Lluç amb verdures', 15.95, 'images/Lluç Verdures.png', 30, 2, 'Filet de lluç fresc a la planxa amb verdures mediterrànies.'),
(6, 'Peix a la sal', 18.50, 'images/Peix Sal.png', 25, 2, 'Peix del dia cuit a la sal marina amb guarnició de verdures.'),

-- AMANIDES I VEGETALS (categoria 3)
(7, 'Amanida Fresca', 8.95, 'images/Amanida Fresca.png', 80, 3, 'Mix de fulles verdes fresques, tomàquet, cogombre i vinagreta de la casa.'),
(8, 'Amanida Gourmet', 9.75, 'images/Amanida Gourmet.png', 75, 3, 'Amanida selecta amb fruits secs, formatge de cabra i vinagreta balsàmica.'),
(9, 'Saltejat de verdures', 9.50, 'images/Saltejat de Verdures.png', 90, 3, 'Verdures fresques saltejades al wok amb oli d''oliva verge extra.'),
(10, 'Verdures rostides', 8.95, 'images/Verdures Rustides.png', 85, 3, 'Verdures de temporada rostides al forn amb fines herbes.'),

-- ARRÒS I PASTA (categoria 4)
(11, 'Paella Valenciana', 16.50, 'images/Paella.png', 40, 4, 'Autèntica paella valenciana amb pollastre, garrofó, bajoqueta i safrà.'),
(12, 'Espaguetis Carbonara', 11.50, 'images/Espaguetis carbonara.png', 70, 4, 'Pasta fresca amb salsa carbonara tradicional italiana.'),
(13, 'Espaguetis al Pesto', 10.95, 'images/Espaguetis Pesto.png', 65, 4, 'Espaguetis amb salsa pesto casolana i parmesà.'),
(14, 'Risotto', 13.95, 'images/Risotto.png', 35, 4, 'Risotto cremós amb bolets i parmesà envellit.'),

-- BEGUDES (categoria 5)
(15, 'Coca-Cola', 2.50, 'images/Coca-cola.jpg', 200, 5, 'Refrescant Coca-Cola en llauna de 33cl.'),
(16, 'Fanta Taronja', 2.50, 'images/Fanta.jpg', 180, 5, 'Fanta taronja en llauna de 33cl.'),
(17, 'Aigua mineral', 1.50, 'images/Aigua.jpg', 300, 5, 'Aigua mineral natural 50cl.'),
(18, 'Vi Torremadrina', 15.95, 'images/Vi Torremadrina.jpeg', 25, 5, 'Vi negre D.O. amb notes afrutades i cos mitjà.'),
(19, 'Vi Viña Esmeralda', 12.95, 'images/Vi Viña Esmeralda.jpeg', 30, 5, 'Vi blanc aromàtic amb tocs florals i frescs.'),

-- POSTRES (categoria 6)
(20, 'Coulant de xocolata', 6.50, 'images/Coulant.png', 100, 6, 'Coulant de xocolata negra amb cor líquid i gelat de vainilla.'),
(21, 'Fruites del bosc', 5.95, 'images/Fruites del bosc.png', 120, 6, 'Selecció de fruites del bosc fresques amb crema xantilly.');

-- Usuari administrador de prova (password: admin123)
INSERT INTO usuari (id_usuari, email, password_hash, "nom i cognoms", adreca, codi_postal, poblacio, id_rol, punts) VALUES 
(1, 'admin@bicifood.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrador BiciFood', 'Carrer Principal 1', '08001', 'Barcelona', 3, 0);
