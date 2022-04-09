package tycho.core.client.controllers;

import tycho.core.misc.*;
import tycho.core.client.beans.UploadBean;
import tycho.core.client.controllers.DataExchange.IHasData;
import tycho.core.client.controllers.DataRecords.TMdbDataRecord;
import tycho.core.client.controllers.Table.EventBasedRowFactory;
import tycho.core.client.CustomFingerprintDB.DB;
import tycho.core.client.fingerprint.Fingerprint;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UploaderController implements IHasData<TMdbDataRecord>, IController {

    private TMdbDataRecord dataRecord;

    public UploaderController() {
        dataRecord = null;
    }

    @FXML
    private AnchorPane root;
    @FXML
    private TableView<UploadBean> fileTable;
    @FXML
    private TableColumn<UploadBean, String> fileNameCol;
    @FXML
    private Button tmdbIdBtn;
    @FXML
    private Button autoMatchBtn;
    @FXML
    private Button uploadBtn;
    @FXML
    private ImageView posterImgView;
    @FXML
    private Label seasonLbl;
    @FXML
    private Label episodeLbl;
    private PreLoaderAnimation preLoaderAnimation;

    @FXML
    public void initialize() {
        preLoaderAnimation = new PreLoaderAnimation(root);
        cssAndTableInit();
        buttonInit();
        dragAndDropInit();
    }

    private void buttonInit() {
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

        autoMatchBtn.setOnMouseClicked(e -> new Thread(() -> {
            AtomicBoolean showAlert = new AtomicBoolean(false);
            String headerText = "";

            if (fileTable.getItems().isEmpty()) {
                headerText = "No items found";
                showAlert.set(true);
            } else if (dataRecord == null || dataRecord.tmdbId() <= 0) {
                headerText = "Please search for a show first";
                showAlert.set(true);
            }

            String finalHeaderText = headerText;
            Platform.runLater(() -> {
                Alert alert = AlertUtils.createAlertWithStyling(Alert.AlertType.WARNING, "Fingerprint Uploader");
                alert.setHeaderText(finalHeaderText);
                //Check if we need to stop here and show the error message
                if (showAlert.get()) {
                    alert.show();
                }
            });
            if (showAlert.get()) return;

            try {
                boolean failed = false;

                for (UploadBean ub : fileTable.getItems()) {
                    int season = TitleScanner.getSeason(ub.getName());
                    int episode = TitleScanner.getEpisode(ub.getName());

                    if (season == 0 || episode == 0) failed = true;

                    ub.setSeason(season);
                    ub.setEpisode(episode);
                    ub.setTmdbID(dataRecord.tmdbId());
                }

                boolean finalFailed = failed;
                Platform.runLater(() -> {
                    Alert alert1 = AlertUtils.createAlertWithStyling(Alert.AlertType.INFORMATION, "Fingerprint Uploader");
                    alert1.setHeaderText(finalFailed
                            ? "One or more failed to match"
                            : "All done!");
                    alert1.show();
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    Alert alert2 = AlertUtils.createAlertWithStyling(Alert.AlertType.INFORMATION, "Fingerprint Uploader");
                    alert2.setHeaderText("Something went wrong");
                    alert2.show();
                });
                ex.printStackTrace();
            }


            UploadBean item = fileTable.getSelectionModel().getSelectedItem();
            if(item == null) return;
            Platform.runLater(() -> seasonLbl.setText("Season: " + item.getSeason()));
            Platform.runLater(() -> episodeLbl.setText("Episode: " + item.getEpisode()));
        }).start());

        uploadBtn.setOnMouseClicked(e -> new Thread(() -> {
            boolean showMsg = false;
            String headerText = "";

            if (fileTable.getItems().isEmpty()) {
                headerText = "No items found";
                showMsg = true;
            } else if (fileTable.getItems().get(0).getTmdbID() == -1) {
                headerText = "Items have not been matched yet";
                showMsg = true;
            }

            String finalHeaderText = headerText;
            boolean finalShowMsg = showMsg;
            Platform.runLater(() -> {
                Alert alert = AlertUtils.createAlertWithStyling(Alert.AlertType.WARNING, "Fingerprint Uploader");
                alert.setHeaderText(finalHeaderText);
                if (finalShowMsg) {
                    alert.show();
                }
            });

            if (showMsg) return;

            try {
                preLoaderAnimation.showLoader();

                AtomicInteger counter = new AtomicInteger();

                for (UploadBean ub : fileTable.getItems()) {
                    Fingerprint f = FingerprintUtils.createFingerprintFromWav(
                            FingerprintUtils.transcodeToAudio(ub.getFile()));

                    if (f == null) {
                        Platform.runLater(() -> {
                            Alert nullPointerEx = AlertUtils.createAlertWithStyling(Alert.AlertType.ERROR, "Fingerprint Uploader");
                            nullPointerEx.setHeaderText("Could not create fingerprint");
                            nullPointerEx.show();
                        });
                        return;
                    }
                    f.setTmdbId(ub.getTmdbID());
                    f.setSeason(ub.getSeason());
                    f.setEpisode(ub.getEpisode());


                    DB db = DB.getInstance();
                    db.addFingerprint(f);
                }

                preLoaderAnimation.hideLoader();

                AlertUtils.showAllDone("Fingerprint Uploader");
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    Alert error = AlertUtils.createAlertWithStyling(Alert.AlertType.ERROR, "Fingerprint Uploader");
                    error.setHeaderText("Something went wrong");
                    error.show();
                });
                ex.printStackTrace();
            } finally {
                preLoaderAnimation.hideLoader();
            }
        }).start());
    }

    private void cssAndTableInit() {
        fileNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        EventBasedRowFactory<UploadBean> factory = new EventBasedRowFactory<>();

        factory.setOnIndexChange((row, tableView) -> {
            UploadBean item = row.getItem();
            seasonLbl.setText("Season: " + item.getSeason());
            episodeLbl.setText("Episode: " + item.getEpisode());
        });

        fileTable.setRowFactory(factory);
    }

    /**
     * Enable drag and drop for the main table 'fileTable'
     */
    private void dragAndDropInit() {
        fileTable.setOnDragOver(dragEvent -> {
            if (dragEvent.getGestureSource() != fileTable
                    && dragEvent.getDragboard().hasFiles()) {
                dragEvent.acceptTransferModes(TransferMode.COPY);
            }
            dragEvent.consume();
        });
        fileTable.setOnDragDropped(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            ObservableList<UploadBean> files = fileTable.getItems();

            if (db.hasFiles()) {
                files.addAll(FXCollections.observableArrayList(db.getFiles().stream().map(UploadBean::new).filter(ogFile -> {
                    //Filter all files out of the list that are already in the table, this way we prevent duplicates
                    boolean found = false;
                    for (UploadBean ub : files) {
                        if (ub.equals(ogFile)) {
                            found = true;
                            break;
                        }
                    }
                    return !found;
                }).sorted(Comparator.comparing(UploadBean::getName)).collect(Collectors.toList())));
                fileTable.setItems(files);
            }
            dragEvent.setDropCompleted(true);

            dragEvent.consume();
        });

    }

    @Override
    public void setObjectData(TMdbDataRecord data) {
        dataRecord = data;
        try {
            posterImgView.setImage(ImageUtils.getImageFromFile(data.posterFile()));
        } catch (Exception e) {
            posterImgView.setImage(ImageUtils.getNoPosterFoundImage());
        }
    }
}
