package tycho.core.misc;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;

public class ImageUtils {

    /**
     * Grabs the resource and turns it into an Image objects, furthermore it handles all the try catches, etc...
     *
     * @param resourceName The name of the file in the resource folder
     * @return Returns the resource as an Image object
     */
    public static Image getImageFromResources(String resourceName){
        try {
            return new Image(
                    new FileInputStream(
                            FileManager.getInstance().getExternalResourceFile(resourceName)
                    ));
        }catch(Exception e){
            return getNoPosterFoundImage();
        }
    }

    /**
     * Uses the file path to turn it into an Image objects, furthermore it handles all the try catches, etc...
     *
     * @param filePath The path to the image file
     * @return Returns an Image object that has been created from the file path
     */
    public static Image getImageFromFile(String filePath){
        try{
            return new Image(
                    new FileInputStream(
                            filePath
                    )
            );
        }catch(Exception e){
            return getNoPosterFoundImage();
        }
    }

    /**
     * Grabs the file and turns it into an Image objects, furthermore it handles all the try catches, etc...
     *
     * @param file The image file (aka '/home/user/Downloads/someRandomName.png')
     * @return Returns the file as an Image object
     */
    public static Image getImageFromFile(File file){
        return getImageFromFile(file.getAbsolutePath());
    }

    /**
     * Simply grabs the noPoster as an Image and returns it
     *
     * @return Returns the 'noPoster.png' as an Image object
     */
    public static Image getNoPosterFoundImage(){
        return getImageFromResources("noPoster.png");
    }
}
