package org.amocer.caniveau.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.amocer.caniveau.calculs.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.amocer.caniveau.ndc.EnregistrateurDePDFs;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public TextField chantierTextField;
    @FXML
    public TextField departementTextField;
    @FXML
    public TextField localisationTextField;
    @FXML
    public TextField responsableTextField;
    @FXML
    public TextField numeroAffaireTextField;
    @FXML
    public TextField dateTextField;
    @FXML
    public TextArea contexteTextArea;
    @FXML
    private ChoiceBox<String> typeFibreChoiceBox;
    @FXML
    private Button verifierButton;
    @FXML
    private Button imprimerNDCButton;

    @FXML
    private ChoiceBox<TypeCaniveau> typeCaniveauChoiceBox;
    @FXML
    private TextField longeurTextField;
    @FXML
    private TextField hauteurTextField;
    @FXML
    private TextField largeurTextField;
    @FXML
    private TextField chargePontuelleTextField;
    @FXML
    private TextField distanceChargePontuelleTextField;
    @FXML
    private TextField chargeUniformeTextField;
    @FXML
    private TextField distanceChargeUniformeTextField;
    @FXML
    private ChoiceBox<String> typeChargeRoulanteChoiceBox;
    @FXML
    private TextField chargeRoulantTextField;
    @FXML
    private TextField poidsCouvercleTextField;
    @FXML
    private TextField hauteurRemblaiTextField;
    @FXML
    private TextField angleFrottementTextField;

    @FXML
    private TableView<Calcul.ResultatDuCalcul> resultatsTableView;

    @FXML
    private TableColumn<Calcul.ResultatDuCalcul, String> typeBetonTableColumn;

    @FXML
    private TableColumn<Calcul.ResultatDuCalcul, String> dossageTableColumn;

    @FXML
    private TableColumn<Calcul.ResultatDuCalcul, String> renfortMinTableColumn;

    @FXML
    private TableColumn<Calcul.ResultatDuCalcul, String> epaisseurMinParoiTableColumn;

    @FXML
    private TableColumn<Calcul.ResultatDuCalcul, String> epaisseurMinFondTableColumn;

    @FXML
    private TableColumn<Calcul.ResultatDuCalcul, String> volumeBetonTableColumn;

    @FXML
    private TableColumn<Calcul.ResultatDuCalcul, String> poidsArmaturesTableColumn;

    @FXML
    private TableColumn<Calcul.ResultatDuCalcul, String> poidsFibreTableColumn;

    @FXML
    private TableColumn<Calcul.ResultatDuCalcul, String> resistanceMinSolColumn;

    @FXML
    private TableColumn<Calcul.ResultatDuCalcul, String> resistanceBetonLevageColumn;

    @FXML
    private TextField poidsVolumiqueSolTextField;
    @FXML
    private ImageView coupe3DImageView;

    @FXML
    private ChoiceBox<TypeCouvercle> typeCouvercleChoiceBox;

    @FXML
    private TextField epaisseurCouvercleTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InputStream coupeImage = VerificateurDeLicence.class.getResourceAsStream("resources/1.png");
        coupe3DImageView.setImage(new Image(coupeImage));

        typeCaniveauChoiceBox.setItems(FXCollections.observableList(Arrays.asList(TypeCaniveau.REMBAI,TypeCaniveau.COUVERCLE,TypeCaniveau.OUVERT)));
        typeCaniveauChoiceBox.getSelectionModel().selectFirst();
        typeCaniveauChoiceBox.setOnAction(event -> changerImage());

        typeFibreChoiceBox.setItems(FXCollections.observableList(Arrays.asList("3D 80/60GG")));
        typeFibreChoiceBox.getSelectionModel().selectFirst();

        typeCouvercleChoiceBox.setItems(FXCollections.observableList(Arrays.asList(TypeCouvercle.DALLE_BA,TypeCouvercle.CAILLEBOTIS)));
        typeCouvercleChoiceBox.getSelectionModel().selectFirst();

        typeChargeRoulanteChoiceBox.setItems(FXCollections.observableList(Arrays.asList(
                TypeChargeRoulante.ESSIEU_3T.type,
                TypeChargeRoulante.ESSIEU_6T.type,
                TypeChargeRoulante.ESSIEU_13T.type,
                TypeChargeRoulante.PIETON.type)));
        typeChargeRoulanteChoiceBox.getSelectionModel().selectFirst();

        verifierButton.setOnAction(e -> verifier());
        imprimerNDCButton.setOnAction(e -> imprimerNDC());
    }

    private void changerImage() {
        if (typeCaniveauChoiceBox.getValue().equals(TypeCaniveau.REMBAI)) {
            InputStream coupeImage = VerificateurDeLicence.class.getResourceAsStream("resources/1.png");
            coupe3DImageView.setImage(new Image(coupeImage));
            typeCouvercleChoiceBox.setDisable(false);
            typeCouvercleChoiceBox.getSelectionModel().selectFirst();
            hauteurRemblaiTextField.setDisable(false);
            if (typeCouvercleChoiceBox.getValue().equals(TypeCouvercle.CAILLEBOTIS)) {
                epaisseurCouvercleTextField.setText(String.valueOf(0));
                epaisseurCouvercleTextField.setDisable(true);
            }
            poidsCouvercleTextField.setDisable(false);
            typeChargeRoulanteChoiceBox.setDisable(false);
            typeChargeRoulanteChoiceBox.getSelectionModel().selectFirst();
            chargeRoulantTextField.setDisable(false);
        }else if (typeCaniveauChoiceBox.getValue().equals(TypeCaniveau.COUVERCLE)){
            InputStream coupeImage = VerificateurDeLicence.class.getResourceAsStream("resources/2.png");
            coupe3DImageView.setImage(new Image(coupeImage));
            typeCouvercleChoiceBox.setDisable(false);
            typeCouvercleChoiceBox.getSelectionModel().selectFirst();
            epaisseurCouvercleTextField.setDisable(false);
            if (typeCouvercleChoiceBox.getValue().equals(TypeCouvercle.CAILLEBOTIS)) {
                epaisseurCouvercleTextField.setText(String.valueOf(0));
                epaisseurCouvercleTextField.setDisable(true);
            }
            hauteurRemblaiTextField.setText(String.valueOf(0));
            hauteurRemblaiTextField.setDisable(true);
            poidsCouvercleTextField.setDisable(false);
            typeChargeRoulanteChoiceBox.setDisable(false);
            typeChargeRoulanteChoiceBox.getSelectionModel().selectFirst();
            chargeRoulantTextField.setDisable(false);
        }else {
            InputStream coupeImage = VerificateurDeLicence.class.getResourceAsStream("resources/3.png");
            coupe3DImageView.setImage(new Image(coupeImage));
            typeCouvercleChoiceBox.setValue(TypeCouvercle.NON);
            typeCouvercleChoiceBox.setDisable(true);
            hauteurRemblaiTextField.setText(String.valueOf(0));
            hauteurRemblaiTextField.setDisable(true);
            epaisseurCouvercleTextField.setText(String.valueOf(0));
            epaisseurCouvercleTextField.setDisable(true);
            poidsCouvercleTextField.setText(String.valueOf(0));
            poidsCouvercleTextField.setDisable(true);
            typeChargeRoulanteChoiceBox.setValue(TypeChargeRoulante.NON.type);
            typeChargeRoulanteChoiceBox.setDisable(true);
            chargeRoulantTextField.setText(String.valueOf(0));
            chargeRoulantTextField.setDisable(true);
        }
    }

    @FXML
    private void valeurValideParLUtilisateur(){
        verifier();
    }

    private void imprimerNDC() {
        Platform.runLater(()->{
            FileChooser fileChooser = new FileChooser();

            //Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichier PDF (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(imprimerNDCButton.getScene().getWindow());
            EnregistrateurDePDFs.enregistrerPDF(lireDonnees(), new File("xsl/ndcModel.fo"), file, "$$");
        });
    }

    private void verifier() {
        resultatsTableView.getItems().clear();

        // Lire des donnees
        Donnee donnee = lireDonnees();
        Calcul calcul= new Calcul(donnee);

        //calcul des resultats
        List<Calcul.ResultatDuCalcul> resultats = calcul.calculer();

        //Affichage des resultats
        resultatsTableView.setItems(FXCollections.observableList(resultats));

        typeBetonTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(ligne.getValue().typeBeton));
        dossageTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(ligne.getValue().dossage)));
        renfortMinTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(ligne.getValue().renfortMini)));
        epaisseurMinParoiTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(ligne.getValue().epaisseurMinParoi)));
        epaisseurMinFondTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(ligne.getValue().epaisseurMinFond)));
        volumeBetonTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(ligne.getValue().volumeBeton)));
        poidsArmaturesTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(ligne.getValue().poidsArmatures)));
        poidsFibreTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(ligne.getValue().poidsFibre)));
        resistanceMinSolColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(ligne.getValue().resistanceMinSol)));
        resistanceBetonLevageColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(ligne.getValue().resistanceBetonLevage)));
    }

    private Donnee lireDonnees() {
        return new Donnee(
                chantierTextField.getText(),
                localisationTextField.getText(),
                departementTextField.getText(),
                responsableTextField.getText(),
                numeroAffaireTextField.getText(),
                dateTextField.getText(),
                contexteTextArea.getText(),

                typeFibreChoiceBox.getValue(),
                Double.parseDouble(poidsVolumiqueSolTextField.getText()),
                typeCaniveauChoiceBox.getValue(),
                Double.parseDouble(longeurTextField.getText()),
                Double.parseDouble(largeurTextField.getText()),
                Double.parseDouble(hauteurTextField.getText()),
                typeCouvercleChoiceBox.getValue(),
                Double.parseDouble(epaisseurCouvercleTextField.getText()),
                Double.parseDouble(hauteurRemblaiTextField.getText()),
                Double.parseDouble(angleFrottementTextField.getText()),
                Double.parseDouble(chargeUniformeTextField.getText()),
                Double.parseDouble(distanceChargeUniformeTextField.getText()),
                Double.parseDouble(chargePontuelleTextField.getText()),
                Double.parseDouble(distanceChargePontuelleTextField.getText()),
                typeChargeRoulanteChoiceBox.getValue(),
                Double.parseDouble(chargeRoulantTextField.getText()),
                Double.parseDouble(poidsCouvercleTextField.getText()));
    }

    @FXML
    public void aboutHandler(ActionEvent event) {
        try {
            Stage aboutStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about.fxml"));
            fxmlLoader.setController(this);
            Parent root = (Parent) fxmlLoader.load();
            Scene scene =  new Scene(root);
            aboutStage.setScene(scene);
            aboutStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
