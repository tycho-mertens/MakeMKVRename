package tycho.core.client.CustomFingerprintDB;

import tycho.core.client.fingerprint.Fingerprint;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class DBJsonFile {

    private static final Logger logger = Logger.getLogger(DBJsonFile.class.getName());

    private final File jsonFile;

    /**
     * Creates a new DBJsonFile if it doesn't exist yet and returns it
     *
     * @param season The season of the DBJsonFile
     * @param episode The episode of the DBJsonFile
     * @param dbFolder The DBFolder of the DBJsonFile
     * @return Returns the DBJsonFile that has been created or already existed
     */
    public static DBJsonFile createNewIfNotExists(int season, int episode, DBFolder dbFolder){
        File dbJsonFile = new File(dbFolder.getLocation(), "S" +
                (season < 10 ? "0" + season : season) +
                "E" +
                (episode < 10 ? "0" + episode : episode));

        if(!dbJsonFile.exists()){
            try{
                logger.info((dbJsonFile.createNewFile()
                        ? "Created new DBJsonFile: "
                        : "Failed to create new DBJsonFile: ") + dbJsonFile.getAbsolutePath());
            }catch(IOException e){
                logger.warning("IoException while creating new DBJsonFile: " + dbJsonFile.getAbsolutePath());
            }
        }
        return new DBJsonFile(dbJsonFile);
    }

    /**
     * Searches for a DBJsonFile with the given season and episode
     *
     * @param season The season of the DBJsonFile
     * @param episode The episode of the DBJsonFile
     * @param dbFolder The DBFolder of the DBJsonFile
     * @return Returns null if none is found, otherwise returns the object that has been found
     */
    public static DBJsonFile getBySeasonEpisode(int season, int episode, DBFolder dbFolder){
        for(DBJsonFile dbJsonFile : dbFolder.getDbJsonFileList()){
            if(dbJsonFile.getSeason() == season
                    && dbJsonFile.getEpisode() == episode) return dbJsonFile;

        }
        logger.info("No DbJsonFileFound for DBFolder: " + dbFolder.getLocation().getAbsolutePath());
        return null;
    }

    /**
     * Default constructor
     *
     * @param jsonFile The file (location) of the DBJsonFile
     */
    public DBJsonFile(File jsonFile){
        this.jsonFile = jsonFile;
    }

    /**
     * Default getter
     *
     * @return Returns the season of the DBJsonFile
     */
    public int getSeason(){
        return Integer.parseInt(jsonFile.getName().substring(1,3));
    }

    /**
     * Default getter
     *
     * @return Returns the episode of the DBJsonFile
     */
    public int getEpisode(){
        return Integer.parseInt(jsonFile.getName().substring(4,6));
    }

    /**
     * Default getter
     *
     * @return Returns the location of the DBJsonFile
     */
    public File getLocation(){
        return jsonFile;
    }

    /**
     * Default getter
     *
     * @return The fingerprint of the DBJsonFile
     */
    public Fingerprint getFingerprint(){
        try {
            return Fingerprint.fromJson(FileUtils.readFileToString(jsonFile, Charset.defaultCharset()));
        }catch(IOException e){
            logger.warning("Failed to read json file: " + jsonFile.getAbsolutePath());
            return null;
        }
    }

    /**
     * Save a fingerprint to the DBJsonFile
     *
     * @param fingerprint The fingerprint you want to save to the file
     */
    public void saveFingerprint(Fingerprint fingerprint){
        try {
            FileUtils.writeStringToFile(jsonFile, fingerprint.toJson(), Charset.defaultCharset());
            logger.info("Saved fingerprint to: " + jsonFile.getAbsolutePath());
        }catch(Exception e){
            logger.warning("Failed to save fingerprint to: " + jsonFile.getAbsolutePath());
        }
    }

}
