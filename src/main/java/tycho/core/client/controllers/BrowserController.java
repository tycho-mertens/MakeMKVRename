package tycho.core.client.controllers;

import tycho.core.client.controllers.DataExchange.IHasData;
import tycho.core.client.controllers.DataRecords.TMdbDataRecord;
import tycho.core.client.controllers.Table.TreeViewHandler;
import tycho.core.misc.ImageUtils;
import tycho.core.misc.PreLoaderAnimation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.logging.Logger;

public class BrowserController implements IHasData<TMdbDataRecord>, IController {

    private static final Logger logger = Logger.getLogger(BrowserController.class.getName());

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView posterImgView;
    @FXML
    private Button tmdbIdBtn;
    @FXML
    private Button searchBtn;
    @FXML
    private TreeView<String> treeView;

    private TMdbDataRecord dataRecord;
    private PreLoaderAnimation preLoaderAnimation;

    public BrowserController(){
        dataRecord = null;
    }

    @Override
    public void initialize() {
        preLoaderAnimation = new PreLoaderAnimation(root);
        tableInit();
        buttonInit();
    }

    private void tableInit() {
        treeView.setEditable(false);
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
        tmdbIdBtn.setOnMouseClicked(e -> TMdbFinderController.startNewStage(this));

        searchBtn.setOnMouseClicked(e -> new Thread(() -> {
            preLoaderAnimation.showLoader();
            new TreeViewHandler(treeView, dataRecord.tmdbId());
            preLoaderAnimation.hideLoader();
        }).start());
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
