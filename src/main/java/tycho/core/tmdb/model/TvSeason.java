package tycho.core.tmdb.model;

import com.google.gson.Gson;

import java.util.List;

public class TvSeason {

    /**
     * No documentation has been written here, because this is a wrapper and should never be used alone.
     * Aka you're never going to have to touch this
     */
    private final String _id;
    private final String air_date;
    private final List<TvEpisode> episodes;
    private final String name;
    private final String overview;
    private final int id;
    private final String poster_path;
    private final int season_number;

    public TvSeason(String _id, String air_date, List<TvEpisode> episodes, String name, String overview, int id, String poster_path, int season_number) {
        this._id = _id;
        this.air_date = air_date;
        this.episodes = episodes;
        this.name = name;
        this.overview = overview;
        this.id = id;
        this.poster_path = poster_path;
        this.season_number = season_number;
    }

    public static TvSeason fromJSON(String json){
        return new Gson().fromJson(json, TvSeason.class);
    }

    public String get_id() {
        return _id;
    }

    public String getAirDate() {
        return air_date;
    }

    public List<TvEpisode> getEpisodes() {
        return episodes;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public int getSeasonNumber() {
        return season_number;
    }
}
