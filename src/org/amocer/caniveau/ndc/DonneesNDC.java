
package org.amocer.caniveau.ndc;

import org.amocer.caniveau.calculs.*;

import java.text.DecimalFormat;
import java.util.List;

public class DonneesNDC {

    private final String chantier;
    private final String localisation;
    private final String departement;
    private final String responsableProjet;
    private final String numeroAffaire;
    private final String dateRealisation;
    private final String contexteProjet;

    private final String nomFibre;
    private final String poidsVolumiqueSol;

    private final String typeCaniveau;
    private final String longueur;
    private final String largeur;
    private final String hauteur;
    private final String typeCouvercle;
    private final String epaisseurCouvercle;
    private final String hauteurRemblai;

    private final String angleFrottement;

    private final String epaisseurParoiChoisie;
    private final String epaisseurFondChoisie;

    private final String chargeUniforme;
    private final String distanceChargeUniforme;
    private final String chargePontuelle;
    private final String distanceChargePontuelle;
    private final String typeChargeRoulante;
    private final String chargeRoulante;
    private final String poidsCouvercle;

    private final String classeBeton;
    private final String dosage;
    private final String epaisseurParoi;
    private final String epaisseurFond;

    private final String renfortHA;
    private final String resistanceSol;
    private final String fR1;
    private final String fR2;
    private final String fR3;
    private final String fR4;
    private final String momentAgissantParoi;
    private final String momentResistantParoi;
    private final String momentAgissantFond;
    private final String momentResistantFond;
    private final String verificationFlexionFond;
    private final String verificationFlexionParoi;
    private final String effortVerticalELU;
    private final String poidsDuSol;
    private final String capacitePortanceSolELU;
    private final String verificationPortanceELU;


    public DonneesNDC(Calcul.ResultatDuCalcul resultatDuCalcul) {
        Donnee donnee = resultatDuCalcul.donnee;
        TypeFibre typeFibre = TypeFibre.getFibre(donnee.nomFibre,resultatDuCalcul.typeBeton);
        Calcul calcul = new Calcul(resultatDuCalcul.donnee);
        DecimalFormat df2 = new DecimalFormat("0.00");
        DecimalFormat df1 = new DecimalFormat("0.0");
        DecimalFormat df0 = new DecimalFormat("0");

        this.chantier = donnee.chantier;
        this.localisation = donnee.localisation;
        this.departement = donnee.departement;
        this.responsableProjet = donnee.responsableProjet;
        this.numeroAffaire = donnee.numeroAffaire;
        this.dateRealisation = donnee.dateRealisation;
        this.contexteProjet = donnee.contexteProjet;

        this.nomFibre = donnee.nomFibre;
        this.poidsVolumiqueSol = df1.format(donnee.poidsVolumiqueSol) ;

        this.typeCaniveau = donnee.typeCaniveau.name();

        this.longueur = df1.format(donnee.longueur);
        this.largeur = df1.format(donnee.largeur);
        this.hauteur = df1.format(donnee.hauteur);

        this.typeCouvercle = donnee.typeCouvercle.name();
        this.epaisseurCouvercle = df1.format(donnee.epaisseurCouvercle);
        this.hauteurRemblai = df1.format(donnee.hauteurRemblai);

        this.angleFrottement = df0.format(donnee.hauteurRemblai);

        this.epaisseurParoiChoisie = df1.format(donnee.epaisseurParoiChoisie);
        this.epaisseurFondChoisie = df1.format(donnee.epaisseurParoiChoisie);

        this.chargeUniforme = df1.format(donnee.chargeUniforme);
        this.distanceChargeUniforme = df1.format(donnee.distanceChargeUniforme);
        this.chargePontuelle = df1.format(donnee.chargePontuelle);
        this.distanceChargePontuelle = df1.format(donnee.distanceChargePontuelle);
        this.typeChargeRoulante = donnee.typeChargeRoulante;
        this.chargeRoulante = df1.format(donnee.chargeRoulante);
        this.poidsCouvercle = df1.format(donnee.poidsCouvercle);


        this.epaisseurParoi = df1.format(resultatDuCalcul.epaisseurMinParoi);
        this.epaisseurFond = df1.format(resultatDuCalcul.epaisseurMinFond);

        this.classeBeton = resultatDuCalcul.typeBeton;
        this.dosage = df0.format(resultatDuCalcul.dossage);

        this.renfortHA = resultatDuCalcul.renfortMini==null?"Non":(resultatDuCalcul.renfortMini + "/ml");

        this.resistanceSol = df1.format(resultatDuCalcul.resistanceMinSol);
        this.fR1 = df1.format(typeFibre.fr1(resultatDuCalcul.dossage));
        this.fR2 = df1.format(typeFibre.fr2(resultatDuCalcul.dossage));
        this.fR3 = df1.format(typeFibre.fr3(resultatDuCalcul.dossage));
        this.fR4 = df1.format(typeFibre.fr4(resultatDuCalcul.dossage));

        this.momentAgissantParoi = df1.format(calcul.getMomentParoiELU());
        this.momentResistantParoi = df1.format(calcul.getMomentParoiELU());
        this.momentAgissantFond = df1.format(calcul.getMomentFondELU(donnee.epaisseurFondChoisie));
        this.momentResistantFond= df1.format(calcul.getMomentParoiELU());
        this.verificationFlexionParoi = "OK";
        this.verificationFlexionFond = "OK";

        this.effortVerticalELU = df1.format(calcul.getEffortVerticalELU(donnee.epaisseurParoiChoisie));
        this.poidsDuSol = df1.format(0.0);
        this.capacitePortanceSolELU = df1.format(calcul.getEffortVerticalELU(donnee.epaisseurParoiChoisie));
        this.verificationPortanceELU = "OK";
    }

}

