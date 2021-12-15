package org.amocer.caniveau.calculs;

public enum TypeAcier {
    // Trois nuances principales en france
    B400C(400, "C"),
    B450A(450, "A"),
    B450B(450, "B"),
    B500A(500, "A"),
    B500B(500, "B");

    public final double fYk, moduleEs, epsilonUk, fUk, epsilonYd, fYd, epsilonUd, fUd;
    public final String ductilite;

    TypeAcier(double fYk, String ductilite) {
        this.fYk = fYk;
        this.ductilite = ductilite;

        //Module d'Young
        moduleEs = 200000.0;

        //Coef. partiel
        double gamaS = 1.15;

        double coefK;
        switch (ductilite) {
            case "A":
                coefK = 1.05;
                epsilonUk = 0.025;
                break;
            case "B":
                coefK = 1.08;
                epsilonUk = 0.05;
                break;
            case "C":
                coefK = 1.15;
                epsilonUk = 0.075;
                break;
            default:
                throw new IllegalStateException();
        }

        // Résistance de calcul à la traction (Pa)
        fYd = fYk / gamaS ;

        // Résistance ultime à la traction (Pa)
        fUk = coefK * fYk / gamaS;
        //

        epsilonYd = fYd / moduleEs;
        //

        epsilonUd = 0.9*epsilonUk;

        //
        fUd = fYd + (fUk - fYd) * (epsilonUd - epsilonYd) / (epsilonUk - epsilonYd);
    }
}
