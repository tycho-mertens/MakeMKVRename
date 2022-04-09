package tycho.core.misc;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import tycho.core.config.Config;

public class PreLoaderAnimation {

    private final AnchorPane anchorPane;
    private final ImageView loader;
    private final Pane background;
    private String backgroundColor;
    private double opacity;
    private double loaderWidth;
    private double loaderHeight;

    /**
     * Default constructor
     *
     * @param anchorPane The main (anchor)pane of the scene, we need this to add our components to it
     */
    public PreLoaderAnimation(AnchorPane anchorPane){
        this.anchorPane = anchorPane;
        loader = new ImageView();
        background = new Pane();

        Config c = Config.getInstance();
        opacity = c.asDouble(Config.PRE_LOADER_OPACITY);
        backgroundColor = c.asString(Config.PRE_LOADER_BACKGROUND_COLOR);

        loaderWidth = 170;
        loaderHeight = 128;

        setBackground();
        setupLoader();
    }

    /**
     * This method will set the proper settings for the loader image/gif
     *
     */
    private void setupLoader() {
        loader.setImage(ImageUtils.getImageFromResources("preLoader.gif"));
        loader.setFitWidth(loaderWidth);
        loader.setFitHeight(loaderHeight);
        loader.setLayoutX((anchorPane.getPrefWidth()/2)-(loader.getFitWidth()/2));
        loader.setLayoutY((anchorPane.getPrefHeight()/2)-(loader.getFitHeight()/2));
        background.getChildren().add(loader);
    }

    /**
     * This method will set the proper settings for the background pane
     *
     */
    private void setBackground(){
        background.setVisible(false);

        background.setOpacity(opacity);
        background.setStyle("-fx-background-color: " + backgroundColor + ";");

        background.setPrefWidth(anchorPane.getPrefWidth());
        background.setPrefHeight(anchorPane.getPrefHeight());
        anchorPane.getChildren().add(background);
    }

    /**
     * Show the loader
     *
     */
    public void showLoader(){
        Platform.runLater(() -> background.setVisible(true));
    }

    /**
     * Hide the loader
     *
     */
    public void hideLoader(){
        Platform.runLater(() -> background.setVisible(false));
    }

    /**
     * Default getter
     *
     * @return Returns the background pane
     */
    public Pane getBackground() {
        return background;
    }

    /**
     * Default getter
     *
     * @return Returns the color of the background pane
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Default setter
     *
     * @param backgroundColor The color you want for the background pane
     */
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Default getter
     *
     * @return Returns the opacity of the background pane
     */
    public double getOpacity() {
        return opacity;
    }

    /**
     * Default setter
     *
     * @param opacity The opacity you want the background pane to be
     */
    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    /**
     * Default getter
     *
     * @return Returns the width of the loader image
     */
    public double getLoaderWidth() {
        return loaderWidth;
    }

    /**
     * Default setter
     *
     * @param loaderWidth The width you want the loader image to be
     */
    public void setLoaderWidth(double loaderWidth) {
        this.loaderWidth = loaderWidth;
    }

    /**
     * Default getter
     *
     * @return Returns the height of the loader image
     */
    public double getLoaderHeight() {
        return loaderHeight;
    }

    /**
     * Default setter
     *
     * @param loaderHeight The height you want the loader image to be
     */
    public void setLoaderHeight(double loaderHeight) {
        this.loaderHeight = loaderHeight;
    }
}
