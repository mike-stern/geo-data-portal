package gov.usgs.gdp.bean;

import gov.usgs.gdp.analysis.GeoToolsFileAnalysis;
import gov.usgs.gdp.io.FileHelper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;

import thredds.catalog.InvCatalog;

public class ShapeFileSetBean implements Serializable {
	private File projectionFile;
	private File shapeFile;
	private File dbfFile;
	private File shapeFileIndexFile;
	private String name;
	private List<String> attributeList;
	private List<String> featureList;
	private String chosenDataset;
	private String chosenAttribute;
	private String chosenFeature;


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static List<String> getAttributeListFromBean(ShapeFileSetBean shapeFileSetBean) {
		
		List<String> result = new ArrayList<String>();
		FileDataStore fds = GeoToolsFileAnalysis.getFileDataStore(shapeFileSetBean.getShapeFile());		
		FeatureSource<SimpleFeatureType, SimpleFeature> featureSource;
		
		try {
			featureSource = fds.getFeatureSource();
		} catch (IOException e) {
			return result;
		}
		fds.dispose();
		
		List<AttributeType> attribTypes = featureSource.getSchema().getTypes();
		for (AttributeType attribType : attribTypes) {
            String attribTypeName = attribType.getName().toString();
            String selectItem = attribTypeName;
            result.add(selectItem);
        }
		return result;
		
	}
	
	/**
	 * Pull a feature list from, a ShapeFileSetBean
	 * 
	 * @param shapeFileSetBean
	 * @return
	 */
	public static List<String> getFeatureListFromBean(ShapeFileSetBean shapeFileSetBean) {
		List<String> result = null;
		FileDataStore shapeFileDataStore;
		try {
			shapeFileDataStore = FileDataStoreFinder.getDataStore(shapeFileSetBean.getShapeFile());
			FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = shapeFileDataStore.getFeatureSource();
			FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection = featureSource.getFeatures();
			Iterator<SimpleFeature> featureIter = featureCollection.iterator();
			Set<String> attribValList = new TreeSet<String>();
			try {
				 while (featureIter.hasNext()) {
                   SimpleFeature feature = featureIter.next();
                   String attribTypeIdentifer = shapeFileSetBean.getChosenAttribute();
                   Object featureAttributeObject = feature.getAttribute(attribTypeIdentifer.trim());                   
                   if (featureAttributeObject != null) attribValList.add(feature.getAttribute(attribTypeIdentifer).toString());
               }
			} finally {
               featureCollection.close(featureIter);
           }
			
			result = new ArrayList<String>();
			for (String attribVal : attribValList) {
				result.add(attribVal);
           }
			
		} catch (IOException e) {
			return null;
		}
		
		return result;
	}
		
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public List<String> getAttributeList() {
		if (this.attributeList == null) this.attributeList = new ArrayList<String>();
		return attributeList;
	}
	public String getChosenAttribute() {
		return chosenAttribute;
	}
	public String getChosenFeature() {
		return chosenFeature;
	}
	public File getDbfFile() {
		return dbfFile;
	}
	public List<String> getFeatureList() {
		if (this.featureList == null) this.featureList = new ArrayList<String>();
		return featureList;
	}
	public String getName() {
		return name;
	}
	public File getProjectionFile() {
		return projectionFile;
	}
	public File getShapeFile() {
		return shapeFile;
	}
	public File getShapeFileIndexFile() {
		return shapeFileIndexFile;
	}
	public void setAttributeList(List<String> attributeList) {
		
		this.attributeList = attributeList;
	}
	public void setChosenAttribute(String chosenAttribute) {
		this.chosenAttribute = chosenAttribute;
	}
	public void setChosenFeature(String chosenfeature) {
		this.chosenFeature = chosenfeature;
	}
	public void setDbfFile(File dbfFile) {
		this.dbfFile = dbfFile;
	}
	
	public void setFeatureList(List<String> featureList) {
		this.featureList = featureList;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setProjectionFile(File projectionFile) {
		this.projectionFile = projectionFile;
	}
	public void setShapeFile(File shapeFile) {
		this.shapeFile = shapeFile;
	}
	public void setShapeFileIndexFile(File shapeFileIndexFile) {
		this.shapeFileIndexFile = shapeFileIndexFile;
	}

	public String getChosenDataset() {
		return chosenDataset;
	}

	public void setChosenDataset(String chosenDataset) {
		this.chosenDataset = chosenDataset;
	}

	
	/**
	 * If a FilesBean contains the proper filetypes to create a ShapeFile set,
	 * method creates a ShapeFileSetBean 
	 * 
	 * @param exampleFilesBean
	 * @return
	 */
	public static ShapeFileSetBean getShapeFileSetBeanFromFilesBean(
			FilesBean exampleFilesBean) {
		ShapeFileSetBean result = null;
		File projectionFile = null;
		File shapeFile = null;
		File dbFile = null;			
		File shapeFileIndexFile = null;
		for (File file : exampleFilesBean.getFiles()) {
			if (file.getName().toLowerCase().contains(".shp")) shapeFile = file;
			if (file.getName().toLowerCase().contains(".prj")) projectionFile = file;
			if (file.getName().toLowerCase().contains(".dbf")) dbFile = file;
			if (file.getName().toLowerCase().contains(".shx")) shapeFileIndexFile = file;
		}
		
		if (projectionFile != null && shapeFile != null && dbFile != null) {
			result = new ShapeFileSetBean();
			result.setName(shapeFile.getName().substring(0, shapeFile.getName().indexOf(".")));
			result.setDbfFile(dbFile);
			result.setShapeFile(shapeFile);
			result.setProjectionFile(projectionFile);
			result.setShapeFileIndexFile(shapeFileIndexFile);
		}
		return result;
	}

}