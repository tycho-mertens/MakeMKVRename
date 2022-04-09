package tycho.core.client.setup.linux;


import tycho.core.client.setup.Installer;

public class FpCalcLinuxInstaller extends Installer {
    @Override
    protected String url() {
        return "Tools/linux/fpcalc";
    }

    @Override
    public void setup() {
        copy(url(), "fpcalc");
    }
}
