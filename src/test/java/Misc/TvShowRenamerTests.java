package Misc;

import tycho.core.config.Config;
import tycho.core.misc.TvShowRenamer;
import tycho.core.tmdb.TMdb;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class TvShowRenamerTests {

    File configFile;
    Config config;
    File one;
    File two;
    File three;

    /*
    * TODO: replace old test files to the new ones, so the test would work properly
    * */
    @Before
    public void setup() throws IOException {
//        configFile = new File("/home/user/IdeaProjects/MakeMkvRename/src/test/java/testfiles/config.json");
        config = Config.getTestInstance();

        one = new File("/home/user/IdeaProjects/MakeMkvRename/src/test/java/testfiles/one.mkv");
        two = new File("/home/user/IdeaProjects/MakeMkvRename/src/test/java/testfiles/two.mkv");
        three = new File("/home/user/IdeaProjects/MakeMkvRename/src/test/java/testfiles/three.mkv");

        //Create files
        if(!one.createNewFile() || !two.createNewFile() || !three.createNewFile()){
            throw new IOException("Failed to create one or more files");
        }
    }

    @Test
    public void renameTests(){
        TMdb tMdb = TMdb.getInstance();

        //Check if files exist
        assertTrue(one.exists());
        assertTrue(two.exists());
        assertTrue(three.exists());

        //One check
        TvShowRenamer renamerOne = new TvShowRenamer("Person of Interest", "Pilot", 1, 1, one);

        assertEquals("Person of Interest - S01E01 - Pilot", renamerOne.getFinalName());

        renamerOne.rename();

        assertFalse(one.exists());
        one = new File(one.getParentFile(), renamerOne.getFinalName() + "." + FilenameUtils.getExtension(one.getAbsolutePath()));
        assertTrue(one.exists());


        //Two check
        TvShowRenamer renamerTwo = new TvShowRenamer("Person of Interest", "Ghosts", 1,2, two);

        assertEquals("Person of Interest - S01E02 - Ghosts", renamerTwo.getFinalName());

        renamerTwo.rename();

        assertFalse(two.exists());
        two = new File(two.getParentFile(), renamerTwo.getFinalName() + "." + FilenameUtils.getExtension(two.getAbsolutePath()));
        assertTrue(two.exists());


        //Three check
        TvShowRenamer renamerThree = new TvShowRenamer("Person of Interest", "Mission Creep", 1, 3, three);

        assertEquals("Person of Interest - S01E03 - Mission Creep", renamerThree.getFinalName());

        renamerThree.rename();

        assertFalse(three.exists());
        three = new File(three.getParentFile(), renamerThree.getFinalName() + "." + FilenameUtils.getExtension(three.getAbsolutePath()));
        assertTrue(three.exists());
    }

    @After
    public void cleanup() throws IOException {
        if(!one.delete() || !two.delete() || !three.delete() || !configFile.delete()){
            throw new IOException("Failed to cleanup files");
        }
    }
}
