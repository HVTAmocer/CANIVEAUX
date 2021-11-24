package org.amocer.caniveau.calculs.math;

public class Vecteur {

    public static void print(double[] Vecteur){
        int row = Vecteur.length;

        for (int i = 0; i < row; i++) {
            System.out.printf("%15.1f\t",Vecteur[i]);
            System.out.println();;
        }
    }

    public static double[] zeros(int row){
        double[] result = new double[row];
        for (int i = 0; i < row; i++) {
            result[i] += 0.0;
        }
        return result;
    }

    public static double multiply(double[] vecteur1, double[] vecteur2) {
        int row1 = vecteur1.length;
        int row2 = vecteur2.length;
        if (row1!=row2){
            throw new IllegalStateException("invalid dimensions");
        }

        double result = 0.0;
        for (int i = 0; i < row1; i++) {
            result += vecteur1[i] * vecteur2[i];
        }
        return result;
    }

    public static double[] multiplyNumber(double[] vecteur, double num) {
        int row = vecteur.length;

        double[] result = new double[row];
        for (int i = 0; i < row; i++) {
            result[i] += vecteur[i] * num;
        }
        return result;
    }

    public static double max(double[] vecteur) {
        int row = vecteur.length;
        double max = 0.0;
        for (double v : vecteur) {
            if (max < v) {
                max = v;
            }
        }
        return max;
    }

    public static double min(double[] vecteur) {
        int row = vecteur.length;
        double min = 0.0;
        for (double v : vecteur) {
            if (min > v) {
                min = v;
            }
        }
        return min;
    }

}
