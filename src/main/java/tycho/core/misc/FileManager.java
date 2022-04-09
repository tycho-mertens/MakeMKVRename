package tycho.core.misc;

import tycho.core.config.Config;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Logger;

public class FileManager {

    private static FileManager instance;
    private static final Logger logger = Logger.getLogger(FileManager.class.getName());

    private final boolean isWindows;
    private final Config config;

    /**
     * Creates a new instance of FileManager if none exists and returns it,
     * otherwise return the current instance
     *
     * @return Returns the current instance of the class FileManager
     */
    public static synchronized FileManager getInstance(){
        if(instance == null)
            new FileManager();
        return instance;
    }

    private FileManager() {
        isWindows = SystemUtils.IS_OS_WINDOWS;
        config = Config.getInstance();
        instance = this;

        createDefaultTempDirectory();
    }

    /**
     * Gets the full (usable) path to the compare script
     *
     * @return Returns the path to the compare script in the resources folder
     */
    public String getAudioComparePath(){
        return getExternalResourceFile(config.asString(Config.AUDIO_COMPARE_PATH));
    }

    /**
     * Gets the full (usable) path to the python executable
     *
     * @return Returns the path to the python executable in the resources folder
     */
    public String getPythonExecutable(){
        return getExternalResourceFile(isWindows?
                config.asString(Config.PYTHON_PATH_WINDOWS)
                : config.asString(Config.PYTHON_PATH_LINUX));

    }

    /**
     * Gets the full (usable) path to the fpcalc executable
     *
     * @return Returns the path to the fpcalc executable in the resources folder
     */
    public String getFpCalcExecutable(){
        return getExternalResourceFile(config.asString(Config.FPCALC_PATH));
    }

    /**
     * Gets the full (usable) path to the ffmpeg executable
     *
     * @return Returns the path to the ffmpeg executable in the resources folder
     */
    public String getFfMpegExecutable(){
        return getExternalResourceFile(config.asString(Config.FFMPEG_PATH));
    }

