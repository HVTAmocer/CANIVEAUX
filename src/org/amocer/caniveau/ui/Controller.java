package org.amocer.caniveau.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.amocer.caniveau.calculs.CalculateurDeMursDeSoutainement;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.amocer.caniveau.calculs.Donnee;
import org.amocer.caniveau.calculs.TypeCaniveau;
import org.amocer.caniveau.ndc.DonneesNDC;
import org.amocer.caniveau.ndc.EnregistrateurDePDFs;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public TextField chantierTextField;
    @FXML
    public TextField clientTextField;
    @FXML
    public TextField localisationTextField;
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
    private Label resistanceSolLabel;
    @FXML
    private Label angleFrottementLabel;
    @FXML
    private Label resistanceBetonAuLevageLabel;


    @FXML
    private TableView<CalculateurDeMursDeSoutainement.ResultatDuCalcul> resultatsTableView;

    @FXML
    private TableColumn<CalculateurDeMursDeSoutainement.ResultatDuCalcul, String> combinaisonsTableColumn;

    @FXML
    private TableColumn<CalculateurDeMursDeSoutainement.ResultatDuCalcul, String> messageTableColumn;

    @FXML
    private TableColumn<CalculateurDeMursDeSoutainement.ResultatDuCalcul, String> etatTableColumn;

    @FXML
    private TableColumn<CalculateurDeMursDeSoutainement.ResultatDuCalcul, String> pourcentageTableColumn;

    @FXML
    private TextField poidsVolumiqueSolTextField;
    @FXML
    private ImageView coupeImageView;

    @FXML
    private ImageView coupe3DImageView;

    @FXML
    private ChoiceBox<String> typeCouvercleChoiceBox;

    @FXML
    private TextField epaisseurCouvercleTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InputStream coupeImage = VerificateurDeLicence.class.getResourceAsStream("resources/1.png");
        coupeImageView.setImage(new Image(coupeImage));
        InputStream coupe3DImage = VerificateurDeLicence.class.getResourceAsStream("resources/1.png");
        coupe3DImageView.setImage(new Image(coupe3DImage));

        typeCaniveauChoiceBox.setItems(FXCollections.observableList(Arrays.asList(TypeCaniveau.REMBAI,TypeCaniveau.COUVERCLE,TypeCaniveau.OUVERT)));
        typeCaniveauChoiceBox.getSelectionModel().selectFirst();
        typeCaniveauChoiceBox.setOnAction(event -> changerImage());

        typeFibreChoiceBox.setItems(FXCollections.observableList(Arrays.asList("3D 80/60GG")));
        typeFibreChoiceBox.getSelectionModel().selectFirst();

        typeCouvercleChoiceBox.setItems(FXCollections.observableList(Arrays.asList("DALLE BA","CAILLEBOTIS","NON")));
        typeCouvercleChoiceBox.getSelectionModel().selectFirst();

        typeChargeRoulanteChoiceBox.setItems(FXCollections.observableList(Arrays.asList("Piéton","3T par essieu ","6T par essieu","13T par essieu")));
        typeChargeRoulanteChoiceBox.getSelectionModel().selectFirst();

        verifierButton.setOnAction(e -> verifier());
        imprimerNDCButton.setOnAction(e -> imprimerNDC());

        combinaisonsTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(ligne.getValue().combinaison.nom));
        messageTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(ligne.getValue().message));
        etatTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(ligne.getValue().typeDeMessage.toString()));
        pourcentageTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(ligne.getValue().pourcentage()));
    }

    private void changerImage() {
        if (typeCaniveauChoiceBox.getValue().equals(TypeCaniveau.REMBAI)) {
            InputStream coupeImage = VerificateurDeLicence.class.getResourceAsStream("resources/1.png");
            coupeImageView.setImage(new Image(coupeImage));
        }else if (typeCaniveauChoiceBox.getValue().equals(TypeCaniveau.COUVERCLE)){
            InputStream coupeImage = VerificateurDeLicence.class.getResourceAsStream("resources/2.png");
            coupeImageView.setImage(new Image(coupeImage));
            hauteurRemblaiTextField.setText(String.valueOf(0));
            hauteurRemblaiTextField.setDisable(true);
        }else {
            InputStream coupeImage = VerificateurDeLicence.class.getResourceAsStream("resources/3.png");
            coupeImageView.setImage(new Image(coupeImage));
            typeCouvercleChoiceBox.setValue("NON");
            typeCouvercleChoiceBox.setDisable(true);
            hauteurRemblaiTextField.setText(String.valueOf(0));
            hauteurRemblaiTextField.setDisable(true);
            epaisseurCouvercleTextField.setText(String.valueOf(0));
            epaisseurCouvercleTextField.setDisable(true);
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
    }

    private Donnee lireDonnees() {
        return new Donnee(
                chantierTextField.getText(),
                localisationTextField.getText(),
                clientTextField.getText(),
                typeFibreChoiceBox.getValue(),
                Double.parseDouble(poidsVolumiqueSolTextField.getText()),
                typeCaniveauChoiceBox.getValue(),
                Double.parseDouble(longeurTextField.getText()),
                Double.parseDouble(largeurTextField.getText()),
                Double.parseDouble(hauteurTextField.getText()),
                typeCouvercleChoiceBox.getValue(),
                Double.parseDouble(epaisseurCouvercleTextField.getText()),
                Double.parseDouble(hauteurRemblaiTextField.getText()),
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
