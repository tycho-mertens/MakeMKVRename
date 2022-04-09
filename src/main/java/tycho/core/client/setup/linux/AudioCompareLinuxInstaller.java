package tycho.core.client.setup.linux;

import tycho.core.client.setup.Installer;

public class AudioCompareLinuxInstaller extends Installer {

    @Override
    protected String url() {
        return "Tools/audio_compare.py";
    }

    @Override
    public void setup() {
        copy(url(), "audio_compare.py");
    }


}
