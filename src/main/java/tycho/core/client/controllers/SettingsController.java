package tycho.core.client.controllers;

import tycho.core.misc.ColorUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import tycho.core.config.Config;
import tycho.core.misc.AlertUtils;

import java.util.Arrays;
import java.util.Optional;

public class SettingsController implements IController {


    @FXML
    private TextField tmdbApi;
    @FXML
    private TextField jsonDb;
    @FXML
    private TextField ffmpegPath;
    @FXML
    private TextField fpcalcPath;
    @FXML
    private TextField pythonPathWin;
    @FXML
    private TextField pythonPathLin;
    @FXML
    private TextField audioComparePath;
    @FXML
    private ComboBox<String> themes;
    @FXML
    private TextField epRenameExpr;
    @FXML
    private Slider lowSimThreshold;
    @FXML
    private ColorPicker lowSimColor;
    @FXML
    private Slider darkenSelColor;
    @FXML
    private ColorPicker selectedRowColor;
    @FXML
    private ColorPicker loaderBackgroundColor;
    @FXML
    private Slider loaderOpacity;

    @FXML
    private Button resetBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;


    @FXML
    private Label tmdbApiLbl;
    @FXML
    private Label jsonDbLbl;
    @FXML
    private Label ffmpegPathLbl;
    @FXML
    private Label fpcalcPathLbl;
    @FXML
    private Label pythonPathWinLbl;
    @FXML
    private Label pythonPathLinLbl;
    @FXML
    private Label audioComparePathLbl;
    @FXML
    private Label themesLbl;
    @FXML
    private Label epRenameExprLbl;
    @FXML
    private Label lowSimThresholdLbl;
    @FXML
    private Label lowSimColorLbl;
    @FXML
    private Label darkenSelColorLbl;
    @FXML
    private Label selectedRowColorLbl;
    @FXML
    private Label loaderBackgroundColorLbl;
    @FXML
    private Label loaderOpacityLbl;

    private final Config config;

    public SettingsController() {
        config = Config.getInstance();
    }

    /**
     * Quickly set the boundaries to a universal default max. 1 and min. 0
     *
     * @param slider The slider you want to adjust the boundaries for
     */
    private void setSliderMaxMinValues(Slider slider){
        slider.setMax(1);
        slider.setMin(0);
    }

    @FXML
    public void initialize() {
        //Set max and min for the sliders
        setSliderMaxMinValues(lowSimThreshold);
        setSliderMaxMinValues(darkenSelColor);
        setSliderMaxMinValues(loaderOpacity);

        loadSettingsConfig(config);

        themes.setItems(FXCollections.observableList(Arrays.asList("Dark", "White")));
        //Set the theme value
        themes.getSelectionModel().select(config.asString(Config.CSS_STYLE)
                .equalsIgnoreCase("dracula.css") ? "Dark" : "White");

        initButtons();
        initTooltips();
    }

    /**
     * Add the Tooltip object to all the given controls
     *
     * @param tooltip The tooltip object you want to add to the controls
     * @param Controls The controls that you want the tooltip to apply to
     */
    private void addTooltip(Tooltip tooltip, Control... Controls) {
        for (Control control : Controls) {
            control.setTooltip(tooltip);
        }
    }

    /**
     * Create a tooltip object with a longer duration to show the text
     *
     * @param text The text you want the Tooltip to have
     * @return Returns a new Tooltip object with the given text and a longer Show Duration
     */
    private Tooltip createLongerDurationTooltip(String text){
        Tooltip t = new Tooltip(text);
        t.setShowDuration(Duration.minutes(1));
        return t;
    }

    /**
     * Create tooltips for all the controls and add them to it
     */
    private void initTooltips() {
        addTooltip(createLongerDurationTooltip("The TMdb api that will be used to match the episodes"), tmdbApi, tmdbApiLbl);
        addTooltip(createLongerDurationTooltip("The path to the jsonDB file"), jsonDb, jsonDbLbl);
        addTooltip(createLongerDurationTooltip("The path to the FfMpeg executable"), ffmpegPath, ffmpegPathLbl);
        addTooltip(createLongerDurationTooltip("The path to the FpCalc executable"), fpcalcPath, fpcalcPathLbl);
        addTooltip(createLongerDurationTooltip("The path to the Python executable for Windows"), pythonPathWin, pythonPathWinLbl);
        addTooltip(createLongerDurationTooltip("The path to the Python executable for Linux"), pythonPathLin, pythonPathLinLbl);
        addTooltip(createLongerDurationTooltip("The path to the compare script"), audioComparePath, audioComparePathLbl);
        addTooltip(createLongerDurationTooltip("Themes for the JavaFX scenes"), themes, themesLbl);
        addTooltip(createLongerDurationTooltip("""
                The expression that we will use to rename the episodes
                {n} - Name of the TV Show (ex. 'Person of Interest')
                {s00e00} - The season and episode (ex. 'S01E01')
                {t} - The title of the episode (ex. 'Pilot')
                """), epRenameExpr, epRenameExprLbl);
        addTooltip(createLongerDurationTooltip("The similarity score threshold that we will consider not to be a match"), lowSimThreshold, lowSimThresholdLbl);
        addTooltip(createLongerDurationTooltip("The background color for the rows with a low similarity score"), lowSimColor, lowSimColorLbl);
        addTooltip(createLongerDurationTooltip("The percentage to darken a color for the row that is selected and already colored"), darkenSelColor, darkenSelColorLbl);
        addTooltip(createLongerDurationTooltip("The background color the row that is selected"), selectedRowColor, selectedRowColorLbl);
        addTooltip(createLongerDurationTooltip("The background color for the Pre Loader Animation"), loaderBackgroundColor, loaderBackgroundColorLbl);
        addTooltip(createLongerDurationTooltip("The opacity for the Pre Loader Animation"), loaderOpacity, loaderOpacityLbl);
    }

