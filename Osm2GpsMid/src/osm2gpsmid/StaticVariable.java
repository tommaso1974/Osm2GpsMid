/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package osm2gpsmid;

import osmToGpsMid.Configuration;

/**
 *
 * @author nexse
 */
public class StaticVariable {
    
    private static StaticVariable _getInstance = null;

    private static boolean _createSuccessfully;
    private static boolean _compressed;
    private static Configuration _config;
    private static String _dontCompress[];
     
    public static synchronized StaticVariable getGetInstance() {
        if(_getInstance ==  null){
            _getInstance = new StaticVariable();
        }
        return _getInstance;
    }
    
    private StaticVariable(){
        _createSuccessfully = false;
        _compressed = true;
        _config = null;
        _dontCompress = null;
    }

    public boolean isCreateSuccessfully() {
        return _createSuccessfully;
    }

    public synchronized void setCreateSuccessfully(boolean createSuccessfully) {
        _createSuccessfully = createSuccessfully;
    }

    public boolean isCompressed() {
        return _compressed;
    }

    public synchronized void setCompressed(boolean compressed) {
        _compressed = compressed;
    }
    
    public Configuration getConfig() {
        return _config;
    }

    public synchronized  void setConfig(Configuration config) {
        _config = config;
    }
 
    public String[] getDontCompress() {
        return _dontCompress;
    }

    public synchronized void setDontCompress(String[] dontCompress) {
        _dontCompress = dontCompress;
    }
    
}
