package org.amocer.wall.ui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class TextFieldAvecLimites extends TextField {
    @FXML
    final
    SimpleDoubleProperty limiteInf = new SimpleDoubleProperty(Double.NEGATIVE_INFINITY);

    @FXML
    final
    SimpleDoubleProperty limiteSup = new SimpleDoubleProperty(Double.POSITIVE_INFINITY);

    public TextFieldAvecLimites() {
        this("");
    }

    public TextFieldAvecLimites(String s) {
        super(s);
        textProperty().addListener((observable, oldValue, newValue)->verificationSaisie());
        setOnKeyTyped(e->verificationSaisie());
        limiteSup.addListener((obs,oldValue,newValue)-> setTooltip());
        limiteInf.addListener((obs,oldValue,newValue)-> setTooltip());
        textProperty().addListener((obs,oldValue,newValue)->{
            EventHandler<ActionEvent> onAction = getOnAction();
            if(onAction!=null){
                onAction.handle(null);
            }
        });
    }

    private void setTooltip() {
        boolean limiteInferieurDefini = Double.isFinite(limiteInf.get());
        boolean limiteSuperieurDefini = Double.isFinite(limiteSup.get());
        String texte;
        if(limiteInferieurDefini&&limiteSuperieurDefini){
            texte = "Rentrer des valeurs comprises entre " + limiteInf.get() + " et " + limiteSup.get();
        }else if(limiteInferieurDefini){
            texte = "Rentrer des valeurs superieures à " + limiteInf.get();
        }else if(limiteSuperieurDefini){
            texte = "Rentrer des valeurs inferieures à " + limiteSup.get();
        }else{
            setTooltip(null);
            return;
        }
        Tooltip tooltip = new Tooltip(texte);
        setTooltip(tooltip);
    }

    private void verificationSaisie() {
        etatErreur();
        String contenu = getText();
        try{
            double valeur = Double.parseDouble(contenu);
            if(valeur< limiteInf.get() ||valeur> limiteSup.get()){
                return;
            }
            etatOk();
        }catch (NumberFormatException ignored){}
    }

    private void etatOk() {
        setStyle("-fx-text-fill: darkgreen");
    }

    private void etatErreur() {
        setStyle("-fx-text-fill: red");
    }

    public double getLimiteInf() {
        return limiteInf.get();
    }

    public SimpleDoubleProperty limiteInfProperty() {
        return limiteInf;
    }

    public void setLimiteInf(double limiteInf) {
        this.limiteInf.set(limiteInf);
    }

    public double getLimiteSup() {
        return limiteSup.get();
    }

    public SimpleDoubleProperty limiteSupProperty() {
        return limiteSup;
    }

    public void setLimiteSup(double limiteSup) {
        this.limiteSup.set(limiteSup);
    }
}
