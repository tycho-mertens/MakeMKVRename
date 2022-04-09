package tycho.core.client.fingerprint;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class MatcherCallable implements Callable<CallableResult> {

    private static final Logger logger = Logger.getLogger(MatcherCallable.class.getName());

    private final Fingerprint fromDb;
    private final Fingerprint matchTo;
    private final Label fingerprintLbl;
    private final CounterRef counterRef;
    private final int totalFingerprintsToTest;
    private final int tmdbId;

    /**
     *
     * @param fromDb The fingerprint from the database
     * @param matchTo The fingerprint from the 'originalFileTable'
     * @param tmdbId The tmdbId of the TV Show
     * @param fingerprintLbl The 'fingerprintLbl' from the main scene, so we can update it
     * @param counterRef A counter reference, which we will use to keep track and update
     * @param totalFingerprintsToTest The total fingerprints we have to test
     */
    public MatcherCallable(Fingerprint fromDb, Fingerprint matchTo, int tmdbId, Label fingerprintLbl
                            , CounterRef counterRef, int totalFingerprintsToTest){
        this.fromDb = fromDb;
        this.matchTo = matchTo;
        this.fingerprintLbl = fingerprintLbl;
        this.counterRef = counterRef;
        this.totalFingerprintsToTest = totalFingerprintsToTest;
        this.tmdbId = tmdbId;
    }

    @Override
    public CallableResult call(){
        int season = fromDb.getSeason();
        int episode = fromDb.getEpisode();

        float highestPercentage = fromDb.compare(matchTo);
        updateFingerprintLabel(++counterRef.counter, totalFingerprintsToTest);

        logger.info(String.format("(%d/%d)", counterRef.counter, totalFingerprintsToTest));
        return new CallableResult(highestPercentage, tmdbId, season, episode);
    }

    /**
     * Updates the label in the main scene
     *
     * @param current The current index
     * @param max The maximum
     */
    private void updateFingerprintLabel(int current, int max){
        Platform.runLater(() -> fingerprintLbl.setText(String.format("(%d/%d)", current, max)));
    }
}
