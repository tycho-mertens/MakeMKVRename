package tycho.core.misc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleScanner {
    /**
     * Extracts a season from an episode title, returns 0 if nothing was found
     *
     * @param title The name of the tv show episode (ex. 'Manifest - Pilot - Season 1 Episode 2.mkv')
     * @return Returns 0 if no season was found, returns anything above 0 if a correct season is found
     */
    public static int getSeason(String title){
        // Code might look ugly, and expression is done in multiple patterns, but this is on purpose.
        // This way we can understand it easier and change something faster when needed
        int season = 0;
        String replaceWithSpace = "[-.]";

        // We replace the characters '-' and '.' to a space, because these characters sometimes represent a space in some cases
        Matcher fullSeasonWord = Pattern.compile("season[ ]?([0-9]+)", Pattern.CASE_INSENSITIVE).matcher(title
                .replaceAll(replaceWithSpace, " "));
        Matcher normalSeasonNotation = Pattern.compile("s[e]?[ ]?([0-9]+)", Pattern.CASE_INSENSITIVE).matcher(title
                .replaceAll(replaceWithSpace, " "));

        // Try to find the season, if found replace 'season' with the correct season, otherwise don't change anything
        season = normalSeasonNotation.find() ? Integer.parseInt(normalSeasonNotation.group(1)) : season;
        season = fullSeasonWord.find() ? Integer.parseInt(fullSeasonWord.group(1)) : season;

        // Check if we found the season, if not then try the last option, which is an exception in naming tv shows
        if(season == 0){
            Matcher exceptionNotation = Pattern.compile("([0-9]+)x([0-9]+)", Pattern.CASE_INSENSITIVE).matcher(title);
            season = exceptionNotation.find() ? Integer.parseInt(exceptionNotation.group(1)) : season;
        }
        return season;
    }

    /**
     * Extracts an episode from an episode title, returns 0 if nothing was found
     *
     * @param title The name of the tv show episode (ex. 'Manifest - Pilot - Season 1 Episode 2.mkv')
     * @return Returns 0 if no episode was found, returns anything above 0 if a correct episode is found
     */
    public static int getEpisode(String title){
        // Code might look ugly, and expression is done in multiple patterns, but this is on purpose.
        // This way we can understand it easier and change something faster when needed
        int episode = 0;
        String replaceWithSpace = "[-.]";

        // We replace the characters '-' and '.' to a space, because these characters sometimes represent a space in some cases
        Matcher fullEpisodeWord = Pattern.compile("episode[ ]?([0-9]+)", Pattern.CASE_INSENSITIVE).matcher(title
                .replaceAll(replaceWithSpace, " "));
        Matcher normalEpisodeNotation = Pattern.compile("e[p]?[ ]?([0-9]+)", Pattern.CASE_INSENSITIVE).matcher(title
                .replaceAll(replaceWithSpace, " "));

        // Try to find the episode, if found replace 'episode' with the correct episode, otherwise don't change anything
        episode = normalEpisodeNotation.find() ? Integer.parseInt(normalEpisodeNotation.group(1)) : episode;
        episode = fullEpisodeWord.find() ? Integer.parseInt(fullEpisodeWord.group(1)) : episode;

        // Check if we found the episode, if not then try the last option, which is an exception in naming tv shows
        if(episode == 0){
            Matcher exceptionNotation = Pattern.compile("([0-9]+)x([0-9]+)", Pattern.CASE_INSENSITIVE).matcher(title);
            episode = exceptionNotation.find() ? Integer.parseInt(exceptionNotation.group(2)) : episode;
        }
        return episode;
    }

}
