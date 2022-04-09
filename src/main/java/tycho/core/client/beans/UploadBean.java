package tycho.core.client.beans;

import java.io.File;

public class UploadBean  {

    private final String name;
    private final File file;
    private int tmdbID, season, episode;

    public UploadBean(File file){
        this.file = file;
        name = file.getName();
        tmdbID = -1;
        season = -1;
        episode = -1;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public int getTmdbID() {
        return tmdbID;
    }

    public void setTmdbID(int tmdbID) {
        this.tmdbID = tmdbID;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }
}
