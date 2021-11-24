package org.amocer.caniveau.calculs;

public enum Combinaison {
    ELU("ELU", 1.35, 1.50, 1.40, 1.20, 1.0 / 15.0),
    ELS_CAR("ELS CAR", 1.00, 1.00, 2.30, 1.20, 1.0 / 2.0),
    ELS_QP("ELS QP", 1.00, 0.50, 2.30, 1.20, 2.0 / 3.0);

    public final double gammaG, gammaQ;
    public final double gammaR, gammaRd;
    public final double excentriciteMax;
    public final String nom;

    Combinaison(String nom, double gammaG, double gammaQ, double gammaR, double gammaRd, double excentriciteMax) {
        this.gammaG = gammaG;
        this.gammaQ = gammaQ;
        this.gammaR = gammaR;
        this.gammaRd = gammaRd;
        this.excentriciteMax = excentriciteMax;
        this.nom = nom;
    }
}
