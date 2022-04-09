package tycho.core.client.beans;

import tycho.core.client.fingerprint.Fingerprint;
import tycho.core.misc.TvShowRenamer;

import java.io.File;

public class OriginalFileBean {

    private final File file;
    private final String name;
    private Fingerprint fingerprint;
    private TvShowRenamer tvShowRenamer;

    /**
     * Default constructor
     *
     * @param file The file that will be renamed to the correct episode title etc...
     */
    public OriginalFileBean(File file) {
        this.file = file;
        name = file.getName();
        tvShowRenamer = null;
        fingerprint = null;
    }

    /**
     * Default getter
     *
     * @return Returns the fingerprint of the file
     */
    public Fingerprint getFingerprint(){
        return fingerprint;
    }

    /**
     * Default setter
     *
     * @param fingerprint Sets the fingerprint of the file
     */
    public void setFingerprint(Fingerprint fingerprint){
        this.fingerprint = fingerprint;
    }

    /**
     * Default getter
     *
     * @return Returns the object that will rename the file
     */
    public TvShowRenamer getTvShowRenamer(){
        return tvShowRenamer;
    }

    /**
     * Default setter
     *
     * @param tvShowRenamer Sets the tvShowRenamer object
     */
    public void setTvShowRenamer(TvShowRenamer tvShowRenamer){
        this.tvShowRenamer = tvShowRenamer;
    }

    /**
     * Default getter
     *
     * @return Returns the file
     */
    public File getFile(){
        return file;
    }

    /**
     * Default getter
     *
     * @return Returns the name of the file/column
     */
    public String getName(){
        return name;
    }

    /**
     * Compare two originalFileBeans with one another
     *
     * @param obj The object you want to compare with the current object 'OriginalFileBean'
     * @return Returns true if both paths of the file in the object are equal, otherwise it returns false
     */
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof OriginalFileBean)) return false;
        return file.getAbsolutePath().equals(((OriginalFileBean) obj).getFile().getAbsolutePath());
    }
}
