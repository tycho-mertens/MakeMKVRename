package tycho.core.misc;

import tycho.core.config.Config;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class TvShowRenamer {

    private static final Logger logger = Logger.getLogger(TvShowRenamer.class.getName());

    private final String tvShowTitle;
    private final String epName;
    private final int season;
    private final int episode;
    private final File fileToRename;
    private List<Tag> tags;

    /**
     * Default constructor, nothing special
     *
     * @param tvShowTitle The TV Show title
     * @param epName The episode title
     * @param season The season nr
     * @param episode The episode nr
     * @param fileToRename The file you want to rename
     */
    public TvShowRenamer(String tvShowTitle, String epName, int season, int episode, File fileToRename){
        this.tvShowTitle = tvShowTitle;
        this.epName = epName;
        this.season = season;
        this.episode = episode;
        this.fileToRename = fileToRename;
        setTags();
    }

    /**
     * Sets the tags in the constructor, these tags are used as a renaming expression
     */
    private void setTags(){
        String season = (this.season < 10 ? "0" : "") + this.season;
        String episode = (this.episode < 10 ? "0" : "") + this.episode;
        tags = Arrays.asList(
                new Tag("\\{s00e00}", "S" + season + "E" + episode),
                new Tag("\\{sxe}", season + "x" + episode),
                new Tag("\\{t}", epName),
                new Tag("\\{n}", tvShowTitle)
        );
    }

    /**
     * This method is used to rename the file, it just returns the name we will use to rename the file.
     *
     * @return Returns the name we will rename the file to
     */
    public String getFinalName(){
        String expr = Config.getInstance().asString(Config.EPISODE_RENAME_EXPRESSION).toLowerCase();
        for(Tag t : tags){
            expr = expr.replaceAll(t.tag, t.value);
        }
        return expr;
    }

    /**
     * Rename the file from the constructor to the correct title
     */
    public void rename(){
        File dest = new File(fileToRename.getParentFile(),
                getFinalName() + "." + FilenameUtils.getExtension(fileToRename.getAbsolutePath()));
        boolean success = fileToRename.renameTo(dest);

        //Inform if the rename succeeded or if it failed
        if(success){
            logger.info("Renamed file to: " + dest.getAbsolutePath());
        }else{
            logger.warning("Failed to rename file from: " + fileToRename.getAbsolutePath());
        }
    }
    
    public static record Tag(String tag, String value){};
}
