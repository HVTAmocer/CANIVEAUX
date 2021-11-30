package org.amocer.caniveau.ndc;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import org.amocer.caniveau.calculs.Calcul;
import org.amocer.caniveau.calculs.Donnee;

import java.util.List;
import java.util.stream.Collectors;


@XmlRootElement(name = "DonneesNDC")

public class DonneesNDC2 {

    private List<Calcul.ResultatDuCalcul> resultats;

    @XmlElementWrapper(name = "resultats")
    @XmlElement(name = "resultat")
    public List<Calcul.ResultatDuCalcul> getResultats(){
        return resultats;
    }
    public void setResultats(List<Calcul.ResultatDuCalcul> resultats){
        this.resultats = resultats;
    }
}
