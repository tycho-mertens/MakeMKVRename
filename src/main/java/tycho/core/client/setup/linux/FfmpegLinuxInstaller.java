package tycho.core.client.setup.linux;

import tycho.core.client.setup.Installer;

import java.util.logging.Logger;

public class FfmpegLinuxInstaller extends Installer {

    private static final Logger logger = Logger.getLogger(FfmpegLinuxInstaller.class.getName());


    @Override
    protected String url() {
        return "Tools/linux/ffmpeg";
    }

    @Override
    public void setup() {
        copy(url(), "ffmpeg");
    }
}
