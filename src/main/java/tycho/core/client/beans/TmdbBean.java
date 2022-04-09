package tycho.core.client.beans;

import tycho.core.misc.FileManager;
import tycho.core.misc.ImageConverter;
import tycho.core.tmdb.model.TvShow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TmdbBean {

    private final File image;

    private final String title;
    private final int tmdbId;
    private final int totalSeasons;
    private final int year;

    /**
     * Default constructor
     *
     * @param tvShow The tvShow that was selected in the TMdbFinderController class
     */
    public TmdbBean(TvShow tvShow) {
        File notFoundImage = new File(FileManager.getInstance().getExternalResourceFile("noPoster.png"));
        //If poster path is null, set image to the notFoundImage, otherwise convert the image
        image = tvShow.getPoster_path() == null ? notFoundImage : new ImageConverter()
                .convertUrlToPng("https://www.themoviedb.org/t/p/w1280" + tvShow.getPoster_path());
        title = tvShow.getName();
        tmdbId = tvShow.getId();

        int tempYear;
        tempYear = 0;
        try{
            tempYear = Integer.parseInt(tvShow.getFirstAirDate().split("-")[0]);
        }catch(Exception ignored){}
        year = tempYear;

        totalSeasons = tvShow.getNumberOfSeasons();
    }

    /**
     * Default getter
     *
     * @return Returns the total seasons the tvShow has
     */
    public int getTotalSeasons(){
        return totalSeasons;
    }

    /**
     * Default getter
     *
     * @return Returns a list of integers representing the seasons
     */
    public List<Integer> getTotalSeasonsAsList(){
        List<Integer> seasons = new ArrayList<>();

        for(int i = 1; i <= totalSeasons; i++){
            seasons.add(i);
        }

        return seasons;
    }

    /**
     * Default getter
     *
     * @return Returns the image as a File object
     */
    public File getImage() {
        return image;
    }

    /**
     * Default getter
     *
     * @return Returns the release year of the tvShow
     */
    public int getYear(){
        return year;
    }

    /**
     * Default getter
     *
     * @return Returns the title of the tvShow
     */
    public String getTitle() {
        return title;
    }

    /**
     * Default getter
     *
     * @return Returns the TMdbId of the tvShow
     */
    public int getTmdbId() {
        return tmdbId;
    }
}
