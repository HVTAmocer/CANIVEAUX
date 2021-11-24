package org.amocer.caniveau.calculs.math;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Matrix {

    public static void print(double[][] Matrice) {
        int row = Matrice.length;
        int col = Matrice[0].length;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++)
                //System.out.print(Matrice[i][j] + " ");
                System.out.printf("%.3e\t", Matrice[i][j]);
            System.out.println();
        }
    }

    public static double[][] zeros(int row, int col) {
        double[][] matriceResultat = new double[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++)
                matriceResultat[i][j] += 0.0;
        }
        return matriceResultat;
    }


    public static double[][] multiply(double[][] matrice1, double[][] matrice2) {
        int row1 = matrice1.length;
        int col1 = matrice1[0].length;
        int row2 = matrice2.length;
        int col2 = matrice2[0].length;

        if (row2 != col1) {
            throw new IllegalStateException("invalid dimensions");
        }

        double[][] matriceResultat = new double[row1][col2];
        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                for (int k = 0; k < row2; k++)
                    matriceResultat[i][j] += matrice1[i][k] * matrice2[k][j];
            }
        }
        return matriceResultat;
    }

    public static double[][] multiplyMatrixNumber(double[][] matrice, double num) {
        int row = matrice.length;
        int col = matrice[0].length;

        double[][] matriceResultat = new double[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matriceResultat[i][j] += matrice[i][j] * num;
            }
        }
        return matriceResultat;
    }

    public static double[][] transpose(double[][] matrice) {
        int row = matrice.length;
        int col = matrice[0].length;

        double[][] res = new double[col][row];
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                res[i][j] = matrice[j][i];
            }
        }
        return res;
    }

    public static double determinant(double[][] matrix) {
        if (matrix.length != matrix[0].length)
            throw new IllegalStateException("invalid dimensions");

        if (matrix.length == 2)
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];

        double det = 0;
        for (int i = 0; i < matrix[0].length; i++)
            det += Math.pow(-1, i) * matrix[0][i]
                    * determinant(minor(matrix, 0, i));
        return det;
    }

    public static double[][] inverse(double[][] matrix) {
        double[][] inverse = new double[matrix.length][matrix.length];
        System.out.println(Arrays.stream(matrix).map(l -> Arrays.stream(l).mapToObj(e -> e + "").collect(Collectors.joining("\t"))).collect(Collectors.joining("\n")));
        // minors and cofactors
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                inverse[i][j] = Math.pow(-1, i + j)
                        * determinant(minor(matrix, i, j));

        // adjugate and determinant
        double det = 1.0 / determinant(matrix);
        for (int i = 0; i < inverse.length; i++) {
            for (int j = 0; j <= i; j++) {
                double temp = inverse[i][j];
                inverse[i][j] = inverse[j][i] * det;
                inverse[j][i] = temp * det;
            }
        }

        return inverse;
    }


    private static double[][] minor(double[][] matrix, int row, int column) {
        double[][] minor = new double[matrix.length - 1][matrix.length - 1];

        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; i != row && j < matrix[i].length; j++)
                if (j != column)
                    minor[i < row ? i : i - 1][j < column ? j : j - 1] = matrix[i][j];
        return minor;
    }

    public static void main(String[] args) {
        double[][] A = { { 4E+08, 1, 1E+08, 8 },
                { 7, 2, 9, 2 },
                { 3, 3, 3 , 0 },
                { 4, 4, 4 , 1} };
        double[][] B =
                {
                {1.1928918109316689E8,0.0,7.45557381832293E7,0.0,0.0,0.0},
                {0.0,2.48519127277431E8,6.212978181935775E7,0.0,0.0,0.0},
                {7.45557381832293E7,6.212978181935775E7,2.48519127277431E8,-7.45557381832293E7,6.212978181935775E7,0.0},
                {0.0,0.0,-7.45557381832293E7,1.1928918109316689E8,0.0,7.45557381832293E7},
                {0.0,0.0,6.212978181935775E7,0.0,2.48519127277431E8,6.212978181935775E7},
                {0.0,0.0,0.0,7.45557381832293E7,6.212978181935775E7,1.242595636387155E8}
                };
        System.out.println(determinant(B));
        System.out.println(inverse(B));
    }
}
