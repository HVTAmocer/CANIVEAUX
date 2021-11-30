package org.amocer.caniveau.calculs;

import org.amocer.caniveau.calculs.math.trigoFunc;

import java.util.*;

public class Calcul {
    public final double POIDS_VOLUMIQUE_BETON = 25.0; //KN/m3
    public final double ANGLE_FROTTEMENT_INTERFACE = 0.0;
    public final double COHESION = 0.0;
    public final Donnee donnee;
    public static String nomFibre;

    public final TypeCaniveau typeCaniveau;

    public final double longeur;
    public final double hauteur;
    public final double largeur;
    public final double epaisseurCouvercle;
    public final double hauteurRemplai;

    public final double angleFrottement;
    public final double epaisseurParoiChoisie;
    public final double epaisseurFondChoisie;

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
        this.nomFibre = donnee.nomFibre;
        this.longeur = donnee.longueur;
        this.hauteur = donnee.hauteur;
        this.largeur = donnee.largeur;
        this.hauteurRemplai = donnee.hauteurRemblai;
        this.angleFrottement = donnee.angleFrottement;
        this.epaisseurParoiChoisie = donnee.epaisseurParoiChoisie;
        this.epaisseurFondChoisie = donnee.epaisseurFondChoisie;
        this.chargePontuelle = donnee.chargePontuelle;
        this.distanceChargePontuelle = donnee.distanceChargePontuelle;
        this.chargeUniforme = donnee.chargeUniforme;
        this.distanceChargeUniforme = donnee.distanceChargeUniforme;
        this.poidsVolumiqueSol = donnee.poidsVolumiqueSol;
        this.chargeRoulante = donnee.chargeRoulante;
        this.epaisseurCouvercle = donnee.epaisseurCouvercle/100.0;
        this.poidsCouvercle = donnee.poidsCouvercle;
    }

    public List<Calcul.ResultatDuCalcul> calculer() {
        List<ResultatDuCalcul> resultats = new LinkedList<>();

        double momentParoiELU = 1.35*poussee_Terre_Paroi().momentAgissant + 1.5*(poussee_ChargePontuelle_Paroi().momentAgissant
                + poussee_ChargeUniforme_Paroi().momentAgissant + poussee_ChargeRoulante_Paroi_Max().momentAgissant);
        double effortTranchantParoiELU = 1.35*poussee_Terre_Paroi().effortTranchant + 1.5*(poussee_ChargePontuelle_Paroi().effortTranchant
                + poussee_ChargeUniforme_Paroi().effortTranchant + poussee_ChargeRoulante_Paroi_Max().effortTranchant);

        List<String> listeTypeBeton = Arrays.asList("C25/30","C30/37","C40/50");
        for (String typeBeton : listeTypeBeton) {
            TypeFibre typeFibre = TypeFibre.getFibre(nomFibre, typeBeton);
            int dossageMin = typeFibre.dosageMin;
            int dossageMax = typeFibre.dosageMax;
            for (int dosage = dossageMin; dosage <= dossageMax; dosage+=5) {
                double epaisseurMiniParoi = getEpaisseurMiniParoi(momentParoiELU, effortTranchantParoiELU, typeBeton, dosage);
                double epaisseurMiniFond = getEpaisseurMiniFond(momentParoiELU, typeBeton, dosage, epaisseurMiniParoi);
                double volumeBeton = getVolumeBeton(epaisseurMiniParoi, epaisseurMiniFond);
                double poidsFibre = dosage*volumeBeton;
                double poidsArmatures = 0.0;
                double resistanceMinSol = getresistanceMinSol(epaisseurMiniParoi, epaisseurMiniFond);
                int joursPourLevage = getjoursPourLevagel(epaisseurMiniParoi, epaisseurMiniFond);
                String renfortMini = "";
                resultats.add(new ResultatDuCalcul(typeBeton, dosage, renfortMini, epaisseurMiniParoi,epaisseurMiniFond,volumeBeton,poidsFibre,poidsArmatures, resistanceMinSol, joursPourLevage));
            }
        }
        for (String typeBeton : listeTypeBeton) {
            resultats.add(getResultatPersonalise(epaisseurParoiChoisie,epaisseurFondChoisie, typeBeton, momentParoiELU, effortTranchantParoiELU));
        }

        for (String typeBeton : listeTypeBeton) {
            resultats.add(getResultatPersonalise(6.0,6.0, typeBeton, momentParoiELU, effortTranchantParoiELU));
        }
        return resultats;
    }

    public ResultatDuCalcul getResultatPersonalise(double epaisseurParoiChoisie, double epaisseurFondChoisie, String typeBeton, double momentParoiELU, double effortTranchantParoiELU) {
        TypeFibre typeFibre = TypeFibre.getFibre(nomFibre, typeBeton);
        double momentFondELU = Math.max(momentParoiELU, 1.35*poussee_Terre_Fond(epaisseurParoiChoisie).momentAgissant + 1.5* poussee_ChargeRoulante_Fond_Max().momentAgissant);
        double effortTranchantFondELU = 1.35*poussee_Terre_Fond(epaisseurParoiChoisie).effortTranchant + 1.5* poussee_ChargeRoulante_Fond_Max().effortTranchant;
        int nombreArmature = 0;
        int dosage = 0;
        int diametreArmature = 0;
        List<Integer> diametres = Arrays.asList(8,10,12,14,16,20);
        myLabel:
        for (int dia:diametres) {
            for (int nombre = 0; nombre <= 20; nombre++) {
                double sectionsArmatures = getSectionsArmatures(nombre,dia);
                for (int dos = typeFibre.dosageMin; dos <= typeFibre.dosageMax ; dos++) {
                    nombreArmature = nombre;
                    diametreArmature = dia;
                    dosage = dos;
                    double momentResistantParoi = momentResistant(typeBeton,nomFibre, dosage,epaisseurParoiChoisie/100.0, sectionsArmatures);
                    double effortTranchantResistantParoi =  effortTranchantResistant(typeBeton,nomFibre, dosage,epaisseurParoiChoisie/100.0, sectionsArmatures);
                    double momentResistantFond = momentResistant(typeBeton,nomFibre, dosage,epaisseurFondChoisie/100.0, sectionsArmatures);
                    double effortTranchantResistantFond =  effortTranchantResistant(typeBeton,nomFibre, dosage,epaisseurFondChoisie/100.0, sectionsArmatures);
                    if ((momentParoiELU <= momentResistantParoi) && (effortTranchantParoiELU <= effortTranchantResistantParoi)
                            && (momentFondELU <= momentResistantFond) && (effortTranchantFondELU <= effortTranchantResistantFond)) {
                        break myLabel;
                    }
                }
            }
        }
        String renfortMini;
        if (nombreArmature == 0){
            renfortMini = "";
        }else {
            renfortMini = nombreArmature + " HA" + diametreArmature;
        }
        double volumeBeton = getVolumeBeton(epaisseurParoiChoisie, epaisseurFondChoisie);
        double poidsFibre = dosage*volumeBeton;
        double poidsArmatures = 8750.0*getSectionsArmatures(nombreArmature,diametreArmature)*3*longeur/10000.0;
        double resistanceMinSol = getresistanceMinSol(epaisseurParoiChoisie, epaisseurFondChoisie);
        int joursPourLevage = getjoursPourLevagel(epaisseurParoiChoisie, epaisseurFondChoisie);
        return new ResultatDuCalcul(typeBeton, dosage, renfortMini, epaisseurParoiChoisie,epaisseurFondChoisie,volumeBeton,poidsFibre,poidsArmatures, resistanceMinSol, joursPourLevage);
    }

    private int[] getArmature(double sectionArmatures) {
        List<Integer> diametres = Arrays.asList(8,10,12,14,16);
        int nombre = 0;
        int diametre = 0;
        for (int dia:diametres) {
            for (nombre = 2; nombre < 10; nombre++) {
                double section = getSectionsArmatures(nombre,dia);
                if (section >= sectionArmatures) {
                    break;
                }
            }
            diametre = dia;
        }
        return new int[]{nombre,diametre};
    }

    private double getEpaisseurMiniFond(double momentParoiELU, String typeBeton, int dosage, double epaisseurMiniParoi) {
        double epaisseurMiniFond = 4.5;
        double momentFondELU = Math.max(momentParoiELU, 1.35*poussee_Terre_Fond(epaisseurMiniParoi).momentAgissant + 1.5* poussee_ChargeRoulante_Fond_Max().momentAgissant);
        double effortTranchantFondELU = 1.35*poussee_Terre_Fond(epaisseurMiniParoi).effortTranchant + 1.5* poussee_ChargeRoulante_Fond_Max().effortTranchant;
        double momentResistantFond;
        double effortTranchantResistantFond;
        do {
            epaisseurMiniFond +=0.5;
            momentResistantFond = momentResistant(typeBeton,nomFibre, dosage,epaisseurMiniFond/100.0, 0.0);
            effortTranchantResistantFond =  effortTranchantResistant(typeBeton,nomFibre, dosage, epaisseurMiniParoi /100.0, 0.0);
        }while ((momentFondELU > momentResistantFond) || (effortTranchantFondELU> effortTranchantResistantFond));
        return epaisseurMiniFond;
    }

    private double getEpaisseurMiniParoi(double momentParoiELU, double effortTranchantParoiELU, String typeBeton, int dosage) {
        double momentResistantParoi;
        double effortTranchantResistantParoi;
        double epaisseurMiniParoi = 4.5;
        do {
            epaisseurMiniParoi +=0.5;
            momentResistantParoi = momentResistant(typeBeton,nomFibre, dosage,epaisseurMiniParoi/100.0, 0.0);
            effortTranchantResistantParoi =  effortTranchantResistant(typeBeton,nomFibre, dosage,epaisseurMiniParoi/100.0, 0.0);
        }while ((momentParoiELU > momentResistantParoi) || (effortTranchantParoiELU > effortTranchantResistantParoi));
        return epaisseurMiniParoi;
    }

    private double getVolumeBeton(double epaisseurMiniParoi, double epaisseurMiniFond) {
        double volumeBeton = longeur*(2*(hauteur+ epaisseurMiniFond /100.0+epaisseurCouvercle/100.0)* epaisseurMiniParoi + epaisseurMiniFond *largeur + epaisseurCouvercle*largeur)/100.0;
        return volumeBeton;
    }

    private double getresistanceMinSol(double epaisseurMiniParoi, double epaisseurMiniFond) {
        double effortVerticalELU = (1.35*poussee_Terre_Fond(epaisseurMiniParoi).pressionMax+1.5*poussee_ChargeRoulante_Fond_Max().pressionMax)*largeur;
        double resistanceMinSol = effortVerticalELU/largeur/10000.0;
        return resistanceMinSol;
    }
    private int getjoursPourLevagel(double epaisseurMiniParoi, double epaisseurMiniFond) {
        double contrainteTraction = calculerPressionLevage(epaisseurMiniFond).momentAgissant*6.0/Math.pow(epaisseurMiniFond,2)/10000;
        int joursPourLevage = 3;
        return joursPourLevage;
    }


    public EffortAgissant poussee_Terre_Paroi() {
        double Ka = CoefficientDePoussee.KaRankine(angleFrottement);
        double pressionMin = Ka*poidsVolumiqueSol*hauteurRemplai;
        double pressionMax = Ka*poidsVolumiqueSol*(hauteurRemplai + epaisseurCouvercle + hauteur);
        double longeurCharge = hauteur + epaisseurCouvercle ;
        double momentAgissant = (pressionMax + 2*pressionMin)*Math.pow(longeurCharge,2)/6.0;;
        double effortTranchant = (pressionMax + pressionMin)*longeurCharge/2.0;;
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }

    public EffortAgissant poussee_ChargeUniforme_Paroi() {
        double Ka = CoefficientDePoussee.KaRankine(angleFrottement);
        double pressionMin; ;
        double pressionMax;
        double longeurCharge;
        if (distanceChargeUniforme <= hauteurRemplai/trigoFunc.tan(angleFrottement)) {
            pressionMin  = Ka*chargeUniforme;
            pressionMax = Ka*chargeUniforme;
            longeurCharge = hauteur + epaisseurCouvercle ;
        }else if (distanceChargeUniforme <= (hauteur + epaisseurCouvercle + hauteurRemplai)/ trigoFunc.tan(angleFrottement)){
            pressionMin  = Ka*chargeUniforme; ;
            pressionMax = Ka*chargeUniforme;
            longeurCharge = hauteur + epaisseurCouvercle + hauteurRemplai - distanceChargeUniforme*trigoFunc.tan(angleFrottement);
        } else {
            pressionMin = 0.0;
            pressionMax = 0.0;
            longeurCharge = 0.0;
        }
        double momentAgissant = (pressionMax + 2*pressionMin)*Math.pow(longeurCharge,2)/6.0;;
        double effortTranchant = (pressionMax + pressionMin)*longeurCharge/2.0;
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }

    private EffortAgissant chargePontuelle_Paroi(double F, double dF) {
        double pressionMin;
        double pressionMax;
        double longeurCharge;
        if (dF <=hauteurRemplai/trigoFunc.tan(45+angleFrottement/2)  || dF >= (hauteur + epaisseurCouvercle + hauteurRemplai)/ trigoFunc.tan(angleFrottement) ) {
            pressionMin = 0.0;
            pressionMax = 0.0;
            longeurCharge = 0.0;
        } else {
            pressionMin = 0.0;
            pressionMax = 4*F*trigoFunc.tan(45.0-angleFrottement/2)/Math.pow(dF,2)/(trigoFunc.tan(45+angleFrottement/2.0)-trigoFunc.tan(angleFrottement));
            longeurCharge = Math.min(hauteur + epaisseurCouvercle + hauteurRemplai, dF*trigoFunc.tan(45+angleFrottement/2)) - Math.max(hauteurRemplai,dF*trigoFunc.tan(angleFrottement)) ;
        }

        double momentAgissant = (2*pressionMax + pressionMin)*Math.pow(longeurCharge,2)/12.0;
        double effortTranchant = (pressionMax + pressionMin)*longeurCharge/4.0;
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }

    public EffortAgissant poussee_ChargePontuelle_Paroi() {
        return chargePontuelle_Paroi(chargePontuelle,distanceChargePontuelle);
    }

    public EffortAgissant poussee_ChargeRoulante_Paroi(double distanceChargeRoulante) {
        return chargePontuelle_Paroi(chargeRoulante, distanceChargeRoulante);
    }

    public EffortAgissant poussee_ChargeRoulante_Paroi_Max() {
        double pressionMin = 0.0;
        double pressionMax = 0.0;
        double longeurCharge = 0.0;
        double momentAgissant = 0.0;
        double effortTranchant = 0.0;

        double momentAgissantMax = 0.0;
        double dR = 0.0;
        while (dR < 10*hauteur) {
            dR += 0.1; //pas de 10 cm
            EffortAgissant effortAgissant = poussee_ChargeRoulante_Paroi(dR);
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


    public EffortAgissant poussee_Terre_Fond(double epaisseurParoi) {
        double poidSolRemblai = poidsVolumiqueSol*hauteurRemplai;
        double poidBeton = POIDS_VOLUMIQUE_BETON*(2*epaisseurParoi) + poidsCouvercle;
        double pressionMin = poidSolRemblai + poidBeton;
        double pressionMax = pressionMin;
        double longeurCharge = largeur;
        double momentAgissant = (3.0*pressionMax + 2.0*pressionMin)*Math.pow(longeurCharge,2)/60.0;;
        double effortTranchant  = (7.0*pressionMax + 3.0*pressionMin)*longeurCharge/20.0;
                return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }


    public EffortAgissant poussee_ChargeRoulante_Fond(double distanceChargeRoulante) {
        double pressionMin;
        double pressionMax;
        double longeurCharge;
        if (distanceChargeRoulante >=0.0) {
            pressionMin  = 0.0;
            pressionMax =  0.0;
            longeurCharge = 0.0;
        }else {
            pressionMin  = chargeRoulante/largeur;
            pressionMax =  pressionMin;
            longeurCharge = largeur;
        }
        double momentAgissant = (3.0*pressionMax + 2.0*pressionMin)*Math.pow(longeurCharge,2)/60.0;
        double effortTranchant  = (7.0*pressionMax + 3.0*pressionMin)*longeurCharge/20.0;
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }

    public EffortAgissant poussee_ChargeRoulante_Fond_Max() {
        return poussee_ChargeRoulante_Fond(-largeur/2.0);
    }

    public final EffortAgissant calculerPressionLevage(double epaisseurFond) {
        double pressionMax = 1.25*POIDS_VOLUMIQUE_BETON*epaisseurFond;
        double pressionMin = pressionMax;
        double longeurCharge = largeur;
        double momentAgissant = (pressionMax + pressionMin)*Math.pow(longeurCharge,2)/8.0;
        double effortTranchant  = (pressionMax + pressionMin)*longeurCharge/2.0;
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }

    public double momentResistant(String nomBeton, String nomFibre, int dosage, double epaisseur, double sectionArmatures) {
        TypeFibre typeFibre = TypeFibre.getFibre(nomFibre,nomBeton);
        double fR1 = typeFibre.fr1(dosage);
        double fR3 = typeFibre.fr3(dosage);
        double fR1d = fR1 / 1.5;
        double fR3d = fR3 / 1.5;
        TypeBeton typeBeton = TypeBeton.get(nomBeton);
        double epsCu = -typeBeton.epsilonCu3;
        double fcd   = -typeBeton.fcd;

        return EffortResistant.momentResistantELUBetonFibre(epsCu, fcd, fR1d, fR3d, epaisseur, 1.0, sectionArmatures);
    }

    public double effortTranchantResistant(String nomBeton, String nomFibre, int dosage, double epaisseur, double sectionArmatures) {
        TypeFibre typeFibre = TypeFibre.getFibre(nomFibre,nomBeton);
        double fR1 = typeFibre.fr1(dosage);
        double fR3 = typeFibre.fr3(dosage);
        TypeBeton typeBeton = TypeBeton.get(nomBeton);
        double fck = typeBeton.fck;
        double enrobage = 0.03;

        return EffortResistant.effortTranchantResistantBetonFibre(fck, fR1, fR3, epaisseur, 1.0, enrobage, sectionArmatures, 0);
    }

    private static double getSectionsArmatures(double nombreDeBarres, double diametreDesBarres) {
        return nombreDeBarres * 3.14 * Math.pow(diametreDesBarres, 2) / 400.; //cm2
    }



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

    public static class ResultatDuCalcul {
        public final String typeBeton;
        public final int dossage;
        public final String renfortMini;
        public final double epaisseurMinParoi;
        public final double epaisseurMinFond;
        public final double volumeBeton;
        public final double poidsFibre;
        public final double poidsArmatures;
        public final double resistanceMinSol;
        public final double joursPourLevage;



        public ResultatDuCalcul(String typeBeton, int dossage, String renfortMini, double epaisseurMinParoi, double epaisseurMinFond, double volumeBeton, double poidsFibre, double poidsArmatures, double resistanceMinSol, int joursPourLevage) {
            this.typeBeton = typeBeton;
            this.dossage = dossage;
            this.renfortMini = renfortMini;
            this.epaisseurMinParoi = epaisseurMinParoi;
            this.epaisseurMinFond = epaisseurMinFond;
            this.volumeBeton = volumeBeton;
            this.poidsFibre = poidsFibre;
            this.poidsArmatures = poidsArmatures;
            this.resistanceMinSol = resistanceMinSol;
            this.joursPourLevage = joursPourLevage;
        }
    }
}
