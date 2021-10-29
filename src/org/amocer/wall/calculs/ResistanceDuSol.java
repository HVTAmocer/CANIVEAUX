package org.amocer.wall.calculs;

public class ResistanceDuSol {

    public static final double COHESION_NON_DRAINEE = 0.15; //MPa
    public static final double COEFF_FROTTEMENT = 0.577; //tan(30°)

    //
    public static double capacitePortance(double qELS, double eccentricite, double largeur, CalculateurDeMursDeSoutainement.Combinaison combinaison) {
        double surfaceEffective = largeur-2*eccentricite;
        double qnet = qELS * 1.2 * 1.4; // bar
        return surfaceEffective*qnet/combinaison.gammaR / combinaison.gammaRd*100.0;  // Methodes semi-empiriques
    }

    //
    public static double resistantGlissement(double excentricite, double largeur, double Vd, CalculateurDeMursDeSoutainement.Combinaison combinaison) {
        //double surfaceEffective = largeur-2*excentricite;
        //double cuk = COHESION_NON_DRAINEE * 1000.0;  // Valeur carractéristique de la cohésion non-drainé du terrain d'assise de la fondation

        return COEFF_FROTTEMENT*Vd/combinaison.gammaR/combinaison.gammaRd;
        //return Math.min(surfaceEffective*cuk/combinaison.gammaR/ combinaison.gammaRd,0.4*Vd);
    }
}
