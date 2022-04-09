package Misc;

import tycho.core.config.Config;
import tycho.core.misc.FileManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class FileManagerTests {

    Config config;
    FileManager fManager;
    String externalResourcesPrefixPath = "/home/user/IdeaProjects/MakeMkvRename/target/classes/"; // This will change whenever you change the path of the project or anything similar to that

    /*
    * TODO: full rework of all these test (current tests are not relevant anymore)
    * */
    @Before
    public void setup() throws IOException {
        config = Config.getTestInstance();
        config.setProgramName(RandomStringUtils.randomAlphanumeric(5));
        fManager = FileManager.getInstance();
    }

    @Test
    public void getAudioComparePathTest(){
        String expected = externalResourcesPrefixPath + "Tools/audio_compare.py";
        assertEquals(expected, fManager.getAudioComparePath());
    }

    @Test
    public void getPythonExecTest(){
        if(SystemUtils.IS_OS_WINDOWS){
            assertEquals(externalResourcesPrefixPath + "Tools/windows/Scripts/python.exe", fManager.getPythonExecutable());
        }else{
            assertEquals(externalResourcesPrefixPath + "Tools/linux/python/bin/python3.8", fManager.getPythonExecutable());
        }
    }

    @Test
    public void getFpcalcExecTest(){
        String expected = externalResourcesPrefixPath + "Tools/" + (SystemUtils.IS_OS_WINDOWS ? "windows" : "linux") + "/fpcalc";
        assertEquals(expected, fManager.getFpCalcExecutable());
    }

    @Test
    public void getFfMpegExecTest(){
        String expected = externalResourcesPrefixPath + "Tools/" + (SystemUtils.IS_OS_WINDOWS ? "windows" : "linux")
                + "/ffmpeg" + (SystemUtils.IS_OS_WINDOWS ? ".exe" : "");
        assertEquals(expected, fManager.getFfMpegExecutable());
    }

    @Test
    public void getExternalResourcesTests(){
        String windowsFfmpegExec = "Tools/windows/ffmpeg.exe";
        String audioCompareScript = "Tools/audio_compare.py";
        String cssTheme = "dracula.css";

        assertEquals(externalResourcesPrefixPath + windowsFfmpegExec,
                fManager.getExternalResourceFile(windowsFfmpegExec));
        assertEquals(externalResourcesPrefixPath + audioCompareScript,
                fManager.getExternalResourceFile(audioCompareScript));
        assertEquals(externalResourcesPrefixPath + cssTheme,
                fManager.getExternalResourceFile(cssTheme));
    }

    @Test
    public void getDefaultTempDirTest(){
        assertEquals(System.getProperty("user.home") + "/." + config.getProgramName() + "/temp",
                fManager.getDefaultTempDir().getAbsolutePath());
    }

    @Test
    public void createDefaultTempDirTest(){
        assertTrue(fManager.getDefaultTempDir().exists());
    }

    @Test
    public void createTempDirTest(){
        File tempDir = fManager.createTempDirectory("testPrefix_", "_testSuffix");
        assertTrue(tempDir.exists());
        if(!tempDir.delete()){
            System.out.println("Failed to delete tempDir: " + tempDir.getAbsolutePath());
        }
    }

    @Test
    public void createTempFileTest(){
        File tempDir = fManager.createTempDirectory();
        File tempFile = fManager.createTempFile("testPrefix_",".exe", tempDir);
        assertTrue(tempFile.exists());
        if(!tempFile.delete() || !tempDir.delete()){
            System.out.println("Failed to delete tempFile and or tempDir");
        }
    }

    @Test
    public void clearTempDirTest(){
        File tempDir = fManager.createTempDirectory();
        File tempFileOne = fManager.createTempFile("", tempDir);
        File tempFileTwo = fManager.createTempFile("", tempDir);
        File tempFileThree = fManager.createTempFile("", tempDir);
        File tempFileFour = fManager.createTempFile("", tempDir);

        //Make sure all files have been created.
        assertTrue(tempDir.exists());
        assertTrue(tempFileOne.exists());
        assertTrue(tempFileTwo.exists());
        assertTrue(tempFileThree.exists());
        assertTrue(tempFileFour.exists());

        fManager.clearTempDirectory(tempDir);

        //Check if all tempFiles have been deleted
        assertFalse(tempFileOne.exists());
        assertFalse(tempFileTwo.exists());
        assertFalse(tempFileThree.exists());
        assertFalse(tempFileFour.exists());

        fManager.deleteTempDirectory(tempDir);
        assertFalse(tempDir.exists());

    }

    @Test
    public void deleteTempDirTest(){
        File tempDir = fManager.createTempDirectory();
        assertTrue(tempDir.exists());

        fManager.deleteTempDirectory(tempDir);
        assertFalse(tempDir.exists());
    }

    @Test
    public void clearDefaultTempDirTest(){
        File tempFileOne = fManager.createTempFile(".jpg");
        File tempFileTwo = fManager.createTempFile(".jpg");
        File tempFileThree = fManager.createTempFile(".jpg");
        File tempFileFour = fManager.createTempFile(".jpg");

        //Make sure all files have been created.
        assertTrue(tempFileOne.exists());
        assertTrue(tempFileTwo.exists());
        assertTrue(tempFileThree.exists());
        assertTrue(tempFileFour.exists());

        fManager.clearDefaultTempDirectory();

        //Check if all tempFiles have been deleted
        assertFalse(tempFileOne.exists());
        assertFalse(tempFileTwo.exists());
        assertFalse(tempFileThree.exists());
        assertFalse(tempFileFour.exists());
    }

    @After
    public void cleanup(){
        File parent = fManager.getDefaultTempDir().getParentFile();
        if(!fManager.getDefaultTempDir().delete() || !parent.delete()){
            System.out.println("Failed to cleanup properly");
        }
    }
}
