package org.amocer.caniveau.calculs;

public class Donnee {
    public final String chantier;
    public final String localisation;
    public final String client;

    public final String typeFibre;
    public final double poidsVolumiqueSol;

    public final TypeCaniveau typeCaniveau;
    public final double longueur;
    public final double largeur;
    public final double hauteur;
    public final String typeCouvercle;
    public double epaisseurCouvercle;
    public final double hauteurRemblai;


    public final double chargeUniforme;
    public final double distanceChargeUniforme;

    public final double chargePontuelle;
    public final double distanceChargePontuelle;

    public final String typeChargeRoulante;
    public final double chargeRoulante;

    public final double poidsCouvercle;



    public Donnee(
            String chantier,
            String localisation,
            String client,

            String typeFibre,
            double poidsVolumiqueSol,

            TypeCaniveau typeCaniveau,
            double longueur,
            double largeur,
            double hauteur,
            String typeCouvercle,
            double epaisseurCouvercle,
            double hauteurRemblai,

            double chargeUniforme,
            double distanceChargeUniforme,
            double chargePontuelle,
            double distanceChargePontuelle,
            String typeChargeRoulante,
            double chargeRoulante,
            double poidsCouvercle) {
        this.chantier = chantier;
        this.localisation = localisation;
        this.client = client;

        this.typeFibre = typeFibre;
        this.poidsVolumiqueSol = poidsVolumiqueSol;

        this.typeCaniveau = typeCaniveau;
        this.largeur = largeur;
        this.longueur = longueur;
        this.hauteur = hauteur;
        this.typeCouvercle = typeCouvercle;
        this.epaisseurCouvercle = epaisseurCouvercle;
        this.hauteurRemblai = hauteurRemblai;

        this.chargeUniforme = chargeUniforme;
        this.distanceChargeUniforme = distanceChargeUniforme;
        this.chargePontuelle = chargePontuelle;
        this.distanceChargePontuelle = distanceChargePontuelle;
        this.typeChargeRoulante = typeChargeRoulante;
        this.chargeRoulante = chargeRoulante;
        this.poidsCouvercle = poidsCouvercle;
    }
}
