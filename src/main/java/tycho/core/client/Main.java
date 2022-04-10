package tycho.core.client;

import tycho.core.client.setup.Installer;
import tycho.core.misc.FileManager;
import javafx.application.Application;
import javafx.stage.Stage;
import tycho.core.misc.SceneBuilder;
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {


        FileManager fManager = FileManager.getInstance();

        //Clear temp files
        fManager.clearDefaultTempDirectory();

        //Install necessary tools to run this program
        Installer.startup();

        //Creating stage and scene from method
        new SceneBuilder("main.fxml")
                .setCloseAllOnExit(true)
                .addImage("icon.png")
                .build()
                .show();

    }
}
