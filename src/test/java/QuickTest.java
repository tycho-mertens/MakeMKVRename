import org.junit.Test;
import tycho.core.client.setup.Installer;
import tycho.core.client.setup.linux.AudioCompareLinuxInstaller;
import tycho.core.client.setup.linux.FfmpegLinuxInstaller;
import tycho.core.client.setup.linux.FpCalcLinuxInstaller;
import tycho.core.client.setup.linux.PythonLinuxInstaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class QuickTest {

    @Test
    public void test()  {
        Installer.startup();
    }

}
