package org.amocer.caniveau.calculs;

import org.amocer.caniveau.calculs.math.trigoFunc;

public class Calcul {
    public final Donnee donnee;
    public final double POIDS_VOLUMIQUE_BETON = 25.0; //KN/m3
    public final double ANGLE_FROTTEMENT_INTERFACE = 0.0;
    public final double COHESION = 0.0;
    public final String typeFibre;

    public final String typeCaniveau;

    public final double longeur;
    public final double hauteur;
    public final double largeur;
    public final double epaisseurCouvercle;
    public final double hauteurRemplai;

    public final double chargeUniforme;
    public final double distanceChargeUniforme;
    public final double chargePontuelle;
    public final double distanceChargePontuelle;
    public final double chargeRoulante;

    public final double poidsVolumiqueSol;
    public final double poidsCouvercle;
    private final Combinaison combinaison;

    public Calcul(Donnee donnee, Combinaison combinaison) {
        this.donnee = donnee;
        this.typeCaniveau = donnee.typeCaniveau;
        this.typeFibre = donnee.typeFibre;
        this.longeur = donnee.longueur;
        this.hauteur = donnee.hauteur;
        this.largeur = donnee.largeur;
        this.hauteurRemplai = donnee.hauteurRemblai;
        this.chargePontuelle = donnee.chargePontuelle;
        this.distanceChargePontuelle = donnee.distanceChargePontuelle;
        this.chargeUniforme = donnee.chargeUniforme;
        this.distanceChargeUniforme = donnee.distanceChargeUniforme;
        this.poidsVolumiqueSol = donnee.poidsVolumiqueSol;
        this.chargeRoulante = donnee.chargeRoulante;
        this.epaisseurCouvercle = donnee.epaisseurCouvercle;
        this.poidsCouvercle = donnee.poidsCouvercle;
        this.combinaison = combinaison;
    }


    public PressionAgissant pousseeTerreParoi(double angleFrottement) {
        double Ka = coefficientDePoussee(angleFrottement);
        double pressionMin = Ka*poidsVolumiqueSol*hauteurRemplai;
        double pressionMax = Ka*poidsVolumiqueSol*(hauteurRemplai + epaisseurCouvercle);
        double longeurCharge = hauteurRemplai + epaisseurCouvercle;
        return new PressionAgissant(pressionMax,pressionMin, longeurCharge);
    }

    public PressionAgissant pousseeTerreParoiDueALaChargeUniforme(double angleFrottement) {
        double Ka = coefficientDePoussee(angleFrottement);
        double pressionMin; ;
        double pressionMax;
        double longeurCharge;
        if (distanceChargeUniforme <= hauteurRemplai/trigoFunc.tan(angleFrottement)) {
            pressionMin  = 0.0;
            pressionMax = Ka*chargeUniforme;
            longeurCharge = hauteur;
        }else if (distanceChargeUniforme <= (hauteur + epaisseurCouvercle + hauteurRemplai)/ trigoFunc.tan(angleFrottement)){
            pressionMin  = 0.0; ;
            pressionMax = Ka*chargeUniforme;
            longeurCharge = hauteur + epaisseurCouvercle + hauteurRemplai - distanceChargeUniforme*trigoFunc.tan(angleFrottement);
        } else {
            pressionMin = 0.0;
            pressionMax = 0.0;
            longeurCharge = 0.0;
        }
        return new PressionAgissant(pressionMax,pressionMin, longeurCharge);
    }

    public PressionAgissant pousseeTerreParoiDueALaChargePontuelle(double angleFrottement) {
        double pressionMin;
        double pressionMax;
        double longeurCharge;
        if (distanceChargePontuelle <=0 || distanceChargePontuelle >= (hauteur + epaisseurCouvercle + hauteurRemplai)/ trigoFunc.tan(angleFrottement) ) {
            pressionMin = 0.0;
            pressionMax = 0.0;
            longeurCharge = 0.0;
        } else {
            pressionMin = 0.0;
            pressionMax = 4*chargePontuelle*trigoFunc.tan(45.0-angleFrottement/2)/Math.pow(distanceChargePontuelle,2)/(trigoFunc.tan(45+angleFrottement/2.0)-trigoFunc.tan(angleFrottement));
            longeurCharge = 2*distanceChargePontuelle*trigoFunc.tan(27.0);
        }
        return new PressionAgissant(pressionMax,pressionMin, longeurCharge);
    }

