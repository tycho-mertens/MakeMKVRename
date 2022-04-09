package tycho.core.client.fingerprint;

import tycho.core.client.CustomFingerprintDB.DB;
import tycho.core.client.CustomFingerprintDB.DBFolder;
import tycho.core.misc.AlertUtils;
import tycho.core.misc.FingerprintUtils;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class FingerprintMatcher {

    private static final Logger logger = Logger.getLogger(FingerprintMatcher.class.getName());

    private final List<MatcherCallable> fingerprints;
    private final Label fingerprintLbl;
    ExecutorService executorService;

    private final CounterRef counterRef;

    public FingerprintMatcher(Label fingerprintLbl){
        fingerprints = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(5);
        counterRef = new CounterRef();
        this.fingerprintLbl = fingerprintLbl;
    }

    /**
     * Converts the videoFile to a fingerprint and adds it to the fingerprints list
     *
     * @param videoFile The video file you want to add
     * @param tmdbId The TMdbId of the TV Show
     */
    public void add(File videoFile, int tmdbId, int season){
        Fingerprint matchTo = FingerprintUtils.createFingerprintFromWav(FingerprintUtils.transcodeToAudio(videoFile));

        DB db = DB.getInstance();
        DBFolder folder = db.byTMdbId(tmdbId);

        if(folder == null){
            Platform.runLater(() -> {
                Alert alert = AlertUtils.createAlertWithStyling(Alert.AlertType.INFORMATION, "Matcher");
                alert.setHeaderText("No TV Show found with that id");
                alert.show();
            });
            return;
        }

        List<Fingerprint> fingerprintsList = folder.getAllFingerprintsForSeason(season);
        int size = fingerprints.size() + fingerprintsList.size();
        for(Fingerprint f : fingerprintsList){
            fingerprints.add(new MatcherCallable(f, matchTo, tmdbId, fingerprintLbl, counterRef, size));
        }
    }

    /**
     * @return Returns the highest percentage CallableResult
     */
    public CallableResult bestMatch(){
        List<CallableResult> list = executeAll();

        //Make sure the list is not null
        if(list == null) return null;

        //Loop over all the results and see who has the highest match percentage and return that
        CallableResult ret = null;
        for (CallableResult callableResultFuture : list) {
            if(ret == null || callableResultFuture.highestPercentage() >= ret.highestPercentage())
                ret = callableResultFuture;
        }

        return ret;
    }

    /**
     * @return Returns a list of all the results
     */
    private List<CallableResult> executeAll(){
        try {
            return executorService.invokeAll(fingerprints).stream().map(future -> {
                try {
                    return future.get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.warning("Failed to get result from future in callables");
                    return null;
                }
            }).toList();
        }catch(InterruptedException e){
            logger.warning("ExecutorService has been interrupted!");
            return new ArrayList<>();
        }
    }


}
