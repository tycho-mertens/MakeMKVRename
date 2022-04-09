package tycho.core.client.setup.linux;

import tycho.core.client.setup.Installer;
import tycho.core.misc.FileManager;

import java.io.File;

public class PythonLinuxInstaller extends Installer {

    @Override
    protected String url() {
        return FileManager.getInstance().getExternalResourceFile("Tools/linux/python_linux.zip");
    }

    @Override
    public void setup() {
        unzip(new File(url()), FileManager.getInstance().getExecutablesDir());
    }

    @Override
    public boolean isInstalled() {
        return new File(FileManager.getInstance().getExecutablesDir(), "python").exists();
    }
}
