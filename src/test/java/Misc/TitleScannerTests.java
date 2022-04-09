package Misc;

import tycho.core.misc.TitleScanner;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TitleScannerTests {

    //TODO Might need to try and add more tests, just to be sure
    private final List<TestInfo> tests = Arrays.asList(
            new TestInfo("Discovery.Channel.How.the.Universe.Works.S01E08.Alien.Moons.720p.HDTV.x264-DHD", 1, 8),
            new TestInfo("how.the.universe.works.s01e08.720p-dhd", 1 ,8),
            new TestInfo("how.the.universe.works.s01e08.2010.720p-dhd", 1 ,8),
            new TestInfo("How the Universe Works Season 2 Episode 2", 2 ,2),
            new TestInfo("How the Universe Works Season 1 E03", 1 ,3),
            new TestInfo("How the Universe Works Season 3 Episode 01",3 ,1),
            new TestInfo("How the Universe Works 02x09", 2 ,9),
            new TestInfo("How the Universe Works 2x1", 2 ,1),
            new TestInfo("How the Universe Works S05E01", 5 ,1),
            new TestInfo("How the Universe Works", 0 ,0)
    );

    @Test
    public void seasonTests(){
        for (TestInfo test : tests) {
            assertEquals(test.season(), TitleScanner.getSeason(test.title()));
        }
    }

    @Test
    public void episodeTests(){
        for (TestInfo test : tests) {
            assertEquals(test.episode(), TitleScanner.getEpisode(test.title()));
        }
    }

    private record TestInfo(String title, int season, int episode){}
}
