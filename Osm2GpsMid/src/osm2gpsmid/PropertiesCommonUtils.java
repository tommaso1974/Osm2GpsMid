package osm2gpsmid;
/**
 *
 * @author tommaso
 */
public class PropertiesCommonUtils {
    
    /*
    midlet.name = GPSmid-MyTown
    map.name = GPSmid-MyTown
    mapSource = /path/to/file.osm.pbf
    * 
    the upper value to "region.1.lat.min",
    the lower value to "region.1.lat.max",
    the left value to "region.1.lon.min" and
    the right value to "region.1.lon.max". 

* //Ho deciso di limitare la gestione solamente ad un elemento rettangolare
*region.1.lat.min = -90
 region.1.lat.max = 90
 region.1.lon.min = -180
 region.1.lon.max = 180

 * 
style-file = style-file.xml
* app = GpsMid-Generic-full
* keepTemporaryFiles = true
* useRouting = motorcar,bicycle,foot

* useSounds = wav, mp3, amr
* useSoundFilesWithSyntax = my_sounds
* 
* useUrlTags=true
* usePhoneTags=true
* enableEditing=true
app = GpsMid-Generic-editing
* lang=de
lang=de;fr;en
* dontCompress=.wav,.amr,.mp3,.ogg

* http://sourceforge.net/apps/mediawiki/gpsmid/index.php?title=Properties_file
    * 
    * 
    * 
    */
 
    public static final String APP = "app";
    public static final String MIDDLE_MAP_NAME = "midlet.name";
    public static final String USE_ROUTING = "useRouting";
    public static final String MAP_SOURCE = "mapSource";
    public static final String MAP_NAME = "map.name";
    public static final String MAX_LAT = "region.lat.max";
    public static final String MIN_LAT = "region.lat.min";
    public static final String MAX_LON = "region.lon.max";
    public static final String MIN_LON = "region.lon.min";
    public static final String APPLICATION_TYPE = "app";
    public static final String USE_SOUND_WITH_FILES = "useSounds";
    public static final String LANG = "lang";
    public static final String USE_LANG = "useLang";
    public static final String LANG_NAME = "langName";
    public static final String USER_LANG_NAME = "useLangName";
    public static final String DONT_COMPRESS_FILES_LIST = "dontCompress";
    public static final String USE_SOUND_FILES_WITH_SYNTAX = "useSoundFilesWithSyntax";
    
    public static final String ADD_TO_MANIFEST = "addToManifest";
    public static final String USE_USER_TAGS = "useUrlTags";
    public static final String USER_PHONE_TAGS = "usePhoneTags";
    public static final String USE_HOUSE_NUMBERS = "useHouseNumbers";
    public static final String USE_WORD_SERACH = "useWordSearch";
    
    public static final String USE_ICONS = "useIcons";
    public static final String STYLE_FILE = "style-file";
    
}
