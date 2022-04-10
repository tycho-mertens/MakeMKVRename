package tycho.core.client.setup;

import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import tycho.core.client.setup.linux.AudioCompareLinuxInstaller;
import tycho.core.client.setup.linux.FfmpegLinuxInstaller;
import tycho.core.client.setup.linux.FpCalcLinuxInstaller;
import tycho.core.client.setup.linux.PythonLinuxInstaller;
import tycho.core.misc.FileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public abstract class Installer {

    private static final Logger logger = Logger.getLogger(Installer.class.getName());

    /**
     * Get the url to the file we need to download
     * @return Returns the url to file
     */
    protected abstract String url();

    /**
     * The name of the installer
     * @return Returns the name
     */
    protected abstract String fileName();

    /**
     * Extracts the files from the zip file (if needed) and moves them to the correct place
     */
    public abstract void setup();

    /**
     * @return returns whether the files have been extracted and moved or not
     */
    public boolean isInstalled(){
       return new File(FileManager.getInstance().getExecutablesDir(), fileName()).exists();
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
     * Extract a file with the given format and compression
     * @param tar The compressed file
     * @param dest Where to extract it
     * @param archiveFormat The format (ex. "tar")
     * @param compression The compression (ex. "xz")
     */
    protected void unzipLinux(File tar, File dest, String archiveFormat, String compression){
        try {
            Archiver archiver = ArchiverFactory.createArchiver(archiveFormat, compression);
            archiver.extract(tar, dest);
        }catch (IOException e){
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
            File sourceFile = new File(source);
            File targetFile = new File(FileManager.getInstance().getExecutablesDir(), target);
            Files.copy(sourceFile.toPath(), targetFile.toPath()
                    , StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * @param url String you want as a URL
     * @return Returns a URL object from string
     */
    protected URL stringToURL(String url){
        try{
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void startup(){
        logger.info("Downloading needed tools if necessary");
        List<Installer> installs = Arrays.asList(new AudioCompareLinuxInstaller(), new FfmpegLinuxInstaller()
                , new FpCalcLinuxInstaller(), new PythonLinuxInstaller());
        for(Installer install : installs){
            if(!install.isInstalled())
                install.setup();
        }
    }
}