    /**
     * Create all the events and listeners etc for the buttons
     */
    private void initButtons() {
        resetBtn.setOnMouseClicked(e -> {
            config.loadDefaultSettings();
            loadSettingsConfig(config);
        });

        cancelBtn.setOnMouseClicked(e -> ((Stage) cancelBtn.getScene().getWindow()).close());

        saveBtn.setOnMouseClicked(e -> {
            Alert alert = AlertUtils.createAlertWithStyling(Alert.AlertType.INFORMATION, "Settings");

            //We need to make sure that the css file has been loaded first, that's why we create the Alert object first
            saveSettings();

            alert.setHeaderText("Settings Saved!");
            alert.setContentText("Please restart the application immediately. The program will act weird if you don't restart after saving the config. This is a bug, that I still have to find a solution for.");
//            alert.setContentText("Restart the application for the settings to take effect.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty()) return;
            if (result.get() == ButtonType.OK) {
                ((Stage) saveBtn.getScene().getWindow()).close();
            }
        });
    }

    /**
     * Saves the settings
     */
    private void saveSettings() {
        config.addProperty(Config.TMDB_API, tmdbApi.getText());
        config.addProperty(Config.JSON_DB, jsonDb.getText());
        config.addProperty(Config.FFMPEG_PATH, ffmpegPath.getText());
        config.addProperty(Config.FPCALC_PATH, fpcalcPath.getText());
        config.addProperty(Config.PYTHON_PATH_LINUX, pythonPathLin.getText());
        config.addProperty(Config.PYTHON_PATH_WINDOWS, pythonPathWin.getText());
        config.addProperty(Config.AUDIO_COMPARE_PATH, audioComparePath.getText());
        config.addProperty(Config.CSS_STYLE, themes.getValue().equalsIgnoreCase("dark") ? "dracula.css" : "");
        config.addProperty(Config.EPISODE_RENAME_EXPRESSION, epRenameExpr.getText());
        config.addProperty(Config.LOW_SIMILARITY_THRESHOLD, (float) lowSimThreshold.getValue());
        config.addProperty(Config.DARKEN_SELECTED_ROW_PERCENTAGE, (float) darkenSelColor.getValue());
        config.addProperty(Config.LOW_SIMILARITY_COLOR, ColorUtils.javaFXColorToHex(lowSimColor.getValue()));
        config.addProperty(Config.HIGHLIGHTED_ROW_COLOR, ColorUtils.javaFXColorToHex(selectedRowColor.getValue()));
        config.addProperty(Config.PRE_LOADER_BACKGROUND_COLOR, ColorUtils.javaFXColorToHex(loaderBackgroundColor.getValue()));
        config.addProperty(Config.PRE_LOADER_OPACITY, loaderOpacity.getValue());
        config.save();
    }

    /**
     * Load the settings from the given Config object
     *
     * @param config The config you want to load the settings from
     */
    private void loadSettingsConfig(Config config) {
        tmdbApi.setText(config.asString(Config.TMDB_API));
        jsonDb.setText(config.asString(Config.JSON_DB));
        ffmpegPath.setText(config.asString(Config.FFMPEG_PATH));
        fpcalcPath.setText(config.asString(Config.FPCALC_PATH));
        pythonPathWin.setText(config.asString(Config.PYTHON_PATH_WINDOWS));
        pythonPathLin.setText(config.asString(Config.PYTHON_PATH_LINUX));
        audioComparePath.setText(config.asString(Config.AUDIO_COMPARE_PATH));
        epRenameExpr.setText(config.asString(Config.EPISODE_RENAME_EXPRESSION));
        lowSimThreshold.setValue(config.asFloat(Config.LOW_SIMILARITY_THRESHOLD));
        lowSimColor.setValue(Color.web(config.asString(Config.LOW_SIMILARITY_COLOR)));
        selectedRowColor.setValue(Color.web(config.asString(Config.HIGHLIGHTED_ROW_COLOR)));
        darkenSelColor.setValue(config.asFloat(Config.DARKEN_SELECTED_ROW_PERCENTAGE));
        themes.getSelectionModel().select(config.asString(Config.CSS_STYLE).equalsIgnoreCase("dracula.css")
                ? "Dark" : "");
        loaderBackgroundColor.setValue(Color.web(config.asString(Config.PRE_LOADER_BACKGROUND_COLOR)));
        loaderOpacity.setValue(config.asDouble(Config.PRE_LOADER_OPACITY));
    }
}
