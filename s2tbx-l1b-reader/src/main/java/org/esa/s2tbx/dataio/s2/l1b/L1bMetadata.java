/*
 *
 * Copyright (C) 2013-2014 Brockmann Consult GmbH (info@brockmann-consult.de)
 * Copyright (C) 2014-2015 CS SI
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see http://www.gnu.org/licenses/
 *
 */

package org.esa.s2tbx.dataio.s2.l1b;

import com.vividsolutions.jts.geom.Coordinate;
import https.psd_13_sentinel2_eo_esa_int.dico._1_0.pdgs.dimap.A_GEOMETRIC_HEADER_LIST_EXPERTISE;
import https.psd_13_sentinel2_eo_esa_int.psd.s2_pdi_level_1b_datastrip_metadata.Level1B_DataStrip;
import https.psd_13_sentinel2_eo_esa_int.psd.s2_pdi_level_1b_granule_metadata.Level1B_Granule;
import https.psd_13_sentinel2_eo_esa_int.psd.user_product_level_1b.Level1B_User_Product;
import jp2.TileLayout;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.esa.s2tbx.dataio.Utils;
import org.esa.s2tbx.dataio.s2.S2Metadata;
import org.esa.s2tbx.dataio.s2.filepatterns.S2DatastripDirFilename;
import org.esa.s2tbx.dataio.s2.filepatterns.S2DatastripFilename;
import org.esa.s2tbx.dataio.s2.filepatterns.S2GranuleDirFilename;
import org.esa.s2tbx.dataio.s2.l1b.filepaterns.S2L1BGranuleDirFilename;
import org.esa.snap.framework.datamodel.MetadataElement;
import org.esa.snap.util.Guardian;
import org.esa.snap.util.SystemUtils;
import org.jdom.DataConversionException;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the Sentinel-2 MSI L1C XML metadata header file.
 * <p>
 * Note: No data interpretation is done in this class, it is intended to serve the pure metadata content only.
 *
 * @author Norman Fomferra
 */
public class L1bMetadata extends S2Metadata {

    private static final String PSD_STRING = "13";

    private MetadataElement metadataElement;
    protected Logger logger = SystemUtils.LOG;


    static class Tile {
        String id;
        String detectorId;
        TileGeometry tileGeometry10M;
        TileGeometry tileGeometry20M;
        TileGeometry tileGeometry60M;

        AnglesGrid sunAnglesGrid;
        AnglesGrid viewingIncidenceAnglesGrids;

        public List<Coordinate> corners;

        public enum idGeom {G10M, G20M, G60M}

        public Tile(String id, String detectorId) {
            this.id = id;
            this.detectorId = detectorId;
            tileGeometry10M = new TileGeometry();
            tileGeometry20M = new TileGeometry();
            tileGeometry60M = new TileGeometry();
        }

        public TileGeometry getGeometry(idGeom index) {
            switch (index) {
                case G10M:
                    return tileGeometry10M;
                case G20M:
                    return tileGeometry20M;
                case G60M:
                    return tileGeometry60M;
                default:
                    throw new IllegalStateException();
            }
        }

        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }

    static class TileGeometry {
        int numRows;
        int numCols;
        public Integer position;
        int xDim;
        int yDim;
        public int resolution;
        public int numRowsDetector;
        public String detector;

        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }

    static class AnglesGrid {
        int bandId;
        int detectorId;
        double zenith;
        double azimuth;

        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }

    static class ProductCharacteristics {
        String spacecraft;
        String datasetProductionDate;
        String processingLevel;
        SpectralInformation[] bandInformations;

        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }

    static class SpectralInformation {
        int bandId;
        String physicalBand;
        int resolution;
        double wavelenghtMin;
        double wavelenghtMax;
        double wavelenghtCentral;
        double spectralResponseStep;
        double[] spectralResponseValues;

        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }

