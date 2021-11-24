package org.amocer.caniveau.calculs;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class CalculateurDeMursDeSoutainement {
/*    final double POIDS_VOLUMIQUE_BETON = 25.0; //KN/m3
    final double ANGLE_FROTTEMENT_INTERFACE = 0.0;

    final double longeurMur;
    final double hauteurMur;
    final double largeurMur;
    final double epaisseurHautRideau;
    final double epaisseurBasRideau;
    final double epaisseurTalon;
    final double angleTalus;
    final double hauteurTalus;
    final double chargePontuelle;
    final double distanceChargePontuelle;
    final double chargeUniforme;
    final double distanceChargeUniforme;
    final String nomBeton;
    final double poidsVolumiqueSol;
    final TypeFibre typeFibre;
    final double resistanceSol;
    final TypeBeton typeBeton;
    public final Donnees donnees;

    public CalculateurDeMursDeSoutainement(Donnees donnees) {
        this.donnees = donnees;
        this.longeurMur = donnees.longueurMur;
        this.hauteurMur = donnees.hauteurMur;
        this.largeurMur = donnees.largeurMur;
        this.epaisseurHautRideau = donnees.epaisseurHautRideau;
        this.epaisseurBasRideau = donnees.epaisseurBasRideau;
        this.epaisseurTalon = donnees.epaisseurTalon;
        this.hauteurTalus = donnees.hauteurRemblai;
        this.chargePontuelle = donnees.chargePontuelle;
        this.distanceChargePontuelle = donnees.distanceChargePontuelle;
        this.chargeUniforme = donnees.chargeUniforme;
        this.distanceChargeUniforme = donnees.distanceChargeUniforme;
        this.nomBeton = donnees.nomBeton;
        this.poidsVolumiqueSol = donnees.poidsVolumiqueSol;
        this.typeFibre = donnees.typeFibre;
        this.resistanceSol = donnees.resistanceSol;
        typeBeton = TypeBeton.get(nomBeton);
    }

    public List<ResultatDuCalcul> calculer() {
        List<ResultatDuCalcul> resultats = new LinkedList<>();
        //EFFORTS AGISSANTS______________________________________________________________________________________________________________
        EffortAgissant effortRideauELU = effortRideau(Combinaison.ELU);
        EffortAgissant effortTalonELU = effortTalon(Combinaison.ELU);
        EffortAgissant effortRideau_ELS_CAR = effortRideau(Combinaison.ELS_CAR);
        EffortAgissant effortTalon_ELS_CAR = effortTalon(Combinaison.ELS_CAR);
        EffortAgissant effortRideau_ELS_QP = effortRideau(Combinaison.ELS_QP);
        EffortAgissant effortTalon_ELS_QP = effortTalon(Combinaison.ELS_QP);

        //VERIFICATION BA
        verifierStructure(effortRideauELU, effortTalonELU, resultats);

        //VERIFICATION SOL
        // Vérification de l'excentrement
        resultats.add(verifierExcentricite(effortRideauELU, effortTalonELU, Combinaison.ELU));
        resultats.add(verifierExcentricite(effortRideau_ELS_CAR, effortTalon_ELS_CAR, Combinaison.ELS_CAR));
        resultats.add(verifierExcentricite(effortRideau_ELS_QP, effortTalon_ELS_QP, Combinaison.ELS_QP));
        resultats.add(verifierExcentriciteImportant(effortRideauELU, effortTalonELU, largeurMur));

        // Portance du sol - non poiconnement du sol
        resultats.add(verifierCapacitePortante(effortRideauELU, effortTalonELU, Combinaison.ELU));
        resultats.add(verifierCapacitePortante(effortRideau_ELS_CAR, effortTalon_ELS_CAR, Combinaison.ELS_CAR));
        resultats.add(verifierCapacitePortante(effortRideau_ELS_QP, effortTalon_ELS_QP, Combinaison.ELS_QP));

        // Vérification du glissement
        resultats.add(verifierGlissement(effortRideauELU, effortTalonELU, Combinaison.ELU));
        resultats.add(verifierGlissement(effortRideau_ELS_CAR, effortTalon_ELS_CAR, Combinaison.ELS_CAR));
        resultats.add(verifierGlissement(effortRideau_ELS_QP, effortTalon_ELS_QP, Combinaison.ELS_QP));

        return resultats;
    }

    private void verifierStructure(EffortAgissant effortRideauELU, EffortAgissant effortTalonELU, List<ResultatDuCalcul> resultatDuCalcul) {

        // Trouver dosage - Moment résistant
        ResultatDuDimensionnement dosageRideau = resultatDimensionnementRideau();
        ResultatDuDimensionnement dosageTalon = resultatDimensionnementTalon();
        int dosage = resultat().dosage;

        double sectionsArmatures = sectionsArmatures(resultat());

        boolean solutionDosageFibreTalonValide = dosageTalon.estOk;
        boolean solutionDosageFibreRideauValide = dosageRideau.estOk;
        if (solutionDosageFibreTalonValide && solutionDosageFibreRideauValide) {
            resultatDuCalcul.add(new ResultatDuCalcul("Dosage de " + dosage + " kg/m\u00b3 retenu.", -1, ResultatDuCalcul.TypeDeMessage.SUCCES, Combinaison.ELU, TypeDeVerification.STRUCTURE));
            resultatDuCalcul.add(new ResultatDuCalcul("Resultats ", -1, ResultatDuCalcul.TypeDeMessage.SUCCES, Combinaison.ELU, TypeDeVerification.STRUCTURE));
            resultatDuCalcul.add(new ResultatDuCalcul("Moment Resistant Talon : OK", pourcentageMomentResistant(dosage, epaisseurBasRideau, sectionsArmatures, effortRideauELU), ResultatDuCalcul.TypeDeMessage.SUCCES, Combinaison.ELU, TypeDeVerification.FLEXION_TALON));
            resultatDuCalcul.add(new ResultatDuCalcul("Moment Resistant Rideau : OK", pourcentageMomentResistant(dosage, epaisseurTalon, sectionsArmatures, effortTalonELU), ResultatDuCalcul.TypeDeMessage.SUCCES, Combinaison.ELU, TypeDeVerification.FLEXION_RIDEAU));
        } else {//si dosage < 0 pas de solution
            if (!solutionDosageFibreTalonValide && !solutionDosageFibreRideauValide) {
                resultatDuCalcul.add(new ResultatDuCalcul("Quantité d'acier insuffisante (Talon+Rideau)! Augmentez le diamètre des armatures", -1, ResultatDuCalcul.TypeDeMessage.ERREUR, Combinaison.ELU, TypeDeVerification.STRUCTURE));
            } else if (!solutionDosageFibreTalonValide) {
                resultatDuCalcul.add(new ResultatDuCalcul("Quantité d'acier insuffisante (Talon)!Augmentez le diamètre des armatures", -1, ResultatDuCalcul.TypeDeMessage.ERREUR, Combinaison.ELU, TypeDeVerification.STRUCTURE));
            } else {
                resultatDuCalcul.add(new ResultatDuCalcul("Quantité d'acier insuffisante (Rideau)!Augmentez le diamètre des armatures", -1, ResultatDuCalcul.TypeDeMessage.ERREUR, Combinaison.ELU, TypeDeVerification.STRUCTURE));
            }
        }
    }

    private static double sectionsArmatures(ResultatDuDimensionnement resultatDuDimensionnement) {
        return resultatDuDimensionnement.nombreDeBarres * 3.14 * Math.pow(resultatDuDimensionnement.diametreDesBarres, 2) / 400.;
    }

    public EffortAgissant effortTalon(Combinaison combinaison) {
        return EffortAgissant.effortTalon(POIDS_VOLUMIQUE_BETON, poidsVolumiqueSol, angleFrottementSol, ANGLE_FROTTEMENT_INTERFACE,
                longeurMur, hauteurMur, largeurMur, epaisseurBasRideau, epaisseurHautRideau, epaisseurTalon, angleTalus, hauteurTalus, chargeUniforme, distanceChargeUniforme, chargePontuelle, distanceChargePontuelle, combinaison);
    }

    public EffortAgissant effortRideau(Combinaison combinaison) {
        return EffortAgissant.effortRideau(POIDS_VOLUMIQUE_BETON, poidsVolumiqueSol, angleFrottementSol, ANGLE_FROTTEMENT_INTERFACE,
                longeurMur, hauteurMur, largeurMur, epaisseurBasRideau, epaisseurHautRideau, epaisseurTalon, angleTalus, hauteurTalus, chargeUniforme, distanceChargeUniforme, chargePontuelle, distanceChargePontuelle, combinaison);
    }

    public ResultatDuCalcul verifierGlissement(EffortAgissant effortRideau, EffortAgissant effortTalon, Combinaison combinaison) {
        double Vd = effortRideau.nEd + effortTalon.nEd;
        double HRd = ResistanceDuSol.resistantGlissement(excentriciteDeCalcul(effortRideau, effortTalon), largeurMur, Vd, combinaison);
        boolean glissementEstOk = effortRideau.vEd < HRd;
        double pourcentage = effortRideau.vEd / HRd;
        if (donnees.nombreAncrages == 0) {
            if (glissementEstOk) {
                return new ResultatDuCalcul("Glissement : OK", pourcentage, ResultatDuCalcul.TypeDeMessage.SUCCES, combinaison, TypeDeVerification.GLISSEMENT);
            } else {
                return new ResultatDuCalcul("Glissement : pas OK", pourcentage, ResultatDuCalcul.TypeDeMessage.ERREUR, combinaison, TypeDeVerification.GLISSEMENT);
            }
        }else {
                return new ResultatDuCalcul("Glissement : OK", 1.0, ResultatDuCalcul.TypeDeMessage.SUCCES, combinaison, TypeDeVerification.GLISSEMENT);
        }
    }


    public double effortGlissement(EffortAgissant effortRideau) {
        return effortRideau.vEd;
    }

    public double resistanceGlissement(EffortAgissant effortRideau, EffortAgissant effortTalon, Combinaison combinaison) {
        double Vd = effortRideau.nEd + effortTalon.nEd;
        return ResistanceDuSol.resistantGlissement(excentriciteDeCalcul(effortRideau, effortTalon), largeurMur, Vd, combinaison);
    }


    public ResultatDuCalcul verifierCapacitePortante(EffortAgissant effortRideau, EffortAgissant effortTalon, Combinaison combinaison) {
        double VRdELU = ResistanceDuSol.capacitePortance(resistanceSol, excentriciteDeCalcul(effortRideau, effortTalon), largeurMur, combinaison);
        double Vd = effortRideau.nEd + effortTalon.nEd;
        double Ro = poidsVolumiqueSol/100 * ((epaisseurBasRideau + epaisseurHautRideau) * (hauteurMur - epaisseurTalon) / 2.0 + epaisseurTalon * largeurMur);
        boolean portanceDuSolEstOk = Vd - Ro < VRdELU;
        double pourcentage = (Vd - Ro) / VRdELU;
        if (portanceDuSolEstOk) {
            return new ResultatDuCalcul("Portance du sol: OK", pourcentage, ResultatDuCalcul.TypeDeMessage.SUCCES, combinaison, TypeDeVerification.PORTANCE);
        } else {
            return new ResultatDuCalcul("Portance du sol: pas OK", pourcentage, ResultatDuCalcul.TypeDeMessage.ERREUR, combinaison, TypeDeVerification.PORTANCE);
        }
    }

    public double efforVertical(EffortAgissant effortRideau, EffortAgissant effortTalon) {
        return effortRideau.nEd + effortTalon.nEd;
    }

    public double poidsDuSol() {
        return poidsVolumiqueSol/100 * ((epaisseurBasRideau + epaisseurHautRideau) * (hauteurMur - epaisseurTalon) / 2.0 + epaisseurTalon * largeurMur);
    }

    public double capacitePortanceSol(EffortAgissant effortRideau, EffortAgissant effortTalon, Combinaison combinaison) {
        return ResistanceDuSol.capacitePortance(resistanceSol, excentriciteDeCalcul(effortRideau, effortTalon), largeurMur, combinaison);
    }


    public ResultatDuCalcul verifierExcentriciteImportant(EffortAgissant effortRideauELU, EffortAgissant effortTalonELU, double lTotal) {
        double excentriciteDeCalcul = excentriciteDeCalcul(effortRideauELU, effortTalonELU);
        double limite = lTotal / 3.0;
        boolean excentrementELUEstImportant = excentriciteDeCalcul > limite;
        double pourcentage = excentriciteDeCalcul / limite;
        if (donnees.nombreAncrages == 0 | donnees.typeAncrage.equals("anti-glissement")) {
            if (excentrementELUEstImportant) {
                return new ResultatDuCalcul("Précautions spéciales pour excentricité importante (NF P 94-281).", pourcentage, ResultatDuCalcul.TypeDeMessage.AVERTISSEMENT, Combinaison.ELU, TypeDeVerification.EXCENTREMENT_IMPORTANT);
            } else {
                return new ResultatDuCalcul("Pas de précautions spéciales pour excentricité importante.", pourcentage, ResultatDuCalcul.TypeDeMessage.SUCCES, Combinaison.ELU, TypeDeVerification.EXCENTREMENT_IMPORTANT);
            }
        } else if (donnees.typeAncrage.equals("anti-basculement")) {
            return new ResultatDuCalcul("Pas de précautions spéciales pour excentricité importante.", 1.0, ResultatDuCalcul.TypeDeMessage.SUCCES, Combinaison.ELU, TypeDeVerification.EXCENTREMENT_IMPORTANT);
        } else {
            return new ResultatDuCalcul("Précautions spéciales pour excentricité importante.", -1.0, ResultatDuCalcul.TypeDeMessage.ERREUR, Combinaison.ELU, TypeDeVerification.EXCENTREMENT_IMPORTANT);
        }
    }

    public ResultatDuCalcul verifierExcentricite(EffortAgissant effortRideau, EffortAgissant effortTalon, Combinaison combinaison) {
        double excentriciteDeCalcul = excentriciteDeCalcul(effortRideau, effortTalon) / largeurMur;
        double excentriciteMax = (1.0 - combinaison.excentriciteMax) / 2.0;
        boolean excentrementELUEstOk = excentriciteDeCalcul <= excentriciteMax;
        double pourcentage = excentriciteDeCalcul / excentriciteMax;
        if (donnees.nombreAncrages == 0|donnees.typeAncrage.equals("anti-glissement")) {
            if (excentrementELUEstOk) {
                return new ResultatDuCalcul("Excentrement : OK.", pourcentage, ResultatDuCalcul.TypeDeMessage.SUCCES, combinaison, TypeDeVerification.EXCENTREMENT);
            } else {
                return new ResultatDuCalcul("Excentrement  : pas OK.", pourcentage, ResultatDuCalcul.TypeDeMessage.ERREUR, combinaison, TypeDeVerification.EXCENTREMENT);
            }
        } else if(donnees.typeAncrage.equals("anti-basculement")) {
            return new ResultatDuCalcul("Excentrement : OK.", 1.0, ResultatDuCalcul.TypeDeMessage.SUCCES, combinaison, TypeDeVerification.EXCENTREMENT);
        } else {
            return new ResultatDuCalcul("Excentrement : pas OK", 1.0, ResultatDuCalcul.TypeDeMessage.ERREUR, combinaison, TypeDeVerification.EXCENTREMENT);
        }
    }

    public double excentriciteDeCalcul(EffortAgissant effortRideau, EffortAgissant effortTalon) {
        double eA = (effortTalon.nEd * largeurMur / 2.0 - effortRideau.mEd) / (effortRideau.nEd + effortTalon.nEd);
        double e0 = largeurMur / 2.0 - epaisseurBasRideau / 2.0 - eA;
        return Math.max(0.0, e0);
    }

    // Trouver dosage et le nombre des armatures
    public ResultatDuDimensionnement dimensionner(double epaisseur, EffortAgissant effortAgissant) {
        Function<ResultatDuDimensionnement, Boolean> fonctionDeVerification = rdd->calculEstOk(epaisseur,effortAgissant,rdd.nombreDeBarres,rdd.diametreDesBarres,rdd.dosage);
        for (int nombreDeBarres = 0; nombreDeBarres < 20; nombreDeBarres++) {
            for (int dosage = typeFibre.dosageMin; dosage <= typeFibre.dosageMax; dosage++) {
                boolean calculEstOk = calculEstOk(epaisseur, effortAgissant, nombreDeBarres, diametreArmatures, dosage);
                if (calculEstOk) {
                    return new ResultatDuDimensionnement(typeFibre, dosage, nombreDeBarres, diametreArmatures, true, fonctionDeVerification);
                }
            }
        }
        return new ResultatDuDimensionnement(typeFibre, typeFibre.dosageMax, 0, diametreArmatures, false, fonctionDeVerification);
    }

    private boolean calculEstOk(double epaisseur, EffortAgissant effortAgissant, int nombreDeBarres, int diametreArmatures, int dosage) {
        boolean resultatVerificationMoment;
        double sectionArmatures = nombreDeBarres*3.14*Math.pow(diametreArmatures,2.0)/400.0;
        ResultatDuCalcul resultatVerificationTranchant;
        resultatVerificationMoment = verifierMomentResistant(effortAgissant, dosage, epaisseur, sectionArmatures);
        resultatVerificationTranchant = verifierTranchantResistant(effortAgissant, dosage, epaisseur, sectionArmatures);
        return resultatVerificationMoment && resultatVerificationTranchant.typeDeMessage.equals(ResultatDuCalcul.TypeDeMessage.SUCCES);
    }

    private boolean verifierMomentResistant(EffortAgissant effortAgissant, int dosage, double epaisseur, double sectionArmatures) {
        return pourcentageMomentResistant(dosage, epaisseur, sectionArmatures, effortAgissant) < 1.0;
    }

    private double pourcentageMomentResistant(int dosage, double epaisseur, double sectionArmatures, EffortAgissant effortAgissant) {
        double mRd = momentResistant(dosage, epaisseur, sectionArmatures);

        return effortAgissant.mEd / Math.abs(mRd);
    }

    private double momentResistant(int dosage, double epaisseur, double sectionArmatures) {
        double fR1 = typeFibre.fr1(dosage);
        double fR3 = typeFibre.fr3(dosage);
        double fR1d = fR1 / 1.5;
        double fR3d = fR3 / 1.5;

        double epsCu = -typeBeton.epsilonCu3;
        double fcd   = -typeBeton.fcd;

        return EffortResistant.momentResistantELUBetonFibre(epsCu, fcd, fR1d, fR3d, epaisseur, 1.0, sectionArmatures);
    }

    public double momentResistantTalon(ResultatDuDimensionnement resultatDuDimensionnement){
        return momentResistant(resultatDuDimensionnement.dosage, epaisseurTalon, sectionsArmatures(resultatDuDimensionnement));
    }

    public double momentResistantRideau(ResultatDuDimensionnement resultatDuDimensionnement){
        return momentResistant(resultatDuDimensionnement.dosage, epaisseurBasRideau, sectionsArmatures(resultatDuDimensionnement));
    }

    public ResultatDuCalcul verifierTranchantResistant(EffortAgissant effortAgissant, int dosage, double epaisseur, double sectionArmatures) {
        double fR1 = typeFibre.fr1(dosage);
        double fR3 = typeFibre.fr3(dosage);

        double fck = typeBeton.fck;
        double enrobage = 0.03;

        double vRd = EffortResistant.effortTranchantResistantBetonFibre(fck, fR1, fR3, epaisseur, 1.0, enrobage, sectionArmatures, 0);

        double pourcentageTranchantResistant = effortAgissant.vEd / vRd;

        if (pourcentageTranchantResistant < 1.0) {
            return new ResultatDuCalcul("Tranchant : OK", pourcentageTranchantResistant, ResultatDuCalcul.TypeDeMessage.SUCCES, Combinaison.ELU, TypeDeVerification.EFFORT_TRANCHANT);
        } else {
            return new ResultatDuCalcul("Tranchant : pas OK", pourcentageTranchantResistant, ResultatDuCalcul.TypeDeMessage.ERREUR, Combinaison.ELU, TypeDeVerification.EFFORT_TRANCHANT);
        }
    }


    public double calculerTractionAncrage() {
        if(donnees.nombreAncrages ==0)return 0;
        if(donnees.typeAncrage.equals("anti-glissement"))return 0;
        EffortAgissant effortAgissantRideau = effortRideau(Combinaison.ELU);
        double effortTraction = effortAgissantRideau.mEd /(largeurMur/2)/donnees.nombreAncrages ;
        return Math.max(0.0,effortTraction);
    }


    public double calculerGlissementAncrage() {
        if(donnees.nombreAncrages ==0)return 0;
        EffortAgissant effortAgissantRideau = effortRideau(Combinaison.ELU);
        double effortGlissement = effortAgissantRideau.vEd/donnees.nombreAncrages ;
        return Math.max(0.0,effortGlissement);
    }

    public double longueurArmatureRideau() {
        ResultatDuDimensionnement resultatDuDimensionnement = resultat();
        if(resultatDuDimensionnement.nombreDeBarres==0)return 0;
        EffortAgissant effortAgissant = effortRideau(Combinaison.ELU);
        double sectionArmatures = sectionsArmatures(resultatDuDimensionnement);
        double pourcentageRideau = pourcentageMomentResistant(resultatDuDimensionnement.dosage, epaisseurBasRideau, sectionArmatures, effortAgissant);
        double pourcentageTalon = pourcentageMomentResistant(resultatDuDimensionnement.dosage, epaisseurTalon, sectionArmatures, effortAgissant);
        double pourcentageMax = Math.max(pourcentageTalon,pourcentageRideau);
        return Math.min((1-pourcentageMax)*hauteurMur+50.0*diametreArmatures/1000.0,hauteurMur);
    }

    public double longueurArmatureTalon() {
        ResultatDuDimensionnement resultatDuDimensionnement = resultat();
        if(resultatDuDimensionnement.nombreDeBarres==0)return 0;
        EffortAgissant effortAgissant = effortTalon(Combinaison.ELU);
        double sectionArmatures = sectionsArmatures(resultatDuDimensionnement);
        double pourcentageRideau = pourcentageMomentResistant(resultatDuDimensionnement.dosage, epaisseurBasRideau, sectionArmatures, effortAgissant);
        double pourcentageTalon = pourcentageMomentResistant(resultatDuDimensionnement.dosage, epaisseurTalon, sectionArmatures, effortAgissant);
        double pourcentageMax = Math.max(pourcentageTalon,pourcentageRideau);
        return Math.min((1-pourcentageMax)*largeurMur+50.0*diametreArmatures/1000.0,largeurMur);
    }

    public ResultatDuDimensionnement resultat() {
        return ResultatDuDimensionnement.max(resultatDimensionnementRideau(), resultatDimensionnementTalon());
    }

    private ResultatDuDimensionnement resultatDimensionnementTalon() {
        return dimensionner(epaisseurTalon, effortTalon(Combinaison.ELU));
    }

    private ResultatDuDimensionnement resultatDimensionnementRideau() {
        return dimensionner(epaisseurBasRideau, effortRideau(Combinaison.ELU));
    }*/

    public static class ResultatDuCalcul {
        public final String message;
        public final TypeDeMessage typeDeMessage;
        public final Combinaison combinaison;
        public final double pourcentage;
        public final TypeDeVerification typeDeVerification;

        public ResultatDuCalcul(String message, double pourcentage, TypeDeMessage typeDeMessage, Combinaison combinaison, TypeDeVerification typeDeVerification) {
            this.message = message;
            this.pourcentage = pourcentage;
            this.typeDeMessage = typeDeMessage;
            this.combinaison = combinaison;
            this.typeDeVerification = typeDeVerification;
        }

        public String pourcentage() {
            if (pourcentage < 0) return "-";
            DecimalFormat decimalFormat = new DecimalFormat("0%");
            return decimalFormat.format(this.pourcentage);
        }

        @Override
        public String toString() {

            return this.typeDeMessage.name() + " " + this.combinaison + " " + this.message +
                    (this.pourcentage == -1 ? "" : "(" + pourcentage() + ")");
        }

        public enum TypeDeMessage {
            ERREUR, AVERTISSEMENT, SUCCES
        }
    }

    public enum TypeDeVerification{
        STRUCTURE,PORTANCE,GLISSEMENT,EXCENTREMENT,EXCENTREMENT_IMPORTANT,EFFORT_TRANCHANT, FLEXION_TALON, FLEXION_RIDEAU
    }

    public static class ResultatDuDimensionnement {
        public final int dosage;
        public final int nombreDeBarres;
        public final int diametreDesBarres;
        public final boolean estOk;
        private final Function<ResultatDuDimensionnement, Boolean> fonctionDeVerification;
        public final TypeFibre typeFibre;

        public ResultatDuDimensionnement(TypeFibre typeFibre, int dosage, int nombreDeBarres, int diametreDesBarres, boolean estOk, Function<ResultatDuDimensionnement, Boolean>  fonctionDeVerification) {
            this.typeFibre = typeFibre;
            this.dosage = dosage;
            this.nombreDeBarres = nombreDeBarres;
            this.diametreDesBarres = diametreDesBarres;
            this.estOk = estOk;
            this.fonctionDeVerification = fonctionDeVerification;
        }

/*        public static ResultatDuDimensionnement max(ResultatDuDimensionnement resultat1, ResultatDuDimensionnement resultat2) {
            boolean estOk = resultat1.estOk && resultat2.estOk;
            int dosage;
            int nombreDeBarres;
            int diametreDesBarres;
            if(estOk){
                boolean resultat1passeAvecDimensionnement2 = resultat1.fonctionDeVerification.apply(resultat2);
                boolean resultat2passeAvecDimensionnement1 = resultat2.fonctionDeVerification.apply(resultat1);
                if(resultat1passeAvecDimensionnement2&&resultat2passeAvecDimensionnement1){
                    if(sectionsArmatures(resultat1)<sectionsArmatures(resultat2)){
                        dosage = resultat1.dosage;
                        nombreDeBarres = resultat1.nombreDeBarres;
                        diametreDesBarres = resultat1.diametreDesBarres;
                    }else{
                        dosage = resultat2.dosage;
                        nombreDeBarres = resultat2.nombreDeBarres;
                        diametreDesBarres = resultat2.diametreDesBarres;
                    }
                }else if(resultat2passeAvecDimensionnement1){
                    dosage = resultat1.dosage;
                    nombreDeBarres = resultat1.nombreDeBarres;
                    diametreDesBarres = resultat1.diametreDesBarres;
                }else if(resultat1passeAvecDimensionnement2){
                    dosage = resultat2.dosage;
                    nombreDeBarres = resultat2.nombreDeBarres;
                    diametreDesBarres = resultat2.diametreDesBarres;
                }else {
                    throw new IllegalStateException();
                }
            }else{
                dosage = resultat1.typeFibre.dosageMax;
                nombreDeBarres = 0;
                diametreDesBarres = 0;
            }
            if(resultat1.typeFibre!=resultat2.typeFibre)throw new IllegalStateException();
            return new ResultatDuDimensionnement(resultat1.typeFibre, dosage, nombreDeBarres, diametreDesBarres, estOk, null);
        }*/
    }
}
