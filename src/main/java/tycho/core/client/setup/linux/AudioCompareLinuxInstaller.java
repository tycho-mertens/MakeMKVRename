package tycho.core.client.setup.linux;

import tycho.core.client.setup.Installer;
import tycho.core.misc.FileManager;

public class AudioCompareLinuxInstaller extends Installer {

    @Override
    protected String url() {
        return FileManager.getInstance().getExternalResourceFile("Tools/audio_compare.py");
    }

    @Override
    protected String fileName() {
        return "audio_compare.py";
    }

    @Override
    public void setup() {
        copy(url(), fileName());
    }


}
