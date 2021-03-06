package gov.usgs.cida.gdp.dataaccess.helper;

import gov.usgs.cida.gdp.utilities.FileHelper;
import java.io.File;
import java.io.IOException;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isuftin
 */
public class ShapeFileEPSGHelper {
    private static Logger log = LoggerFactory.getLogger(ShapeFileEPSGHelper.class);

    public static String getDeclaredEPSGFromPrj(final File prjFile) throws IOException, FactoryException {
        String result = null;
        if (prjFile == null || !prjFile.exists()) {
            return result;
        }
        log.debug(new StringBuilder("Attempting to get EPSG from file: ").append(prjFile.getPath()).toString());

        byte[] wktByteArray = FileHelper.getByteArrayFromFile(prjFile);
        result = ShapeFileEPSGHelper.getDeclaredEPSGFromWKT(new String(wktByteArray));
        return result;
    }

    /**
     * 
     * @param wkt
     * @return
     * @throws FactoryException 
     */
    public static String getDeclaredEPSGFromWKT(final String wkt) throws FactoryException {
        return ShapeFileEPSGHelper.getDeclaredEPSGFromWKT(wkt, true);
    }
    
    /**
     * 
     * @param wkt
     * @param useBaseCRSFailover Use base CRS to do a lookup
     * @return
     * @throws FactoryException 
     */
    public static String getDeclaredEPSGFromWKT(final String wkt, boolean useBaseCRSFailover) throws FactoryException {
        log.debug(new StringBuilder("Attempting to get EPSG from WKT: ").append(wkt).toString());
        String result = null;
        if (wkt == null || "".equals(wkt)) {
            return result;
        }

        CoordinateReferenceSystem crs = null;
        try {
            crs = CRS.parseWKT(wkt);
        } catch (FactoryException ex) {
            log.error(ex.getMessage());
            throw ex;
        }

        result = CRS.lookupIdentifier(crs, true);

        if (result == null && crs instanceof ProjectedCRS && useBaseCRSFailover) {
            result = CRS.lookupIdentifier(((ProjectedCRS)crs).getBaseCRS(), true);
        }
        log.debug("Found " + result);
        return result;
    }

}
