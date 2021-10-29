package org.amocer.wall.calculs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CoefficientDePoussee {
   private final static List<CoefficientDePoussee> abaqueDeCaquot = new LinkedList<>();
    private static final int LIGNES_FICHIER_DONNEES = 20;

    static {
        chargerAbaqueDeCaquot();
    }

    private static void chargerAbaqueDeCaquot() {
        Double[][] lignes;
        try (InputStream resource = CoefficientDePoussee.class.getResourceAsStream("resources/K.dat")) {
            lignes =
                    new BufferedReader(new InputStreamReader(resource,
                            StandardCharsets.UTF_8)).lines().collect(Collectors.toList()).stream()
                            .map(
                                    ligne -> Arrays.stream(ligne.split("\t"))
                                            .map(Double::parseDouble)
                                            .toArray(Double[]::new)
                            )
                            .toArray(Double[][]::new);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
            return;
        }

        for (int i = 0; i< LIGNES_FICHIER_DONNEES; i++){
            for(int j=0; j<lignes[i].length;j++){
                double delta_over_phi = lignes[i+LIGNES_FICHIER_DONNEES*3][j];
                double beta_over_phi = lignes[i+LIGNES_FICHIER_DONNEES*1][j];
                double lambda = lignes[i+LIGNES_FICHIER_DONNEES*2][j];
                double phi = lignes[i+LIGNES_FICHIER_DONNEES*4][j];
                double valeurDuCoefficient = lignes[i+LIGNES_FICHIER_DONNEES*0][j];
                abaqueDeCaquot.add(new CoefficientDePoussee(delta_over_phi, beta_over_phi, lambda, phi, valeurDuCoefficient));
            }
        }
    }

    static double coefficientDePousseeCaquot(double delta_over_phi, double beta_over_phi, double lambda, double phi) throws IllegalStateException{
        Function<CoefficientDePoussee, Double> fonctionX;
        Function<CoefficientDePoussee, String> fonctionZ;
        Consumer<CoefficientDePoussee> fonctionDeMiseAJourX;
        List<CoefficientDePoussee> valeursInterpoles = abaqueDeCaquot;

        //interpoler beta_sur_phi
        fonctionX = cdp -> cdp.beta_over_phi;
        fonctionDeMiseAJourX = cdp -> cdp.beta_over_phi = beta_over_phi;
        fonctionZ = cdp->"phi"+cdp.phi+"lambda"+cdp.lambda+"deltaOverPhi"+cdp.delta_over_phi;
        valeursInterpoles = fonctionInterpolatrice(valeursInterpoles, beta_over_phi, fonctionX, fonctionZ, fonctionDeMiseAJourX);

        //interpoler lambda
        fonctionX = cdp -> cdp.lambda;
        fonctionDeMiseAJourX = cdp -> cdp.lambda = lambda;
        fonctionZ = cdp->"phi"+cdp.phi+"beta_sur_phi"+cdp.beta_over_phi+"deltaOverPhi"+cdp.delta_over_phi;
        valeursInterpoles = fonctionInterpolatrice(valeursInterpoles, lambda, fonctionX, fonctionZ, fonctionDeMiseAJourX);

        //interpoler delta_over_phi
        fonctionX = cdp -> cdp.delta_over_phi;
        fonctionDeMiseAJourX = cdp -> cdp.delta_over_phi = delta_over_phi;
        fonctionZ = cdp->"phi"+cdp.phi+"beta_sur_phi"+cdp.beta_over_phi+"lambda"+cdp.lambda;
        valeursInterpoles = fonctionInterpolatrice(valeursInterpoles, delta_over_phi, fonctionX, fonctionZ, fonctionDeMiseAJourX);

        //interpoler phi
        fonctionX = cdp -> cdp.phi;
        fonctionDeMiseAJourX = cdp -> cdp.phi = phi;
        fonctionZ = cdp->"delta_over_phi"+cdp.delta_over_phi+"beta_sur_phi"+cdp.beta_over_phi+"lambda"+cdp.lambda;
        valeursInterpoles = fonctionInterpolatrice(valeursInterpoles, phi, fonctionX, fonctionZ, fonctionDeMiseAJourX);

        if(valeursInterpoles.size()!=1)throw new IllegalStateException("Erreur d'interpolation");
        return valeursInterpoles.get(0).valeurDuCoefficient;
    }

    private static List<CoefficientDePoussee> fonctionInterpolatrice(List<CoefficientDePoussee> valeursAInterpoler , double beta_over_phi, Function<CoefficientDePoussee, Double> fonctionX, Function<CoefficientDePoussee, String> fonctionZ, Consumer<CoefficientDePoussee> fonctionDeMiseAJourX) {
        Function<CoefficientDePoussee, Double> fonctionY = cdp -> cdp.valeurDuCoefficient;
        Map<String, List<CoefficientDePoussee>> valeursGroupes = valeursAInterpoler.stream()
                .collect(Collectors.groupingBy(fonctionZ));
        valeursGroupes.values().forEach(list->list.sort(Comparator.comparing(fonctionX)));
        List<CoefficientDePoussee> valeursInterpoles = valeursGroupes.values().stream()
                .map(sousListe->{
                            Consumer<CoefficientDePoussee> functionDeMiseAJour = cdp -> {
                                fonctionDeMiseAJourX.accept(cdp);
                                cdp.valeurDuCoefficient = InterpolationLineaire.interpoler(
                                        beta_over_phi,
                                        sousListe.stream().map(fonctionX).toArray(Double[]::new),
                                        sousListe.stream().map(fonctionY).toArray(Double[]::new));
                            };
                            return new CoefficientDePoussee(sousListe.get(0), functionDeMiseAJour);
                }
                ).collect(Collectors.toList());
        return valeursInterpoles;
    }

    double delta_over_phi, beta_over_phi, lambda, phi, valeurDuCoefficient;

    public CoefficientDePoussee(double delta_over_phi, double beta_over_phi, double lambda, double phi, double valeurDuCoefficient) {
        this.delta_over_phi = delta_over_phi;
        this.beta_over_phi = beta_over_phi;
        this.lambda = lambda;
        this.phi = phi;
        this.valeurDuCoefficient = valeurDuCoefficient;
    }

    public CoefficientDePoussee(CoefficientDePoussee coefficientDePoussee, Consumer<CoefficientDePoussee> functionDeMiseAJour) {
        this.delta_over_phi = coefficientDePoussee.delta_over_phi;
        this.beta_over_phi = coefficientDePoussee.beta_over_phi;
        this.lambda = coefficientDePoussee.lambda;
        this.phi = coefficientDePoussee.phi;
        this.valeurDuCoefficient = coefficientDePoussee.valeurDuCoefficient;
        functionDeMiseAJour.accept(this);
    }


    // THEORIE COULOMB - FORMULE PONCELET
    // - Sol pulvérulent (cohésion c=0)
    public static double KaCoulomb(double phi, double beta, double delta, double lambda ) {
        phi = Math.toRadians(phi);
        beta = Math.toRadians(beta);
        delta = Math.toRadians(delta);
        double nuy = Math.toRadians(90 - lambda);
        return Math.pow(Math.sin(nuy-phi),2)/Math.pow(Math.sin(nuy),2)/Math.sin(nuy+delta)*
                Math.pow((1+Math.sqrt(Math.sin(phi+delta)*Math.sin(phi-beta)/Math.sin(nuy+delta)/Math.sin(nuy-beta))),-2);
    }


    // THEORIE DE RANKINE
    // - Sol pulvérulent (cohésion c=0)
    // - Sans prise en compte le frottement qui existe entre le sol et le mur (delta =0)
    // - Terrain plan à l'arrière de la structure (beta =0)
    public static double KaRankine(double phi) {
        double radPhi = Math.toRadians(phi);
        return Math.pow(Math.tan(Math.PI/4-radPhi/2),2);
    }

    // THEORIE BOUSSINESQ - FORMULE CAQUOT-KERISEL

}
