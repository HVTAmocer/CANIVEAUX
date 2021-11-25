package org.amocer.caniveau.calculs;

import org.amocer.caniveau.calculs.math.trigoFunc;

public class Calcul {
    public final double POIDS_VOLUMIQUE_BETON = 25.0; //KN/m3
    public final double ANGLE_FROTTEMENT_INTERFACE = 0.0;
    public final double COHESION = 0.0;
    public final Donnee donnee;
    public final String typeFibre;

    public final TypeCaniveau typeCaniveau;

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

    public Calcul(Donnee donnee) {
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
    }


    public EffortAgissant poussee_Terre_Paroi(double angleFrottement) {
        double Ka = coefficientDePoussee(angleFrottement);
        double pressionMin = Ka*poidsVolumiqueSol*hauteurRemplai;
        double pressionMax = Ka*poidsVolumiqueSol*(hauteur + epaisseurCouvercle);
        double longeurCharge = hauteur;
        double momentAgissant;
        double effortTranchant;
        if (typeCaniveau == TypeCaniveau.OUVERT) {
            momentAgissant = (pressionMax + 2*pressionMin)*Math.pow(longeurCharge,2)/6.0;
            effortTranchant = (pressionMax + pressionMin)*longeurCharge/2.0;
        }else {
            momentAgissant = (8.0*pressionMax + 7.0*pressionMin)*Math.pow(longeurCharge,2)/120.0;
            effortTranchant = (16.0*pressionMax + 9.0*pressionMin)*longeurCharge/40.0;
        }
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }

    public EffortAgissant poussee_ChargeUniforme_Paroi(double angleFrottement) {
        double Ka = coefficientDePoussee(angleFrottement);
        double pressionMin; ;
        double pressionMax;
        double longeurCharge;
        if (distanceChargeUniforme <= hauteurRemplai/trigoFunc.tan(angleFrottement)) {
            pressionMin  = Ka*chargeUniforme;
            pressionMax = Ka*chargeUniforme;
            longeurCharge = hauteur ;
        }else if (distanceChargeUniforme <= (hauteur + epaisseurCouvercle + hauteurRemplai)/ trigoFunc.tan(angleFrottement)){
            pressionMin  = Ka*chargeUniforme; ;
            pressionMax = Ka*chargeUniforme;
            longeurCharge = hauteur + epaisseurCouvercle + hauteurRemplai - distanceChargeUniforme*trigoFunc.tan(angleFrottement);
        } else {
            pressionMin = 0.0;
            pressionMax = 0.0;
            longeurCharge = 0.0;
        }
        double momentAgissant;
        double effortTranchant;
        if (typeCaniveau == TypeCaniveau.OUVERT) {
            momentAgissant = (pressionMax + 2*pressionMin)*Math.pow(longeurCharge,2)/6.0;
            effortTranchant = (pressionMax + pressionMin)*longeurCharge/2.0;
        }else {
            momentAgissant = (8.0*pressionMax + 7.0*pressionMin)*Math.pow(longeurCharge,2)/120.0;
            effortTranchant = (16.0*pressionMax + 9.0*pressionMin)*longeurCharge/40.0;
        }
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }

    public EffortAgissant poussee_ChargePontuelle_Paroi(double angleFrottement) {
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

        double momentAgissant;
        double effortTranchant;
        if (typeCaniveau == TypeCaniveau.OUVERT) {
            momentAgissant = (pressionMax + 2*pressionMin)*Math.pow(longeurCharge,2)/6.0;
            effortTranchant = (pressionMax + pressionMin)*longeurCharge/2.0;
        }else {
            momentAgissant = (8.0*pressionMax + 7.0*pressionMin)*Math.pow(longeurCharge,2)/120.0;
            effortTranchant = (16.0*pressionMax + 9.0*pressionMin)*longeurCharge/40.0;
        }
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }

    public EffortAgissant poussee_ChargeRoulante_Paroi(double angleFrottement, double distanceChargeRoulante) {
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
        double momentAgissant;
        double effortTranchant;
        if (typeCaniveau == TypeCaniveau.OUVERT) {
            momentAgissant = (pressionMax + 2*pressionMin)*Math.pow(longeurCharge,2)/6.0;
            effortTranchant = (pressionMax + pressionMin)*longeurCharge/2.0;
        }else {
            momentAgissant = (8.0*pressionMax + 7.0*pressionMin)*Math.pow(longeurCharge,2)/120.0;
            effortTranchant = (16.0*pressionMax + 9.0*pressionMin)*longeurCharge/40.0;
        }
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }

