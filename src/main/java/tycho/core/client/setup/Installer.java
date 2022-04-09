package tycho.core.client.setup;

import tycho.core.client.setup.linux.AudioCompareLinuxInstaller;
import tycho.core.client.setup.linux.FfmpegLinuxInstaller;
import tycho.core.client.setup.linux.FpCalcLinuxInstaller;
import tycho.core.client.setup.linux.PythonLinuxInstaller;
import tycho.core.misc.FileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public abstract class Installer {

    /**
     * Get the url to the file we need to download
     * @return Returns the url to file
     */
    protected abstract String url();

    /**
     * Extracts the files from the zip file (if needed) and moves them to the correct place
     */
    public abstract void setup();

    /**
     * @return returns whether the files have been extracted and moved or not
     */
    public boolean isInstalled(){
       return new File(FileManager.getInstance().getExecutablesDir(), new File(url()).getName()).exists();
    }

    /**
     * Unzip compressed files
     * @param zip The zip file to extract from
     * @param destDir Where to put the files we extract
     */
    protected void unzip(File zip, File destDir){
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zip))) {
            ZipEntry zipEntry = zis.getNextEntry();

            while(zipEntry != null){
                Path newPath = destDir.toPath().resolve(zipEntry.getName());
                if(zipEntry.getName().endsWith(File.separator)){
                    Files.createDirectories(newPath);
                }else{
                    if(newPath.getParent() != null && Files.notExists(newPath.getParent()))
                        Files.createDirectories(newPath.getParent());
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Quickly copy files from resource to drive
     * @param source The path to a resource file
     * @param target Where to copy to, in the executables directory
     */
    protected void copy(String source, String target){
        try {
            File sourceFile = new File(FileManager.getInstance().getExternalResourceFile(source));
            File targetFile = new File(FileManager.getInstance().getExecutablesDir(), target);
            Files.copy(sourceFile.toPath(), targetFile.toPath()
                    , StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void startup(){
        List<Installer> installs = Arrays.asList(new AudioCompareLinuxInstaller(), new FfmpegLinuxInstaller()
                , new FpCalcLinuxInstaller(), new PythonLinuxInstaller());
        for(Installer install : installs){
            if(!install.isInstalled())
                install.setup();
        }
    }
}
