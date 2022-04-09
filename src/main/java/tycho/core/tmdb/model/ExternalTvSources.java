package tycho.core.tmdb.model;

import com.google.gson.Gson;

import java.util.List;

public class ExternalTvSources {

    /**
     * No documentation has been written here, because this is a wrapper and should never be used alone.
     * Aka you're never going to have to touch this
     */
    private final List<ExternalResult> tv_results;

    public ExternalTvSources(List<ExternalResult> tvResults) {
        this.tv_results = tvResults;
    }


    public List<ExternalResult> getTvResults() {
        return tv_results;
    }


    public static ExternalTvSources fromJSON(String json){
        return new Gson().fromJson(json, ExternalTvSources.class);
    }

    public static class ExternalResult{
        private final int id;

        public ExternalResult(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
