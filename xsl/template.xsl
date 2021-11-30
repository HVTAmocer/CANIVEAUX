<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:barcode="org.krysalis.barcode4j.xalan.BarcodeExt" xmlns:common="http://exslt.org/common"
                xmlns:xalan="http://xml.apache.org" exclude-result-prefixes="barcode common xalan"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="org.krysalis.barcode4j.xalan.BarcodeExt ">
    <xsl:decimal-format name="euro" decimal-separator="," grouping-separator="."/>
    <xsl:template match=" DonneesNDC">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simple"
                                       page-height="21cm"
                                       page-width="29.7cm"
                                       margin-top="1cm"
                                       margin-bottom="2cm"
                                       margin-left="2.0cm"
                                       margin-right="2.0cm">
                    <fo:region-body margin-top="2cm"/>
                    <fo:region-before extent="2cm"/>
                    <fo:region-after extent="0cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="simple" white-space-collapse="false">
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block text-align="center">
                        <fo:external-graphic src="url(logo - socramat.jpg)"
                                             content-width="3cm"
                                             scaling="uniform">
                        </fo:external-graphic>
                    </fo:block>
                </fo:static-content>

                <fo:static-content flow-name="xsl-region-after">
                    <fo:block text-align="end">
                        Page <fo:page-number/>/<fo:page-number-citation ref-id="last-page"/>
                    </fo:block>
                </fo:static-content>

                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-family="Arial" font-size="11pt" font-weight="normal" text-align="justify">

                        <fo:block font-size="20pt"
                                  font-family="sans-serif"
                                  line-height="32pt"
                                  space-after.optimum="15pt"
                                  text-align="center"
                                  padding-top="3pt"
                                  space-before="3mm">
                            Note de calcul des panneaux - <xsl:value-of select="/DonneesNDC/terrains/terrain/nomTerrain"/><fo:block/>
                            <fo:block white-space-collapse="true">
                                Files :
                                <xsl:for-each select="/DonneesNDC/terrains/terrain">
                                    <xsl:value-of select="nomRangees"/>
                                </xsl:for-each>
                            </fo:block>
                        </fo:block>

                        <fo:block font-size="14pt" font-weight="bold" space-after="3mm"> 1. Introduction</fo:block>
                        <!-- ____________________________________________________________________________________________________________________ -->
                        <fo:block font-size="12pt" font-weight="bold" space-after="3mm" space-before="3mm"> 1.1 Projet </fo:block>
                        <fo:table table-layout="fixed" width="100%">
                            <fo:table-column column-number="1" column-width="5cm"/>
                            <fo:table-column column-number="2" column-width="5cm"/>
                            <fo:table-body>
                                <fo:table-row >
                                    <fo:table-cell > <fo:block> Chantier </fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : <xsl:value-of select="/DonneesNDC/terrains/terrain/nomTerrain"/></fo:block> </fo:table-cell>
                                </fo:table-row>

                                <fo:table-row>
                                    <fo:table-cell> <fo:block> Commande </fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : <xsl:value-of select="/DonneesNDC/terrains/terrain/commande"/> </fo:block> </fo:table-cell>
                                </fo:table-row>

                                <fo:table-row>
                                    <fo:table-cell> <fo:block> Lieu </fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : <xsl:value-of select="/DonneesNDC/terrains/terrain/lieu"/> </fo:block> </fo:table-cell>
                                </fo:table-row>

                                <fo:table-row>
                                    <fo:table-cell> <fo:block> Maitre d'ouvrage </fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : <xsl:value-of select="/DonneesNDC/terrains/terrain/maitreDouvrage"/> </fo:block> </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>

                        <!-- ____________________________________________________________________________________________________________________ -->
                        <fo:block font-size="12pt" font-weight="bold" space-after="3mm" space-before="3mm"> 1.2 Données géographiques </fo:block>
                        <fo:table>
                            <fo:table-column column-width="80mm"/>
                            <fo:table-column column-width="50mm"/>
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell> <fo:block> Zone de vent (NF EN 1991-1-4)</fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : <xsl:value-of select="/DonneesNDC/terrains/terrain/zoneVent"/> </fo:block> </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell> <fo:block> Catégorie de terrain </fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : <xsl:value-of select="/DonneesNDC/terrains/terrain/categorieTerrain"/> </fo:block> </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell> <fo:block> Hauteur à l'acrotère </fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : <xsl:value-of select="/DonneesNDC/terrains/terrain/hauterAcrotere"/>m </fo:block> </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell> <fo:block> Zone de séisme (NF EN 1998-1:2005) </fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : <xsl:value-of select="/DonneesNDC/terrains/terrain/zoneSeisme"/> </fo:block> </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell> <fo:block> Catégorie d'importance </fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : <xsl:value-of select="/DonneesNDC/terrains/terrain/categorieImportance"/> </fo:block> </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell> <fo:block> Classe du sol </fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : <xsl:value-of select="/DonneesNDC/terrains/terrain/classeDeSol"/> </fo:block> </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>

                        <!-- ____________________________________________________________________________________________________________________ -->
                        <fo:block font-size="12pt" font-weight="bold" space-after="3mm" space-before="3mm"> 1.3 Matériaux </fo:block>
                        <fo:table>
                            <fo:table-column column-width="80mm"/>
                            <fo:table-column column-width="50mm"/>
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell> <fo:block> Classe du béton (EN 1992-1-1/3.1)</fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : <xsl:value-of select="/DonneesNDC/terrains/terrain/nomBeton"/> </fo:block> </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell> <fo:block> Nuance d'acier (EN 1992-1-1/Annexe C) </fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : <xsl:value-of select="/DonneesNDC/terrains/terrain/nomAcier"/> </fo:block> </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell> <fo:block> Classe d'exposition (4.2)</fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : XC1 </fo:block> </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell> <fo:block> Classe structurale (Tableau 4.3) </fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : S4 </fo:block> </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>

                        <!-- ____________________________________________________________________________________________________________________ -->
                        <fo:block font-size="12pt" font-weight="bold" space-after="3mm" space-before="3mm"> 1.4 Normes de référence </fo:block>
                        <fo:table>
                            <fo:table-column column-width="80mm"/>
                            <fo:table-column column-width="80mm"/>
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell> <fo:block> Calcul béton armé </fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : EN 1992-1-2 </fo:block> </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell> <fo:block> Calcul béton fibré </fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : EN 1992-1-2/FIB Model Code 2010 </fo:block> </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell> <fo:block> Charges et combinaisons </fo:block> </fo:table-cell>
                                    <fo:table-cell> <fo:block> : EN 1990/EN 1991 </fo:block> </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>

                        <!-- ____________________________________________________________________________________________________________________ -->
                        <fo:block font-size="14pt" font-weight="bold" space-after="3mm" break-before="page"> 2. Hypothèse de chargement et Combinaisons </fo:block>
                        <!-- ____________________________________________________________________________________________________________________ -->
                        <fo:block font-size="12pt" font-weight="bold" space-after="3mm" space-before="3mm"> 2.1 Charges prises en compte </fo:block>
                        <fo:block> - Charges verticales :</fo:block>
                        <fo:block start-indent="1cm"> (G) : Poids propres des murs (2500 daN/m<fo:inline font-size="6" baseline-shift="super">3</fo:inline>) </fo:block>
                        <!--                            <fo:block start-indent="1cm"> Prise en compte du coefficient dynamique 1.25 pour le phase de levage </fo:block>-->

                        <fo:block space-before="3mm"> - Charges horizontales :</fo:block>
                        <fo:block start-indent="1cm"> (V) : Charges de vent (NF EN 1991-1-4/NA). Prise en compte du vent en acrotère. </fo:block>
                        <fo:block start-indent="1cm"> (S) : Charges de séisme (NF EN 1998-1/NA)  </fo:block>

                        <!-- ____________________________________________________________________________________________________________________ -->
                        <fo:block font-size="12pt" font-weight="bold" space-after="3mm" space-before="3mm"> 2.2 Combinaisons des charges (NF EN 1990)</fo:block>
                        <fo:table text-align="center" line-height="8mm">
                            <fo:table-column column-width="4cm"/>
                            <fo:table-column column-width="6cm"/>

                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold"> Combinaisons </fo:block> </fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold"> Définitions </fo:block> </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block> ELU </fo:block> </fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block> 1.35*G + 1.50*V </fo:block> </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block> ELS </fo:block> </fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block> 1.00*G + 1.00*V </fo:block> </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block> ELA-FEU </fo:block> </fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block> 1.00*G + 0.2*V </fo:block> </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block> ELA-SEISME </fo:block> </fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block> 1.00*G + 1.0*S </fo:block> </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>


                        <!-- ____________________________________________________________________________________________________________________ -->
                        <fo:block font-size="14pt" font-weight="bold" space-before="3mm" space-after="3mm"> 3. Hypothèses de calcul </fo:block>

                        <fo:block> - Le voile inférieur travaille comme une poutre voile. Les voiles supérieurs reposent linéairement sur celui-ci et ne sont donc pas sollicités verticalement. </fo:block>
                        <fo:block> - Chaque voile reprend les efforts de vents qui lui sont appliquée. Ils travaillent donc tous horizontalement. Les armatures de répartition horizontale sont déterminés par mètre linéaire. </fo:block>
                        <fo:block> - Le degré CF est assuré par la pose d'un cordon CF entre les panneaux disposés dans un système de tenon-mortaise pour protéger celui-ci d'un contact direct avec les flammes. </fo:block>




                        <fo:block font-size="14pt" font-weight="bold" space-after="3mm" break-before="page"> 4. Données géométriques et des chargements </fo:block>
                        <!-- ____________________________________________________________________________________________________________________ -->
                        <fo:block font-size="12pt" font-weight="bold" space-after="3mm" space-before="3mm"> 4.1 Données des rangées </fo:block>
                        <fo:table table-layout="fixed" width="100%" text-align="center" table-omit-header-at-break="false" >
                            <fo:table-column column-number="1" column-width="2cm"/>
                            <fo:table-column column-number="2" column-width="2.5cm"/>
                            <fo:table-column column-number="3" column-width="2.5cm"/>
                            <fo:table-column column-number="4" column-width="2cm"/>
                            <fo:table-column column-number="5" column-width="2cm"/>
                            <fo:table-column column-number="6" column-width="2cm"/>
                            <fo:table-column column-number="7" column-width="2cm"/>
                            <fo:table-column column-number="8" column-width="2cm"/>
                            <fo:table-column column-number="9" column-width="2cm"/>
                            <fo:table-column column-number="10" column-width="2cm"/>

                            <fo:table-header line-height="8mm">
                                <fo:table-row>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Rangées</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Résistance au feu</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Epaisseur Panneaux</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Enrobage</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Type</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" number-columns-spanned="3"><fo:block font-weight="bold">Pression de Vent (daN/m<fo:inline font-size="6" baseline-shift="super">2</fo:inline>)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" number-columns-spanned="2"><fo:block font-weight="bold">Coef. d'affaiblissement</fo:block></fo:table-cell>
                                </fo:table-row>
                            </fo:table-header>

                            <fo:table-body line-height="8mm">
                                <fo:table-row>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block >-</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block >-</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block >(cm)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block >(cm)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block >-</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" ><fo:block>Zone Courante</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" ><fo:block>Zone Acrotère</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" ><fo:block>Au Feu</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" ><fo:block>Acier</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" ><fo:block>Béton</fo:block></fo:table-cell>
                                </fo:table-row>
                                <xsl:for-each select="/DonneesNDC/terrains/terrain">
                                    <xsl:for-each select="./rangees/rangee">
                                        <fo:table-row border="solid 0.1mm black">
                                            <fo:table-cell border="solid 0.1mm black">
                                                <fo:block> <xsl:value-of select="nomRangee"/></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border="solid 0.1mm black">
                                                <fo:block> <xsl:value-of select="rei"/></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border="solid 0.1mm black">
                                                <fo:block> <xsl:value-of select="epaisseurPanneau"/></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border="solid 0.1mm black">
                                                <fo:block> <xsl:value-of select="enrobage"/></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border="solid 0.1mm black">
                                                <fo:block> <xsl:value-of select="typeRangee"/></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border="solid 0.1mm black">
                                                <fo:block><xsl:value-of select="pressionVentZoneCourante"/></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border="solid 0.1mm black">
                                                <fo:block><xsl:value-of select="pressionVentZoneAcrotere"/></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border="solid 0.1mm black">
                                                <fo:block><xsl:value-of select="pressionVentAuFeu"/></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border="solid 0.1mm black">
                                                <fo:block><xsl:value-of select="coefAffaiblementAcier"/></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border="solid 0.1mm black">
                                                <fo:block><xsl:value-of select="coefAffaiblementBeton"/></fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </xsl:for-each>
                                </xsl:for-each>
                            </fo:table-body>
                        </fo:table>
                        <!-- ____________________________________________________________________________________________________________________ -->
                        <fo:block font-size="12pt" font-weight="bold" space-after="3mm" space-before="3mm"> 4.2 Données des travées </fo:block>
                        <fo:table text-align="center" table-omit-header-at-break="false" >
                            <fo:table-column column-number="1" column-width="2cm"/>
                            <fo:table-column column-number="2" column-width="2.5cm"/>
                            <fo:table-column column-number="3" column-width="2.5cm"/>
                            <fo:table-column column-number="4" column-width="2cm"/>
                            <fo:table-column column-number="5" column-width="3cm"/>
                            <fo:table-column column-number="6" column-width="2cm"/>
                            <fo:table-column column-number="7" column-width="2cm"/>
                            <fo:table-column column-number="8" column-width="3cm"/>
                            <fo:table-column column-number="9" column-width="3cm"/>

                            <fo:table-header line-height="8mm">
                                <fo:table-row border="solid 0.1mm black">
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Rangées</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Travées</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Finition</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Portée</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Pression Sismique Equivalente</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Hauteur Poteau Gauche</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Hauteur Poteau Droite</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Largeur Appuis Acrotères Gauche</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Largeur Appuis Acrotères Droite</fo:block></fo:table-cell>
                                </fo:table-row>
                            </fo:table-header>

                            <fo:table-body line-height="8mm">
                                <fo:table-row border="solid 0.1mm black">
                                    <fo:table-cell border="solid 0.1mm black"><fo:block >-</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block >-</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block >-</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block >(m)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block >(daN/m<fo:inline font-size="6" baseline-shift="super">2</fo:inline>)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block >(m)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block >(m)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block >(m)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block >(m)</fo:block></fo:table-cell>
                                </fo:table-row>

                                <xsl:for-each select="/DonneesNDC/terrains/terrain">
                                    <xsl:for-each select="./rangees/rangee">
                                        <xsl:for-each select="./travees/travee">
                                            <fo:table-row border="solid 0.1mm black">
                                                <fo:table-cell border="solid 0.1mm black">
                                                    <fo:block> <xsl:value-of select="nomRangee"/></fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell border="solid 0.1mm black">
                                                    <fo:block> <xsl:value-of select="nomTravee"/></fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell border="solid 0.1mm black">
                                                    <fo:block> <xsl:value-of select="finition"/></fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell border="solid 0.1mm black">
                                                    <fo:block> <xsl:value-of select="portee"/></fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell border="solid 0.1mm black">
                                                    <fo:block> <xsl:value-of select="pressionSismiqueEquivalente"/></fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell border="solid 0.1mm black">
                                                    <fo:block> <xsl:value-of select="hauteurPoteauGauche"/></fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell border="solid 0.1mm black">
                                                    <fo:block> <xsl:value-of select="hauteurPoteauDroite"/></fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell border="solid 0.1mm black">
                                                    <fo:block> <xsl:value-of select="largeurAppuisAcroteresGauche"/></fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell border="solid 0.1mm black">
                                                    <fo:block> <xsl:value-of select="largeurAppuisAcroteresDroite"/></fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                        </xsl:for-each>
                                    </xsl:for-each>
                                </xsl:for-each>
                            </fo:table-body>
                        </fo:table>

                        <!-- ____________________________________________________________________________________________________________________ -->
                        <fo:block font-size="14pt" font-weight="bold" space-after="3mm" break-before="page"> 5. Efforts de calcul</fo:block>
                        <!-- ____________________________________________________________________________________________________________________ -->
                        <fo:block font-size="12pt" font-weight="bold" space-after="3mm" space-before="3mm" > 5.1 Flexion verticale </fo:block>
                        <!-- ____________________________________________________________________________________________________________________ -->

                        <fo:table text-align="center" table-omit-header-at-break="false" >
                            <fo:table-column column-number="1" column-width="2.0cm"/>
                            <fo:table-column column-number="2" column-width="2.0cm"/>
                            <fo:table-column column-number="3" column-width="2.0cm"/>
                            <fo:table-column column-number="4" column-width="3.5cm"/>
                            <fo:table-column column-number="5" column-width="1.5cm"/>
                            <fo:table-column column-number="6" column-width="1.5cm"/>
                            <fo:table-column column-number="7" column-width="3.5cm"/>
                            <fo:table-column column-number="8" column-width="1.5cm"/>
                            <fo:table-column column-number="9" column-width="1.5cm"/>
                            <fo:table-column column-number="10" column-width="3.5cm"/>
                            <fo:table-column column-number="11" column-width="1.5cm"/>
                            <fo:table-column column-number="12" column-width="1.5cm"/>


                            <fo:table-header line-height="8mm">
                                <fo:table-row border="solid 0.1mm black">
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Rangées</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Travées</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Panneaux</fo:block></fo:table-cell>

                                    <fo:table-cell number-columns-spanned="3" border="solid 0.1mm black"><fo:block font-weight="bold">Sollicitation phase courante</fo:block> </fo:table-cell>
                                    <fo:table-cell number-columns-spanned="3" border="solid 0.1mm black"><fo:block font-weight="bold">Sollicitation phase feu</fo:block> </fo:table-cell>
                                    <fo:table-cell number-columns-spanned="3" border="solid 0.1mm black"><fo:block font-weight="bold">Sollicitation au séisme</fo:block> </fo:table-cell>
                                </fo:table-row >
                                <fo:table-row border="solid 0.1mm black" line-height="8mm">
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">-</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">-</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">-</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Charge permanent (daN/m)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" column-number="4"><fo:block font-weight="bold">M<fo:inline font-size="6" baseline-shift="sub">ELU</fo:inline> (daNm) </fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" column-number="5"><fo:block font-weight="bold">V<fo:inline font-size="6" baseline-shift="sub">ELU</fo:inline> (daN)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Charge permanent (daN/m)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" column-number="6"><fo:block font-weight="bold">M<fo:inline font-size="6" baseline-shift="sub">FEU</fo:inline> (daNm) </fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" column-number="7"><fo:block font-weight="bold">V<fo:inline font-size="6" baseline-shift="sub">FEU</fo:inline> (daN)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Charge permanent (daN/m)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" column-number="6"><fo:block font-weight="bold">M<fo:inline font-size="6" baseline-shift="sub">SEISME</fo:inline> (daNm) </fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" column-number="7"><fo:block font-weight="bold">V<fo:inline font-size="6" baseline-shift="sub">SEISME</fo:inline> (daN)</fo:block></fo:table-cell>
                                </fo:table-row>
                            </fo:table-header>

                            <fo:table-body line-height="8mm">
                                <xsl:for-each select="/DonneesNDC/terrains/terrain">
                                    <xsl:for-each select="./rangees/rangee">
                                        <xsl:for-each select="./travees/travee">
                                            <xsl:for-each select="./panneaux/panneau">
                                                <fo:table-row border="solid 0.1mm black">
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="nomRangee"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="nomTravee"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="nomPanneau"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="chargeVerticaleCpExploitation"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="mEdVerticalELU"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="vEdVerticalELU"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="chargeVerticaleCpFeu"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="mEdVerticalFeu"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="vEdVerticalFeu"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="chargeVerticaleCpSeisme"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="mEdVerticalSeisme"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="vEdVerticalSeisme"/></fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </xsl:for-each>
                                        </xsl:for-each>
                                    </xsl:for-each>
                                </xsl:for-each>
                            </fo:table-body>
                        </fo:table>

                        <!-- ____________________________________________________________________________________________________________________ -->
                        <fo:block font-size="12pt" font-weight="bold" space-after="3mm" space-before="3mm" break-before="page"> 5.2 Flexion horizontale </fo:block>
                        <fo:table text-align="center" table-omit-header-at-break="false" >
                            <fo:table-column column-number="1" column-width="2.0cm"/>
                            <fo:table-column column-number="2" column-width="2.0cm"/>
                            <fo:table-column column-number="3" column-width="2.0cm"/>
                            <fo:table-column column-number="4" column-width="3.5cm"/>
                            <fo:table-column column-number="5" column-width="1.5cm"/>
                            <fo:table-column column-number="6" column-width="1.5cm"/>
                            <fo:table-column column-number="7" column-width="3.5cm"/>
                            <fo:table-column column-number="8" column-width="1.5cm"/>
                            <fo:table-column column-number="9" column-width="1.5cm"/>
                            <fo:table-column column-number="10" column-width="3.5cm"/>
                            <fo:table-column column-number="11" column-width="1.5cm"/>
                            <fo:table-column column-number="12" column-width="1.5cm"/>

                            <fo:table-header line-height="8mm">
                                <fo:table-row border="solid 0.1mm black">
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Rangées</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Travées</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Panneaux</fo:block></fo:table-cell>

                                    <fo:table-cell number-columns-spanned="3" border="solid 0.1mm black"><fo:block font-weight="bold">Sollicitation phase courante</fo:block> </fo:table-cell>
                                    <fo:table-cell number-columns-spanned="3" border="solid 0.1mm black"><fo:block font-weight="bold">Sollicitation phase feu</fo:block> </fo:table-cell>
                                    <fo:table-cell number-columns-spanned="3" border="solid 0.1mm black"><fo:block font-weight="bold">Sollicitation au séisme</fo:block> </fo:table-cell>
                                </fo:table-row >
                                <fo:table-row border="solid 0.1mm black" line-height="8mm">
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">-</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">-</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">-</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Charge du vent (daN/m<fo:inline font-size="6" baseline-shift="super">2</fo:inline>)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" column-number="4"><fo:block font-weight="bold">M<fo:inline font-size="6" baseline-shift="sub">ELU</fo:inline> (daNm) </fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" column-number="5"><fo:block font-weight="bold">V<fo:inline font-size="6" baseline-shift="sub">ELU</fo:inline> (daN)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Charge du vent (daN/m<fo:inline font-size="6" baseline-shift="super">2</fo:inline>)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" column-number="6"><fo:block font-weight="bold">M<fo:inline font-size="6" baseline-shift="sub">FEU</fo:inline> (daNm) </fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" column-number="7"><fo:block font-weight="bold">V<fo:inline font-size="6" baseline-shift="sub">FEU</fo:inline> (daN)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Charge de séisme (daN/m<fo:inline font-size="6" baseline-shift="super">2</fo:inline>)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" column-number="6"><fo:block font-weight="bold">M<fo:inline font-size="6" baseline-shift="sub">SEISME</fo:inline> (daNm) </fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black" column-number="7"><fo:block font-weight="bold">V<fo:inline font-size="6" baseline-shift="sub">SEISME</fo:inline> (daN)</fo:block></fo:table-cell>
                                </fo:table-row>
                            </fo:table-header>

                            <fo:table-body line-height="8mm">
                                <xsl:for-each select="/DonneesNDC/terrains/terrain">
                                    <xsl:for-each select="./rangees/rangee">
                                        <xsl:for-each select="./travees/travee">
                                            <xsl:for-each select="./panneaux/panneau">
                                                <fo:table-row border="solid 0.1mm black">
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="nomRangee"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="nomTravee"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="nomPanneau"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="chargeHorizontaleVentExploitation"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="mEdHorizontalELU"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="vEdHorizontalELU"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="chargeHorizontaleVentFeu"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="mEdHorizontalFeu"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="vEdHorizontalFeu"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="chargeHorizontaleSeisme"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="mEdHorizontalSeisme"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="vEdHorizontalSeisme"/></fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </xsl:for-each>
                                        </xsl:for-each>
                                    </xsl:for-each>
                                </xsl:for-each>
                            </fo:table-body>
                        </fo:table>


                        <!-- ____________________________________________________________________________________________________________________ -->
                        <fo:block font-size="14pt" font-weight="bold" space-after="3mm" break-before="page"> 6. Vérifications</fo:block>

                        <fo:table text-align="center" table-omit-header-at-break="false" >
                            <fo:table-column column-number="1" column-width="2cm"/>
                            <fo:table-column column-number="2" column-width="2cm"/>
                            <fo:table-column column-number="3" column-width="2cm"/>
                            <fo:table-column column-number="4" column-width="2cm"/>
                            <fo:table-column column-number="5" column-width="2.5cm"/>
                            <fo:table-column column-number="6" column-width="2.5cm"/>
                            <fo:table-column column-number="7" column-width="1.5cm"/>
                            <fo:table-column column-number="8" column-width="2.0cm"/>
                            <fo:table-column column-number="9" column-width="2.0cm"/>
                            <fo:table-column column-number="10" column-width="2.0cm"/>
                            <fo:table-column column-number="11" column-width="2.0cm"/>
                            <fo:table-column column-number="12" column-width="2.0cm"/>
                            <fo:table-column column-number="13" column-width="2.0cm"/>

                            <fo:table-header>
                                <fo:table-row border="solid 0.1mm black" line-height="8mm">
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Rangées</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Travées</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Longueur Travée (m)</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Panneaux</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Chainage Haut</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Armatures de répartition horizontale</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Cadres</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Chainage Bas</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Vérif. Résistance</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Vérif. Feu</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Vérif. Séisme</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Vérif. Flèche</fo:block></fo:table-cell>
                                    <fo:table-cell border="solid 0.1mm black"><fo:block font-weight="bold">Vérif. Déver- sement</fo:block></fo:table-cell>
                                </fo:table-row>
                            </fo:table-header>

                            <fo:table-body line-height="8mm">
                                <xsl:for-each select="/DonneesNDC/terrains/terrain">
                                    <xsl:for-each select="./rangees/rangee">
                                        <xsl:for-each select="./travees/travee">
                                            <xsl:for-each select="./panneaux/panneau">
                                                <fo:table-row border="solid 0.1mm black">
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="nomRangee"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="nomTravee"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="/DonneesNDC/terrains/terrain/rangees/rangee/travees/travee/portee"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="nomPanneau"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="sectionChainageHaut"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="sectionCourantHorizontal"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="sectionCourantVertical"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="sectionChainageBas"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="resultatVerificationResistance"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="resultatVerificationSeisme"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="resultatVerificationFeu"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="resultatVerificationFleche"/></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell border="solid 0.1mm black">
                                                        <fo:block> <xsl:value-of select="resultatVerificationDeversement"/></fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </xsl:for-each>
                                        </xsl:for-each>
                                    </xsl:for-each>
                                </xsl:for-each>
                            </fo:table-body>
                        </fo:table>


                    </fo:block>
                    <fo:block id="last-page"> </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>