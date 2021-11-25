package org.amocer.caniveau.calculs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeFibre {
    private static final Map<String, TypeFibre> typesDeFibre = new HashMap<>();
    static {
        importerDonnees();
    }

    public static void importerDonnees(){
        List<String> lignes;
        try (InputStream resource = CoefficientDePoussee.class.getResourceAsStream("resources/data.dat")) {
            lignes = new BufferedReader(new InputStreamReader(resource,
                            StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
            return;
        }
        TypeFibre.chargerDonnees(lignes.stream().map(ligne->ligne.split("\t")).collect(Collectors.toList()));
    }

    static void chargerDonnees(List<String[]> donnees) {
        for(int i=0;i<donnees.size();i+=4){
            String nomFibre = donnees.get(i)[0];
            String nomBeton = donnees.get(i)[1];
            TypeFibre typeFibre = new TypeFibre(donnees.get(i), donnees.get(i + 1), donnees.get(i + 2), donnees.get(i + 3));
            typesDeFibre.put(nomFibre+nomBeton, typeFibre);
        }
    }

    //
    static TypeFibre getFibre(String nomFibre, String nomBeton){
        return typesDeFibre.get(nomFibre+nomBeton);
    }

    public final String nomFibre;
    final TypeBeton typeBeton;
    final Integer dosageMin, dosageMax;
    private final Map<Integer, ValeursFR> mapDosageValeursFr = new HashMap<>();

    private TypeFibre(String[] donneesFr1, String[] donneesFr2, String[] donneesFr3, String[] donneesFr4){
        nomFibre = donneesFr1[0];
        typeBeton = TypeBeton.valueOf(donneesFr1[1].substring(0,3));
        dosageMin = Integer.parseInt(donneesFr1[2]);
        dosageMax = Integer.parseInt(donneesFr1[3]);
        for(int i=5;i<donneesFr1.length;i++){
            double fr1 = Double.parseDouble(donneesFr1[i]);
            double fr2 = Double.parseDouble(donneesFr2[i]);
            double fr3 = Double.parseDouble(donneesFr3[i]);
            double fr4 = Double.parseDouble(donneesFr4[i]);
            mapDosageValeursFr.put(dosageMin+i-5, new ValeursFR(fr1, fr2, fr3, fr4));
        }
    }

    public double fr1(int dosage){
        return mapDosageValeursFr.get(dosage).fr1;
    }

    public double fr2(int dosage){
        return mapDosageValeursFr.get(dosage).fr2;
    }

    public double fr3(int dosage){
        return mapDosageValeursFr.get(dosage).fr3;
    }

    public double fr4(int dosage){
        return mapDosageValeursFr.get(dosage).fr4;
    }


    static class ValeursFR{
        final double fr1, fr2, fr3, fr4;

        ValeursFR(double fr1, double fr2, double fr3, double fr4) {
            this.fr1 = fr1;
            this.fr2 = fr2;
            this.fr3 = fr3;
            this.fr4 = fr4;
        }
    }
}
