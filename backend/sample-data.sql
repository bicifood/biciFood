-- 🚴‍♂️ BiciFood - Dades de Prova per Desenvolupament
-- Aquest script insereix dades d'exemple per poder provar l'aplicació

USE bicifood_db;

-- Inserir rols bàsics
INSERT IGNORE INTO rol (id, nom, descripcio) VALUES 
(1, 'CLIENT', 'Usuari client que fa comandes'),
(2, 'REPARTIDOR', 'Usuari repartidor que lliura comandes'),
(3, 'ADMIN', 'Administrador del sistema');

-- Inserir categories de productes
INSERT IGNORE INTO categoria (id, nom, descripcio, activa) VALUES
(1, 'CARNS', 'Productes càrnics frescos', true),
(2, 'PEIXOS', 'Productes del mar i marisc', true), 
(3, 'BEGUDES', 'Begudes i refrescos', true),
(4, 'POSTRES', 'Dolços i postres casolanes', true),
(5, 'VERDURES', 'Verdures i hortalisses fresques', true),
(6, 'FRUITES', 'Fruites de temporada', true),
(7, 'LACTIS', 'Productes lactis i ous', true),
(8, 'PA_CEREALS', 'Pa, cereals i llegums', true);

-- Inserir estats de comanda
INSERT IGNORE INTO estat_comanda (id, nom, descripcio) VALUES
(1, 'PENDENT', 'Comanda rebuda, pendent de processar'),
(2, 'PREPARANT', 'Comanda en procés de preparació'),
(3, 'EN_RUTA', 'Comanda assignada a repartidor'),
(4, 'LLIURADA', 'Comanda lliurada correctament'),
(5, 'CANCEL_LADA', 'Comanda cancel·lada');

