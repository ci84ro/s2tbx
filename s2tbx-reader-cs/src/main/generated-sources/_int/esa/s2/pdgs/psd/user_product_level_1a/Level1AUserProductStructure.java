//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.7 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2014.07.07 à 03:07:45 PM CEST 
//


package _int.esa.s2.pdgs.psd.user_product_level_1a;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour Level-1A_User_Product_Structure complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="Level-1A_User_Product_Structure">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Product_Metadata_File">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="GRANULE">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="DATASTRIP">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="AUX_DATA">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Browse_Image" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="manifest.safe" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="rep_info" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="INSPIRE" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="HTML" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Level-1A_User_Product_Structure", propOrder = {
    "productMetadataFile",
    "granule",
    "datastrip",
    "auxdata",
    "browseImage",
    "manifestSafe",
    "repInfo",
    "inspire",
    "html"
})
public class Level1AUserProductStructure {

    @XmlElement(name = "Product_Metadata_File", required = true)
    protected Level1AUserProductStructure.ProductMetadataFile productMetadataFile;
    @XmlElement(name = "GRANULE", required = true)
    protected Level1AUserProductStructure.GRANULE granule;
    @XmlElement(name = "DATASTRIP", required = true)
    protected Level1AUserProductStructure.DATASTRIP datastrip;
    @XmlElement(name = "AUX_DATA", required = true)
    protected Level1AUserProductStructure.AUXDATA auxdata;
    @XmlElement(name = "Browse_Image")
    protected Object browseImage;
    @XmlElement(name = "manifest.safe")
    protected Object manifestSafe;
    @XmlElement(name = "rep_info")
    protected Object repInfo;
    @XmlElement(name = "INSPIRE", required = true)
    protected Object inspire;
    @XmlElement(name = "HTML", required = true)
    protected Object html;

    /**
     * Obtient la valeur de la propriété productMetadataFile.
     * 
     * @return
     *     possible object is
     *     {@link Level1AUserProductStructure.ProductMetadataFile }
     *     
     */
    public Level1AUserProductStructure.ProductMetadataFile getProductMetadataFile() {
        return productMetadataFile;
    }

    /**
     * Définit la valeur de la propriété productMetadataFile.
     * 
     * @param value
     *     allowed object is
     *     {@link Level1AUserProductStructure.ProductMetadataFile }
     *     
     */
    public void setProductMetadataFile(Level1AUserProductStructure.ProductMetadataFile value) {
        this.productMetadataFile = value;
    }

    /**
     * Obtient la valeur de la propriété granule.
     * 
     * @return
     *     possible object is
     *     {@link Level1AUserProductStructure.GRANULE }
     *     
     */
    public Level1AUserProductStructure.GRANULE getGRANULE() {
        return granule;
    }

    /**
     * Définit la valeur de la propriété granule.
     * 
     * @param value
     *     allowed object is
     *     {@link Level1AUserProductStructure.GRANULE }
     *     
     */
    public void setGRANULE(Level1AUserProductStructure.GRANULE value) {
        this.granule = value;
    }

    /**
     * Obtient la valeur de la propriété datastrip.
     * 
     * @return
     *     possible object is
     *     {@link Level1AUserProductStructure.DATASTRIP }
     *     
     */
    public Level1AUserProductStructure.DATASTRIP getDATASTRIP() {
        return datastrip;
    }

    /**
     * Définit la valeur de la propriété datastrip.
     * 
     * @param value
     *     allowed object is
     *     {@link Level1AUserProductStructure.DATASTRIP }
     *     
     */
    public void setDATASTRIP(Level1AUserProductStructure.DATASTRIP value) {
        this.datastrip = value;
    }

    /**
     * Obtient la valeur de la propriété auxdata.
     * 
     * @return
     *     possible object is
     *     {@link Level1AUserProductStructure.AUXDATA }
     *     
     */
    public Level1AUserProductStructure.AUXDATA getAUXDATA() {
        return auxdata;
    }

    /**
     * Définit la valeur de la propriété auxdata.
     * 
     * @param value
     *     allowed object is
     *     {@link Level1AUserProductStructure.AUXDATA }
     *     
     */
    public void setAUXDATA(Level1AUserProductStructure.AUXDATA value) {
        this.auxdata = value;
    }

    /**
     * Obtient la valeur de la propriété browseImage.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getBrowseImage() {
        return browseImage;
    }

    /**
     * Définit la valeur de la propriété browseImage.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setBrowseImage(Object value) {
        this.browseImage = value;
    }

    /**
     * Obtient la valeur de la propriété manifestSafe.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getManifestSafe() {
        return manifestSafe;
    }

    /**
     * Définit la valeur de la propriété manifestSafe.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setManifestSafe(Object value) {
        this.manifestSafe = value;
    }

    /**
     * Obtient la valeur de la propriété repInfo.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getRepInfo() {
        return repInfo;
    }

    /**
     * Définit la valeur de la propriété repInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setRepInfo(Object value) {
        this.repInfo = value;
    }

    /**
     * Obtient la valeur de la propriété inspire.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getINSPIRE() {
        return inspire;
    }

    /**
     * Définit la valeur de la propriété inspire.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setINSPIRE(Object value) {
        this.inspire = value;
    }

    /**
     * Obtient la valeur de la propriété html.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getHTML() {
        return html;
    }

    /**
     * Définit la valeur de la propriété html.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setHTML(Object value) {
        this.html = value;
    }


    /**
     * <p>Classe Java pour anonymous complex type.
     * 
     * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class AUXDATA {


    }


    /**
     * <p>Classe Java pour anonymous complex type.
     * 
     * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class DATASTRIP {


    }


    /**
     * <p>Classe Java pour anonymous complex type.
     * 
     * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class GRANULE {


    }


    /**
     * <p>Classe Java pour anonymous complex type.
     * 
     * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class ProductMetadataFile {


    }

}