    private List<Tile> tileList;
    private List<String> imageList; //todo populate imagelist
    private ProductCharacteristics productCharacteristics;

    public static L1bMetadata parseHeader(File file, TileLayout[] tileLayouts) throws JDOMException, IOException, JAXBException {
        return new L1bMetadata(new FileInputStream(file), file, file.getParent(), tileLayouts);
    }

    public List<Tile> getTileList() {
        return tileList;
    }

    public ProductCharacteristics getProductCharacteristics() {
        return productCharacteristics;
    }


    public MetadataElement getMetadataElement() {
        return metadataElement;
    }

    private L1bMetadata(InputStream stream, File file, String parent, TileLayout[] tileLayouts) throws DataConversionException, JAXBException, FileNotFoundException {
        super(tileLayouts, L1bMetadataProc.getJaxbContext(), PSD_STRING);

        try {
            Object userProductOrTile = updateAndUnmarshal(stream);

            if(userProductOrTile instanceof Level1B_User_Product)
            {
                initProduct(stream, file, parent, userProductOrTile);
            }
            else
            {
                initTile(stream, file, parent, userProductOrTile);
            }

        } catch (JAXBException | JDOMException | IOException e) {
            logger.severe(Utils.getStackTrace(e));
        }
    }


    private void initProduct(InputStream stream, File file, String parent, Object casted
                             ) throws IOException, JAXBException, JDOMException {
        Level1B_User_Product product = (Level1B_User_Product) casted;
        productCharacteristics = L1bMetadataProc.getProductOrganization(product);

        Collection<String> tileNames = L1bMetadataProc.getTiles(product);
        List<File> fullTileNamesList = new ArrayList<>();

        tileList = new ArrayList<>();

        for (String granuleName : tileNames) {
            File nestedMetadata = new File(parent, "GRANULE" + File.separator + granuleName);

            if (nestedMetadata.exists()) {
                logger.log(Level.FINE, "File found: " + nestedMetadata.getAbsolutePath());
                S2GranuleDirFilename aGranuleDir = S2L1BGranuleDirFilename.create(granuleName);
                Guardian.assertNotNull("aGranuleDir", aGranuleDir);
                String theName = aGranuleDir.getMetadataFilename().name;

                File nestedGranuleMetadata = new File(parent, "GRANULE" + File.separator + granuleName + File.separator + theName);
                if (nestedGranuleMetadata.exists()) {
                    fullTileNamesList.add(nestedGranuleMetadata);
                } else {
                    String errorMessage = "Corrupted product: the file for the granule " + granuleName + " is missing";
                    logger.log(Level.WARNING, errorMessage);
                }
            } else {
                logger.log(Level.SEVERE, "File not found: " + nestedMetadata.getAbsolutePath());
            }
        }

        for (File aGranuleMetadataFile : fullTileNamesList) {
            FileInputStream granuleStream = new FileInputStream(aGranuleMetadataFile);
            Level1B_Granule aGranule = (Level1B_Granule) updateAndUnmarshal(granuleStream);

            Map<Integer, TileGeometry> geoms = L1bMetadataProc.getGranuleGeometries(aGranule, getTileLayouts());

            Tile t = new Tile(aGranule.getGeneral_Info().getGRANULE_ID().getValue(), aGranule.getGeneral_Info().getDETECTOR_ID().getValue());

            t.tileGeometry10M = geoms.get(10);
            t.tileGeometry20M = geoms.get(20);
            t.tileGeometry60M = geoms.get(60);

            t.sunAnglesGrid = L1bMetadataProc.getSunGrid(aGranule);
            t.viewingIncidenceAnglesGrids = L1bMetadataProc.getAnglesGrid(aGranule);

            t.corners = L1bMetadataProc.getGranuleCorners(aGranule); // counterclockwise

            tileList.add(t);
        }

        S2DatastripFilename stripName = L1bMetadataProc.getDatastrip(product);
        S2DatastripDirFilename dirStripName = L1bMetadataProc.getDatastripDir(product);

        File dataStripMetadata = new File(parent, "DATASTRIP" + File.separator + dirStripName.name + File.separator + stripName.name);

        metadataElement = new MetadataElement("root");
        MetadataElement userProduct = parseAll(new SAXBuilder().build(file).getRootElement());
        MetadataElement dataStrip = parseAll(new SAXBuilder().build(dataStripMetadata).getRootElement());
        metadataElement.addElement(userProduct);
        metadataElement.addElement(dataStrip);
        MetadataElement granulesMetaData = new MetadataElement("Granules");

        // get datastrip...
        FileInputStream dataStripStream = new FileInputStream(dataStripMetadata);
        Level1B_DataStrip theDataStrip = (Level1B_DataStrip) updateAndUnmarshal(dataStripStream);
        //int numheaders = theDataStrip.getImage_Data_Info().getGeometric_Header_List().getGeometric_Header().size();


        List<AnglesGrid> sunGrid = new ArrayList<>();
        List<AnglesGrid> incidenceGrid = new ArrayList<>();

        List<A_GEOMETRIC_HEADER_LIST_EXPERTISE.Geometric_Header> headers = theDataStrip.getImage_Data_Info().getGeometric_Header_List().getGeometric_Header();
        for (A_GEOMETRIC_HEADER_LIST_EXPERTISE.Geometric_Header header : headers) {
            Iterator it = header.getLocated_Geometric_Header().iterator();
            while (it.hasNext()) {
                A_GEOMETRIC_HEADER_LIST_EXPERTISE.Geometric_Header.Located_Geometric_Header o = (A_GEOMETRIC_HEADER_LIST_EXPERTISE.Geometric_Header.Located_Geometric_Header) it.next();
                AnglesGrid tmpGrid = new AnglesGrid();
                tmpGrid.azimuth = o.getSolar_Angles().getAZIMUTH_ANGLE().getValue();
                tmpGrid.zenith = o.getSolar_Angles().getZENITH_ANGLE().getValue();
                sunGrid.add(tmpGrid);

                AnglesGrid tmpIncidenceGrid = new AnglesGrid();
                tmpIncidenceGrid.azimuth = o.getIncidence_Angles().getAZIMUTH_ANGLE().getValue();
                tmpIncidenceGrid.zenith = o.getIncidence_Angles().getZENITH_ANGLE().getValue();
                incidenceGrid.add(tmpIncidenceGrid);
            }
        }

        for (File aGranuleMetadataFile : fullTileNamesList) {
            MetadataElement aGranule = parseAll(new SAXBuilder().build(aGranuleMetadataFile).getRootElement());
            granulesMetaData.addElement(aGranule);
        }

        metadataElement.addElement(granulesMetaData);
    }

    private void initTile(InputStream stream, File file, String parent, Object casted) throws IOException, JAXBException, JDOMException {
        Level1B_Granule product = (Level1B_Granule) casted;
        productCharacteristics = new L1bMetadata.ProductCharacteristics();

        tileList = new ArrayList<>();

        {
            Level1B_Granule aGranule = product;
            Map<Integer, TileGeometry> geoms = L1bMetadataProc.getGranuleGeometries(aGranule, getTileLayouts());

            Tile t = new Tile(aGranule.getGeneral_Info().getGRANULE_ID().getValue(), aGranule.getGeneral_Info().getDETECTOR_ID().getValue());

            t.tileGeometry10M = geoms.get(10);
            t.tileGeometry20M = geoms.get(20);
            t.tileGeometry60M = geoms.get(60);

            t.sunAnglesGrid = L1bMetadataProc.getSunGrid(aGranule);
            t.viewingIncidenceAnglesGrids = L1bMetadataProc.getAnglesGrid(aGranule);

            t.corners = L1bMetadataProc.getGranuleCorners(aGranule); // counterclockwise

            tileList.add(t);
        }
    }
}
