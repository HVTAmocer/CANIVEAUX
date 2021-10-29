package org.amocer.wall.calculs;

public enum TypeBeton {
    C20(20),
    C25(25),
    C28(28),
    C30(30),
    C32(32),
    C35(35),
    C40(40),
    C45(45),
    C50(50),
    C55(55),
    C60(60),
    C70(70),
    C80(80),
    C90(90);

    public final double fck, fcm, fctm, fctk005, fctk095, Ecm,
            epsilonC1, epsilonCu1, epsilonC2, epsilonCu2,
            nDegree, epsilonC3, epsilonCu3,
            gamaC, alphaCC, fcd;

    TypeBeton(double fck) {
        this.fck = fck;
        //
        fcm = fck + 8.0;

        if (fck <= 50) {
            fctm = 0.3 * Math.pow(fck, (double) 2 / 3);
        } else {
            fctm = 2.12 * Math.log(1 + (fck + 8.0) / 10);
        }

        fctk005 = 0.7 * fctm;

        fctk095 = 1.3 * fctm;

        Ecm = 22000.0 * Math.pow((fck + 8) / 10, 0.3) ;

        epsilonC1 = Math.min(0.7 * Math.pow(fck + 8.0, 0.31), 2.8)* Math.pow(10, -3);

        if (fck <= 50) {
            epsilonCu1 = 3.5 * Math.pow(10, -3);
        } else {
            epsilonCu1 = (2.8 + 27 * Math.pow((98 - fck + 8.0) / 100, 4)) * Math.pow(10, -3);
        }

        if (fck <= 50) {
            epsilonC2 = 2.0 * Math.pow(10, -3);
        } else {
            epsilonC2 = (2.0 + 0.085 * Math.pow(fck - 50, 0.53)) * Math.pow(10, -3);
        }

        if (fck <= 50) {
            epsilonCu2 = 3.5 * Math.pow(10, -3);
        } else {
            epsilonCu2 = (2.6 + 35 * Math.pow((90 - fck) / 100, 4)) * Math.pow(10, -3);
        }


        if (fck <= 50) {
            nDegree = 2.0;
        } else {
            nDegree = 1.4 + 23.4 * Math.pow((90 - fck) / 100, 4);
        }

        if (fck <= 50) {
            epsilonC3 = 1.75 * Math.pow(10, -3);
        } else {
            epsilonC3 = (1.75 + 0.55 * (fck - 50) / 40) * Math.pow(10, -3);
        }

        if (fck <= 50) {
            epsilonCu3 = 3.5 * Math.pow(10, -3);
        } else {
            epsilonCu3 = (2.6 + 35 * Math.pow((90 - fck) / 100, 4)) * Math.pow(10, -3);
        }

        // Coefficient partiel
        gamaC = 1.50;
        alphaCC = 1.0;

        // Résistance de calcul
        fcd = alphaCC * this.fck / gamaC;
    }

    public static TypeBeton get(String nomBeton) {
        return TypeBeton.valueOf(nomBeton.substring(0,3));
    }
}
