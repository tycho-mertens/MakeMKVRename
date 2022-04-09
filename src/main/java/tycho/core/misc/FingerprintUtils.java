package tycho.core.misc;

import tycho.core.client.fingerprint.Fingerprint;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class FingerprintUtils {

    private static final Logger logger = Logger.getLogger(FingerprintUtils.class.getName());

    /**
     * Transcode a video file to a audio .wav file
     *
     * @param videoFile The video file object
     * @return Returns the .wav as a File object
     */
    public static File transcodeToAudio(File videoFile){
        FileManager fM = FileManager.getInstance();
        File transcodedWav = fM.createTempFile("encoded", ".wav");

        try {
//            FFmpeg ffmpeg = new FFmpeg(FileManager.getInstance().getFfMpegExecutable());
            FFmpeg ffmpeg = new FFmpeg("/usr/bin/ffmpeg");//TODO properly fix this
            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(videoFile.getAbsolutePath())
                    .overrideOutputFiles(true)
                    .addOutput(transcodedWav.getAbsolutePath())
                    .setAudioChannels(1)
                    .done();

            FFmpegExecutor exec = new FFmpegExecutor(ffmpeg);
            logger.info("Transcoding started for file: " + videoFile.getAbsolutePath());
            FFmpegJob job = exec.createJob(builder);
            job.run();

            while (job.getState().equals(FFmpegJob.State.RUNNING)) {
                logger.info("Waiting for encoder...");
                TimeUnit.SECONDS.sleep(5);
            }
        }catch(Exception e){
            logger.warning("Failed to transcode videoFile: " + videoFile.getAbsolutePath());
        }
        return transcodedWav;
    }

    /**
     * Creates a fingerprint from the given audio file and returns it
     *
     * @param wavFile The audio file you want to create a fingerprint for
     * @return Returns the fingerprint for the given audio file
     */
    public static Fingerprint createFingerprintFromWav(File wavFile){
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{FileManager.getInstance().getFpCalcExecutable()
                    , "-raw", "-json", wavFile.getAbsolutePath()});

            //Get fingerprint from fpcalc output
            BufferedInputStream bis = new BufferedInputStream(proc.getInputStream());
            StringBuilder sb = new StringBuilder();

            int c;
            while((c = bis.read()) != -1){
                sb.append((char) c);
            }

            //Close everything that we don't need anymore
            bis.close();
            proc.getErrorStream().close();
            proc.getOutputStream().close();

            //Delete wav file, we don't need it anymore
            if(!wavFile.delete())
                logger.warning("Failed to cleanup audio file after creating fingerprint: " + wavFile.getAbsolutePath());

            return Fingerprint.fromJson(sb.toString());
        }catch(IOException e){
            logger.warning("Failed to create fingerprint from audio file: " + wavFile.getAbsolutePath());
            return null;
        }
    }
}
