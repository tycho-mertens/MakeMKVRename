package tycho.core.client.CustomFingerprintDB;

import tycho.core.client.fingerprint.Fingerprint;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class DBFolder {

    private static final Logger logger = Logger.getLogger(DBFolder.class.getName());

    private final File folderLocation;

    /**
     * Creates a new DBFolder for the TMdbID if it doesn't exist yet and returns it
     *
     * @param tmdbId The TMdbID of the TV Show
     * @param db The database object
     * @return Returns the DBFolder for the TMdbID
     */
    public static DBFolder createNewIfNotExists(int tmdbId, DB db){
        File dbFolder = new File(db.getDbLocation(), String.valueOf(tmdbId));

        if(!dbFolder.exists()){
            logger.info((dbFolder.mkdirs()
                    ? "Created new DBFolder with id: "
                    : "Failed to create new DBFolder with id: ") + tmdbId);
        }

        return new DBFolder(dbFolder);
    }

    public List<Fingerprint> getAllFingerprintsForSeason(int season){
        return getDbJsonFileList().stream().map(DBJsonFile::getFingerprint)
                .filter(fingerprint -> fingerprint.getSeason() == season).toList();
    }


    /**
     * Finds the DBFolder object by a TMdbID
     *
     * @param tmdbId The TMdbID of the TV Show
     * @param db The database
     * @return Returns the DBFolder object from the TV Show
     */
    public static DBFolder getById(int tmdbId, DB db){
        List<DBFolder> folders;

        try {
            folders = db.getDbFolders();
        }catch(NullPointerException e){
            return null;
        }

        for(DBFolder folder : folders){
            if(folder.getTMdbId() == tmdbId){
                return folder;
            }
        }
        logger.info("No entry found by this TMdb ID: " + tmdbId);
        return null;
    }

    /**
     * Default constructor
     *
     * @param folderLocation The location of the DBFolder
     */
    public DBFolder(File folderLocation){
        this.folderLocation = folderLocation;
    }

    /**
     * Default getter
     *
     * @return Returns a list of all the DBJsonFile objects in the DBFolder
     */
    public List<DBJsonFile> getDbJsonFileList(){
        return Arrays.stream(Objects
                .requireNonNull(folderLocation.listFiles(File::isFile)))
                .map(DBJsonFile::new).toList();
    }

    /**
     * Default getter
     *
     * @return Returns the location of the current DBFolder
     */
    public File getLocation(){
        return folderLocation;
    }

    /**
     * Default getter
     *
     * @return Returns the TMdbID of the DBFolder
     */
    public int getTMdbId(){
        return Integer.parseInt(folderLocation.getName());
    }

    /**
     * Searches for the DBJsonFile that matches the given season and episode
     *
     * @param season The season of the DBJsonFile you're searching for
     * @param episode The episode of the DBJsonFile you're searching for
     * @return Returns the DBJsonFile that matches the parameters
     */
    public DBJsonFile bySeasonEpisode(int season, int episode){
        return DBJsonFile.getBySeasonEpisode(season, episode, this);
    }
}
