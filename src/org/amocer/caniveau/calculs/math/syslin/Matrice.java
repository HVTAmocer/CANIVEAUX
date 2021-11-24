package org.amocer.caniveau.calculs.math.syslin;

import java.util.Arrays;

public class Matrice implements Cloneable {
    private final double[][] matrice;

    private Matrice(double[][] matrice) {
        this.matrice = matrice;
    }

    public static Matrice identite(int n) {
        double[][] identite = new double[n][n];
        for (int i = 0; i < identite.length; i++) {
            identite[i][i] = 1.0;
        }
        return new Matrice(identite);
    }

    public Matrice clone() {
        double[][] matriceCopie = new double[matrice.length][];
        for (int i = 0; i < matrice.length; i++) {
            matriceCopie[i] = new double[matrice[i].length];
            for (int j = 0; j < matrice[i].length; j++) {
                matriceCopie[i][j] = matrice[i][j];
            }
        }
        return new Matrice(matriceCopie);
    }

    public static Matrice carree(double[][] matrice) {
        int nombreDeLignes = matrice.length;
        if (nombreDeLignes == 0) throw new IllegalStateException();
        for (double[] ligne : matrice) {
            int nombreDeColonnes = ligne.length;
            if (nombreDeLignes != nombreDeColonnes) throw new IllegalStateException();
        }
        return new Matrice(matrice);
    }

    public static Matrice ligne(double[] ligne) {
        double[][] matrice = new double[1][ligne.length];
        for (int i = 0; i < ligne.length; i++) {
            matrice[0][i] = ligne[i];
        }
        return new Matrice(matrice);
    }

    public static Matrice colonne(double[] colonne) {
        double[][] matrice = new double[colonne.length][1];
        for (int i = 0; i < colonne.length; i++) {
            matrice[i][0] = colonne[i];
        }
        return new Matrice(matrice);
    }

    public static Matrice colonneConstante(int nombreDeLignes, double constant) {
        double[] colonne = new double[nombreDeLignes];
        Arrays.fill(colonne, constant);
        return colonne(colonne);
    }

    public static Matrice ligneConstante(int nombreDeColonnes, double constant) {
        double[] ligne = new double[nombreDeColonnes];
        Arrays.fill(ligne, constant);
        return ligne(ligne);
    }

    public int nLignes() {
        return matrice.length;
    }

    public int nColonnes() {
        return matrice[0].length;
    }

    public double valeur(int ligne, int colonne) {
        return matrice[ligne][colonne];
    }

    public void diviserLignePourUneConstant(int ligne, double diviseur) {
        if(diviseur==1.0)return;
        for (int colonne = 0; colonne < matrice[ligne].length; colonne++) {
            matrice[ligne][colonne] /= diviseur;
        }
    }

    public void sommeLignes(int ligneOrigine, double coefficientLigneOrigine, int ligneDestination) {
        if(coefficientLigneOrigine==0)return;
        for (int colonne = 0; colonne < matrice[ligneDestination].length; colonne++) {
            matrice[ligneDestination][colonne] += (matrice[ligneOrigine][colonne] * coefficientLigneOrigine);
        }
    }

    public Matrice transpose() {
        double[][] transpose = new double[nColonnes()][nLignes()];
        for (int ligne = 0; ligne < nColonnes(); ligne++) {
            for (int colonne = 0; colonne < nLignes(); colonne++) {
                transpose[ligne][colonne] = matrice[colonne][ligne];
            }
        }
        return new Matrice(transpose);
    }

    public static Matrice produitMatriciel(Matrice matrice1, Matrice matrice2) {
        if (matrice1.nColonnes() != matrice2.nLignes()) throw new IllegalStateException();
        int kMax = matrice1.nColonnes();
        double[][] resultat = new double[matrice1.nLignes()][matrice2.nColonnes()];
        for (int i = 0; i < resultat.length; i++) {
            for (int j = 0; j < resultat[i].length; j++) {
                for (int k = 0; k < kMax; k++) {
                    resultat[i][j] += (matrice1.valeur(i, k) * matrice2.valeur(k, j));
                }
            }
        }
        return new Matrice(resultat);
    }

    public double[][] toArray(){
        return clone().matrice;
    }

    public String export(){
        StringBuilder sb = new StringBuilder();
        for(double[] ligne:matrice){
            for(double value:ligne){
                sb.append(value).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
