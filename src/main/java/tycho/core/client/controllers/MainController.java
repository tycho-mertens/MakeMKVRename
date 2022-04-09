package tycho.core.client.controllers;

import tycho.core.client.beans.RenameToBean;
import tycho.core.client.controllers.Table.ChainedRowFactory;
import tycho.core.client.controllers.Table.ChainedTableHandler;
import tycho.core.client.fingerprint.CallableResult;
import tycho.core.client.fingerprint.FingerprintMatcher;
import tycho.core.misc.AlertUtils;
import tycho.core.misc.ImageUtils;
import tycho.core.misc.TvShowRenamer;
import tycho.core.tmdb.TMdb;
import tycho.core.tmdb.model.TvEpisode;
import tycho.core.client.controllers.DataExchange.IHasData;
import tycho.core.client.controllers.DataRecords.TMdbDataRecord;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import tycho.core.client.beans.OriginalFileBean;
import tycho.core.misc.SceneBuilder;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MainController implements IController, IHasData<TMdbDataRecord> {

    private static final Logger logger = Logger.getLogger(MainController.class.getName());

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<OriginalFileBean> originalFilesTable;

    @FXML
    private TableView<RenameToBean> renameToTable;

    @FXML
    private Button findMatchesBtn;

    @FXML
    private Button renameBtn;

    @FXML
    private Button uploadFingerprintsBtn;

    @FXML
    private Button settingsBtn;

    @FXML
    private Button browseBtn;

    @FXML
    private Button tmdbIdBtn;

    @FXML
    private ContextMenu originalFileContextMenu;

    @FXML
    private TableColumn<OriginalFileBean, String> ogFileNameCol;

    @FXML
    private TableColumn<RenameToBean, String> titleCol;

    @FXML
    private TableColumn<RenameToBean, Integer> seasonCol;

    @FXML
    private TableColumn<RenameToBean, Integer> episodeCol;

    @FXML
    private TableColumn<RenameToBean, Float> percentageCol;

    @FXML
    private ComboBox<Integer> seasonComboBox;

    @FXML
    private ImageView posterImg;

    @FXML
    private Label itemLbl;

    @FXML
    private Label fingerprintLbl;

    @FXML
    private Label titleLbl;

    @FXML
    private Label yearLbl;

    @FXML
    private MenuItem deleteMenu1;

    @FXML
    private MenuItem deleteMenu2;

    private ChainedTableHandler<OriginalFileBean, RenameToBean> tableHandler;

    private TMdbDataRecord dataRecord;

    /**
     * Constructor in case we need to do something in here
     */
    public MainController() {
        dataRecord = null;
    }

    /**
     * Initialize the scene
     */
    @FXML
    public void initialize() {
        cssAndTableInit();
        buttonInit();
        dragAndDropInit();
    }

    /**
     * Initialize all buttons
     */
    private void buttonInit() {
        //Set icon for the tmdbId button
        tmdbIdBtn.setStyle("""
                -fx-min-height: 25px;
                -fx-min-width: 25px;
                -fx-background-image: url("databasesearch.png");
                -fx-background-size: 16px 16px;
                -fx-background-repeat: no-repeat;
                -fx-background-position: 6px;
                """);

        //Set action for tmdbIdBtn
        tmdbIdBtn.setOnMouseClicked(e ->
                TMdbFinderController.startNewStage(this));

        browseBtn.setOnMouseClicked(e -> new SceneBuilder("browser.fxml")
                .setSubTitle("Browser")
                .setResizable(false)
                .setCloseOnKeyPressed(KeyCode.ESCAPE)
                .setAlwaysOnTop()
                .build()
                .show());

        //Set action for settingsBtn
        settingsBtn.setOnMouseClicked(e -> new SceneBuilder("settings.fxml")
                .setAlwaysOnTop()
                .setSubTitle("Settings")
                .setResizable(false)
                .setCloseOnKeyPressed(KeyCode.ESCAPE)
                .build()
                .show());

        //Set action for findMatchesBtn
        findMatchesBtn.setOnMouseClicked(e -> {

            //Checks
            Alert deny = AlertUtils.createAlertWithStyling(Alert.AlertType.INFORMATION, "Matcher");
            if (originalFilesTable.getItems().isEmpty()) {
                deny.setHeaderText("You have to add items first");
                deny.show();
                return;
            } else if (dataRecord == null) {
                deny.setHeaderText("Please select a show first");
                deny.show();
                return;
            } else if (seasonComboBox.getSelectionModel().getSelectedIndex() == -1) {
                deny.setHeaderText("Please select a season first");
                deny.show();
                return;
            }


            //Loop over all the video files
            int totalItems = originalFilesTable.getItems().size();
            itemLbl.setText(String.format("(%d/%d)", 0, totalItems));
            fingerprintLbl.setText(String.format("(%d/%s)", 0, "?"));

            //Start new thread, so the main application won't freeze
            new Thread(() -> {
                for (int i = 0; i < totalItems; i++) {
                    int finalI = i;
                    OriginalFileBean ofb = originalFilesTable.getItems().get(i);
                    Platform.runLater(() -> itemLbl.setText(String.format("(%d/%d)", finalI + 1, totalItems)));
                    FingerprintMatcher matcher = new FingerprintMatcher(fingerprintLbl);
                    matcher.add(ofb.getFile(), dataRecord.tmdbId(), seasonComboBox.getSelectionModel().getSelectedItem());

                    CallableResult result = matcher.bestMatch();

                    if (result == null) return;

                    TMdb tmdb = TMdb.getInstance();
                    TvEpisode episode = tmdb.getTvEpisode(result.tmdbId(), result.season(), result.episode());
                    RenameToBean rtb = new RenameToBean(result.highestPercentage(), episode.getName(), result.season(), result.episode());
                    List<RenameToBean> renameToBeanList = new ArrayList<>(renameToTable.getItems());
                    renameToBeanList.add(rtb);
                    renameToTable.setItems(FXCollections.observableList(renameToBeanList));
                }

                tableHandler.clearAllCssRowStyles();
                tableHandler.colorAllLowSimilarityRows();
                AlertUtils.showAllDone("Matcher");
            }).start();

        });

        renameBtn.setOnMouseClicked(e -> {
            //Checks
            if(renameToTable.getItems().isEmpty()){
                AlertUtils.showMsg("No files to rename", "Renamer");
                return;
            }
            for (int i = 0; i < originalFilesTable.getItems().size(); i++) {
                OriginalFileBean ofb = originalFilesTable.getItems().get(i);
                RenameToBean rtb = renameToTable.getItems().get(i);

                TvShowRenamer renamer = new TvShowRenamer(dataRecord.title(), rtb.getTitle(),
                        rtb.getSeason(), rtb.getEpisode(), ofb.getFile());

                renamer.rename();
            }

            originalFilesTable.getItems().clear();
            renameToTable.getItems().clear();
            AlertUtils.showMsg("Files renamed", "Renamer");
        });

        uploadFingerprintsBtn.setOnMouseClicked(e -> {
            Stage stage = new SceneBuilder("upload.fxml")
                    .setSubTitle("Fingerprint Uploader")
                    .addImage("databasesearch.png")
                    .setResizable(false)
                    .setAlwaysOnTop()
                    .setCloseOnKeyPressed(KeyCode.ESCAPE)
                    .build();
            stage.show();
        });
    }

    /**
     * Enable css and initialize tables
     */
    private void cssAndTableInit() {

        //Set some resize policies
        originalFilesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        renameToTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Deny table sorting
        ogFileNameCol.setSortable(false);
        titleCol.setSortable(false);
        seasonCol.setSortable(false);
        episodeCol.setSortable(false);
        percentageCol.setSortable(false);

        //Some stuff regarding binding columns with the correct fields etc...
        ogFileNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        seasonCol.setCellValueFactory(new PropertyValueFactory<>("season"));
        episodeCol.setCellValueFactory(new PropertyValueFactory<>("episode"));
        percentageCol.setCellValueFactory(new PropertyValueFactory<>("similarity"));

        percentageCol.setMaxWidth(80);
        seasonCol.setMaxWidth(80);
        episodeCol.setMaxWidth(80);

        //Init table handler, this will handle everything relating colors, selection, deletion, etc. for the tables.
        //Create the row factories
        ChainedRowFactory<OriginalFileBean> factoryOne = new ChainedRowFactory<>();
        ChainedRowFactory<RenameToBean> factoryTwo = new ChainedRowFactory<>();
        //Set the factories to the correct table
        originalFilesTable.setRowFactory(factoryOne);
        renameToTable.setRowFactory(factoryTwo);

        //Create a new table handler, this will handle two tables and link them together
        tableHandler =
                new ChainedTableHandler<>(originalFilesTable, renameToTable,
                        null, null, 0);

        //Make it possible to access the ChainedTableHandler from the ChainedRowFactory
        factoryOne.setHandler(tableHandler);
        factoryTwo.setHandler(tableHandler);

        //Make deletion of a row possible
        deleteMenu1.setOnAction((e) -> tableHandler.removeSelectedFullRow());
        deleteMenu2.setOnAction((e) -> tableHandler.removeSelectedFullRow());
    }

    /**
     * Enable drag and drop for the main table 'originalFilesTable'
     */
    private void dragAndDropInit() {
        originalFilesTable.setOnDragOver(dragEvent -> {
            if (dragEvent.getGestureSource() != originalFilesTable
                    && dragEvent.getDragboard().hasFiles()) {
                dragEvent.acceptTransferModes(TransferMode.COPY);
            }
            dragEvent.consume();
        });
        originalFilesTable.setOnDragDropped(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            ObservableList<OriginalFileBean> files = originalFilesTable.getItems();

            if (db.hasFiles()) {
                files.addAll(FXCollections.observableArrayList(db.getFiles().stream().map(OriginalFileBean::new).filter(ogFile -> {
                    //Filter all files out of the list that are already in the table, this way we prevent duplicates
                    boolean found = false;
                    for (OriginalFileBean ofb : files) {
                        if (ofb.equals(ogFile)) {
                            found = true;
                            break;
                        }
                    }
                    return !found;
                }).sorted(Comparator.comparing(OriginalFileBean::getName)).collect(Collectors.toList())));
                originalFilesTable.setItems(files);
            }
            dragEvent.setDropCompleted(true);

            dragEvent.consume();
        });

    }

    /**
     * Communicate with other scenes
     *
     * @param data The data to pass through the another scene
     */
    @Override
    public void setObjectData(TMdbDataRecord data) {
        dataRecord = data;
        seasonComboBox.setItems(FXCollections.observableList(data.seasons()));
        titleLbl.setText(data.title());
        yearLbl.setText("" + data.year());
        try {
            posterImg.setImage(ImageUtils.getImageFromFile(data.posterFile()));
        } catch (Exception e) {
            posterImg.setImage(ImageUtils.getNoPosterFoundImage());
        }
    }
}
