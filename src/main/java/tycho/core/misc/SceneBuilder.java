package tycho.core.misc;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import tycho.core.config.Config;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class SceneBuilder {

    private static final Logger logger = Logger.getLogger(SceneBuilder.class.getName());

    private final Stage stage;
    private Parent root;
    private FXMLLoader loader;
    private boolean closeAllOnExit;
    private boolean onTop;

    /**
     * Constructor that initializes the Stage and Parent objects
     *
     * @param scene The path to the fxml file
     */
    public SceneBuilder(String scene){
        Config config = Config.getInstance();
        stage = new Stage();

        onTop = false;

        try {
            //Create loader
            loader = new FXMLLoader(Objects.requireNonNull(SceneBuilder.class.getClassLoader().getResource(scene)));
            //Create parent object from fxml and set the scene for the stage
            root = loader.load();
        } catch (IOException e) {
            logger.warning("Failed to create Parent object for scene: " + scene);
            e.printStackTrace();
        }

        //Set the title for the stage and disable resizing
        stage.setTitle(config.getProgramName());
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);

        closeAllOnExit = false;
        addStyleSheet(Config.getInstance().asString(Config.CSS_STYLE));
    }

    /**
     * Returns the fully initialized Stage object
     *
     * @return Returns the fully initialized Stage object
     */
    public Stage build(){
        stage.setScene(new Scene(root));

        if(closeAllOnExit){
            //Make sure every thread closes properly
            stage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(1);
            });
        }

        stage.setAlwaysOnTop(onTop);//This sometimes works. Likely workaround to work -> use listeners and call toFront and toBack and create a whole handler
        return stage;
    }

    public FXMLLoader getLoader(){
        return loader;
    }

    /**
     * Determine if you want to close all threads or not when you close the window of the stage
     *
     * @param closeAllOnExit Whether to close every thread and stage of the application or not
     * @return Return the current SceneBuilder object
     */
    public SceneBuilder setCloseAllOnExit(boolean closeAllOnExit){
        this.closeAllOnExit = closeAllOnExit;
        return this;
    }

    public SceneBuilder setCloseOnKeyPressed(KeyCode keyCode){
        root.setOnKeyPressed(e -> {
            if(e.getCode().equals(keyCode)){
                ((Stage) root.getScene().getWindow()).close();
            }
        });
        return this;
    }

    public SceneBuilder setAlwaysOnTop(){
//        StageOrder.getInstance().add(stage);
        return this;
    }

    /**
     * Sets the subtitle for the stage
     *
     * @param subTitle The subtitle for the window
     * @return Returns the current SceneBuilder object
     */
    public SceneBuilder setSubTitle(String subTitle){
        stage.setTitle(stage.getTitle() + " - " + subTitle);
        return this;
    }

    /**
     * Sets the possibility of resizing for the stage
     *
     * @param resizable Allow or disallow resizing of the window
     * @return Returns the current SceneBuilder object
     */
    public SceneBuilder setResizable(boolean resizable){
        stage.setResizable(resizable);
        return this;
    }

    /**
     * Adds a styleSheet to the stage object
     *
     * @param cssPath The path to the css (styleSheet) path
     * @return Returns the current SceneBuilder object
     */
    public SceneBuilder addStyleSheet(String cssPath){
        root.getStylesheets().add(cssPath);
        return this;
    }

    /**
     * Adds an image to the stage object, so we can use it later on
     *
     * @param imgPath The path to the image
     * @return Returns the current SceneBuilder object
     */
    public SceneBuilder addImage(String imgPath){
        stage.getIcons().add(new Image(Objects.requireNonNull(SceneBuilder.class.getClassLoader().getResourceAsStream(imgPath))));
        return this;
    }


//    /**
//     * This class was found here: https://www.databaseusers.com/article/7722094/How+to+keep+a+Stage+always+on+top... (yes with the three dots at the end)
//     * I'll probably write my own when I feel like it's worth to do so
//     *
//     * NOTE: This class is not thread safe (AKA if you open a new stage twice really close together, it will freeze)
//     */
//    private static class StageOrder implements ListChangeListener<Stage> {
//        ObservableList<Stage> stages = FXCollections.observableArrayList();
//
//        private boolean FIRST_TOP = true;
//
//        private static StageOrder instance;
//
//        public static synchronized StageOrder getInstance(){
//            if(instance == null) {
//                instance = new StageOrder();
//                instance.setFirstTop(false);
//            }
//            return instance;
//        }
//
//        private StageOrder(){
//            stages.addListener(this);
//        }
//
//        public void setFirstTop(boolean b){
//            FIRST_TOP = b;
//        }
//        public ObservableList<Stage> getStages() {
//            return stages;
//        }
//
//
//        public void refresh(){
//            if(FIRST_TOP){
//                for (Stage value : stages) {
//                    value.toBack();
//                }
//            }else{
//                for (Stage value : stages) {
//                    value.toFront();
//                }
//            }
//        }
//
//        public void onChanged(Change<? extends Stage> c) {
//            refresh();
//        }
//
//        public void add(final Stage s){
//            stages.add(s);
//            s.focusedProperty().addListener((observable, oldValue, newValue) -> refresh());
//        }
//    }

}


