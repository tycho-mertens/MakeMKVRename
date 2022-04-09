package tycho.core.tmdb.model;

import com.google.gson.Gson;

import java.util.List;

public class TvSearch {
    /**
     * No documentation has been written here, because this is a wrapper and should never be used alone.
     * Aka you're never going to have to touch this
     */
    private final int page;
    private final List<TvSearch.Result> results;
    private final int total_pages;
    private final int total_results;

    public TvSearch(int page, List<TvSearch.Result> results, int total_pages, int total_results) {
        this.page = page;
        this.results = results;
        this.total_pages = total_pages;
        this.total_results = total_results;
    }

    public static TvSearch fromJSON(String json){
        return new Gson().fromJson(json, TvSearch.class);
    }

    public int getPage() {
        return page;
    }

    public List<TvSearch.Result> getResults() {
        return results;
    }

    public int getTotalPages() {
        return total_pages;
    }

    public int getTotalResults() {
        return total_results;
    }

    public static class Result{
        private final String backdrop_path;
        private final String first_air_date;
        private final List<Integer> genre_ids;
        private final int id;
        private final String name;
        private final List<String> origin_country;
        private final String original_language;
        private final String original_name;
        private final String overview;
        private final float popularity;
        private final String poster_path;
        private final double vote_average;
        private final int vote_count;

        public Result(String backdrop_path, String first_air_date, List<Integer> genre_ids, int id, String name,
                      List<String> origin_country, String original_language, String original_name, String overview,
                      float popularity, String poster_path, double vote_average, int vote_count) {
            this.backdrop_path = backdrop_path;
            this.first_air_date = first_air_date;
            this.genre_ids = genre_ids;
            this.id = id;
            this.name = name;
            this.origin_country = origin_country;
            this.original_language = original_language;
            this.original_name = original_name;
            this.overview = overview;
            this.popularity = popularity;
            this.poster_path = poster_path;
            this.vote_average = vote_average;
            this.vote_count = vote_count;
        }

        public String getBackdropPath() {
            return backdrop_path;
        }

        public String getFirstAirDate() {
            return first_air_date;
        }

        public List<Integer> getGenreIds() {
            return genre_ids;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<String> getOriginCountry() {
            return origin_country;
        }

        public String getOriginalLanguage() {
            return original_language;
        }

        public String getOriginalName() {
            return original_name;
        }

        public String getOverview() {
            return overview;
        }

        public float getPopularity() {
            return popularity;
        }

        public String getPosterPath() {
            return poster_path;
        }

        public double getVoteAverage() {
            return vote_average;
        }

        public int getVoteCount() {
            return vote_count;
        }
    }
}