-- Inserir usuaris de prova
INSERT IGNORE INTO usuari (id, nom, cognoms, email, telefon, adreca, password_hash, punts, data_registre, actiu, rol_id) VALUES
(1, 'Joan', 'Garcia Puig', 'joan@bicifood.cat', '600123456', 'Carrer Major, 123, Barcelona', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqfqvVkn5EkJEOFqBS3w3.C', 150, NOW(), true, 1),
(2, 'Maria', 'López Fernández', 'maria@bicifood.cat', '600654321', 'Avinguda Diagonal, 456, Barcelona', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqfqvVkn5EkJEOFqBS3w3.C', 75, NOW(), true, 1),
(3, 'Pau', 'Martínez Roca', 'pau.repartidor@bicifood.cat', '600789123', 'Carrer Balmes, 789, Barcelona', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqfqvVkn5EkJEOFqBS3w3.C', 0, NOW(), true, 2),
(4, 'Admin', 'BiciFood', 'admin@bicifood.cat', '600000000', 'Oficina Central, Barcelona', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqfqvVkn5EkJEOFqBS3w3.C', 1000, NOW(), true, 3);

-- Inserir productes de prova
INSERT IGNORE INTO producte (id, nom, descripcio, preu, stock, disponible, categoria_id, data_creacio) VALUES
-- CARNS
(1, 'Pollastre Ecològic', 'Pollastre ecològic de granja local, 1kg', 12.50, 25, true, 1, NOW()),
(2, 'Vedella Premium', 'Entrecot de vedella premium, peça de 300g', 18.90, 15, true, 1, NOW()),
(3, 'Porc Ibèric', 'Llom de porc ibèric curat, 200g', 22.00, 12, true, 1, NOW()),

-- PEIXOS  
(4, 'Salmó Noruec', 'Filet de salmó noruec fresc, 250g', 15.75, 20, true, 2, NOW()),
(5, 'Lluç del Mediterrani', 'Lluç fresc del Mediterrani, peça sencera', 28.50, 8, true, 2, NOW()),
(6, 'Gambes de Palamós', 'Gambes fresques de Palamós, 500g', 35.00, 6, true, 2, NOW()),

-- BEGUDES
(7, 'Aigua Mineral', 'Aigua mineral natural, ampolla 1.5L', 1.20, 100, true, 3, NOW()),
(8, 'Cervesa Artesana', 'Cervesa artesana catalana, ampolla 33cl', 3.50, 50, true, 3, NOW()),
(9, 'Vi Negre DO', 'Vi negre Denominació d Origen, ampolla 75cl', 12.00, 30, true, 3, NOW()),

-- POSTRES
(10, 'Crema Catalana', 'Crema catalana casolana, pot individual', 4.50, 25, true, 4, NOW()),
(11, 'Mel Artesana', 'Mel d eucaliptus artesana, pot 500g', 8.90, 20, true, 4, NOW()),

-- VERDURES
(12, 'Tomàquets de Montserrat', 'Tomàquets ecològics de Montserrat, 1kg', 3.80, 40, true, 5, NOW()),
(13, 'Enciam Francès', 'Enciam francès fresc, unitat', 1.50, 35, true, 5, NOW()),
(14, 'Carbassó Català', 'Carbassons catalans, 500g', 2.20, 30, true, 5, NOW()),

-- FRUITES
(15, 'Maduixes del Maresme', 'Maduixes fresques del Maresme, barqueta 250g', 4.20, 25, true, 6, NOW()),
(16, 'Taronges València', 'Taronges de València per sucs, 2kg', 5.50, 45, true, 6, NOW()),
(17, 'Pomes Fuji', 'Pomes Fuji categoria extra, 1kg', 3.90, 50, true, 6, NOW()),

-- LACTIS
(18, 'Formatge Manchego', 'Formatge manchego curat, tall 200g', 8.50, 18, true, 7, NOW()),
(19, 'Iogurt Ecològic', 'Iogurt natural ecològic, pack 4 unitats', 3.20, 60, true, 7, NOW()),
(20, 'Ous Camperos', 'Ous de gallines camperes, dotzena', 4.80, 40, true, 7, NOW()),

-- PA I CEREALS  
(21, 'Pa de Sègol', 'Pa de sègol artesà, barra 500g', 2.80, 25, true, 8, NOW()),
(22, 'Arròs Bomba', 'Arròs bomba DO Delta de l Ebre, paquet 1kg', 6.50, 35, true, 8, NOW()),
(23, 'Llenties Roges', 'Llenties roges ecològiques, paquet 500g', 3.40, 45, true, 8, NOW());

-- Inserir algunes comandes d'exemple
INSERT IGNORE INTO comanda (id, client_id, data_comanda, total_comanda, observacions, estat_id) VALUES
(1, 1, '2024-11-01 10:30:00', 45.70, 'Lliurar per la porta del darrere', 4),
(2, 2, '2024-11-02 14:15:00', 28.90, '', 4),  
(3, 1, '2024-11-04 16:45:00', 67.20, 'Trucar abans de pujar', 2),
(4, 2, '2024-11-05 12:20:00', 23.40, '', 1);

-- Inserir línies de comanda d'exemple
INSERT IGNORE INTO linia_comanda (id, comanda_id, producte_id, quantitat, preu_unitari, subtotal) VALUES
-- Comanda 1 (Joan - Lliurada)
(1, 1, 1, 2, 12.50, 25.00),  -- 2 Pollastres  
(2, 1, 7, 3, 1.20, 3.60),    -- 3 Aigües
(3, 1, 10, 4, 4.50, 18.00),  -- 4 Cremes catalanes

-- Comanda 2 (Maria - Lliurada)  
(4, 2, 4, 1, 15.75, 15.75),  -- 1 Salmó
(5, 2, 12, 2, 3.80, 7.60),   -- 2kg Tomàquets
(6, 2, 21, 2, 2.80, 5.60),   -- 2 Pans de sègol

-- Comanda 3 (Joan - Preparant)
(7, 3, 6, 1, 35.00, 35.00),  -- 1 Gambes de Palamós  
(8, 3, 9, 2, 12.00, 24.00),  -- 2 Vins negres
(9, 3, 18, 1, 8.50, 8.50),   -- 1 Formatge manchego

-- Comanda 4 (Maria - Pendent)
(10, 4, 15, 2, 4.20, 8.40),  -- 2 Maduixes
(11, 4, 19, 3, 3.20, 9.60),  -- 3 Iogurts  
(12, 4, 22, 1, 6.50, 6.50);  -- 1 Arròs bomba

-- Inserir lliuraments d'exemple
INSERT IGNORE INTO lliurament (id, comanda_id, repartidor_id, adreca_lliurament, data_lliurament, observacions) VALUES
(1, 1, 3, 'Carrer Major, 123, Barcelona', '2024-11-01 11:45:00', 'Lliurat correctament per la porta del darrere'),
(2, 2, 3, 'Avinguda Diagonal, 456, Barcelona', '2024-11-02 15:30:00', 'Lliurat correctament');

-- Actualitzar l'auto-increment per evitar conflictes
ALTER TABLE rol AUTO_INCREMENT = 4;
ALTER TABLE categoria AUTO_INCREMENT = 9;  
ALTER TABLE estat_comanda AUTO_INCREMENT = 6;
ALTER TABLE usuari AUTO_INCREMENT = 5;
ALTER TABLE producte AUTO_INCREMENT = 24;
ALTER TABLE comanda AUTO_INCREMENT = 5;
ALTER TABLE linia_comanda AUTO_INCREMENT = 13;
ALTER TABLE lliurament AUTO_INCREMENT = 3;

-- Mostrar resum de les dades insertades
SELECT 'Dades de prova insertades correctament!' as Status;
SELECT 'Rols:', COUNT(*) as Total FROM rol;
SELECT 'Categories:', COUNT(*) as Total FROM categoria;  
SELECT 'Estats:', COUNT(*) as Total FROM estat_comanda;
SELECT 'Usuaris:', COUNT(*) as Total FROM usuari;
SELECT 'Productes:', COUNT(*) as Total FROM producte;
SELECT 'Comandes:', COUNT(*) as Total FROM comanda;
SELECT 'Línies comanda:', COUNT(*) as Total FROM linia_comanda;
SELECT 'Lliuraments:', COUNT(*) as Total FROM lliurament;

-- Informació d'accés per testing
SELECT '=== USUARIS DE PROVA ===' as Info;
SELECT 'Email: joan@bicifood.cat | Password: bicifood123 | Rol: CLIENT' as 'Usuari 1';
SELECT 'Email: maria@bicifood.cat | Password: bicifood123 | Rol: CLIENT' as 'Usuari 2';  
SELECT 'Email: pau.repartidor@bicifood.cat | Password: bicifood123 | Rol: REPARTIDOR' as 'Usuari 3';
SELECT 'Email: admin@bicifood.cat | Password: admin123 | Rol: ADMIN' as 'Usuari 4';