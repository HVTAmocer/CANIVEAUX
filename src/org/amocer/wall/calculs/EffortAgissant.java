package org.amocer.wall.calculs;

public class EffortAgissant {
    public final double mEd, vEd, nEd;

    private EffortAgissant(double mEd, double vEd, double nEd) {
        this.mEd = mEd;
        this.vEd = vEd;
        this.nEd = nEd;
    }

    public static EffortAgissant effortRideau(double poidsVolumiqueBeton, double poidsVolumiqueSol, double phi, double delta,
                                              double LongeurMur, double hTotal, double lTotal, double eRideauBas, double eRideauHaut, double eTalon, double beta, double hTN,
                                              double chargeUniforme, double dP, double chargePontuelle, double dF,
                                              CalculateurDeMursDeSoutainement.Combinaison combinaison) {
        // Coefficients de poussée
        double hRideau = hTotal - eTalon;
        double lTalon = lTotal - eRideauBas;
        double lambda = Math.toRadians(Math.atan((eRideauBas-eRideauHaut)/hRideau));

        double Ka0H = CoefficientDePoussee.KaRankine(phi);


        double KaBeta;
        try{
            KaBeta = CoefficientDePoussee.coefficientDePousseeCaquot(delta/phi,beta/phi,lambda,phi);
        }catch (IllegalStateException e){
            System.out.println("Erreur d'interpolation");
            e.printStackTrace();
            KaBeta = Double.NaN;
        }
        double KaBetaH = KaBeta*Math.cos(delta*3.14/180);

        double rhoS = poidsVolumiqueSol/100; //KN/m3
        // Diagrame de pression horizontale
        double DD;
        if (hTN == 0|beta ==0) {
            DD = 0;
        }else {
            DD = hTN/Math.tan(beta*3.14/180);
        }
        //
        double BD = hTotal + hTN;
        double BC = hTotal + Math.min(DD,lTalon+eRideauBas-eRideauHaut)*Math.tan(beta*3.14/180);
        double CD = BD - BC;
        double DH = KaBetaH*CD/(KaBetaH-Ka0H);
        double BH = BD - DH;
        double CH = DH - CD;


        // Efforts dues aux charges permanentes (poussées des terres y compris le talus incliné)
        double p1 = Ka0H*rhoS*(BD-eTalon);
        double p2 = Ka0H*rhoS*DH;
        double MEdPermanent = p2*Math.pow(CH,2)/6 + (p1+p2)*(BH-eTalon)*hTotal/6;
        double VEdPermanent = p2*CH/2 + (p1+p2)*(BH-eTalon)/2;
        double NEdPermanent = poidsVolumiqueBeton*hTotal*(eRideauBas+eRideauHaut)/2;
        // Efforts dues aux charges d'exploitation
        double p3 = Ka0H*chargeUniforme;
        double BE = Math.min(hTotal,Math.max(BD-(dP-lTalon-eRideauBas+eRideauHaut)*Math.tan(phi*3.14/180),0));
        double p4;
        if (chargePontuelle==0|dF == 0) {
            p4 = 0;
        } else {
            p4 = 4*chargePontuelle*Math.tan(3.14/4-phi*3.14/180/2)/Math.pow(dF,2)/(Math.tan(3.14/4+phi*3.14/180/2)-Math.tan(phi*3.14/180));
        }

        double BF = Math.min(hTotal,Math.max(BD - dF*Math.tan(phi*3.14/180),0));
        double BK = Math.min(hTotal,Math.max(BD - dF*Math.tan(phi*3.14/180/2+3.14/4),0));
        double FK = BF - BK;
        double largeurForce = 2*dF*Math.tan(27*3.14/180);
        double MEdExploitation = p3*BE*lTotal/2 + p4*Math.pow(FK,2)*largeurForce/3/LongeurMur;
        double VEdExploitation = p3*BE + p4*FK*largeurForce/2/LongeurMur;
        double NEdExploitation = 0.0;
        //
        double MEdRideau = MEdPermanent*combinaison.gammaG + MEdExploitation*combinaison.gammaQ;
        double VEdRideau = VEdPermanent*combinaison.gammaG + VEdExploitation*combinaison.gammaQ;
        double NEdRideau = NEdPermanent*combinaison.gammaG + NEdExploitation*combinaison.gammaQ;
        return new EffortAgissant(MEdRideau,VEdRideau, NEdRideau);
    }

    public static EffortAgissant effortTalon(double poidsVolumiqueBeton, double poidsVolumiqueSol, double phi, double delta,
                                             double LongeurMur, double hTotal, double lTotal, double eRideauBas, double eRideauHaut, double eTalon, double beta, double hTN,
                                             double chargeUniforme, double dP, double chargePontuelle, double dF,
                                             CalculateurDeMursDeSoutainement.Combinaison combinaison) {
        double hRideau = hTotal - eTalon;
        double lTalon = lTotal - eRideauBas;
        double rhoS = poidsVolumiqueSol/100; //KN
        // Pression du au poids propre du talon
        double p5 = poidsVolumiqueBeton*eTalon;

        double DD;
        if (hTN == 0|beta ==0) {
            DD = 0;
        }else {
            DD = hTN/Math.tan(beta*3.14/180);
        }
        // Pression du au poids propre du sol au dessus du talon
        double p6;
        if (DD >= lTalon) {
            p6 = rhoS*(hRideau+lTalon*Math.tan(beta*3.14/180)/2);
        }else {
            p6 = rhoS*(hRideau+(2*lTalon-DD)*hTN/2/lTalon);
        }

        // Efforts permanents
        double VEdPermanent = 0.0;
        double NEdPermanent = (p5+p6)*lTalon;

        double p7 = chargeUniforme*Math.max(0,lTalon-dP)/lTalon;
        double p8= chargePontuelle*Math.max(0,lTalon-dF)/lTalon;

        double VEdExploitation = 0.0;
        double NEdExploitation = (p7+p8)*lTalon;


        double MEdTalon = EffortAgissant.effortRideau( poidsVolumiqueBeton,  poidsVolumiqueSol,  phi,  delta, LongeurMur,  hTotal,  lTotal,  eRideauBas,  eRideauHaut,  eTalon,  beta,  hTN, chargeUniforme,  dP,  chargePontuelle,  dF, combinaison).mEd;
        double VEdTalon = VEdPermanent*combinaison.gammaG + VEdExploitation*combinaison.gammaQ;
        double NEdTalon = NEdPermanent*combinaison.gammaG + NEdExploitation*combinaison.gammaQ ;


        return new EffortAgissant(MEdTalon,VEdTalon, NEdTalon);
    }
}
