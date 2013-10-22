/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package osm2gpsmid;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author tommaso
 */
public class PropertiesFileReader {

    private Properties _prop = null;
    private String _propertyPath;
    private String _propertyPathSubFolder;
    public PropertiesFileReader() {
        _prop = new Properties();
    }

    public boolean readProperty(String filePath) {

        boolean retValue = false;
        try {
            //load a properties file
            _prop.load(new FileInputStream(filePath));
            extractPropertyFilePath(filePath);
            
            retValue = true;
        } catch (IOException ex) {
            retValue = false;
        }

        return retValue;
    }

    private void extractPropertyFilePath(String filePath){
        _propertyPath = OsmCommonUtils.extractFilePath(filePath);
        String fileName = OsmCommonUtils.extractFileNameWithoutExtension(filePath);
        _propertyPathSubFolder = OsmCommonUtils.concatenePath(_propertyPath, fileName);
        
        _propertyPathSubFolder = OsmCommonUtils.removeFileExtension(_propertyPathSubFolder);
    }
    
//    public String getPropertyFilePath(){
//        return _propertyPath;
//    }
// 
//    public String getPropertyFilePathWithSubFolder(){
//        return _propertyPathSubFolder;
//    }
//    
//    public String getOsmFileName() {
//        return _prop.getProperty(PropertiesCommonUtils.OSM_FILE_NAME);
//    }
//
//    public String getOsmFilePath() {
//
//        String retValue = "";
//
//        switch (OsmCommonUtils.getPathByOS()) {
//            case 0: {
//                retValue = _prop.getProperty(PropertiesCommonUtils.OSM_FILE_PATH_LINUX);
//                break;
//            }
//
//            case 1: {
//                retValue = _prop.getProperty(PropertiesCommonUtils.OSM_FILE_PATH_WINDOWS);
//                break;
//            }
//        }
//
//        return retValue;
//    }
//
//    public boolean loadOsmInDB() {
//        String loadDataInDb = _prop.getProperty(PropertiesCommonUtils.LOAD_OSM_IN_DATABASE);
//        return Boolean.valueOf(loadDataInDb);
//    }
//
//    public boolean isLogDisabled() {
//        String isDisabled = _prop.getProperty(PropertiesCommonUtils.DISABLED_LOG);
//        return Boolean.valueOf(isDisabled);
//    }
//
//    public int nodes2Commit() {
//        return row2Commit(PropertiesCommonUtils.NODE_ROWS_COMMIT);
//    }
//
//    public int way2Commit() {
//        return row2Commit(PropertiesCommonUtils.WAY_ROWS_COMMIT);
//    }
//
//    public int relation2Commit() {
//        return row2Commit(PropertiesCommonUtils.RELATION_ROWS_COMMIT);
//    }
//
//    private int row2Commit(String properties) {
//        String row2Commit = _prop.getProperty(properties);
//        return Integer.valueOf(row2Commit);
//    }
//
//    public boolean buildCompliteMap() {
//        String buildCompliteMap = _prop.getProperty(PropertiesCommonUtils.BUILD_COMPLITE_MAP);
//        return Boolean.valueOf(buildCompliteMap);
//    }
//
//    public boolean buildDecimatorMap() {
//        String buildDecimatorMap = _prop.getProperty(PropertiesCommonUtils.BUILD_DECIMETOR_MAP);
//        return Boolean.valueOf(buildDecimatorMap);
//    }
//
//    public boolean removedPoiWithoutName() {
//        String removed = _prop.getProperty(PropertiesCommonUtils.REMOVED_POI_WITHOUT_NAME);
//        return Boolean.valueOf(removed);
//    }
//
//    public boolean usingRTree() {
//        String removed = _prop.getProperty(PropertiesCommonUtils.USING_RTREE);
//        return Boolean.valueOf(removed);
//    }
//
//    public boolean usingPoiFileList() {
//        String removed = _prop.getProperty(PropertiesCommonUtils.PROCESS_POI_WAY_LIST_FROM_FILE);
//        return Boolean.valueOf(removed);
//    }
//
//    public String folderFilesName(){
//        return _prop.getProperty(PropertiesCommonUtils.OSM_FILES_FOLDER_NAME);
//    }
//    
//    public String poiFileListPath() {
//        return _prop.getProperty(PropertiesCommonUtils.PROCESS_POI_WAY_LIST_FILE_PATH);
//    }
//
//    public boolean areBuildAggregation() {
//        String removed = _prop.getProperty(PropertiesCommonUtils.ENABLED_BUILD_AGGREGATION);
//        return Boolean.valueOf(removed);
//    }
//
//    public boolean readingWayFromFile() {
//        String waysFromFile = _prop.getProperty(PropertiesCommonUtils.PROCESS_POI_WAY_LIST_FROM_FILE);
//        return Boolean.valueOf(waysFromFile);
//    }
//
//    public String getPoiWayNodeListFromFile() {
//        return _prop.getProperty(PropertiesCommonUtils.PROCESS_POI_WAY_LIST_FILE_PATH);
//    }
//    
//    public boolean enabledProcessGeometry(){
//        String waysFromFile = _prop.getProperty(PropertiesCommonUtils.ENABLED_PROCESS_GEOMETRY);
//        return Boolean.valueOf(waysFromFile);
//    }
}
