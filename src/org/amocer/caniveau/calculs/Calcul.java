package org.amocer.caniveau.calculs;

import org.amocer.caniveau.calculs.math.trigoFunc;

import java.util.*;

public class Calcul {
    public final double POIDS_VOLUMIQUE_BETON = 25.0; //KN/m3
    public final double ANGLE_FROTTEMENT_INTERFACE = 0.0;
    public final double COHESION = 0.0;
    public final Donnee donnee;
    public final String nomFibre;

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
    public final String typeChargeRoulante;
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
        this.typeChargeRoulante = donnee.typeChargeRoulante;
        this.chargeRoulante = calculerChargeRoulante();
        this.epaisseurCouvercle = donnee.epaisseurCouvercle/100.0;
        this.poidsCouvercle = donnee.poidsCouvercle;
    }


    private double calculerChargeRoulante() {
        double chargeRoulante = 0.0;
        if (typeChargeRoulante.equals(TypeChargeRoulante.PIETON.type)){
            chargeRoulante = TypeChargeRoulante.PIETON.charge;
        } else if(typeChargeRoulante.equals(TypeChargeRoulante.ESSIEU_3T.type)){
            chargeRoulante = TypeChargeRoulante.ESSIEU_3T.charge;
        }else if(typeChargeRoulante.equals(TypeChargeRoulante.ESSIEU_6T.type)){
            chargeRoulante = TypeChargeRoulante.ESSIEU_6T.charge;
        }else if(typeChargeRoulante.equals(TypeChargeRoulante.ESSIEU_13T.type)){
            chargeRoulante = TypeChargeRoulante.ESSIEU_13T.charge;
        }else if(typeChargeRoulante.equals(TypeChargeRoulante.NON.type)){
            chargeRoulante = TypeChargeRoulante.NON.charge;
        }
        return chargeRoulante;
    }

    public List<Calcul.ResultatDuCalcul> calculer() {
        List<ResultatDuCalcul> resultats = new LinkedList<>();

        double momentParoiELU = getMomentParoiELU();
        double effortTranchantParoiELU = getEffortTranchantParoiELU();

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
                int joursPourLevage = getJoursPourLevagel(typeBeton, epaisseurMiniParoi, epaisseurMiniFond);
                String renfortMini = "";
                String message = Message.OK.toString();
                resultats.add(new ResultatDuCalcul(typeBeton, dosage, renfortMini, epaisseurMiniParoi,epaisseurMiniFond,volumeBeton,poidsFibre,poidsArmatures, resistanceMinSol, joursPourLevage, message, donnee));
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

    public ResultatDuCalcul getResultatPersonalise(double epaisseurParoiChoisie, double epaisseurFondChoisie, String nomBeton, double momentParoiELU, double effortTranchantParoiELU) {
        TypeFibre typeFibre = TypeFibre.getFibre(nomFibre, nomBeton);
        double momentFondELU = getMomentFondELU(epaisseurParoiChoisie);
        double effortTranchantFondELU = getEffortTranchantFondELU(epaisseurParoiChoisie);
        double sectionsArmaturesMax = sectionArmatureMax(1.0, Math.min(epaisseurParoiChoisie,epaisseurFondChoisie)/100.);
        double sectionsArmaturesMin = sectionArmatureMin(1.0, Math.min(epaisseurParoiChoisie,epaisseurFondChoisie)/100.,TypeBeton.get(nomBeton),TypeAcier.B500B);

        String renfortMini = "";
        double volumeBeton = getVolumeBeton(epaisseurParoiChoisie, epaisseurFondChoisie);;
        double poidsFibre = 0;
        double poidsArmatures = 0;
        double resistanceMinSol = 0;
        int joursPourLevage = 0;
        String message = Message.PasOK.toString();

        int dosage = 0;
        int nombreArmature = 0;
        int diametreArmature = 0;
        List<Integer> diametres = Arrays.asList(8,10,12,14,16);
        myLabel:
        for (int dia:diametres) {
            for (int nombre = 0; nombre <= 20; nombre++) {
                double sectionsArmatures = getSectionsArmatures(nombre,dia);
                for (int dos = typeFibre.dosageMin; dos <= typeFibre.dosageMax ; dos++) {
                    nombreArmature = nombre;
                    diametreArmature = dia;
                    if (nombre == 0) {
                        dosage = dos;
                    }else {
                        dosage = typeFibre.dosageMax;
                    }
                    double momentResistantParoi = getMomentResistantParoi(epaisseurParoiChoisie, nomBeton, dosage, sectionsArmatures);
                    double effortTranchantResistantParoi =  effortTranchantResistant(nomBeton,nomFibre, dosage,epaisseurParoiChoisie/100.0, sectionsArmatures);
                    double momentResistantFond = getMomentResistantFond(epaisseurFondChoisie, nomBeton, dosage, sectionsArmatures);
                    double effortTranchantResistantFond =  effortTranchantResistant(nomBeton,nomFibre, dosage,epaisseurFondChoisie/100.0, sectionsArmatures);
                    if ((momentParoiELU <= momentResistantParoi) && (effortTranchantParoiELU <= effortTranchantResistantParoi)
                            && (momentFondELU <= momentResistantFond) && (effortTranchantFondELU <= effortTranchantResistantFond) && sectionsArmatures <sectionsArmaturesMax) {
                        renfortMini = nombreArmature == 0?"":(nombreArmature + " HA" + diametreArmature);
                        poidsFibre = dosage*volumeBeton;
                        poidsArmatures = 8750.0*getSectionsArmatures(nombreArmature,diametreArmature)*3*longeur/10000.0;
                        resistanceMinSol = getresistanceMinSol(epaisseurParoiChoisie, epaisseurFondChoisie);
                        joursPourLevage = getJoursPourLevagel(nomBeton, epaisseurParoiChoisie, epaisseurFondChoisie);
                        message = Message.OK.toString();

                        break myLabel;
                    }
                }
            }
        }
        return new ResultatDuCalcul(nomBeton, dosage, renfortMini, epaisseurParoiChoisie,epaisseurFondChoisie,volumeBeton,poidsFibre,poidsArmatures, resistanceMinSol, joursPourLevage, message, donnee);
    }

    // Calcul des efforts résistants
    public double getMomentResistantFond(double epaisseurFondChoisie, String typeBeton, int dosage, double sectionsArmatures) {
        return momentResistant(typeBeton, nomFibre, dosage, epaisseurFondChoisie / 100.0, sectionsArmatures);
    }

    public double getMomentResistantParoi(double epaisseurParoiChoisie, String typeBeton, int dosage, double sectionsArmatures) {
        return momentResistant(typeBeton, nomFibre, dosage, epaisseurParoiChoisie / 100.0, sectionsArmatures);
    }

    // Calcul des efforts aggissants
    private double getEffortTranchantParoiELU() {
        return 1.35*poussee_Terre_Paroi().effortTranchant + 1.5*(poussee_ChargePontuelle_Paroi().effortTranchant
                + poussee_ChargeUniforme_Paroi().effortTranchant + poussee_ChargeRoulante_Paroi_Max().effortTranchant);
    }
    private double getEffortTranchantFondELU(double epaisseurParoiChoisie) {
        return 1.35*poussee_Terre_Fond(epaisseurParoiChoisie).effortTranchant + 1.5* poussee_ChargeRoulante_Fond_Max().effortTranchant;
    }

    public double getMomentParoiELU() {
        return 1.35*poussee_Terre_Paroi().momentAgissant + 1.5*(poussee_ChargePontuelle_Paroi().momentAgissant
                + poussee_ChargeUniforme_Paroi().momentAgissant + poussee_ChargeRoulante_Paroi_Max().momentAgissant);
    }

    public double getMomentFondELU(double epaisseurParoiChoisie) {
        double momentParoiELU = getMomentParoiELU();
        return Math.max(momentParoiELU, 1.35 * poussee_Terre_Fond(epaisseurParoiChoisie).momentAgissant + 1.5 * poussee_ChargeRoulante_Fond_Max().momentAgissant);
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
        double momentFondELU = getMomentFondELU(epaisseurMiniParoi);
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
        return longeur*(2*(hauteur+ epaisseurMiniFond /100.0+epaisseurCouvercle/100.0)* epaisseurMiniParoi + epaisseurMiniFond *largeur + epaisseurCouvercle*largeur)/100.0;
    }

    private double getresistanceMinSol(double epaisseurMiniParoi, double epaisseurMiniFond) {
        double effortVerticalELU = getEffortVerticalELU(epaisseurMiniParoi);
        return effortVerticalELU/largeur/1.5/10000.0;
    }

    public double getEffortVerticalELU(double epaisseurMiniParoi) {
        return (1.35*poussee_Terre_Fond(epaisseurMiniParoi).pressionMax+1.5*poussee_ChargeRoulante_Fond_Max().pressionMax)*largeur;
    }

    public int getJoursPourLevagel(String nomBeton, double epaisseurMiniParoi, double epaisseurMiniFond) {
        double contrainteTraction = calculerPressionLevage(epaisseurMiniFond).momentAgissant*6.0/Math.pow(epaisseurMiniFond,2)/10000;
        int joursPourLevage = 0;
        for (int jour = 3; jour < 28; jour++) {
            TypeBeton typeBeton = TypeBeton.get(nomBeton);
            double resistanceTractionBeton = typeBeton.getResistanceTractionTemps(jour);
            if (contrainteTraction < resistanceTractionBeton) {
                joursPourLevage = jour;
                break;
            }
        }
        return joursPourLevage;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public EffortAgissant poussee_Terre_Paroi() {
        double Ka = CoefficientDePoussee.KaRankine(angleFrottement);
        double pressionMin = Ka*poidsVolumiqueSol*hauteurRemplai;
        double pressionMax = Ka*poidsVolumiqueSol*(hauteurRemplai + epaisseurCouvercle + hauteur);
        double longeurCharge = hauteur + epaisseurCouvercle ;
        double momentAgissant = (pressionMax + 2*pressionMin)*Math.pow(longeurCharge,2)/6.0;
        double effortTranchant = (pressionMax + pressionMin)*longeurCharge/2.0;
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }

    public EffortAgissant poussee_ChargeUniforme_Paroi() {
        double Ka = CoefficientDePoussee.KaRankine(angleFrottement);
        double pressionMin  = Ka*chargeUniforme; ;
        double pressionMax = Ka*chargeUniforme;
        double longeurCharge;
        double Z1 = distanceChargeUniforme*trigoFunc.tan(angleFrottement);
        if (Z1 <= hauteurRemplai) {
            longeurCharge = hauteur + epaisseurCouvercle ;
        }else if (Z1 <= (hauteur + epaisseurCouvercle + hauteurRemplai)){
            longeurCharge = hauteur + epaisseurCouvercle + hauteurRemplai - Z1;
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
        double pressionMin = 0;
        double pressionMax;
        double longeurCharge;
        double largueurCharge = 2*dF*trigoFunc.tan(27);
        double Z1 = dF*trigoFunc.tan(angleFrottement);
        double Z2 = dF*trigoFunc.tan(45+angleFrottement/2);
        pressionMax = 4*F*trigoFunc.tan(45.0-angleFrottement/2)/Math.pow(dF,2)/(trigoFunc.tan(45+angleFrottement/2.0)-trigoFunc.tan(angleFrottement));
        if (Z2 <= hauteurRemplai || Z1 >= (hauteur + epaisseurCouvercle + hauteurRemplai)) {
            pressionMin = 0.0;
            pressionMax = 0.0;
        } else if (Z1 <= hauteurRemplai ) {
            pressionMax *= (Z2-hauteurRemplai)/Z2;
            Z1 = hauteurRemplai;
        }
        longeurCharge = Z2-Z1 ;
        double brasLevier = Math.max(0,hauteur + epaisseurCouvercle + hauteurRemplai - Z2 + 2.0*(Z2-Z1)/3.0);
        double forceTotale = pressionMax*longeurCharge*largueurCharge/6;
        double momentAgissant = forceTotale*brasLevier/Math.max(longeur,1.0);
        double effortTranchant = forceTotale/Math.max(longeur,1.0);
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissant, effortTranchant);
    }

    public EffortAgissant poussee_ChargePontuelle_Paroi() {
        return chargePontuelle_Paroi(chargePontuelle,distanceChargePontuelle);
    }

    public EffortAgissant poussee_ChargeRoulante_Paroi(double distanceChargeRoulante) {
        if (longeur <= 2.0) {
            return chargePontuelle_Paroi(chargeRoulante/2, distanceChargeRoulante);
        }else {
            return chargePontuelle_Paroi(chargeRoulante, distanceChargeRoulante);
        }
    }

    public EffortAgissant poussee_ChargeRoulante_Paroi_Max() {
        double pressionMin = 0.0;
        double pressionMax = 0.0;
        double longeurCharge = 0.0;
        double momentAgissant = 0.0;
        double effortTranchant = 0.0;

        double momentAgissantMax = 0.0;
        double dR = 0.0;
        while (dR < 5.0*hauteur) {
            dR += 0.1; //pas de 10 cm
            EffortAgissant effortAgissant = poussee_ChargeRoulante_Paroi(dR);
            momentAgissant = effortAgissant.momentAgissant;
            if (momentAgissant > momentAgissantMax) {
                pressionMax = effortAgissant.pressionMax;
                pressionMin = effortAgissant.pressionMin;
                longeurCharge = effortAgissant.longeurCharge;
                momentAgissantMax = momentAgissant;
                effortTranchant = effortAgissant.effortTranchant;
            }
        }
        return new EffortAgissant(pressionMax,pressionMin, longeurCharge, momentAgissantMax, effortTranchant);
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
        double enrobage = epaisseur/2.0;

        return EffortResistant.effortTranchantResistantBetonFibre(fck, fR1, fR3, epaisseur, 1.0, enrobage, sectionArmatures, 0);
    }

    private static double getSectionsArmatures(double nombreDeBarres, double diametreDesBarres) {
        return nombreDeBarres * 3.14 * Math.pow(diametreDesBarres, 2) / 400.0; //cm2
    }

    public double sectionArmatureMin(double largeurSection,double hauteursection, TypeBeton typeBeton, TypeAcier typeAcier ) {
        double largeurZoneTendue = largeurSection;
        double hauteurCalcul = 0.9*hauteursection;
        return Math.max(0.26* typeBeton.fctm/typeAcier.fYk,0.0013)*largeurZoneTendue*hauteurCalcul*10000.0; //cm2
    }
    //
    public double sectionArmatureMax(double largeurSection, double hauteursection) {
        return 0.04*largeurSection*hauteursection*10000.0; //cm2
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
        public final String message;
        public Donnee donnee;


        public ResultatDuCalcul(String typeBeton, int dossage, String renfortMini, double epaisseurMinParoi, double epaisseurMinFond, double volumeBeton, double poidsFibre, double poidsArmatures, double resistanceMinSol, int joursPourLevage, String message, Donnee donnee) {
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
            this.message = message;
            this.donnee = donnee;
        }
    }

    public enum Message {
        OK("OK"), PasOK("NON"), VIDE("");
        final String texte;

        Message(String texte) {
            this.texte = texte;
        }

        @Override
        public String toString() {
            return texte;
        }
    }
}