    public EffortAgissant poussee_ChargeRoulante_Paroi_Max(double angleFrottement) {
        double pressionMin = 0.0;
        double pressionMax = 0.0;
        double longeurCharge = 0.0;
        double momentAgissant = 0.0;
        double effortTranchant = 0.0;

        double momentAgissantMax = 0.0;
        int dR = 0;
        while (dR < 10*hauteur) {
            dR += 0.1;
            EffortAgissant effortAgissant = poussee_ChargeRoulante_Paroi(angleFrottement, dR);
            momentAgissant = effortAgissant.momentAgissant;
            if (momentAgissant > momentAgissantMax) {
                pressionMax = effortAgissant.pressionMax;
                pressionMin = effortAgissant.pressionMax;
                longeurCharge = effortAgissant.longeurCharge;
                momentAgissantMax = momentAgissant;
                effortTranchant =effortAgissant.effortTranchant;
            }
        }
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }



    public EffortAgissant poussee_Terre_Fond(double epaisseurFond, double epaisseurParoi) {
        double poidSolRemblai = poidsVolumiqueSol*hauteurRemplai;
        double poidBeton = POIDS_VOLUMIQUE_BETON*(2*epaisseurParoi + epaisseurFond) + poidsCouvercle;
        double pressionMin = poidSolRemblai + poidBeton;
        double pressionMax = pressionMin;
        double longeurCharge = largeur;
        double momentAgissant = (3.0*pressionMax + 2.0*pressionMin)*Math.pow(longeurCharge,2)/60.0;;
        double effortTranchant  = (7.0*pressionMax + 3.0*pressionMin)*longeurCharge/20.0;
                return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }

    public EffortAgissant poussee_ChargeUniforme_Fond(double angleFrottementSol) {
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
        double momentAgissant = (3.0*pressionMax + 2.0*pressionMin)*Math.pow(longeurCharge,2)/60.0;;
        double effortTranchant  = (7.0*pressionMax + 3.0*pressionMin)*longeurCharge/20.0;
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }

    public EffortAgissant poussee_ChargePontuelle_Fond() {
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
        double momentAgissant = (3.0*pressionMax + 2.0*pressionMin)*Math.pow(longeurCharge,2)/60.0;;
        double effortTranchant  = (7.0*pressionMax + 3.0*pressionMin)*longeurCharge/20.0;
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }

    public EffortAgissant poussee_ChargeRoulante_Fond(double distanceChargeRoulante) {
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
        double momentAgissant = (3.0*pressionMax + 2.0*pressionMin)*Math.pow(longeurCharge,2)/60.0;;
        double effortTranchant  = (7.0*pressionMax + 3.0*pressionMin)*longeurCharge/20.0;
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }

    public EffortAgissant poussee_ChargeRoulante_Fond_Max() {
        double pressionMin = 0.0;
        double pressionMax = 0.0;
        double longeurCharge = 0.0;
        double momentAgissant = 0.0;
        double effortTranchant = 0.0;

        double momentAgissantMax = 0.0;
        int dR = 0;
        while (dR < 10*hauteur) {
            dR += 0.1;
            EffortAgissant effortAgissant = poussee_ChargeRoulante_Fond(dR);
            momentAgissant = effortAgissant.momentAgissant;
            if (momentAgissant > momentAgissantMax) {
                pressionMax = effortAgissant.pressionMax;
                pressionMin = effortAgissant.pressionMax;
                longeurCharge = effortAgissant.longeurCharge;
                momentAgissantMax = momentAgissant;
                effortTranchant =effortAgissant.effortTranchant;
            }
        }
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }



    public double coefficientDePoussee(double angleFrottementSol) {
        return CoefficientDePoussee.KaRankine(angleFrottementSol);
    }


    public final EffortAgissant calculerPressionLevage(double epaisseurFond) {
        double pressionMax = 1.25*POIDS_VOLUMIQUE_BETON*epaisseurFond;
        double pressionMin = pressionMax;
        double longeurCharge = largeur;
        double momentAgissant = (3.0*pressionMax + 2.0*pressionMin)*Math.pow(longeurCharge,2)/60.0;;
        double effortTranchant  = (7.0*pressionMax + 3.0*pressionMin)*longeurCharge/20.0;
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }

/*    public final double effortVerticalFond(double epaisseurFond,) {
        double pressionMax = 1.25*POIDS_VOLUMIQUE_BETON*epaisseurFond;
        double pressionMin = pressionMax;
        double longeurCharge = largeur;
        double momentAgissant = (3.0*pressionMax + 2.0*pressionMin)*Math.pow(longeurCharge,2)/60.0;;
        double effortTranchant  = (7.0*pressionMax + 3.0*pressionMin)*longeurCharge/20.0;
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }*/

    public static class EffortAgissant {
        public final double pressionMax, pressionMin, longeurCharge,momentAgissant,effortTranchant;

        public EffortAgissant(double pressionMax, double pressionMin, double longeurCharge, double momentAgissant, double effortTranchant) {
            this.pressionMax = pressionMax;
            this.pressionMin = pressionMin;
            this.longeurCharge = longeurCharge;
            this.momentAgissant = momentAgissant;
            this.effortTranchant = effortTranchant;
        }
    }
}
