import org.junit.Test;
import tycho.core.client.setup.Installer;
import tycho.core.client.setup.linux.AudioCompareLinuxInstaller;
import tycho.core.client.setup.linux.FfmpegLinuxInstaller;
import tycho.core.client.setup.linux.FpCalcLinuxInstaller;
import tycho.core.client.setup.linux.PythonLinuxInstaller;

public class QuickTest {

    @Test
    public void test()  {
        Installer.startup();
    }
}
