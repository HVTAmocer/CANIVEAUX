package org.amocer.caniveau.calculs.math.syslin;

public class SolveurDeSystemesLineaires {
    private final Matrice matriceInverse;

    public SolveurDeSystemesLineaires(Matrice matriceDesCoefficients) {
        Matrice matriceDesCoefficientsClonee = matriceDesCoefficients.clone();
        matriceInverse = Matrice.identite(matriceDesCoefficientsClonee.nLignes());
        triangulaireSuperieure(matriceDesCoefficientsClonee);
        triangulaireInferieure(matriceDesCoefficientsClonee);
    }

    private void triangulaireSuperieure(Matrice matriceDesCoefficients) {
        for (int ligne = 0; ligne < matriceDesCoefficients.nLignes(); ligne++) {
            double valeurDiagonale = matriceDesCoefficients.valeur(ligne, ligne);
            matriceDesCoefficients.diviserLignePourUneConstant(ligne, valeurDiagonale);
            matriceInverse.diviserLignePourUneConstant(ligne, valeurDiagonale);
            for (int ligne2 = ligne + 1; ligne2 < matriceDesCoefficients.nLignes(); ligne2++) {
                double coefficientLigneOrigine = -matriceDesCoefficients.valeur(ligne2, ligne);
                matriceDesCoefficients.sommeLignes(ligne, coefficientLigneOrigine, ligne2);
                matriceInverse.sommeLignes(ligne, coefficientLigneOrigine, ligne2);
            }
        }
    }

    private void triangulaireInferieure(Matrice matriceDesCoefficients) {
        for (int ligne = matriceDesCoefficients.nLignes() - 1; ligne > 0; ligne--) {
            double valeurDiagonale = matriceDesCoefficients.valeur(ligne, ligne);
            matriceDesCoefficients.diviserLignePourUneConstant(ligne, valeurDiagonale);
            matriceInverse.diviserLignePourUneConstant(ligne, valeurDiagonale);
            for (int ligne2 = ligne - 1; ligne2 >= 0; ligne2--) {
                double coefficientLigneOrigine = -matriceDesCoefficients.valeur(ligne2, ligne);
                matriceDesCoefficients.sommeLignes(ligne, coefficientLigneOrigine, ligne2);
                matriceInverse.sommeLignes(ligne, coefficientLigneOrigine, ligne2);
            }
        }
    }

    public Matrice solve(Matrice vecteurDesConstants) {
        return Matrice.produitMatriciel(matriceInverse, vecteurDesConstants);
    }

    public Matrice matriceInverse() {
        return matriceInverse.clone();
    }
}