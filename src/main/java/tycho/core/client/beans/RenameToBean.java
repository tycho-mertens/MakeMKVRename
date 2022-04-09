package tycho.core.client.beans;

public class RenameToBean {

    private final float similarity;
    private final String title;
    private final int season;
    private final int episode;


    public RenameToBean(float similarity, String title, int season, int episode) {
        this.similarity = similarity;
        this.title = title;
        this.season = season;
        this.episode = episode;
    }

    public float getSimilarity() {
        return similarity;
    }

    public String getTitle() {
        return title;
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }
}
