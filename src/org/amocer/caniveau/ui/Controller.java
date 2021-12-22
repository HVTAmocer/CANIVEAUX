package org.amocer.caniveau.ui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BackgroundFill;
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
import org.amocer.caniveau.ndc.DonneesNDC;
import org.amocer.caniveau.ndc.DonneesNDC2;
import org.amocer.caniveau.ndc.EnregistrateurDePDFs;
import org.amocer.caniveau.ndc.PDFHandler;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    private TextField poidsCouvercleTextField;
    @FXML
    private TextField hauteurRemblaiTextField;
    @FXML
    private TextField angleFrottementTextField;
    @FXML
    private TextField epaisseurParoiChoisieTextField;
    @FXML
    private TextField epaisseurFondChoisieTextField;
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
    private TableColumn<Calcul.ResultatDuCalcul, String> joursPourLevageColumn;

    @FXML
    private TableColumn<Calcul.ResultatDuCalcul, String> messageColumn;

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

        typeCaniveauChoiceBox.setItems(FXCollections.observableList(Arrays.asList(TypeCaniveau.REMBLAI,TypeCaniveau.COUVERCLE,TypeCaniveau.OUVERT)));
        typeCaniveauChoiceBox.getSelectionModel().selectFirst();
        typeCaniveauChoiceBox.setOnAction(event -> changerImage());

        typeFibreChoiceBox.setItems(FXCollections.observableList(Collections.singletonList("3D 80/60GG")));
        typeFibreChoiceBox.getSelectionModel().selectFirst();

        typeCouvercleChoiceBox.setItems(FXCollections.observableList(Arrays.asList(TypeCouvercle.DALLE_BA,TypeCouvercle.CAILLEBOTIS)));
        typeCouvercleChoiceBox.getSelectionModel().selectFirst();
        typeCouvercleChoiceBox.setOnAction(e -> desactiverCouvercle());

        typeChargeRoulanteChoiceBox.setItems(FXCollections.observableList(Arrays.asList(
                TypeChargeRoulante.ESSIEU_3T.type,
                TypeChargeRoulante.ESSIEU_6T.type,
                TypeChargeRoulante.ESSIEU_13T.type,
                TypeChargeRoulante.PIETON.type,
                TypeChargeRoulante.NON.type)));
        typeChargeRoulanteChoiceBox.getSelectionModel().selectFirst();

        verifierButton.setOnAction(e -> verifier());
        imprimerNDCButton.setOnAction(e -> imprimerNDC());
