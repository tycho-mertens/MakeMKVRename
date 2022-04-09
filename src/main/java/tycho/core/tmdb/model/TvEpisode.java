package tycho.core.tmdb.model;

import com.google.gson.Gson;

import java.util.List;

public class TvEpisode {
    /**
     * No documentation has been written here, because this is a wrapper and should never be used alone.
     * Aka you're never going to have to touch this
     */

    private final String air_date;
    private final List<Crew> crew;
    private final int episode_number;
    private final List<GuestStar> guest_stars;
    private final String name;
    private final String overview;
    private final int id;
    private final String production_code;
    private final int season_number;
    private final String still_path;
    private final double vote_average;
    private final double vote_count;
    private TvShow tvShow;

    public TvEpisode(String air_date, List<Crew> crew, int episode_number, List<GuestStar> guest_stars, String name,
                     String overview, int id, String production_code, int season_number, String still_path, int vote_average,
                     int vote_count) {
        this.air_date = air_date;
        this.crew = crew;
        this.episode_number = episode_number;
        this.guest_stars = guest_stars;
        this.name = name;
        this.overview = overview;
        this.id = id;
        this.production_code = production_code;
        this.season_number = season_number;
        this.still_path = still_path;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
    }

    public TvShow getTvShow() {
        return tvShow;
    }

    public void setTvShow(TvShow tvShow) {
        this.tvShow = tvShow;
    }

    public static TvEpisode fromJSON(String json){
        return new Gson().fromJson(json, TvEpisode.class);
    }

    public String getAirDate() {
        return air_date;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public int getEpisodeNumber() {
        return episode_number;
    }

    public List<GuestStar> getGuestStars() {
        return guest_stars;
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

    public String getProductionCode() {
        return production_code;
    }

    public int getSeasonNumber() {
        return season_number;
    }

    public String getStillPath() {
        return still_path;
    }

    public double getVoteAverage() {
        return vote_average;
    }

    public double getVoteCount() {
        return vote_count;
    }

    public static class GuestStar{
        private final String character;
        private final String credit_id;
        private final int order;
        private final boolean adult;
        private final int gender;
        private final float popularity;
        private final String profile_path;

        public GuestStar(String character, String credit_id, int order, boolean adult, int gender, float popularity, String profile_path) {
            this.character = character;
            this.credit_id = credit_id;
            this.order = order;
            this.adult = adult;
            this.gender = gender;
            this.popularity = popularity;
            this.profile_path = profile_path;
        }

        public String getCharacter() {
            return character;
        }

        public String getCreditId() {
            return credit_id;
        }

        public int getOrder() {
            return order;
        }

        public boolean isAdult() {
            return adult;
        }

        public int getGender() {
            return gender;
        }

        public float getPopularity() {
            return popularity;
        }

        public String getProfilePath() {
            return profile_path;
        }
    }

    public static class Crew{
        private final String department;
        private final String job;
        private final String credit_id;
        private final boolean adult;
        private final int gender;
        private final int id;
        private final String known_for_department;
        private final String name;
        private final String original_name;
        private final float popularity;
        private final String profile_path;

        public Crew(String department, String job, String credit_id, boolean adult, int gender, int id, String known_for_department, String name, String original_name, float popularity, String profile_path) {
            this.department = department;
            this.job = job;
            this.credit_id = credit_id;
            this.adult = adult;
            this.gender = gender;
            this.id = id;
            this.known_for_department = known_for_department;
            this.name = name;
            this.original_name = original_name;
            this.popularity = popularity;
            this.profile_path = profile_path;
        }

        public String getDepartment() {
            return department;
        }

        public String getJob() {
            return job;
        }

        public String getCreditId() {
            return credit_id;
        }

        public boolean isAdult() {
            return adult;
        }

        public int getGender() {
            return gender;
        }

        public int getId() {
            return id;
        }

        public String getKnownForDepartment() {
            return known_for_department;
        }

        public String getName() {
            return name;
        }

        public String getOriginalName() {
            return original_name;
        }

        public float getPopularity() {
            return popularity;
        }

        public String getProfilePath() {
            return profile_path;
        }
    }
}
