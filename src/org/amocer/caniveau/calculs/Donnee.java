package org.amocer.caniveau.calculs;

public class Donnee {
    public final String chantier;
    public final String localisation;
    public final String departement;
    public final String responsableProjet;
    public final String numeroAffaire;
    public final String dateRealisation;
    public final String contexteProjet;


    public final String nomFibre;
    public final double poidsVolumiqueSol;

    public final TypeCaniveau typeCaniveau;
    public final double longueur;
    public final double largeur;
    public final double hauteur;
    public final TypeCouvercle typeCouvercle;
    public double epaisseurCouvercle;
    public final double hauteurRemblai;

    public  final double angleFrottement;

    public final double epaisseurParoiChoisie;
    public final double epaisseurFondChoisie;

    public final double chargeUniforme;
    public final double distanceChargeUniforme;

    public final double chargePontuelle;
    public final double distanceChargePontuelle;

    public final String typeChargeRoulante;

    public final double poidsCouvercle;


    public Donnee(
            String chantier,
            String localisation,
            String departement,
            String responsableProjet,
            String numeroAffaire,
            String dateRealisation,
            String contexteProjet,

            String nomFibre,
            double poidsVolumiqueSol,

            TypeCaniveau typeCaniveau,
            double longueur,
            double largeur,
            double hauteur,
            TypeCouvercle typeCouvercle,
            double epaisseurCouvercle,
            double hauteurRemblai,

            double angleFrottement,

            double epaisseurParoiChoisie,
            double epaisseurFondChoisie,

            double chargeUniforme,
            double distanceChargeUniforme,
            double chargePontuelle,
            double distanceChargePontuelle,
            String typeChargeRoulante,
            double poidsCouvercle)
    {
        this.chantier = chantier;
        this.localisation = localisation;
        this.departement = departement;
        this.responsableProjet = responsableProjet;
        this.numeroAffaire = numeroAffaire;
        this.dateRealisation = dateRealisation;
        this.contexteProjet = contexteProjet;

        this.nomFibre = nomFibre;
        this.poidsVolumiqueSol = poidsVolumiqueSol;

        this.typeCaniveau = typeCaniveau;
        this.largeur = largeur;
        this.longueur = longueur;
        this.hauteur = hauteur;
        this.typeCouvercle = typeCouvercle;
        this.epaisseurCouvercle = epaisseurCouvercle;
        this.hauteurRemblai = hauteurRemblai;

        this.angleFrottement = angleFrottement;

        this.epaisseurParoiChoisie = epaisseurParoiChoisie;
        this.epaisseurFondChoisie = epaisseurFondChoisie;

        this.chargeUniforme = chargeUniforme;
        this.distanceChargeUniforme = distanceChargeUniforme;
        this.chargePontuelle = chargePontuelle;
        this.distanceChargePontuelle = distanceChargePontuelle;
        this.typeChargeRoulante = typeChargeRoulante;
        this.poidsCouvercle = poidsCouvercle;
    }
}
