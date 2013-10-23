/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package osm2gpsmid;

import java.io.File;

/**
 *
 * @author tommaso
 */
public class OsmCommonUtils {

    public static String concatenePath(String path, String name) {

        String tmp = path;
        if (!path.endsWith(File.separator)) {
            tmp += File.separator;
        }
        tmp += name;

        return tmp;
    }

    public static String extractFileName(String fileName) {
        return new File(fileName).getName();
    }

    public static String extractFileNameWithoutExtension(String fileName) {
        return removeFileExtension(new File(fileName).getName());
    }

    public static String extractFilePath(String fileName) {

        String retValue = fileName;
        int pos = retValue.lastIndexOf(File.separator);
        if (pos > 0) {
            retValue = retValue.substring(0, pos + 1);
        }

        return retValue;
    }

    public static String removeFileExtension(String fileName) {
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            return fileName.substring(0, pos);
        }

        return fileName;
    }

    public static int getPathByOS() {
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            return 1;
        }

        //     return "/home/tommaso/osm/";
        return 0;
    }
    
    public static String getBaseFolder(){
         
        if(getPathByOS() == 0)
            return "/tmp";
        
        return "C:\\";
    }
}
