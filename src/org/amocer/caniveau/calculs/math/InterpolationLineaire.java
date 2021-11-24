package org.amocer.caniveau.calculs.math;

public class InterpolationLineaire {
	
	/**
	 * 
	 * @param xData base de donnée en entrée ordonnée
	 * @param yData base de donnée en sortie
	 * @param x valeur connue en entrée
	 * @return valeurs interpolée
	 * @throws Exception
	 */
	public static double interpoler(double[] xData, double[] yData, double x) throws IllegalStateException {
		if(xData.length!=yData.length)throw new IllegalStateException("Donné d'interpolations invalides : Les données d'interpolation sont incomplets");
		if(xData.length<2)throw new IllegalStateException("Donné d'interpolations invalides : Pas assez des données");
		int i = 0;
		while(i<xData.length-1) {
			double x0 = xData[i];
			double x1 = xData[i+1];
			if(x0==x1)throw new IllegalStateException("Donné d'interpolations invalides : Division par zero");
			double y0 = yData[i];
			double y1 = yData[i+1];
			if(x0<=x&&x1>=x)return y0+(y1-y0)*(x-x0)/(x1-x0);
			i++;
		}
		throw new IllegalStateException("Donné d'interpolations invalides : Donnée Insuffisants (Hors Plage)");
	}
}
