package org.amocer.caniveau.calculs;

public class InterpolationLineaire {

    public static double interpoler(double xi, Double[] xData, Double[] yData) throws IllegalStateException {
        if (xData.length != yData.length) { throw new IllegalStateException("Données invalides"); }
        if (xData.length < 2) {throw new IllegalStateException("Données invalides"); }
        int i = 0;
        while (i<xData.length-1){
            double x0 = xData[i];
            double x1 = xData[i+1];
            if (x0 == x1) {throw new IllegalStateException("Données invalides");}
            double y0 = yData[i];
            double y1 = yData[i+1];
            if (x0 <= xi && xi <= x1) {
                return y0+(y1-y0)*(xi-x0)/(x1-x0);
            }
            i++;
        }
        throw new IllegalStateException("Données invalides : Hors plage");
    }
}
