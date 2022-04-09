package tycho.core.misc;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class ImageConverter {

    private static final Logger logger = Logger.getLogger(ImageConverter.class.getName());

    public static File tempPosterDir = null;

    public ImageConverter(){
        //Create new temp dir if none exists
        tempPosterDir = tempPosterDir == null
                ? FileManager.getInstance().createTempDirectory()
                : tempPosterDir;
    }

    /**
     * Converts the given image to a png format
     *
     * @param img The image you want to convert as a File object
     * @return Returns an image that is in the png format as a File object
     */
    public File convertImgToPng(File img){
        FileManager fm = FileManager.getInstance();

        //Create temp file
        File outTempFile = fm.createTempFile(".png", tempPosterDir);
        try {
            //Convert input img to png and save it in the output file
            ImageIO.write(ImageIO.read(img), "png", outTempFile);
        }catch(IOException e){
            logger.warning("Failed to convert image to the png format");
            e.printStackTrace();
        }

        //Return result
        return outTempFile;
    }

    /**
     * Downloads the given image from the url and converts it to a png format image
     *
     * @param imgUrl The url to the image you want to convert
     * @return Returns an image that is in the png format as a File object
     */
    public File convertUrlToPng(String imgUrl) {
        FileManager fm = FileManager.getInstance();

        //Create temp file
        File inpTempFile = fm.createTempFile(getExtensionFromUrl(imgUrl), tempPosterDir);

        try {
            //Download the image into the input temp file
            FileUtils.copyURLToFile(new URL(imgUrl), inpTempFile);
        }catch(IOException e){
            logger.warning("Failed to download image to file (most likely something with the url)");
            e.printStackTrace();
        }
        //Return result
        return convertImgToPng(inpTempFile);
    }

    /**
     * Extracts the extension from an url, if nothing is found it will return '.jpg'
     *
     * @param url The url you want to try and extract the extension from
     * @return Returns the extension, if one is found, otherwise it returns the default 'jpg' as the extension
     */
    private String getExtensionFromUrl(String url){
        String ext = FilenameUtils.getExtension(url);

        //Return the extension if found, otherwise return the default 'jpg' as extension
        return "." +
                ((ext == null || ext.isEmpty())
                ? "jpg"
                : ext);
    }

    public File getTempDir() {
        return tempPosterDir;
    }
}