    /**
     * Gets the full (usable) path of the given file path
     *
     * @param file The path to a file in the resources folder
     * @return Returns the full path to the given file, so we can use it properly
     */
    public String getExternalResourceFile(String file) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(file.
                replace("{os}", isWindows? "windows" : "linux") + (isWindows? ".exe" : ""));
        return Objects.requireNonNull(url).getPath();//Might throw an exception
    }

    /**
     * Gets the default temp directory location and returns it
     *
     * @return Returns the default temp directory as a file object
     */
    public File getDefaultTempDir(){
        return new File(Config.getProgramDirectory(), "temp");
    }

    /**
     * @return returns the directory where all the executables are stored
     */
    public File getExecutablesDir(){
        File dir = new File(getProgramDirectory(), "executables");
        if(!dir.exists() && !dir.mkdir())
                logger.warning("Failed to create executables directory");
        return dir;
    }
    /**
     * Gets the default directory location for this program
     * @return returns the default directory for this program
     */
    public File getProgramDirectory(){
        return Config.getProgramDirectory();
    }
    /**
     * Creates the default temp directory if it doesn't exist
     */
    private void createDefaultTempDirectory(){
        if(getDefaultTempDir().mkdirs()){
            logger.info("Created default temp directory");
            return;
        }
        if(!getDefaultTempDir().exists())
            logger.warning("Failed to create default temp directory, might result in a lot of errors");
    }

    /**
     * Creates a temp directory with a random name and returns it
     *
     * @return Returns the newly created temp directory, so you can use it
     */
    public File createTempDirectory(){
        return createTempDirectory("","");
    }

    /**
     * Creates a temp directory with a prefix and suffix and a random string in the middle and returns it
     *
     * @param prefix Prefix for the name of the new temp directory
     * @param suffix Suffix for the name of the new temp directory
     * @return Returns the newly created temp directory, so you can use it
     */
    public File createTempDirectory(String prefix, String suffix){
        String dirName = prefix + RandomStringUtils.randomAlphanumeric(20, 50) + suffix;
        File tempDir = new File(getDefaultTempDir(), dirName);

        //Create the tempdir and log it for debugging purposes
        if(tempDir.mkdirs()){
            logger.info("Created new temp directory: " + dirName);
        }else{
            logger.warning("Failed to create new temp directory: " + tempDir.getAbsolutePath());
        }

        return tempDir;
    }

    /**
     * Creates a temp directory in the given directory location
     *
     * @param tempDir The location you want the new temp dir to be in
     * @return Returns a file object that points to the new temp dir
     */
    public File createTempDirectory(File tempDir){
        String dirName = RandomStringUtils.randomAlphanumeric(20, 50);
        File ret = new File(tempDir, dirName);

        //Create the tempdir and log it for debugging purposes
        if(ret.mkdirs()){
            logger.info("Created new temp directory: " + dirName);
        }else{
            logger.warning("Failed to create new temp directory: " + tempDir.getAbsolutePath());
        }
        return ret;
    }

    /**
     * Creates a temp file with a random name and the given extension/suffix in the default temp directory
     *
     * @param suffix Suffix for the name of the new temp file, you can look at this as the extension (ex. '.exe', '.jpg', ...)
     * @return Returns the newly created temp file, so you can use it
     */
    public File createTempFile(String suffix){
        return createTempFile("", suffix);
    }

    /**
     * Creates a temp file with a random name, the given prefix and extension/suffix in the given temp directory
     *
     * @param prefix Prefix for the name of the new temp file
     * @param suffix Suffix for the name of the new temp file, you can look at this as the extension (ex. '.exe', '.jpg', ...)
     * @param tempDir The temp directory where the new temp file will be created
     * @return Returns the newly created temp file, so you can use it
     */
    public File createTempFile(String prefix, String suffix, File tempDir){
        String fileName = prefix + RandomStringUtils.randomAlphanumeric(20, 50) + suffix;
        File tempFile = new File(tempDir, fileName);

        //Try to create the new temp file and handle to exception if needed
        try {
            if(tempFile.createNewFile()){
                logger.info("New temp file created: " + tempFile.getAbsolutePath());
            }else{
                logger.warning("Failed to create new temp file: " + tempFile.getAbsolutePath());
            }
        } catch (IOException e) {
            logger.warning("Error while creating new temp file:  " + e);
        }
        return tempFile;
    }

    /**
     * Creates a temp file with a random name and the given extension/suffix in the given temp directory
     *
     * @param suffix Suffix for the name of the new temp file, you can look at this as the extension (ex. '.exe', '.jpg', ...)
     * @param tempDir The temp directory where the new temp file will be created
     * @return Returns the newly created temp file, so you can use it
     */
    public File createTempFile(String suffix, File tempDir){
        return createTempFile("", suffix, tempDir);
    }

    /**
     * Creates a temp file with a random name, the given prefix and extension/suffix in the default temp directory
     *
     * @param prefix Prefix for the name of the new temp file
     * @param suffix Suffix for the name of the new temp file, you can look at this as the extension (ex. '.exe', '.jpg', ...)
     * @return Returns the newly created temp file, so you can use it
     */
    public File createTempFile(String prefix, String suffix){
        return createTempFile(prefix, suffix, getDefaultTempDir());
    }

    /**
     * Removes all the files in the given temp directory
     *
     * @param tempDir Temp directory to remove all the files from
     */
    public void clearTempDirectory(File tempDir){
        for (File file : Objects.requireNonNull(tempDir.listFiles())) {
            if(!file.delete()){
                logger.info("Failed to delete temp file: " + file.getAbsolutePath());
            }
        }
    }

    /**
     * Deletes the given temp directory
     *
     * @param tempDir Temp directory to delete
     */
    public void deleteTempDirectory(File tempDir){
        try {
            FileUtils.deleteDirectory(tempDir);
            logger.info("Deleted temp dir: " + tempDir.getAbsolutePath());
        }catch(IOException e){
            logger.warning("Error while deleting temp directory: " + e);
        }
    }

    /**
     * Removes all the files in the default temp directory
     */
    public void clearDefaultTempDirectory(){
        deleteTempDirectory(getDefaultTempDir());
    }
}
