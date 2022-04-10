package tycho.core.client.setup.linux;

import tycho.core.client.setup.Installer;
import tycho.core.misc.FileManager;

import java.io.File;

public class PythonLinuxInstaller extends Installer {

    @Override
    protected String url() {
        return FileManager.getInstance().getExternalResourceFile("Tools/linux/python.tar.xz");
    }

    @Override
    protected String fileName() {
        return "python";
    }

    @Override
    public void setup() {
        unzipLinux(new File(url()), FileManager.getInstance().getExecutablesDir(), "tar", "xz");
    }

    @Override
    public boolean isInstalled() {
        return new File(FileManager.getInstance().getExecutablesDir(), "python").exists();
    }
}
