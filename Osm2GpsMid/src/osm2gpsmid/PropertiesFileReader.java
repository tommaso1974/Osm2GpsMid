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

    private final String FALSE = "false";
    private final String TRUE = "true";
    
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
            System.out.println("Eccezzione nel leggere il file di properties " + filePath);
            System.out.println("Eccezzione " + ex.toString());
        }

        return retValue;
    }

    private void extractPropertyFilePath(String filePath) {
        _propertyPath = OsmCommonUtils.extractFilePath(filePath);
        String fileName = OsmCommonUtils.extractFileNameWithoutExtension(filePath);
        _propertyPathSubFolder = OsmCommonUtils.concatenePath(_propertyPath, fileName);

        _propertyPathSubFolder = OsmCommonUtils.removeFileExtension(_propertyPathSubFolder);
    }

    public String getApplicationType() {
        return _prop.getProperty(PropertiesCommonUtils.APPLICATION_TYPE);
    }

    public String filesDontCompressList() {
        return _prop.getProperty(PropertiesCommonUtils.DONT_COMPRESS_FILES_LIST);
    }

    public String languageList() {
        return _prop.getProperty(PropertiesCommonUtils.LANG);
    }

    public String mapName() {
        return _prop.getProperty(PropertiesCommonUtils.MAP_NAME);
    }

    public String maxLat() {
        return _prop.getProperty(PropertiesCommonUtils.MAX_LAT);
    }
    
    public float maxLatFloat() {
        String tmp = maxLat();
        return Float.valueOf(tmp);
 
    }

    public String minLat() {
        return _prop.getProperty(PropertiesCommonUtils.MIN_LAT);
    }
    
    public float minLatFloat() {
        String tmp = minLat();
        return Float.valueOf(tmp);
    }
    
    public String maxLon() {
        return _prop.getProperty(PropertiesCommonUtils.MAX_LON);
    }

    public float maxLonFloat() {
        String tmp = maxLon();
        return Float.valueOf(tmp);
    }

    public String minLon() {
        return _prop.getProperty(PropertiesCommonUtils.MIN_LON);
    }

    public float minLonFloat() {
        String tmp = minLon();
        return Float.valueOf(tmp);
    }
        
    public String middleMapName() {
        return _prop.getProperty(PropertiesCommonUtils.MIDDLE_MAP_NAME);
    }

//    public String SourceFilePath() {
//        return _prop.getProperty(PropertiesCommonUtils.ORIGINAL_MAP_PATH);
//    }

    public String soundsSourceFilesType() {
        return _prop.getProperty(PropertiesCommonUtils.USE_SOUND_WITH_FILES);
    }

    public String userRouting(){
        return _prop.getProperty(PropertiesCommonUtils.USE_ROUTING);
    }
    
    public String mapSource(){
        return _prop.getProperty(PropertiesCommonUtils.MAP_SOURCE);
    }
    
    public String useSoundFilesWithSyntax(){
        return  _prop.getProperty(PropertiesCommonUtils.USE_SOUND_FILES_WITH_SYNTAX);
    }
    
    public String dontCompress(){
        return  _prop.getProperty(PropertiesCommonUtils.DONT_COMPRESS_FILES_LIST);
    }

    public String app(){
        return  _prop.getProperty(PropertiesCommonUtils.APP);
    }
    
    public String dontCompressLowerCase() {

        String dontCompress = dontCompress();
        
        if(dontCompress == null){
            return "";
        }
        
        if (dontCompress.isEmpty()) {
            return dontCompress;
        }

        return dontCompress.toLowerCase();
    }

    public String lang() {
        return _prop.getProperty(PropertiesCommonUtils.LANG);
    }
    
    public String userLang() {
        return _prop.getProperty(PropertiesCommonUtils.USE_LANG);
    }

    public String langName() {
        return _prop.getProperty(PropertiesCommonUtils.LANG_NAME);
    }
    
    public String userLangName(){
        return _prop.getProperty(PropertiesCommonUtils.USER_LANG_NAME);
    }

    public String style_file() {
        return _prop.getProperty(PropertiesCommonUtils.STYLE_FILE);
    }

    public String addToManifest(){
        return _prop.getProperty(PropertiesCommonUtils.ADD_TO_MANIFEST);
    }

    public boolean useUserTags() {
        String tmp = _prop.getProperty(PropertiesCommonUtils.USE_USER_TAGS, FALSE);
        return Boolean.valueOf(tmp);
    }
    
    public boolean usePhoneTags() {
        String tmp = _prop.getProperty(PropertiesCommonUtils.USER_PHONE_TAGS, TRUE);
        return Boolean.valueOf(tmp);
    }
       
    public boolean useHouseNumbers() {
        String tmp = _prop.getProperty(PropertiesCommonUtils.USER_PHONE_TAGS, TRUE);
        return Boolean.valueOf(tmp);
    }

    public boolean useWordSearch() {
        String tmp = _prop.getProperty(PropertiesCommonUtils.USE_WORD_SERACH, TRUE);
        return Boolean.valueOf(tmp);
    }

    public String useIcons() {
        return _prop.getProperty(PropertiesCommonUtils.USE_ICONS, FALSE);
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
