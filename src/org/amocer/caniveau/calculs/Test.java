package org.amocer.caniveau.calculs;

public class Test {
    public static void main(String[] args) {
        Donnee donneeCas1 = new Donnee(
                "",
                "",
                "",
                "3D 80/60GG",
                1800.0,
                TypeCaniveau.REMBAI,
                10.0,
                1.0,
                1.0,
                "REMBLAI",
                0.14,
                0.5,
                10000.0,
                1.0,
                10000.0,
                2.0,
                "ESSEI 3T",
                60000.0,
                350.0);
        Donnee donneeCas3 = new Donnee(
                "",
                "",
                "",
                "3D 80/60GG",
                1800.0,
                TypeCaniveau.OUVERT,
                10.0,
                1.0,
                1.0,
                "REMBLAI",
                0.0,
                0.0,
                10000.0,
                1.0,
                10000.0,
                2.0,
                "ESSEI 3T",
                60000.0,
                0.0);

        Calcul calcul3 = new Calcul(donneeCas3);
        calcul3.poussee_Terre_Paroi(30.0);
        calcul3.poussee_ChargeUniforme_Paroi(30.0);
        calcul3.poussee_ChargePontuelle_Fond();
    }
}
