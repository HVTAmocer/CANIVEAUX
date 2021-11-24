package org.amocer.caniveau.ndc;

public class DonneesNDC {
  /*  private final String conclusionNDC;
    private final String conclusionNDCcolor;
    private final String longueurDuMur;
    private final String largeurDuMur;
    private final String epaisseurTalon;
    private final String epaisseurBaseRideau;
    private final String epaisseurHauteRideau;
    private final String hauteurTalus;
    private final String angleTalus;
    private final String typeAncrage;
    private final String nombreAncrages;
    private final String chargeUniforme;
    private final String distanceChargeUniforme;
    private final String chargePontuelle;
    private final String distanceChargePontuelle;
    private final String resistanceSol;
    private final String poidsVolumiqueSol;
    private final String fR1;
    private final String fR2;
    private final String fR3;
    private final String fR4;
    private final String angleFrottementSol;
    private final String momentAgissantTalon;
    private final String momentResistantTalon;
    private final String verificationFlexionTalon;
    private final String momentAgissantRideau;
    private final String momentResistantRideau;
    private final String verificationFlexionRideau;
    private final String classeDuBeton;
    private final String typeDeFibre;
    private final String dosage;
    private final String renfortHA;
    private final String LV;
    private final String LH;
    private final String armaturesSecondaires;
    private final String hauteurDuMur;

    private final String exentriciteELU;
    private final String exentriciteELSQP;
    private final String exentriciteELSCAR;
    private final String verificationExcentrementELU;
    private final String verificationExcentrementELSCAR;
    private final String verificationExcentrementELSQP;
    private final String verificationExcentrementImportant;

    private final String effortVerticalELU;
    private final String effortVerticalELSCAR;
    private final String effortVerticalELSQP;
    private final String poidsDuSol;
    private final String capacitePortanceSolELU;
    private final String capacitePortanceSolELSCAR;
    private final String capacitePortanceSolELSQP;
    private final String verificationPortanceELU;
    private final String verificationPortanceELSCAR;
    private final String verificationPortanceELSQP;

    private final String effortGlissementELU;
    private final String effortGlissementELSCAR;
    private final String effortGlissementELSQP;
    private final String resistanceGlissementELU;
    private final String resistanceGlissementELSQP;
    private final String resistanceGlissementELSCAR;
    private final String verificationGlissementELU;
    private final String verificationGlissementELSCAR;
    private final String verificationGlissementELSQP;


    public DonneesNDC(CalculateurDeMursDeSoutainement calculateurDeMursDeSoutainement) {
        Donnees donnees = calculateurDeMursDeSoutainement.donnees;
        CalculateurDeMursDeSoutainement.ResultatDuDimensionnement resultatDuDimensionnement = calculateurDeMursDeSoutainement.resultat();
        List<CalculateurDeMursDeSoutainement.ResultatDuCalcul> resultatDuCalculs = calculateurDeMursDeSoutainement.calculer();
        DecimalFormat df = new DecimalFormat("0.00");
        DecimalFormat entier = new DecimalFormat("0");
        this.hauteurDuMur = df.format(donnees.hauteurMur);
        this.longueurDuMur = df.format(donnees.longueurMur);
        this.largeurDuMur = df.format(donnees.largeurMur);
        this.epaisseurHauteRideau = df.format(donnees.epaisseurHautRideau);
        this.epaisseurBaseRideau = df.format(donnees.epaisseurBasRideau);
        this.epaisseurTalon = df.format(donnees.epaisseurTalon);
        this.hauteurTalus = df.format(donnees.hauteurRemblai);
        this.angleTalus = df.format(donnees.angleTalus);
        this.typeAncrage = donnees.typeAncrage;
        this.nombreAncrages = entier.format(donnees.nombreAncrages);

        this.chargeUniforme = df.format(donnees.chargeUniforme);
        this.distanceChargeUniforme = df.format(donnees.distanceChargeUniforme);
        this.chargePontuelle = df.format(donnees.chargePontuelle);
        this.distanceChargePontuelle = df.format(donnees.distanceChargePontuelle);

        this.classeDuBeton = donnees.nomBeton;
        this.typeDeFibre = donnees.typeFibre.nomFibre;
        this.dosage = entier.format(resultatDuDimensionnement.dosage);
        this.fR1 = df.format(donnees.typeFibre.fr1(resultatDuDimensionnement.dosage));
        this.fR2 = df.format(donnees.typeFibre.fr2(resultatDuDimensionnement.dosage));
        this.fR3 = df.format(donnees.typeFibre.fr3(resultatDuDimensionnement.dosage));
        this.fR4 = df.format(donnees.typeFibre.fr4(resultatDuDimensionnement.dosage));

        if(resultatDuDimensionnement.nombreDeBarres>0){
            this.renfortHA = resultatDuDimensionnement.nombreDeBarres+"HA"+resultatDuDimensionnement.diametreDesBarres;
            this.LV = df.format(calculateurDeMursDeSoutainement.longueurArmatureRideau());
            this.LH = df.format(calculateurDeMursDeSoutainement.longueurArmatureTalon());
            this.armaturesSecondaires = "HA6,e=300mm ";
        }else {
            this.renfortHA = "Aucun";
            this.LV = df.format(0.0);
            this.LH = df.format(0.0);
            this.armaturesSecondaires = "Aucun";
        }
        this.resistanceSol = df.format(donnees.resistanceSol);
        this.poidsVolumiqueSol = df.format(donnees.poidsVolumiqueSol);
        this.angleFrottementSol = df.format(donnees.angleFrottementSol);

        this.momentResistantRideau = df.format(calculateurDeMursDeSoutainement.momentResistantRideau(resultatDuDimensionnement));
        this.momentResistantTalon = df.format(calculateurDeMursDeSoutainement.momentResistantTalon(resultatDuDimensionnement));
        this.momentAgissantTalon = df.format(calculateurDeMursDeSoutainement.effortTalon(Combinaison.ELU).mEd);
        this.momentAgissantRideau = df.format(calculateurDeMursDeSoutainement.effortRideau(Combinaison.ELU).mEd);
        this.verificationFlexionTalon = trouveResultat(CalculateurDeMursDeSoutainement.TypeDeVerification.FLEXION_TALON, Combinaison.ELU, resultatDuCalculs);
        this.verificationFlexionRideau = trouveResultat(CalculateurDeMursDeSoutainement.TypeDeVerification.FLEXION_RIDEAU, Combinaison.ELU, resultatDuCalculs);

        EffortAgissant effortRideauELU = calculateurDeMursDeSoutainement.effortRideau(Combinaison.ELU);
        EffortAgissant effortTalonELU = calculateurDeMursDeSoutainement.effortTalon(Combinaison.ELU);
        EffortAgissant effortRideauELSCAR = calculateurDeMursDeSoutainement.effortRideau(Combinaison.ELS_CAR);
        EffortAgissant effortTalonELSCAR = calculateurDeMursDeSoutainement.effortTalon(Combinaison.ELS_CAR);
        EffortAgissant effortRideauELSQP = calculateurDeMursDeSoutainement.effortRideau(Combinaison.ELS_QP);
        EffortAgissant effortTalonELSQP = calculateurDeMursDeSoutainement.effortTalon(Combinaison.ELS_QP);

        this.effortGlissementELU = df.format(calculateurDeMursDeSoutainement.effortGlissement(effortRideauELU));
        this.effortGlissementELSCAR = df.format(calculateurDeMursDeSoutainement.effortGlissement(effortRideauELSCAR));
        this.effortGlissementELSQP = df.format(calculateurDeMursDeSoutainement.effortGlissement(effortRideauELSQP));
        this.resistanceGlissementELU = df.format(calculateurDeMursDeSoutainement.resistanceGlissement(effortRideauELU, effortTalonELU, Combinaison.ELU));
        this.resistanceGlissementELSCAR = df.format(calculateurDeMursDeSoutainement.resistanceGlissement(effortRideauELSCAR, effortTalonELSCAR, Combinaison.ELS_CAR));
        this.resistanceGlissementELSQP = df.format(calculateurDeMursDeSoutainement.resistanceGlissement(effortRideauELSQP, effortTalonELSQP, Combinaison.ELS_QP));
        this.verificationGlissementELU = trouveResultat(CalculateurDeMursDeSoutainement.TypeDeVerification.GLISSEMENT, Combinaison.ELU, resultatDuCalculs);
        this.verificationGlissementELSCAR = trouveResultat(CalculateurDeMursDeSoutainement.TypeDeVerification.GLISSEMENT, Combinaison.ELS_CAR, resultatDuCalculs);
        this.verificationGlissementELSQP = trouveResultat(CalculateurDeMursDeSoutainement.TypeDeVerification.GLISSEMENT, Combinaison.ELS_QP, resultatDuCalculs);

        this.effortVerticalELU = df.format(calculateurDeMursDeSoutainement.efforVertical(effortRideauELU, effortTalonELU));
        this.effortVerticalELSCAR = df.format(calculateurDeMursDeSoutainement.efforVertical(effortRideauELSCAR, effortTalonELSCAR));
        this.effortVerticalELSQP = df.format(calculateurDeMursDeSoutainement.efforVertical(effortRideauELSQP, effortTalonELSQP));
        this.poidsDuSol = df.format(calculateurDeMursDeSoutainement.poidsDuSol());
        this.capacitePortanceSolELU = df.format(calculateurDeMursDeSoutainement.capacitePortanceSol(effortRideauELU, effortTalonELU, Combinaison.ELU));
        this.capacitePortanceSolELSCAR = df.format(calculateurDeMursDeSoutainement.capacitePortanceSol(effortRideauELSCAR, effortTalonELSCAR, Combinaison.ELS_CAR));;
        this.capacitePortanceSolELSQP = df.format(calculateurDeMursDeSoutainement.capacitePortanceSol(effortRideauELSQP, effortTalonELSQP, Combinaison.ELS_QP));;

        this.verificationPortanceELU = trouveResultat(CalculateurDeMursDeSoutainement.TypeDeVerification.PORTANCE, Combinaison.ELU, resultatDuCalculs);
        this.verificationPortanceELSCAR = trouveResultat(CalculateurDeMursDeSoutainement.TypeDeVerification.PORTANCE, Combinaison.ELS_CAR, resultatDuCalculs);
        this.verificationPortanceELSQP = trouveResultat(CalculateurDeMursDeSoutainement.TypeDeVerification.PORTANCE, Combinaison.ELS_QP, resultatDuCalculs);


        this.exentriciteELU = df.format(calculateurDeMursDeSoutainement.excentriciteDeCalcul(effortRideauELU, effortTalonELU));
        this.exentriciteELSCAR = df.format(calculateurDeMursDeSoutainement.excentriciteDeCalcul(effortRideauELSCAR, effortTalonELSCAR));
        this.exentriciteELSQP = df.format(calculateurDeMursDeSoutainement.excentriciteDeCalcul(effortRideauELSQP, effortTalonELSQP));
        this.verificationExcentrementELU = trouveResultat(CalculateurDeMursDeSoutainement.TypeDeVerification.EXCENTREMENT, Combinaison.ELU, resultatDuCalculs);
        this.verificationExcentrementELSCAR = trouveResultat(CalculateurDeMursDeSoutainement.TypeDeVerification.EXCENTREMENT, Combinaison.ELS_CAR, resultatDuCalculs);
        this.verificationExcentrementELSQP = trouveResultat(CalculateurDeMursDeSoutainement.TypeDeVerification.EXCENTREMENT, Combinaison.ELS_QP, resultatDuCalculs);
        this.verificationExcentrementImportant = trouveResultat(CalculateurDeMursDeSoutainement.TypeDeVerification.EXCENTREMENT_IMPORTANT, Combinaison.ELU, resultatDuCalculs);

        this.conclusionNDC = resultatDuDimensionnement.estOk?"Le mur de soutènement est bien dimensionné selon les hypotheses ci-dessus.":"Le mur n'est pas verifié.";
        this.conclusionNDCcolor = resultatDuDimensionnement.estOk?"lightgreen":"red";

    }

    private String trouveResultat(CalculateurDeMursDeSoutainement.TypeDeVerification typeDeVerification, Combinaison combinaison, List<CalculateurDeMursDeSoutainement.ResultatDuCalcul> resultatDuCalculs) {
        String resultats = resultatDuCalculs.stream()
                .filter(rdc->rdc.combinaison.equals(combinaison))
                .filter(rdc->rdc.typeDeVerification.equals(typeDeVerification))
                .map(rdc->rdc.message)
                .findAny().orElse("Erreur");
        return resultats;
    }*/
}
