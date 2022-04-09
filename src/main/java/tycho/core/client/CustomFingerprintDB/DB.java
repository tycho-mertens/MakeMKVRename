package tycho.core.client.CustomFingerprintDB;

import tycho.core.client.fingerprint.Fingerprint;
import tycho.core.config.Config;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class DB {

    private static final Logger logger = Logger.getLogger(DB.class.getName());

    private final File dbLocation;
    private static DB instance;

    public static synchronized DB getInstance(){
        if(instance == null)
            instance = new DB(new File(Config.getInstance().asString(Config.JSON_DB)));
        return instance;
    }

    /**
     * Create a database object from the file location
     *
     * @param dbLocation The location of the database
     */
    private DB(File dbLocation){
        this.dbLocation = dbLocation;
    }

    /**
     * Default getter
     *
     * @return Returns all the DBFolder objects in the database
     */
    public List<DBFolder> getDbFolders(){
        return Arrays.stream(Objects
                .requireNonNull(dbLocation.listFiles(File::isDirectory)))
                .map(DBFolder::new).toList();
    }

    /**
     * Default getter
     *
     * @return Returns the location of the database file
     */
    public File getDbLocation(){
        return dbLocation;
    }

    /**
     * Search for a TV Show by its TMdb ID
     *
     * @param tmdbId The TMdbID of the TV Show
     * @return Returns a DBFolder of the TV Show you searched for
     */
    public DBFolder byTMdbId(int tmdbId){
        return DBFolder.getById(tmdbId, this);
    }


    /**
     * Adds a fingerprint to the database, in the correct DBFolder (location)
     *
     * @param fingerprint The fingerprint you want to add to the DB
     */
    public void addFingerprint(Fingerprint fingerprint){
        DBFolder dbFolder = DBFolder.createNewIfNotExists(fingerprint.getTmdbId(), this);
        DBJsonFile dbJsonFile = DBJsonFile.createNewIfNotExists(
                fingerprint.getSeason(), fingerprint.getEpisode(), dbFolder);
        dbJsonFile.saveFingerprint(fingerprint);
    }
}

