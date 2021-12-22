package org.amocer.caniveau.ui;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class VerificateurDeLicence {
    private static final String HASH_SALT = "12345";
    public static final String EULA_FILE = "eula.txt";
    public static final String LICENCE_FILE = "licence.lic";

    private static final String    HEXES    = "0123456789ABCDEF";

    static String getHex(byte[] raw) {
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    public static boolean pcAutorise() {
        String storedHash;
        try{
            List<String> contenuDuFichier = Files.readAllLines(Paths.get(LICENCE_FILE));
            if(contenuDuFichier.size()!=1)return false;
            storedHash = contenuDuFichier.get(0);
        }catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            return false;
        }
        try{
            return leCodeEstCorrecte(storedHash, hashNomPc());
        }catch (UnknownHostException e){
            e.printStackTrace();
            return false;
        }
    }

    private static boolean leCodeEstCorrecte(String storedHash, String hashNomPc) {
        String hash = codeDeDeblocage(hashNomPc);
        return storedHash.equals(hash);
    }

    public static boolean leCodeEstCorrecte(String code) {
        try{
            return leCodeEstCorrecte(code.toUpperCase(), hashNomPc());
        } catch (UnknownHostException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String codeDeDeblocage(String hashNomPc) {
        return hash(hashNomPc + HASH_SALT);
    }

    static String hashNomPc() throws UnknownHostException {
        return hash(nomPc());
    }

    private static String nomPc() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    public static boolean licenceAccepte() {
        return Files.exists(Paths.get(EULA_FILE));
    }

    public static String texteEULA() {
        InputStream is = VerificateurDeLicence.class.getResourceAsStream("resources/"+EULA_FILE);
        return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
    }

    private static String hash(String texte) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            printProviders();
            System.exit(0);
        }
        final byte[] hash = digest.digest(texte.getBytes(StandardCharsets.UTF_8));
        return getHex(hash);
    }

    private static void printProviders() {
        for (Provider provider : Security.getProviders()) {
            System.out.println("Provider: " + provider.getName());
            for (Provider.Service service : provider.getServices()) {
                System.out.println("  Algorithm: " + service.getAlgorithm());
            }
        }
    }


    public static void main(String[] args) {
        System.out.println("Code de licence non-valide:\n");
        Scanner in = new Scanner(System.in);
        String codeNonValide = in.nextLine();
        System.out.println("Code de licence valide:\n");
        System.out.println(codeDeDeblocage(codeNonValide));
    }
}
