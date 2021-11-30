package org.amocer.caniveau.calculs;

public enum TypeChargeRoulante {
        PIETON("Piéton", 250.0),
        ESSIEU_3T("3T par essieu", 3000.0),
        ESSIEU_6T("6T par essieu", 6000.0),
        ESSIEU_13T("13T par essieu", 13000.0),
        NON("NON", 0.0);


    public final String type;
    public final double charge;

    TypeChargeRoulante(String type, double charge) {
        this.type = type;
        this.charge = charge;
    }
}

