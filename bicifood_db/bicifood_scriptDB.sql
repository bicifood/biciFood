CREATE TABLE postre (
    -- id_producte és PK i FK a PRODUCTE
    id_producte INT PRIMARY KEY, 
    
    necessita_refrigeracio TINYINT(1) DEFAULT 1, -- La majoria de postres sí
    es_sense_gluten TINYINT(1) DEFAULT 0,
    
    -- Definició de la Clau Forana (FK) a la taula pare PRODUCTE
    CONSTRAINT fk_postre_producte 
        FOREIGN KEY (id_producte) 
        REFERENCES producte(id_producte)
        ON DELETE CASCADE -- Si s'elimina el producte, s'elimina la postre
);