    public PressionAgissant pousseeTerreParoiDueALaChargeRoulante(double angleFrottement, double distanceChargeRoulante) {
        double pressionMin;
        double pressionMax;
        double longeurCharge;
        if (distanceChargeRoulante <=0 || distanceChargeRoulante >= (hauteur + epaisseurCouvercle + hauteurRemplai)/ trigoFunc.tan(angleFrottement) ) {
            pressionMin = 0.0;
            pressionMax = 0.0;
            longeurCharge = 0.0;
        } else {
            pressionMin = 0.0;
            pressionMax = 4*chargeRoulante*trigoFunc.tan(45.0-angleFrottement/2)/Math.pow(distanceChargeRoulante,2)/(trigoFunc.tan(45+angleFrottement/2.0)-trigoFunc.tan(angleFrottement));
            longeurCharge = 2*distanceChargeRoulante*trigoFunc.tan(27.0);
        }
        return new PressionAgissant(pressionMax,pressionMin, longeurCharge);
    }


    public PressionAgissant pousseeTerreFond(double epaisseurFond, double epaisseurParoi) {
        double poidSolRemblai = poidsVolumiqueSol*hauteurRemplai;
        double poidBeton = POIDS_VOLUMIQUE_BETON*(2*epaisseurParoi + epaisseurFond) + poidsCouvercle;
        double pressionMin = poidSolRemblai + poidBeton;
        double pressionMax = pressionMin;
        double longeurCharge = largeur;
        return new PressionAgissant(pressionMax, pressionMin, longeurCharge);
    }

    public PressionAgissant pousseeTerreFondDueALaChargeUniforme(double angleFrottementSol) {
        double Ka = coefficientDePoussee(angleFrottementSol);
        double pressionMin;
        double pressionMax;
        double longeurCharge;
        if (distanceChargeUniforme >0.0) {
            pressionMin  = 0.0;
            pressionMax =  0.0;
            longeurCharge = 0.0;
        }else {
            pressionMin  = chargeUniforme;
            pressionMax =  chargeUniforme;
            longeurCharge = Math.min(largeur, Math.abs(distanceChargeUniforme) );
        }
        return new PressionAgissant(pressionMax,pressionMin, longeurCharge);
    }

    public PressionAgissant pousseeTerreFondDueALaChargePontuelle(double angleFrottementSol) {
        double Ka = coefficientDePoussee(angleFrottementSol);
        double pressionMin;
        double pressionMax;
        double longeurCharge;
        if (distanceChargePontuelle >0.0) {
            pressionMin  = 0.0;
            pressionMax =  0.0;
            longeurCharge = 0.0;
        }else {
            pressionMin  = chargePontuelle/largeur;
            pressionMax =  pressionMin;
            longeurCharge = largeur;
        }
        return new PressionAgissant(pressionMax,pressionMin, longeurCharge);
    }

    public PressionAgissant pousseeTerreFondDueALaChargePontuelle(double angleFrottementSol, double distanceChargeRoulante) {
        double Ka = coefficientDePoussee(angleFrottementSol);
        double pressionMin;
        double pressionMax;
        double longeurCharge;
        if (distanceChargeRoulante >0.0) {
            pressionMin  = 0.0;
            pressionMax =  0.0;
            longeurCharge = 0.0;
        }else {
            pressionMin  = chargeRoulante/largeur;
            pressionMax =  pressionMin;
            longeurCharge = largeur;
        }
        return new PressionAgissant(pressionMax,pressionMin, longeurCharge);
    }


    public double coefficientDePoussee(double angleFrottementSol) {
        return CoefficientDePoussee.KaRankine(angleFrottementSol);
    }


    public final PressionAgissant calculerPressionLevage(double epaisseurFond) {
        double pressionMax = 1.25*POIDS_VOLUMIQUE_BETON*epaisseurFond;
        double pressionMin = pressionMax;
        double longeurCharge = largeur;
        return new PressionAgissant(pressionMax,pressionMin, longeurCharge);
    }

    public class PressionAgissant {
        public final double pressionMax, pressionMin, longeurCharge;

        private PressionAgissant(double pressionMax, double pressionMin, double longeurCharge) {
            this.pressionMax = pressionMax;
            this.pressionMin = pressionMin;
            this.longeurCharge = longeurCharge;
        }
    }
    public class EffortAgissant {
        public final double mEd, vEd;

        private EffortAgissant(double mEd, double vEd) {
            this.mEd = mEd;
            this.vEd = vEd;
        }
    }
}
