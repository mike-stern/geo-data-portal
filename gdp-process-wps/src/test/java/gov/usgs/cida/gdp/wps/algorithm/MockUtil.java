package gov.usgs.cida.gdp.wps.algorithm;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.io.IOUtils;
import org.apache.xmlbeans.XmlException;
import org.n52.wps.PropertyDocument;
import org.n52.wps.PropertyDocument.Property;
import org.n52.wps.commons.WPSConfig;
import org.n52.wps.io.GeneratorFactory;
import org.n52.wps.io.IGenerator;
import org.n52.wps.io.IParser;
import org.n52.wps.io.ParserFactory;

/**
 *
 * @author tkunicki
 */
public class MockUtil {

    public final static String SUPPORTED_SCHEMA = "supportedSchema";
    public final static String SUPPORTED_FORMAT = "supportedFormat";
    public final static String SUPPORTED_ENCODING = "supportedEncoding";


    private static WPSConfig MOCK_CONFIG;
    public synchronized static WPSConfig getMockConfig() throws XmlException, IOException {
        if (MOCK_CONFIG == null) {
            InputStream configInputStream = null;
            try {
//                configInputStream = MockUtil.class.getResourceAsStream(
//                        "/org/n52/test/mock/wps_config.xml");
                configInputStream = new BufferedInputStream(new FileInputStream(
                        "src/main/webapp/config/wps_config.xml"));
                WPSConfig.forceInitialization(configInputStream);
                MOCK_CONFIG = WPSConfig.getInstance();
                ParserFactory.initialize(MOCK_CONFIG.getActiveRegisteredParser());
                GeneratorFactory.initialize(MOCK_CONFIG.getActiveRegisteredGenerator());
            } finally {
                IOUtils.closeQuietly(configInputStream);
            }
        }
        return MOCK_CONFIG;
    }

    public static Collection<String> getParserSupportedSchemas(Class<? extends IParser> clazz) {
        return getParserPropertyValues(clazz, SUPPORTED_SCHEMA);
    }

    public static Collection<String> getParserSupportedFormats(Class<? extends IParser> clazz) {
        return getParserPropertyValues(clazz, SUPPORTED_FORMAT);
    }

    public static Collection<String> getParserSupportedEncodings(Class<? extends IParser> clazz) {
        return getParserPropertyValues(clazz, SUPPORTED_ENCODING);
    }

    public static Collection<String> getGeneratorSupportedSchemas(Class<? extends IGenerator> clazz) {
        return getGeneratorPropertyValues(clazz, SUPPORTED_SCHEMA);
    }

    public static Collection<String> getGeneratorSupportedFormats(Class<? extends IGenerator> clazz) {
        return getGeneratorPropertyValues(clazz, SUPPORTED_FORMAT);
    }

    public static Collection<String> getGeneratorSupportedEncodings(Class<? extends IGenerator> clazz) {
        return getGeneratorPropertyValues(clazz, SUPPORTED_ENCODING);
    }

    public static Collection<String> getParserPropertyValues(Class<? extends IParser> clazz, String propertyName) {
        String clazzName = clazz.getName();
        ArrayList<String> propertyList = new ArrayList<String>();
        try {
            WPSConfig mockConfig = MockUtil.getMockConfig();
            PropertyDocument.Property properties[] =
                    mockConfig.getPropertiesForParserClass(clazzName);
            for (Property property : properties) {
                if (propertyName.equals(property.getName())) {
                    propertyList.add(property.getStringValue());
                }
            }
            propertyList.trimToSize();
        } catch (Exception e) {
            System.err.println("ERROR parsing property " + propertyName + " for Parser class " + clazzName);
        }
        return propertyList;
    }

    public static Collection<String> getGeneratorPropertyValues(Class<? extends IGenerator> clazz, String propertyName) {
        String clazzName = clazz.getName();
        ArrayList<String> propertyList = new ArrayList<String>();
        try {
            WPSConfig mockConfig = MockUtil.getMockConfig();
            PropertyDocument.Property properties[] =
                    mockConfig.getPropertiesForGeneratorClass(clazzName);
            for (Property property : properties) {
                if (propertyName.equals(property.getName())) {
                    propertyList.add(property.getStringValue());
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR parsing property " + propertyName + " for Generator class " + clazzName);
        }
        propertyList.trimToSize();
        return propertyList;
    }

}
