package com.bicifood.model;

/**
 * Enumeració dels possibles estats d'una comanda
 */
public enum EstatComanda {
    PENDENT("Pendent de confirmació"),
    CONFIRMADA("Confirmada"),
    EN_PREPARACIO("En preparació"),
    LLESTA("Llesta per recollir"),
    ENTREGADA("Entregada"),
    CANCEL_LADA("Cancel·lada");

    private final String descripcio;

    EstatComanda(String descripcio) {
        this.descripcio = descripcio;
    }

    public String getDescripcio() {
        return descripcio;
    }

    @Override
    public String toString() {
        return descripcio;
    }
}