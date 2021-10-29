package org.amocer.wall.ui;

import org.amocer.wall.calculs.CalculateurDeMursDeSoutainement;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import org.amocer.wall.ndc.DonneesNDC;
import org.amocer.enregistrateurpdf.EnregistrateurDePDFs;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public Label glissementAncrageLabel;
    @FXML
    private TextFieldAvecLimites distanceChargeUniformeTextFieldAvecLimites;

    @FXML
    private TextFieldAvecLimites chargeUniformeTextFieldAvecLimites;

    @FXML
    private TextFieldAvecLimites chargePontuelleTextFieldAvecLimites;

    @FXML
    private TextFieldAvecLimites epaisseurHautRideauTextFieldAvecLimites;

    @FXML
    private TextFieldAvecLimites angleTalusTextFeld;

    @FXML
    private TextFieldAvecLimites hauteurTalusTextFieldAvecLimites;

    @FXML
    private TextFieldAvecLimites distanceChargePontuelleTextFieldAvecLimites;

    @FXML
    private TextFieldAvecLimites epaisseurBasRideauTextFieldAvecLimites;

    @FXML
    private TextFieldAvecLimites largeurMurTextFieldAvecLimites;

    @FXML
    private TextFieldAvecLimites epaisseurTalonTextFieldAvecLimites;

    @FXML
    private TextFieldAvecLimites longueurMurTextFieldAvecLimites;

    @FXML
    private ChoiceBox<String> classeBetonChoiceBox;

    @FXML
    private ChoiceBox<String> typeFibreChoiceBox;

    @FXML
    private TextFieldAvecLimites qsolTextFieldAvecLimites;

    @FXML
    private TextFieldAvecLimites rhoTextFieldAvecLimites;

    @FXML
    private TextFieldAvecLimites phiTextFieldAvecLimites;

    @FXML
    private TextFieldAvecLimites hauteurMurTextFieldAvecLimites;

    @FXML
    private Label etatLabel;

    @FXML
    private Label dosageLabel;

    @FXML
    private Label longeurArmatureRideauLabel;

    @FXML
    private Label tractionAncrageLabel;

    @FXML
    private Label longeurArmatureTalonLabel;

    @FXML
    private Label nombreArmaturesLabel;

    @FXML
    private ChoiceBox<Integer> diametreArmaturesChoiceBox;

    @FXML
    private ChoiceBox<Integer>  nombreAncragesChoiceBox;

    @FXML
    private ChoiceBox<String>  typeAncragesChoiceBox;

    @FXML
    private Button verifierButton;

    @FXML
    private Button imprimerNDCButton;

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
    private ImageView coupeImageView;

    @FXML
    private ImageView vue3dImageView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InputStream coupeImage = VerificateurDeLicence.class.getResourceAsStream("resources/coupe.png");
        coupeImageView.setImage(new Image(coupeImage));
        InputStream vue3dImage = VerificateurDeLicence.class.getResourceAsStream("resources/vue3D.png");
        vue3dImageView.setImage(new Image(vue3dImage));

        nombreAncragesChoiceBox.setDisable(true);


        classeBetonChoiceBox.setItems(FXCollections.observableList(Arrays.asList("C35/45", "C40/50")));
        classeBetonChoiceBox.getSelectionModel().selectFirst();
        typeFibreChoiceBox.setItems(FXCollections.observableList(Arrays.asList("3D 65/35GG", "3D 80/60GG")));
        typeFibreChoiceBox.getSelectionModel().selectFirst();

        diametreArmaturesChoiceBox.setItems(FXCollections.observableList(Arrays.asList(8,10,12,14,16,20)));
        diametreArmaturesChoiceBox.getSelectionModel().selectFirst();
        //diametreArmaturesChoiceBox.getStyleClass().add("center");
        //diametreArmaturesChoiceBox.setStyle( "-fx-alignment:center;");

        typeAncragesChoiceBox.setItems(FXCollections.observableList(Arrays.asList("Non","anti-basculement","anti-glissement")));
        typeAncragesChoiceBox.getSelectionModel().selectFirst();

        nombreAncragesChoiceBox.setItems(FXCollections.observableList(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10)));
        nombreAncragesChoiceBox.getSelectionModel().selectFirst();

        typeAncragesChoiceBox.setOnAction(event -> changerCoupe());

        verifierButton.setOnAction(e -> verifier());

        imprimerNDCButton.setOnAction(e -> imprimerNDC());

        combinaisonsTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(ligne.getValue().combinaison.nom));
        messageTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(ligne.getValue().message));
        etatTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(ligne.getValue().typeDeMessage.toString()));
        pourcentageTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(ligne.getValue().pourcentage()));
    }
    @FXML
    private void changerCoupe() {
        InputStream coupeImage= VerificateurDeLicence.class.getResourceAsStream("resources/coupe.png");
        InputStream coupeImageAvecAncrage = VerificateurDeLicence.class.getResourceAsStream("resources/coupeAvecAncrage.png");
        if (typeAncragesChoiceBox.getValue() != "Non") {
            coupeImageView.setImage(new Image(coupeImageAvecAncrage));
            nombreAncragesChoiceBox.setDisable(false);
        }else {
            coupeImageView.setImage(new Image(coupeImage));
            nombreAncragesChoiceBox.getSelectionModel().selectFirst();
            nombreAncragesChoiceBox.setDisable(true);
        }
    }


    @FXML
    private void valeurValideParLUtilisateur(){
        verifier();
    }

    private void imprimerNDC() {
        //Platform.runLater(()->{
            FileChooser fileChooser = new FileChooser();

            //Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichier PDF (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(imprimerNDCButton.getScene().getWindow());
            EnregistrateurDePDFs.enregistrerPDF(new DonneesNDC(new CalculateurDeMursDeSoutainement(lireDonnees())), new File("xsl/ndcModel.fo"), file, "$$");
           /* EnregistrateurDePDFs.enregistrerPDF(new DonneesNDC(new CalculateurDeMursDeSoutainement(lireDonnees())), Controller.class.getResourceAsStream("xsl/ndcModel.fo"), file, "$$");*/
        //});
    }

    private void verifier() {
        resultatsTableView.getItems().clear();
        etatLabel.setText("");
        Background blankBackground = new Background(new BackgroundFill(Paint.valueOf("transparent"),null,null));
        etatLabel.setBackground(blankBackground);

        dosageLabel.setText("");
        dosageLabel.setBackground(blankBackground);

        //verification et lecture des donnees
        CalculateurDeMursDeSoutainement.Donnees donnees = lireDonnees();

        //calcul des resultats
        CalculateurDeMursDeSoutainement calculateurDeMursDeSoutainement = new CalculateurDeMursDeSoutainement(donnees);
        List<CalculateurDeMursDeSoutainement.ResultatDuCalcul> resultats = calculateurDeMursDeSoutainement.calculer();

        //Affichage des resultats
        resultatsTableView.setItems(FXCollections.observableList(resultats));
        boolean etatEstOk = resultats.stream().noneMatch(r->r.typeDeMessage.equals(CalculateurDeMursDeSoutainement.ResultatDuCalcul.TypeDeMessage.ERREUR));
        etatLabel.setText(etatEstOk?"OK":"NON");
        Background okBackground = new Background(new BackgroundFill(Paint.valueOf("green"),null,null));
        Background nonBackground = new Background(new BackgroundFill(Paint.valueOf("red"),null,null));
        etatLabel.setBackground(etatEstOk?okBackground:nonBackground);

        dosageLabel.setText(etatEstOk?calculateurDeMursDeSoutainement.resultat().dosage +" kg/m³":"");

        DecimalFormat df1 = new DecimalFormat("0");
        double nombreArmatures = calculateurDeMursDeSoutainement.resultat().nombreDeBarres;
        nombreArmaturesLabel.setText(etatEstOk&&nombreArmatures>0?df1.format(nombreArmatures):"");

        DecimalFormat df = new DecimalFormat("0.00");
        double tractionAncrage = calculateurDeMursDeSoutainement.calculerTractionAncrage();
        tractionAncrageLabel.setText(df.format(tractionAncrage) + "KN");

        double glissementAncrage = calculateurDeMursDeSoutainement.calculerGlissementAncrage();
        glissementAncrageLabel.setText(df.format(glissementAncrage) + "KN");

        double longueurArmatureRideau = calculateurDeMursDeSoutainement.longueurArmatureRideau();
        longeurArmatureRideauLabel.setText(etatEstOk&&longueurArmatureRideau>0?df.format(longueurArmatureRideau):"");

        double longueurArmatureTalon = calculateurDeMursDeSoutainement.longueurArmatureTalon();
        longeurArmatureTalonLabel.setText(etatEstOk&&longueurArmatureTalon>0?df.format(longueurArmatureTalon):"");

        diametreArmaturesChoiceBox.valueProperty().addListener((obs,oldValue,newValue)->{
            verifier();
        });

        typeFibreChoiceBox.valueProperty().addListener((obs,oldValue,newValue)->{
            verifier();
        });

        classeBetonChoiceBox.valueProperty().addListener((obs,oldValue,newValue)->{
            verifier();
        });

        nombreAncragesChoiceBox.valueProperty().addListener((obs,oldValue,newValue)->{
            verifier();
        });

        typeAncragesChoiceBox.valueProperty().addListener((obs,oldValue,newValue)->{
            verifier();
        });
    }

    private CalculateurDeMursDeSoutainement.Donnees lireDonnees() {
        return new CalculateurDeMursDeSoutainement.Donnees(
                Double.parseDouble(distanceChargeUniformeTextFieldAvecLimites.getText()),
                Double.parseDouble(chargeUniformeTextFieldAvecLimites.getText()),
                Double.parseDouble(chargePontuelleTextFieldAvecLimites.getText()),
                Double.parseDouble(epaisseurHautRideauTextFieldAvecLimites.getText()),
                Double.parseDouble(angleTalusTextFeld.getText()),
                Double.parseDouble(hauteurTalusTextFieldAvecLimites.getText()),
                Double.parseDouble(distanceChargePontuelleTextFieldAvecLimites.getText()),
                Double.parseDouble(epaisseurBasRideauTextFieldAvecLimites.getText()),
                Double.parseDouble(largeurMurTextFieldAvecLimites.getText()),
                Double.parseDouble(epaisseurTalonTextFieldAvecLimites.getText()),
                Double.parseDouble(longueurMurTextFieldAvecLimites.getText()),
                classeBetonChoiceBox.getValue(),
                typeFibreChoiceBox.getValue(),
                Double.parseDouble(qsolTextFieldAvecLimites.getText()),
                Double.parseDouble(rhoTextFieldAvecLimites.getText()),
                Double.parseDouble(phiTextFieldAvecLimites.getText()),
                Double.parseDouble(hauteurMurTextFieldAvecLimites.getText()),
                diametreArmaturesChoiceBox.getValue(),
                typeAncragesChoiceBox.getValue(),
                nombreAncragesChoiceBox.getValue());
    }
}
