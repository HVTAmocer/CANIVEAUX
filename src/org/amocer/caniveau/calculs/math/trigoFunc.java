package org.amocer.caniveau.calculs.math;

public class trigoFunc {

    public static double sin(double degs){
        double rads = Math.toRadians(degs);
        return 1.0 / Math.sin(rads);
    }

    // Calculate cotangent
    public static double tan(double degs){
        double rads = Math.toRadians(degs);
        return  Math.tan(rads);
    }

        // Calculate cotangent
    public static double coTan(double degs){
        double rads = Math.toRadians(degs);
        return 1.0 / Math.tan(rads);
    }

    public static double arcTan(double tanA){
        double rads = Math.atan(tanA);
        return Math.toDegrees(rads);
    }

    public static double arcCoTan(double coTanA){
        double rads = Math.atan(1/coTanA);
        return Math.toDegrees(rads);
    }
}

