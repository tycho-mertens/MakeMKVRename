package tycho.core.tmdb;

import org.yaml.snakeyaml.util.UriEncoder;
import tycho.core.config.Config;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import tycho.core.tmdb.model.*;

import java.io.IOException;
import java.util.Objects;

public class TMdb {

    private final String apiKey;
    private final String baseUrl;

    private static TMdb instance;

    public static synchronized TMdb getInstance(){
        if(instance == null)
            instance = new TMdb(Config.getInstance().asString(Config.TMDB_API));
        return instance;
    }
    /**
     * Default constructor
     * Comment: This is a fast and dirty way of implementing a rest api wrapper in java
     *
     * @param apiKey Personal api key for TMdb (create an account to get an api key)
     */
    private TMdb(String apiKey){
        this.apiKey = apiKey;
        baseUrl = "https://api.themoviedb.org/3/";
    }

    /**
     * Default getter
     *
     * @return Returns the api key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Search for a tv show, and receive a list of possible matches.
     *
     * @param query A tv show you want to search for (ex. 'Person of Interest', 'Manifest', 'Lost', ...)
     * @return Returns the search result as a TvSearch object (TvSearch contains a list with all the results)
     */
    public TvSearch searchTv(String query){
        String url = String.format("search/tv?query=%s&api_key=apiKey", UriEncoder.encode(query));
        return TvSearch.fromJSON(sendRequest(url));
    }

    /**
     * Get all the information about a tv show by its id
     *
     * @param tvShowId The TMdb id for the tv show
     * @return Returns a TvShow object with all the information about the tv show that TMdb has on it
     */
    public TvShow getTvShow(int tvShowId){
        String url = String.format("tv/%d?api_key=apiKey", tvShowId);
        return TvShow.fromJSON(sendRequest(url));
    }

    /**
     * Get all the information about a tv show season by its tv show id and season number
     *
     * @param tvShowId The TMdb id for the tv show
     * @param season The season of the tv season you're searching for
     * @return Returns a TvSeason object, which contains all the information of that season for the tv show
     */
    public TvSeason getTvSeason(int tvShowId, int season){
        String url = String.format("tv/%d/season/%d?api_key=apiKey", tvShowId, season);
        return TvSeason.fromJSON(sendRequest(url));
    }

    /**
     * Get all the information about a tv show episode by its tv show id, season number and episode number
     *
     * @param tvShowId The TMdb id for the tv show
     * @param season The season of the tv episode you're searching for
     * @param episode The episode of the tv episode you're searching for
     * @return Returns the tv show episode with all the information collected form TMdb
     */
    public TvEpisode getTvEpisode(int tvShowId, int season, int episode){
        String url = String.format("tv/%d/season/%d/episode/%d?api_key=apiKey", tvShowId, season, episode);
        TvEpisode ep = TvEpisode.fromJSON(sendRequest(url));
        ep.setTvShow(getTvShow(tvShowId));
        return ep;
    }

    /**
     * Get all the information about a tv show by its imdbID
     *
     * @param imdbId The imdbId for the tv show you're trying to get from TMdb
     * @return Returns a TvShow object, which contains all the information about that tv show from TMdb
     */
    public TvShow getTvShowFromImdbID(String imdbId){
        String url = String.format("find/%s?external_source=imdb_id&api_key=apiKey", imdbId);
        ExternalTvSources sources = ExternalTvSources.fromJSON(sendRequest(url));

        return sources.getTvResults().isEmpty() ? null : getTvShow(sources.getTvResults().get(0).getId());
    }

    /**
     * Send a rest api request to TMdb
     *
     * @param url TMdb rest api endpoint
     * @return Returns a json rest response as a String
     */
    private String sendRequest(String url){
        String json = "";
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .url(baseUrl + url.replaceAll("apiKey", apiKey))
                .build();
        try {
            json = Objects.requireNonNull(Objects.requireNonNull(client.newCall(req).execute().body()).string());
        } catch (IOException ignored) {}
        return json;
    }
}
