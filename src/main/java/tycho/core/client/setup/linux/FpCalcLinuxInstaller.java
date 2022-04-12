package tycho.core.client.setup.linux;

import tycho.core.client.setup.Installer;
public class FpCalcLinuxInstaller extends Installer {
    @Override
    protected String url() {
        return "https://github.com/acoustid/chromaprint/releases/download/v1.5.1/chromaprint-fpcalc-1.5.1-linux-x86_64.tar.gz";
    }

    @Override
    protected String fileName() {
        return "fpcalc";
    }

    @Override
    protected String getCompression() {
        return "gz";
    }

    //    @Override
//    public void setup() {
//        try {
//            File tar = new File(FileManager.getInstance().createTempDirectory(), "compressed.tar.gz");
//            FileUtils.copyURLToFile(stringToURL(url()), tar);
//            unzipLinux(tar, tar.getParentFile(), "tar", "gz");
//            copy(Arrays.stream(
//                            Objects.requireNonNull(
//                                    tar.getParentFile().listFiles(File::isDirectory)
//                            )).toList().get(0).getAbsolutePath() + File.separator + fileName()
//                    , fileName());
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }
}
