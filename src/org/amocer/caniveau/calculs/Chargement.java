package org.amocer.caniveau.calculs;

public abstract class Chargement {
    public final double intensite;
    public final TypeChargement typeCharge;

    public Chargement(double intensite, TypeChargement typeCharge) {
        this.intensite = intensite;
        this.typeCharge = typeCharge;
    }

    public static class ChargePonctuelle extends Chargement{
        public double position;

        public ChargePonctuelle(double intensite, double position, TypeChargement typeCharge) {
            super(intensite, typeCharge);
            this.position = position;
        }
    }

    public static class ChargeUniforme extends Chargement{
        public double position;

        public ChargeUniforme(double intensite, double position, TypeChargement typeCharge) {
            super( intensite, typeCharge);
            this.position = position;
        }
    }

    public static double getGama(TypeChargement typeChargement, Combinaison combinaison){
        double gama = 1.0;
        if (combinaison == Combinaison.ELU) {
            gama = typeChargement.gammaELU;
        } else if (combinaison == Combinaison.ELS){
            gama = typeChargement.gammaELS;
        }
        return gama;
    }

    public enum TypeChargement {
        permanent(1.35, 1.0),
        exploitation(1.50, 1.0),
        neige(1.50, 1.0),
        vent(1.50, 1.0),
        seisme(1.0, 1.0);


        public final double gammaELU,gammaELS ;

        TypeChargement(double gammaELU, double gammaELS) {
            this.gammaELU = gammaELU;
            this.gammaELS = gammaELS;
        }
    }
    public enum Combinaison {
        ELU,
        ELS,
        ELA
    }
}
