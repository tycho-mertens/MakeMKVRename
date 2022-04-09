package tmdb;

import tycho.core.tmdb.TMdb;
import tycho.core.tmdb.model.TvEpisode;
import tycho.core.tmdb.model.TvSearch;
import tycho.core.tmdb.model.TvSeason;
import tycho.core.tmdb.model.TvShow;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TMdbTests {

    TMdb tmdb;

    /*
    * TODO: find a better solution to test tmdb, because these test results tend to change sometimes
    * */
    @Before
    public void setup(){
        tmdb = TMdb.getInstance();
    }

    @Test
    public void ExternalTvSourcesTest(){
        TvShow external = tmdb.getTvShowFromImdbID("tt1839578");

        assertEquals("Person of Interest", external.getName());
        assertEquals("2011-09-22", external.getFirstAirDate());
        assertEquals(1411, external.getId());
        assertEquals(103, external.getNumberOfEpisodes());
    }

    @Test
    public void TvEpisodeTest(){
        TvEpisode ep = tmdb.getTvEpisode(1411, 1, 1);

        //General Tests
        assertEquals("2011-09-22", ep.getAirDate());
        assertEquals(3, ep.getCrew().size());
        assertEquals(13, ep.getGuestStars().size());
        assertEquals(64055, ep.getId());
        assertEquals("Pilot", ep.getName());
        assertEquals("Person of Interest", ep.getTvShow().getName());

        //Crew Tests
        TvEpisode.Crew crew = ep.getCrew().get(1);
        assertEquals("Costume & Make-Up", crew.getDepartment());
        assertEquals("55f5802ec3a3686d27005852", crew.getCreditId());
        assertEquals("Juliet Polcsa", crew.getName());
        assertEquals(957570, crew.getId());

        //Guest Star Tests
        TvEpisode.GuestStar star = ep.getGuestStars().get(5);
        assertEquals("Anton's Father", star.getCharacter());
        assertEquals("5fb864ea1685da0041ac906a", star.getCreditId());
        assertEquals(2, star.getGender());
    }

    @Test
    public void TvSearchTest(){
        TvSearch tvSearch = tmdb.searchTv("person of interest");

        assertEquals(2, tvSearch.getTotalResults());

        TvSearch.Result res = tvSearch.getResults().get(1);
        assertEquals(114760, res.getId());
        assertEquals("2020-01-07", res.getFirstAirDate());
        assertEquals(1, res.getGenreIds().size());

        assertEquals(1411, tvSearch.getResults().get(0).getId());
    }

    @Test
    public void TvSeason(){
        TvSeason season = tmdb.getTvSeason(1411, 5);

        assertEquals(70251, season.getId());
        assertEquals("2016-05-03", season.getAirDate());
        assertEquals("Season 5", season.getName());
        assertEquals(5, season.getSeasonNumber());
        assertEquals(13, season.getEpisodes().size());

        TvEpisode ep = season.getEpisodes().get(6);
        assertEquals(7, ep.getEpisodeNumber());
        assertEquals(1176503, ep.getId());
        assertEquals("QSO", ep.getName());
        assertEquals("3J6006", ep.getProductionCode());
    }

    @Test
    public void TvShow(){
        TvShow show = tmdb.getTvShow(1411);

        assertEquals("2011-09-22", show.getFirstAirDate());
        assertEquals(1411, show.getId());
        assertFalse(show.isInProduction());
        assertEquals("2016-06-21", show.getLastAirDate());
        assertEquals(103, show.getNumberOfEpisodes());

        //Created By
        TvShow.CreatedBy by = show.getCreatedBy().get(0);
        assertEquals(527, by.getId());
        assertEquals("Jonathan Nolan", by.getName());

        //Genres
        TvShow.Genre genre = show.getGenres().get(2);
        assertEquals(80, genre.getId());
        assertEquals("Crime", genre.getName());


        //Production Companies
        TvShow.ProductionCompany company = show.getProductionCompanies().get(0);
        assertEquals(103490, company.getId());
        assertEquals("Kilter Films", company.getName());

        //Production Countries
        TvShow.ProductionCountry country = show.getProductionCountries().get(0);
        assertEquals("US", country.getIso_3166_1());
        assertEquals("United States of America", country.getName());
    }

}
