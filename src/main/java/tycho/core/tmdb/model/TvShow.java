package tycho.core.tmdb.model;

import com.google.gson.Gson;

import java.util.List;

public class TvShow {

    /**
     * No documentation has been written here, because this is a wrapper and should never be used alone.
     * Aka you're never going to have to touch this
     */

    private final String backdrop_path;
    private final List<TvShow.CreatedBy> created_by;
    private final List<Integer> episode_run_time;
    private final String first_air_date;
    private final List<TvShow.Genre> genres;
    private final String homepage;
    private final int id;
    private final boolean in_production;
    private final List<String> languages;
    private final String last_air_date;
    private final TvEpisode last_episode_to_air;
    private final String name;
    private final TvEpisode next_episode_to_air;
    private final List<TvShow.Network> networks;
    private final int number_of_episodes;
    private final int number_of_seasons;
    private final List<String> origin_country;
    private final String original_language;
    private final String original_name;
    private final String overview;
    private final float popularity;
    private final String poster_path;
    private final List<TvShow.ProductionCompany> production_companies;
    private final List<TvShow.ProductionCountry> production_countries;
    private final List<TvSeason> seasons;
    private final List<TvShow.SpokenLanguage> spoken_languages;
    private final String status;
    private final String tagline;
    private final String type;
    private final double vote_average;
    private final double vote_count;

    public TvShow(String backdrop_path, List<TvShow.CreatedBy> created_by, List<Integer> episode_run_time, String first_air_date,
                  List<TvShow.Genre> genres, String homepage, int id, boolean in_production, List<String> languages,
                  String last_air_date, TvEpisode last_episode_to_air, String name, TvEpisode next_episode_to_air,
                  List<TvShow.Network> networks, int number_of_episodes, int number_of_seasons, List<String> origin_country,
                  String original_language, String original_name, String overview, float popularity, String poster_path,
                  List<TvShow.ProductionCompany> production_companies, List<TvShow.ProductionCountry> production_countries,
                  List<TvSeason> seasons, List<TvShow.SpokenLanguage> spoken_languages, String status, String tagline,
                  String type, double vote_average, double vote_count) {
        this.backdrop_path = backdrop_path;
        this.created_by = created_by;
        this.episode_run_time = episode_run_time;
        this.first_air_date = first_air_date;
        this.genres = genres;
        this.homepage = homepage;
        this.id = id;
        this.in_production = in_production;
        this.languages = languages;
        this.last_air_date = last_air_date;
        this.last_episode_to_air = last_episode_to_air;
        this.name = name;
        this.next_episode_to_air = next_episode_to_air;
        this.networks = networks;
        this.number_of_episodes = number_of_episodes;
        this.number_of_seasons = number_of_seasons;
        this.origin_country = origin_country;
        this.original_language = original_language;
        this.original_name = original_name;
        this.overview = overview;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.production_companies = production_companies;
        this.production_countries = production_countries;
        this.seasons = seasons;
        this.spoken_languages = spoken_languages;
        this.status = status;
        this.tagline = tagline;
        this.type = type;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
    }

    public static TvShow fromJSON(String json){
        return new Gson().fromJson(json, TvShow.class);
    }
    public String getBackdropPath() {
        return backdrop_path;
    }

    public List<TvShow.CreatedBy> getCreatedBy() {
        return created_by;
    }

    public List<Integer> getEpisodeRunTime() {
        return episode_run_time;
    }

    public String getFirstAirDate() {
        return first_air_date;
    }

    public List<TvShow.Genre> getGenres() {
        return genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public int getId() {
        return id;
    }

    public boolean isInProduction() {
        return in_production;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public String getLastAirDate() {
        return last_air_date;
    }

    public TvEpisode getLastEpisodeToAir() {
        return last_episode_to_air;
    }

    public String getName() {
        return name;
    }

    public TvEpisode getNextEpisodeToAir() {
        return next_episode_to_air;
    }

    public List<TvShow.Network> getNetworks() {
        return networks;
    }

    public int getNumberOfEpisodes() {
        return number_of_episodes;
    }

    public int getNumberOfSeasons() {
        return number_of_seasons;
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

    public String getPoster_path() {
        return poster_path;
    }

    public List<TvShow.ProductionCompany> getProductionCompanies() {
        return production_companies;
    }

    public List<TvShow.ProductionCountry> getProductionCountries() {
        return production_countries;
    }

    public List<TvSeason> getSeasons() {
        return seasons;
    }

    public List<TvShow.SpokenLanguage> getSpokenLanguages() {
        return spoken_languages;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public String getType() {
        return type;
    }

    public double getVoteAverage() {
        return vote_average;
    }

    public double getVoteCount() {
        return vote_count;
    }

    public static class SpokenLanguage{
        private final String english_name;
        private final String iso_639_1;
        private final String name;

        public SpokenLanguage(String english_name, String iso_639_1, String name) {
            this.english_name = english_name;
            this.iso_639_1 = iso_639_1;
            this.name = name;
        }

        public String getEnglishName() {
            return english_name;
        }

        public String getIso_639_1() {
            return iso_639_1;
        }

        public String getName() {
            return name;
        }
    }
    public static class ProductionCountry{
        private final String iso_3166_1;
        private final String name;

        public ProductionCountry(String iso_3166_1, String name) {
            this.iso_3166_1 = iso_3166_1;
            this.name = name;
        }

        public String getIso_3166_1() {
            return iso_3166_1;
        }

        public String getName() {
            return name;
        }
    }
    public static class ProductionCompany{
        private final int id;
        private final String logo_path;
        private final String name;
        private final String origin_country;

        public ProductionCompany(int id, String logo_path, String name, String origin_country) {
            this.id = id;
            this.logo_path = logo_path;
            this.name = name;
            this.origin_country = origin_country;
        }

        public int getId() {
            return id;
        }

        public String getLogoPath() {
            return logo_path;
        }

        public String getName() {
            return name;
        }

        public String getOriginCountry() {
            return origin_country;
        }
    }
    public static class CreatedBy{
        private final int id;
        private final String credit_id;
        private final String name;
        private final int gender;
        private final String profile_path;

        public CreatedBy(int id, String credit_id, String name, int gender, String profile_path) {
            this.id = id;
            this.credit_id = credit_id;
            this.name = name;
            this.gender = gender;
            this.profile_path = profile_path;
        }

        public int getId() {
            return id;
        }

        public String getCreditId() {
            return credit_id;
        }

        public String getName() {
            return name;
        }

        public int getGender() {
            return gender;
        }

        public String getProfilePath() {
            return profile_path;
        }
    }
    public static class Genre{
        private final int id;
        private final String name;

        public Genre(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
    public static class Network{
        private final String name;
        private final int id;
        private final String logo_path;
        private final String origin_country;

        public Network(String name, int id, String logo_path, String origin_country) {
            this.name = name;
            this.id = id;
            this.logo_path = logo_path;
            this.origin_country = origin_country;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public String getLogoPath() {
            return logo_path;
        }

        public String getOriginCountry() {
            return origin_country;
        }
    }
}
