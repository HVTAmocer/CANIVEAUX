package org.amocer.caniveau.calculs;

public class EffortResistant {

    public static double momentResistantELUBetonFibre(double epsCu,
                                                      double fcd, double fR1d, double fR3d,
                                                      double hauteurSection, double largeurSection, double sectionAcier) {
        fcd = fcd*Math.pow(10,6);
        fR1d = fR1d*Math.pow(10,6);
        fR3d = fR3d*Math.pow(10,6);

        double B = largeurSection;
        double h= hauteurSection;
        // double facteurDOrientation = 1.0;
        double facteurDOrientation = Math.min(0.5 + 0.5 / 1.5 * hauteurSection * largeurSection,0.9);

        double fyd = 500.0 / 1.15 * Math.pow(10,6);

        sectionAcier /=10000.0;

        double b_var =  ( ( (266240000.0 * B * epsCu * fR3d + 262144000.0 * B * epsCu * fR1d) * facteurDOrientation * Math.pow(fcd, 2.0) + (15360000.0 * B * epsCu * Math.pow(fR3d, 2.0) + 56832000.0 * B * epsCu * fR1d * fR3d + 52569600.0 * B * epsCu * Math.pow(fR1d, 2.0)) * Math.pow(facteurDOrientation, 2.0) * fcd) * h +  (307200000.0 * sectionAcier * epsCu * fR3d + 568320000.0 * sectionAcier * epsCu * fR1d) * facteurDOrientation * fcd * fyd +  (640000.0 * B * fR3d + 4784000.0 * B * fR1d) * facteurDOrientation * Math.pow(fcd, 2.0) +  (-304000.0 * B * Math.pow(fR3d, 2.0) - 1484800.0 * B * fR1d * fR3d - 1706440.0 * B * Math.pow(fR1d, 2.0)) * Math.pow(facteurDOrientation, 2.0) * fcd + (24000.0 * B * Math.pow(fR3d, 3.0) + 133200.0 * B * fR1d * Math.pow(fR3d, 2.0) + 246420.0 * B * Math.pow(fR1d, 2.0) * fR3d + 151959.0 * B * Math.pow(fR1d, 3.0)) * Math.pow(facteurDOrientation, 3.0));
        double c_var = 163840000.0 * Math.pow(B, 2.0) * Math.pow(epsCu, 2.0) * Math.pow(fcd, 2.0) * Math.pow(h, 2.0) + (5120000.0 * Math.pow(B, 2.0) * epsCu * Math.pow(fcd, 2.0) + (512000.0 * Math.pow(B, 2.0) * epsCu * fR3d + 947200.0 * Math.pow(B, 2.0) * epsCu * fR1d)* facteurDOrientation * fcd)* h + 10240000.0 * sectionAcier * B * epsCu * fcd * fyd + 40000.0 * Math.pow(B, 2.0) * Math.pow(fcd, 2.0) + (-8000.0 * Math.pow(B, 2.0) * fR3d - 14800.0 * Math.pow(B, 2.0) * fR1d)* facteurDOrientation * fcd + (400.0 * Math.pow(B, 2.0) * Math.pow(fR3d, 2.0) + 1480.0 * Math.pow(B, 2.0) * fR1d * fR3d + 1369.0 * Math.pow(B, 2.0) * Math.pow(fR1d, 2.0))* Math.pow(facteurDOrientation, 2.0);
        double d_var = ((-3407872000000.0 * Math.pow(B, 2.0) * Math.pow(epsCu, 2.0) * fR3d - 3355443200000.0 * Math.pow(B, 2.0) * Math.pow(epsCu, 2.0) * fR1d)* facteurDOrientation * Math.pow(fcd, 3.0) + (-196608000000.0 * Math.pow(B, 2.0) * Math.pow(epsCu, 2.0) * Math.pow(fR3d, 2.0) - 727449600000.0 * Math.pow(B, 2.0) * Math.pow(epsCu, 2.0) * fR1d * fR3d - 672890880000.0 * Math.pow(B, 2.0) * Math.pow(epsCu, 2.0) * Math.pow(fR1d, 2.0))* Math.pow(facteurDOrientation, 2.0) * Math.pow(fcd, 2.0));
        double e_var = (((-3932160000000.0 * sectionAcier * B * Math.pow(epsCu, 2.0) * fR3d - 7274496000000.0 * sectionAcier * B * Math.pow(epsCu, 2.0) * fR1d)* facteurDOrientation * Math.pow(fcd, 2.0) - 78643200000000.0 * sectionAcier * B * Math.pow(epsCu, 2.0) * Math.pow(fcd, 3.0))* fyd + (61440000000.0 * Math.pow(B, 2.0) * epsCu * fR3d + 113664000000.0 * Math.pow(B, 2.0) * epsCu * fR1d)* facteurDOrientation * Math.pow(fcd, 3.0) + (-4505600000.0 * Math.pow(B, 2.0) * epsCu * Math.pow(fR3d, 2.0) - 7454720000.0 * Math.pow(B, 2.0) * epsCu * fR1d * fR3d + 1629184000.0 * Math.pow(B, 2.0) * epsCu * Math.pow(fR1d, 2.0))* Math.pow(facteurDOrientation, 2.0) * Math.pow(fcd, 2.0) + (-614400000.0 * Math.pow(B, 2.0) * epsCu * Math.pow(fR3d, 3.0) - 3409920000.0 * Math.pow(B, 2.0) * epsCu * fR1d * Math.pow(fR3d, 2.0) - 6308352000.0 * Math.pow(B, 2.0) * epsCu * Math.pow(fR1d, 2.0) * fR3d - 3890150400.0 * Math.pow(B, 2.0) * epsCu * Math.pow(fR1d, 3.0))* Math.pow(facteurDOrientation, 3.0) * fcd);
        double f_var = (3932160000000.0 * sectionAcier * B * Math.pow(epsCu, 2.0) * Math.pow(fcd, 3.0) + (77824000000.0 * sectionAcier * B * epsCu * fR3d + 236134400000.0 * sectionAcier * B * epsCu * fR1d)* facteurDOrientation * Math.pow(fcd, 2.0) + (-12288000000.0 * sectionAcier * B * epsCu * Math.pow(fR3d, 2.0) - 45465600000.0 * sectionAcier * B * epsCu * fR1d * fR3d - 42055680000.0 * sectionAcier * B * epsCu * Math.pow(fR1d, 2.0))* Math.pow(facteurDOrientation, 2.0) * fcd);
        double g_var = (128000000.0 * Math.pow(B, 2.0) * fR3d + 956800000.0 * Math.pow(B, 2.0) * fR1d);
        double h_var = (-73600000.0 * Math.pow(B, 2.0) * Math.pow(fR3d, 2.0) - 416320000.0 * Math.pow(B, 2.0) * fR1d * fR3d - 518296000.0 * Math.pow(B, 2.0) * Math.pow(fR1d, 2.0));
        double i_var = (10880000.0 * Math.pow(B, 2.0) * Math.pow(fR3d, 3.0) + 67584000.0 * Math.pow(B, 2.0) * fR1d * Math.pow(fR3d, 2.0) + 138350400.0 * Math.pow(B, 2.0) * Math.pow(fR1d, 2.0) * fR3d + 93530080.0 * Math.pow(B, 2.0) * Math.pow(fR1d, 3.0));
        double l_var = (-480000.0 * Math.pow(B, 2.0) * Math.pow(fR3d, 4.0) - 3552000.0 * Math.pow(B, 2.0) * fR1d * Math.pow(fR3d, 3.0) - 9856800.0 * Math.pow(B, 2.0) * Math.pow(fR1d, 2.0) * Math.pow(fR3d, 2.0) - 12156720.0 * Math.pow(B, 2.0) * Math.pow(fR1d, 3.0) * fR3d - 5622483.0 * Math.pow(B, 2.0) * Math.pow(fR1d, 4.0));

        double m1_var = b_var * Math.sqrt(c_var);
        double m2_var = d_var * Math.pow(h, 2.0);
        double m3_var = e_var * h;
        double m4_var = - 39321600000000.0 * Math.pow(sectionAcier, 2.0) * Math.pow(epsCu, 2.0) * Math.pow(fcd, 2.0) * Math.pow(fyd, 2.0);
        double m5_var = f_var * fyd;
        double m6_var = g_var * facteurDOrientation * Math.pow(fcd, 3.0);
        double m7_var = h_var * Math.pow(facteurDOrientation, 2.0) * Math.pow(fcd, 2.0);
        double m8_var = i_var * Math.pow(facteurDOrientation, 3.0) * fcd;
        double m9_var = l_var * Math.pow(facteurDOrientation, 4.0);

        double a_var = m1_var + m2_var + m3_var + m4_var + m5_var + m6_var + m7_var + m8_var + m9_var;
        double z_var = (78643200000000.0 * B * Math.pow(epsCu, 2.0) * Math.pow(fcd, 3.0));

        double momentResistantELU = - a_var /  z_var;

        momentResistantELU = momentResistantELU / 10.0; //N*m->daN*m
        return momentResistantELU;
    }


    public static double effortTranchantResistantBetonFibre(double fck, double fR1k, double fR3k, double hauteurSection,
                                                            double largeurSection, double enrobage, double sectionArmature,
                                                            double sigmaCP) {
        //
        double bw = largeurSection;
        double deff = 0.75*hauteurSection;

        double k = Math.min( 1.0 + Math.pow((200/(deff*1000)),0.5), 2.0);
        double vmin = 0.035*Math.pow(k,3.0/2.0)*Math.pow(fck,0.5);
        double VRdc = (vmin+0.15*sigmaCP)*bw*deff*100000.0; //daN
        double kH = Math.max(1.6-hauteurSection,1.0);
        double gamaFt = 1.25;

        double fR3d = fR3k/1.5;
        double VRdf = kH*(0.18*fR3d)/(1.4*gamaFt)*bw*deff*100000.0;;

        double VRd = VRdc + VRdf;

        return VRd;
    }
}
