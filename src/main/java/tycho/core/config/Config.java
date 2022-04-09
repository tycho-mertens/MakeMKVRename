package tycho.core.config;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class Config {

    private static final Logger logger = Logger.getLogger(Config.class.getName());

    public static final String TMDB_API = "tmdb_api";
    public static final String JSON_DB = "json_db";

    public static final String FFMPEG_PATH = "ffmpeg_path";
    public static final String FPCALC_PATH = "fpcalc_path";

    public static final String PYTHON_PATH_LINUX = "python_path_linux";
    public static final String PYTHON_PATH_WINDOWS = "python_path_windows";

    public static final String AUDIO_COMPARE_PATH = "audio_compare_path";
    public static final String CSS_STYLE = "css_style";
    public static final String EPISODE_RENAME_EXPRESSION = "episode_rename_expression";

    public static final String LOW_SIMILARITY_THRESHOLD = "low_similarity_threshold";
    public static final String LOW_SIMILARITY_COLOR = "low_similarity_color";
    public static final String DARKEN_SELECTED_ROW_PERCENTAGE = "darken_selected_row_percentage";
    public static final String HIGHLIGHTED_ROW_COLOR = "highlighted_row_color";

    public static final String PRE_LOADER_OPACITY = "pre_loader_opacity";
    public static final String PRE_LOADER_BACKGROUND_COLOR = "pre_loader_background_color";

    private static Config instance;

    private Configuration config;
    private final File configFile;
    private static String programName = "MakeMkvRenameV2";

    /**
     * Creates the instance of the class Config if none exists and returns it,
     * otherwise return the current instance
     *
     * @return Returns the current instance of the Config
     */
    public static synchronized Config getInstance() {
        if (instance == null)
            try {
                instance = new Config(new File(getProgramDirectory(), "config.json").getAbsolutePath());
            } catch (Exception e) {
                logger.warning("Failed to create a new config instance: " + e);
            }
        return instance;
    }

    public static synchronized Config getTestInstance(){
        if(instance == null)
            try{
                instance = new Config("/home/user/testConfig.json");
            }catch(Exception e){
                logger.warning("Failed to create a new config instance: " + e);
            }
        return instance;
    }


    /**
     * Default constructor
     *
     * @param configFile The location of the config file
     */
    private Config(String configFile) {
        this.configFile = new File(configFile);
        instance = this;

        //Create the config file if it doesn't exist or load from the file if it does exist
        if (this.configFile.exists()) {
            loadFromFile();
        }else {
            config = new Configuration();
            setDefaultConfig();
            save();
        }

        //Create the program's default directory if it doesn't exist
        if(!getProgramDirectory().exists()){
            if(!getProgramDirectory().mkdirs()){
                logger.warning("Failed to create the default folder for this program, this might result in a lot of warninging and or crashes.");
            }
        }

    }

    /**
     * Set the name of the program, mainly used for debugging purposes and junit tests
     * @param name The name you would like for this program
     */
    public void setProgramName(String name){
        programName = name;
    }

    /**
     * Default getter
     * @return Returns the name of this program
     */
    public String getProgramName(){
        return programName;
    }

    /**
     * Gets the default directory for this program in the user's home folder (ex. '/home/user/.ProgramName')
     *
     * @return Returns the default program folder in the user home directory
     */
    public static File getProgramDirectory() {
        return new File(System.getProperty("user.home"), "." + programName);
    }


    /**
     * Sets the default configuration settings
     */
    private void setDefaultConfig() {
        addProperty(TMDB_API, "7d0ce7f30799c80edb670f24efce975c");
        addProperty(JSON_DB, new File(getProgramDirectory(), "JsonDB").getAbsolutePath());
        addProperty(FFMPEG_PATH, "Tools/{os}/ffmpeg");
        addProperty(FPCALC_PATH, "Tools/{os}/fpcalc");
        addProperty(PYTHON_PATH_LINUX, "Tools/linux/python/bin/python3.8");
        addProperty(PYTHON_PATH_WINDOWS, "Tools/windows/Scripts/python.exe");
        addProperty(AUDIO_COMPARE_PATH, "Tools/audio_compare.py");
        addProperty(CSS_STYLE, "dracula.css");
        addProperty(EPISODE_RENAME_EXPRESSION, "{n} - {s00e00} - {t}");
        addProperty(LOW_SIMILARITY_THRESHOLD, 0.85f);
        addProperty(DARKEN_SELECTED_ROW_PERCENTAGE, 0.35f);
        addProperty(LOW_SIMILARITY_COLOR, "#941515");
        addProperty(HIGHLIGHTED_ROW_COLOR, "#4869a7");
        addProperty(PRE_LOADER_OPACITY, 0.75d);
        addProperty(PRE_LOADER_BACKGROUND_COLOR, "#565656");
    }

    /**
     * Loads the default settings
     */
    public void loadDefaultSettings(){
        setDefaultConfig();
    }

    /**
     * Loads the config from the 'configFile', which was set in the constructor
     */
    private void loadFromFile(){
        try {
            config = new Gson().fromJson(FileUtils.readFileToString(configFile, Charset.defaultCharset())
                    , Configuration.class);
            logger.info("Loaded new config from: " + configFile.getAbsolutePath());
        }catch(IOException e){
            logger.warning("Failed to load config from file: " + configFile.getAbsolutePath());
            e.printStackTrace();
        }
    }

    /**
     * Saves the config to 'configFile', which was set in the constructor
     */
    public void save(){
        try {
            FileUtils.writeStringToFile(configFile,
                    new Gson().toJson(config, Configuration.class), Charset.defaultCharset(), false);
            logger.info("Saved config to: " + configFile.getAbsolutePath());
        }catch(IOException e){
            logger.info("Failed to save config to file: " + configFile.getAbsolutePath());
            e.printStackTrace();
        }
    }

    /**
     * Adds the new property with the assigned value to the config,
     * you still have to save the config though
     *
     * @param property The name of the config option
     * @param value    The value of the config option
     */
    public void addProperty(String property, Object value) {
        config.getProperties().put(property, value);
    }

    /**
     * Gets the value of the given property and returns it as an object
     *
     * @param property The name of the config option
     * @return Returns the config option value as an object
     */
    public Object getProperty(String property) {
        return config.getProperties().get(property);
    }

    /**
     * Gets the value of the given property and returns it as a string
     *
     * @param property The name of the config option
     * @return Returns the config option value as a string
     */
    public String asString(String property) {
        return (String) getProperty(property);
    }

    /**
     * Gets the value of the given property and returns it as an integer
     *
     * @param property The name of the config option
     * @return Returns the config option value as an integer
     */
    public int asInteger(String property) {
        return (int) getProperty(property);
    }

    /**
     * Gets the value of the given property and returns it as a double
     *
     * @param property The name of the config option
     * @return Returns the config option value as a double
     */
    public double asDouble(String property) {
        return (double) getProperty(property);
    }

    /**
     * Gets the value of the given property and returns it as an object
     *
     * @param property The name of the config option
     * @return Returns the config option value as a float
     */
    public float asFloat(String property) {
        //We have to parse the float here and not cast it because it can't be cast to a float
        return Float.parseFloat("" + getProperty(property));
    }

    /**
     * Gets the value of the given property and returns it as a boolean
     *
     * @param property The name of the config option
     * @return Returns the config option value as a boolean
     */
    public boolean asBoolean(String property) {
        return (boolean) getProperty(property);
    }
}
