package org.amocer.caniveau.ndc;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.xml.sax.SAXException;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnregistrateurDePDFs {
    public static void enregistrerPDF(Object sampleDataContainer, File fichierSource, File fichierPDF, String balise) {
        balise = Matcher.quoteReplacement(balise);
        String texteSource = texteSource(fichierSource);
        if (texteSource == null) return;
        Map<String, String> givenInputs = getGivenInputs(sampleDataContainer);
        Set<String> requiredInputs = getRequiredInputs(balise, texteSource);
        compareInputs(givenInputs, requiredInputs);
        texteSource = remplirTexteSource(texteSource, balise, givenInputs);

        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(fichierPDF))) {
            //FopFactory fopFactory = FopFactory.newInstance(copyInputStreamToFile(EnregistrateurDePDFs.class.getResourceAsStream("/xsl/fop.xconf")));
            FopFactory fopFactory = FopFactory.newInstance(new File("xsl/fop.xconf"));
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            Source src = new StreamSource(new StringReader(texteSource));
            Result res = new SAXResult(fop.getDefaultHandler());

            transformer.transform(src, res);
        } catch (IOException | TransformerException | SAXException e) {
            e.printStackTrace();
        }
    }

    private static File copyInputStreamToFile(InputStream inputStream) throws IOException {
        File file = Files.createTempFile("temp",".tmp").toFile();
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[8192];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
        return file;
    }

    private static String remplirTexteSource(String texteSource, String balise, Map<String, String> givenInputs) {
        for(String clef:givenInputs.keySet()){
            texteSource = texteSource.replaceAll(balise+Matcher.quoteReplacement(clef)+balise, givenInputs.get(clef));
        }
        return texteSource;
    }

    private static String texteSource(File fichierSource) {
        String texteSource = null;
        try {
            texteSource = String.join("\n", Files.readAllLines(fichierSource.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return texteSource;
    }

/*    private static String texteSource(InputStream fichierSource) {
        return new BufferedReader(new InputStreamReader(fichierSource, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
    }*/


    private static void compareInputs(Map<String, String> givenInputs, Set<String> requiredInputs) {
        System.out.println("Missing from required :");
        for(String input:givenInputs.keySet()){
            if(!requiredInputs.contains(input))System.out.println(input);
        }
        System.out.println("Missing from given :");
        for(String input:requiredInputs){
            if(!givenInputs.containsKey(input))System.out.println(input);
        }
    }

    private static Set<String> getRequiredInputs(String balise, String texteSource) {
        Set<String> requiredInputs = new HashSet<>();
        Matcher m = Pattern.compile(balise+".+"+balise)
                .matcher(texteSource);
        while (m.find()) {
            requiredInputs.add(m.group().replaceAll(balise, ""));
        }
        return requiredInputs;
    }

    private static Map<String, String> getGivenInputs(Object sampleDataContainer) {
        Map<String, String> valuesMap = new HashMap<>();
        final Class<?> classe = sampleDataContainer.getClass();
        Field[] fields = classe.getDeclaredFields();
        for(Field field:fields){
            String name = field.getName();
            String value = null;
            try {
                field.setAccessible(true);
                value = field.get(sampleDataContainer).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            valuesMap.put(name, value);
        }
        return valuesMap;
    }
}