/*        imprimerNDCButton.setOnAction(e->exportNDC(imprimerNDCButton));*/
    }

    private void desactiverCouvercle() {
        if (typeCouvercleChoiceBox.getValue().equals(TypeCouvercle.CAILLEBOTIS)) {
            epaisseurCouvercleTextField.setText(String.valueOf(0));
            epaisseurCouvercleTextField.setDisable(true);
        }else {
            epaisseurCouvercleTextField.setText(String.valueOf(14));
            epaisseurCouvercleTextField.setDisable(false);
        }
    }

    private void changerImage() {
        if (typeCaniveauChoiceBox.getValue().equals(TypeCaniveau.REMBLAI)) {
            InputStream coupeImage = VerificateurDeLicence.class.getResourceAsStream("resources/1.png");
            coupe3DImageView.setImage(new Image(coupeImage));
            typeCouvercleChoiceBox.setDisable(false);
            typeCouvercleChoiceBox.getSelectionModel().selectFirst();
            epaisseurCouvercleTextField.setDisable(false);
            hauteurRemblaiTextField.setDisable(false);
            poidsCouvercleTextField.setDisable(false);
            typeChargeRoulanteChoiceBox.setDisable(false);
            typeChargeRoulanteChoiceBox.getSelectionModel().selectFirst();
        }else if (typeCaniveauChoiceBox.getValue().equals(TypeCaniveau.COUVERCLE)){
            InputStream coupeImage = VerificateurDeLicence.class.getResourceAsStream("resources/2.png");
            coupe3DImageView.setImage(new Image(coupeImage));
            typeCouvercleChoiceBox.setDisable(false);
            typeCouvercleChoiceBox.getSelectionModel().selectFirst();
            epaisseurCouvercleTextField.setDisable(false);
            hauteurRemblaiTextField.setText(String.valueOf(0));
            hauteurRemblaiTextField.setDisable(true);
            poidsCouvercleTextField.setDisable(false);
            typeChargeRoulanteChoiceBox.setDisable(false);
            typeChargeRoulanteChoiceBox.getSelectionModel().selectFirst();
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
        }
    }

    @FXML
    private void valeurValideParLUtilisateur(){
        verifier();
    }

    private void imprimerNDC() {
        Platform.runLater(()->{
            FileChooser fileChooser = new FileChooser();
            // Il faut choisir une ligne du tableau
            Calcul.ResultatDuCalcul resultatDuCalcul = resultatsTableView.getSelectionModel().getSelectedItem();

            if(resultatDuCalcul != null) {
                // creer le plan de ferraillage
                copierImage(resultatDuCalcul);

                //Set extension filter for text files
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichier PDF (*.pdf)", "*.pdf");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showSaveDialog(imprimerNDCButton.getScene().getWindow());
                EnregistrateurDePDFs.enregistrerPDF(new DonneesNDC(resultatDuCalcul), new File("xsl/ndcModel.fo"), file, "$$");
            }
        });
    }

    private void copierImage(Calcul.ResultatDuCalcul resultatDuCalcul)  {
        try {
            File dest = new File("xsl/schemaFerraillageChoisi.png");
            if (resultatDuCalcul.epaisseurMinParoi <= 10) {
                File source = new File("xsl/schemaFerraillage1.png");
                FileUtils.copyFile(source, dest);
            } else {
                File source = new File("xsl/schemaFerraillage2.png");
                FileUtils.copyFile(source, dest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportNDC(Button button) {
        //verifier();

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichier PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File pdfFile = fileChooser.showSaveDialog(button.getScene().getWindow());

        Donnee donnee = lireDonnees();
        Calcul calcul= new Calcul(donnee);
        List<Calcul.ResultatDuCalcul> resultats = calcul.calculer();
        DonneesNDC2 data = new DonneesNDC2();
        data.setResultats(resultats);

        PDFHandler handler = new PDFHandler();

        try {
            ByteArrayOutputStream streamSource = handler.getXMLSource(data);
            handler.createPDFFile(pdfFile, streamSource,new File("xsl/template.xsl"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verifier() {
        resultatsTableView.getItems().clear();

        // Lire des donnees
        Donnee donnee = lireDonnees();
        Calcul calcul = new Calcul(donnee);

        //calcul des resultats
        List<Calcul.ResultatDuCalcul> resultats = calcul.calculer().stream()
                .sorted(Comparator.comparing(r->r.typeResultat))
                .collect(Collectors.toList());

        //Affichage des resultats
        resultatsTableView.setItems(FXCollections.observableList(resultats));

        typeBetonTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(ligne.getValue().typeBeton));
        dossageTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(ligne.getValue().dossage)));
        renfortMinTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(ligne.getValue().renfortMini)));
        epaisseurMinParoiTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(ligne.getValue().epaisseurMinParoi)));
        epaisseurMinFondTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(ligne.getValue().epaisseurMinFond)));
        volumeBetonTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(new DecimalFormat("#.##").format(ligne.getValue().volumeBeton))));
        poidsArmaturesTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(new DecimalFormat("#.##").format(ligne.getValue().poidsArmatures))));
        poidsFibreTableColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(new DecimalFormat("#.##").format(ligne.getValue().poidsFibre))));
        resistanceMinSolColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(new DecimalFormat("#.##").format(ligne.getValue().resistanceMinSol))));
        joursPourLevageColumn.setCellValueFactory(ligne -> new SimpleStringProperty(String.valueOf(ligne.getValue().joursPourLevage)));
        messageColumn.setCellValueFactory(ligne -> new SimpleStringProperty(ligne.getValue().message));
        messageColumn.setCellFactory(e -> new TableCell<Calcul.ResultatDuCalcul, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == Calcul.Message.OK.toString()) {
                    this.setText("OK");
                    this.setStyle("-fx-text-fill: green; -fx-font-size: 14px;-fx-font-weight: bold;");
                    //this.setStyle("-fx-background-color: green;");
                } else if (item == Calcul.Message.PasOK.toString()) {
                    this.setText("NON");
                    this.setStyle("-fx-text-fill: red; -fx-font-size: 14px;-fx-font-weight: bold;");
                    //this.setStyle("-fx-background-color: red;");
                }
            }
        });
        resultatsTableView.setRowFactory(row -> new TableRow<Calcul.ResultatDuCalcul>() {
            @Override
            public void updateItem(Calcul.ResultatDuCalcul item, boolean empty) {
                super.updateItem(item, empty);
/*                Calcul.ResultatDuCalcul selectedRow = resultatsTableView.getSelectionModel().getSelectedItem();
                if(item!=null&&item.equals(selectedRow)) {
                    setStyle("");
                    resultatsTableView.refresh();
                    return;
                }*/
                if (item == null || empty) {
                    setText(null);
                }else if (item.typeResultat.equals(Calcul.TypeResultat.EPAISSEUR_UTILISATEUR)) {
                    this.setStyle("-fx-background-color: lightgreen;");
                    //this.setStyle("-fx-background-color: lightgreen; -fx-selection-bar: red;-fx-selection-bar-text : black;");
                }else if (item.typeResultat.equals(Calcul.TypeResultat.EPAISSEURS_AUTO)) {
                    setStyle("-fx-background-color: orange");
                } else if (item.typeResultat.equals(Calcul.TypeResultat.EPAISSEUR_MINI)) {
                    setStyle("-fx-background-color: lightgrey");
                }
            }
        });
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
                Double.parseDouble(epaisseurParoiChoisieTextField.getText()),
                Double.parseDouble(epaisseurFondChoisieTextField.getText()),
                Double.parseDouble(chargeUniformeTextField.getText()),
                Double.parseDouble(distanceChargeUniformeTextField.getText()),
                Double.parseDouble(chargePontuelleTextField.getText()),
                Double.parseDouble(distanceChargePontuelleTextField.getText()),
                typeChargeRoulanteChoiceBox.getValue(),
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

    @FXML
    private void quitterHandler(){
        Platform.exit();
    }

    @FXML
    private void ouvrirHandler(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selectionner Fichier...");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DAT file","*.dat"));
        //fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showOpenDialog(resultatsTableView.getScene().getWindow());
        //File file = new File("export.dat");
        try {
            List<String> importedData = Files.readAllLines(file.toPath());
            for(String ligne:importedData){
                String[] contenuDeLaLigne = ligne.split("\t");
                if(contenuDeLaLigne.length>0){
                    switch (contenuDeLaLigne[0]){
                        case "DONNEES":
                            chantierTextField.setText(contenuDeLaLigne[1]);
                            localisationTextField.setText(contenuDeLaLigne[2]);
                            departementTextField.setText(contenuDeLaLigne[3]);
                            responsableTextField.setText(contenuDeLaLigne[4]);
                            numeroAffaireTextField.setText(contenuDeLaLigne[5]);
                            dateTextField.setText(contenuDeLaLigne[6]);
                            contexteTextArea.setText(contenuDeLaLigne[7]);
                            typeFibreChoiceBox.setValue(contenuDeLaLigne[8]);
                            poidsVolumiqueSolTextField.setText(contenuDeLaLigne[9]);
                            typeCaniveauChoiceBox.setValue(TypeCaniveau.valueOf(contenuDeLaLigne[10]));
                            largeurTextField.setText(contenuDeLaLigne[11]);
                            longeurTextField.setText(contenuDeLaLigne[12]);
                            hauteurTextField.setText(contenuDeLaLigne[13]);
                            typeCouvercleChoiceBox.setValue(TypeCouvercle.valueOf(contenuDeLaLigne[14]));
                            epaisseurCouvercleTextField.setText(contenuDeLaLigne[15]);
                            hauteurRemblaiTextField.setText(contenuDeLaLigne[16]);
                            angleFrottementTextField.setText(contenuDeLaLigne[17]);
                            epaisseurParoiChoisieTextField.setText(contenuDeLaLigne[18]);
                            epaisseurFondChoisieTextField.setText(contenuDeLaLigne[19]);
                            chargeUniformeTextField.setText(contenuDeLaLigne[20]);
                            distanceChargeUniformeTextField.setText(contenuDeLaLigne[21]);
                            chargePontuelleTextField.setText(contenuDeLaLigne[22]);
                            distanceChargePontuelleTextField.setText(contenuDeLaLigne[23]);
                            typeChargeRoulanteChoiceBox.setValue(contenuDeLaLigne[24]);
                            poidsCouvercleTextField.setText(contenuDeLaLigne[25]);

                            break;
                        default:
                            throw new IllegalStateException("Erreur de chargement du fichier : "+ligne);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void enregistrerHandler() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT files (*.dat)", "*.dat");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File fichier = fileChooser.showSaveDialog(resultatsTableView.getScene().getWindow());
        if (fichier != null) {
            List<String> export = new LinkedList<>();
            Donnee donnee = lireDonnees();
            export.add(String.join("\t", "DONNEES",
                    donnee.chantier,
                    donnee.localisation,
                    donnee.departement,
                    donnee.responsableProjet,
                    donnee.numeroAffaire,
                    donnee.dateRealisation,
                    donnee.contexteProjet,
                    donnee.nomFibre,
                    String.valueOf(donnee.poidsVolumiqueSol),
                    String.valueOf(donnee.typeCaniveau),
                    String.valueOf(donnee.largeur),
                    String.valueOf(donnee.longueur),
                    String.valueOf(donnee.hauteur),
                    String.valueOf(donnee.typeCouvercle),
                    String.valueOf(donnee.epaisseurCouvercle),
                    String.valueOf(donnee.hauteurRemblai),
                    String.valueOf(donnee.angleFrottement),
                    String.valueOf(donnee.epaisseurParoiChoisie),
                    String.valueOf(donnee.epaisseurFondChoisie),
                    String.valueOf(donnee.chargeUniforme),
                    String.valueOf(donnee.distanceChargeUniforme),
                    String.valueOf(donnee.chargePontuelle),
                    String.valueOf(donnee.distanceChargePontuelle),
                    String.valueOf(donnee.typeChargeRoulante),
                    String.valueOf(donnee.poidsCouvercle)));
            try {
                Files.write(fichier.toPath(), export, Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
