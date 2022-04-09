package tycho.core.client.fingerprint;

import io.jsondb.annotation.Document;
import tycho.core.misc.FileManager;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Document(collection = "fingerprints", schemaVersion = "1.0")
public class Fingerprint {

    private static final Logger logger = Logger.getLogger(Fingerprint.class.getName());

    private final double duration;
    private List<Long> fingerprint;
    private int tmdbId;
    private int season, episode;
    private String id;
    /**
     * Default constructor
     *
     * @param duration The duration of the fingerprint
     */
    private Fingerprint(double duration) {
        this.duration = duration;
        fingerprint = new ArrayList<>();
        tmdbId = 0;
        season = 0;
        episode = 0;
        id = UUID.randomUUID().toString();
    }

    /**
     * Default getter
     *
     * @return Returns the duration of the fingerprint
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Default getter
     *
     * @return Returns the fingerprint as a list of 'longs'
     */
    public List<Long> getFingerprint() {
        return fingerprint;
    }

    /**
     * Default setter
     *
     * @param fingerprint The fingerprint you want to set
     */
    public void setFingerprint(List<Long> fingerprint) {
        this.fingerprint = fingerprint;
    }

    /**
     * Default getter
     *
     * @return Returns the current TMdbId
     */
    public int getTmdbId() {
        return tmdbId;
    }

    /**
     * Default setter
     *
     * @param tmdbId Sets the TMdbid
     */
    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    /**
     * Default getter
     *
     * @return Returns the current season
     */
    public int getSeason() {
        return season;
    }

    /**
     * Default setter
     *
     * @param season Sets the season
     */
    public void setSeason(int season) {
        this.season = season;
    }

    /**
     * Default getter
     *
     * @return Returns the current episode
     */
    public int getEpisode() {
        return episode;
    }

    /**
     * Default setter
     *
     * @param episode Sets the current episode
     */
    public void setEpisode(int episode) {
        this.episode = episode;
    }

    /**
     * Writes the current fingerprint to a file, so we can use it later in the audio compare script
     *
     * @return Returns the file with the fingerprint in it, you can use this file in the audio compare script
     * @throws IOException Throws an exception when it fails to write to the hdd
     */
    private File writeToTempFile() throws IOException {
        FileManager fManager = FileManager.getInstance();
        //Create temp file
        File tempFile = fManager.createTempFile("fingerprint_", ".json");

        //Write fingerprint to temp file
        FileWriter w = new FileWriter(tempFile);
        w.write(this.toJson());
        w.close();
        return tempFile;
    }


    /**
     * Returns the current object 'Fingerprint' as a json string
     *
     * @return Returns the current object 'Fingerprint' as a json string
     */
    public String toJson(){
        return new Gson().toJson(this, Fingerprint.class);
    }

    /**
     * Reconstruct an object 'Fingerprint' from the given json string and return that object
     *
     * @param json The 'Fingerprint' object as a json string
     * @return Returns an object 'Fingerprint' constructed from the given json string
     */
    public static Fingerprint fromJson(String json){
        return new Gson().fromJson(json, Fingerprint.class);
    }

    //TODO database interactions

    /**
     * Compares two fingerprints which one another and returns a percentage of likeness
     *
     * @param fingerprint The fingerprint you want to compare with the current fingerprint
     * @return Returns a percentage as a float, between 0 and 1. It represents the likeness of two fingerprints
     */
    public float compare(Fingerprint fingerprint){
        try{

            FileManager fManager = FileManager.getInstance();

            //Write the fingerprint to a temp file, so we can use it in the python compare script
            File fOne = writeToTempFile();
            File fTwo = fingerprint.writeToTempFile();

            //Create a process to start the compare script
            Process proc = Runtime.getRuntime().exec(new String[]{fManager.getPythonExecutable(),
                                fManager.getAudioComparePath(), fOne.getAbsolutePath(), fTwo.getAbsolutePath()});
            //Wait for the compare process to complete
            proc.waitFor();

            //Read the results
            InputStreamReader isr = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            //Parse the results to a float, so we can use it
            float result = Float.parseFloat(br.readLine().split(":")[1].strip());
            logger.info("Similarity: " + result);

            //Cleanup
            if(!fOne.delete() || !fTwo.delete()){
                logger.info("Failed to cleanup one or more fingerprints");
            }

            return result;
        }catch(IOException | InterruptedException e){
            logger.warning("Failed to compare audio: " + e);
        }
        return 0;
    }
}
