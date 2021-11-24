package org.amocer.caniveau.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Writer;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
        verifierLicence();
        validerEULA();
        demarrerProgrammePrincipal(primaryStage);
    }

    private void demarrerProgrammePrincipal(Stage primaryStage) throws java.io.IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ui.fxml"));
        primaryStage.setTitle("CACTUS - CANIVEAU BETON FIBRE- DEVELOPPE PAR AMOCER GROUP");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        String css = this.getClass().getResource("application.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setResizable(false);
        primaryStage.getIcons().setAll(new Image(Main.class.getResourceAsStream("resources/LogoLWall.jpg")));
        primaryStage.show();
    }

    private void verifierLicence() {
        if(!VerificateurDeLicence.pcAutorise()){
            try {
                Files.delete(Paths.get(VerificateurDeLicence.EULA_FILE));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String hashNomPc = "N.D.";
            try {
                hashNomPc = VerificateurDeLicence.hashNomPc();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                System.exit(0);
            }
            TextInputDialog dialog = new TextInputDialog(hashNomPc);
            dialog.setTitle("Licence Non Valide");
            dialog.setHeaderText("Inserer un code de licence valide SN = "+hashNomPc);
            dialog.setContentText("Code de licence :");
            Optional<String> codeSaisiParUtilisateur = dialog.showAndWait();
            if (codeSaisiParUtilisateur.isPresent()){
                if(!VerificateurDeLicence.leCodeEstCorrecte(codeSaisiParUtilisateur.get())){
                    messageDErreur("Code Incorrecte");
                    System.exit(0);
                }else{
                    ecrireFichier(VerificateurDeLicence.LICENCE_FILE, codeSaisiParUtilisateur.orElse(""));
                }
            }else{
                System.exit(0);
            }
        }
    }

    private void validerEULA() {
        if(!VerificateurDeLicence.licenceAccepte()){
            ButtonType accepterButton = new ButtonType("Accepter", ButtonBar.ButtonData.OK_DONE);
            ButtonType refuserButton = new ButtonType("Refuser", ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "EULA",accepterButton, refuserButton);

            //alert.setTitle("EULA");
            alert.setHeaderText("CONTRAT DE LICENCE D’UTILISATION DE LOGICIEL");
            String contenuEULA = VerificateurDeLicence.texteEULA();

            TextArea textArea = new TextArea(contenuEULA);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);

            alert.getDialogPane().setContent(textArea);

            if(alert.showAndWait().orElse(ButtonType.CANCEL).equals(accepterButton)){
                ecrireFichier(VerificateurDeLicence.EULA_FILE, VerificateurDeLicence.texteEULA());
            }else{
                System.exit(0);
            }
        }
    }

    private void ecrireFichier(String nomFichier, String texte) {
        try {
            Path path = Paths.get(nomFichier);
            Writer writer = Files.newBufferedWriter(path, Charset.defaultCharset(),
                    StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            writer.write(texte);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            messageDErreur("Erreur D'Ecriture");
        }
    }

    private void messageDErreur(String texte) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(texte);
        alert.setContentText(texte);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
