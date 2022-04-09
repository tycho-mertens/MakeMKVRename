package tycho.core.client.controllers;

import tycho.core.misc.*;
import tycho.core.client.beans.TmdbBean;
import tycho.core.client.controllers.DataExchange.IHasData;
import tycho.core.client.controllers.DataExchange.ISendData;
import tycho.core.client.controllers.DataRecords.TMdbDataRecord;
import tycho.core.client.controllers.Table.EventBasedRowFactory;
import tycho.core.tmdb.TMdb;
import tycho.core.tmdb.model.TvSearch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.logging.Logger;

public class TMdbFinderController implements IController, ISendData<TMdbDataRecord> {

    private static final Logger logger = Logger.getLogger(TMdbFinderController.class.getName());

    private IHasData<TMdbDataRecord> controller;

    private Stage stage;

    @FXML
    private TextField searchField;
    @FXML
    private Button searchBtn;
    @FXML
    private TableView<TmdbBean> resultTable;
    @FXML
    private TableColumn<TmdbBean, String> titleCol;
    @FXML
    private ImageView posterImg;
    @FXML
    private AnchorPane root;

    private PreLoaderAnimation preLoaderAnimation;

    /**
     * Default constructor
     */
    public TMdbFinderController() {
        controller = null;
        stage = null;
    }

    /**
     * Default initializer for FXML controllers
     */
    @FXML
    public void initialize() {
        //Make sure the cursor is put inside the searchField, quality of life stuff
        Platform.runLater(() -> searchField.requestFocus());

        //Init everything
        initSearchBtn();
        initTable();

        //Exit on esc key


        preLoaderAnimation = new PreLoaderAnimation(root);
    }

    /**
     * Set up the table, and do all the necessary things like cellValueFactory, etc...
     */
    private void initTable() {

        //Set the cell value factory for titleCol
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        //When we click twice on a row we will close the stage/scene and pass data through to the main scene, so it can use it.
        resultTable.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            TmdbBean bean = resultTable.getSelectionModel().getSelectedItem();
            if(bean == null) return; //Make sure it's not null (it's null when clicked on the header)
            if(e.getClickCount() == 2) {
                controller.setObjectData(new TMdbDataRecord(//Pass the data through to the main stage/scene
                        bean.getTotalSeasonsAsList(), bean.getTmdbId(), bean.getImage(), bean.getTitle(), bean.getYear()));

                //Cleanup -> delete all temp posters
                FileManager.getInstance().deleteTempDirectory(new ImageConverter().getTempDir());
                stage.close();//Close this stage/scene
            }
        });

        EventBasedRowFactory<TmdbBean> x = new EventBasedRowFactory<>();
        x.setOnIndexChange((row, table) -> {
            TmdbBean bean = resultTable.getSelectionModel().getSelectedItem();
            posterImg.setImage(ImageUtils.getImageFromFile(bean.getImage()));
        });
        resultTable.setRowFactory(x);

    }

    /**
     * Search for give tvShow when button is pressed or when the key 'ENTER' has been pressed
     */
    private void initSearchBtn() {
        searchBtn.setOnMouseClicked(e -> search());//Search when we click the button

        searchField.setOnKeyPressed(e -> {//Search when we press enter while in the searchField
            if(e.getCode() == KeyCode.ENTER) search();//Make sure the key we pressed is actually the ENTER key
        });
    }

    /**
     * Searches for the tvShow
     */
    private void search(){
        //TODO multithreading for converting images
        new Thread(() -> {//Create a new thread (needed for some animation stuff, otherwise it won't do anything and freezes)
            preLoaderAnimation.showLoader();//Show the loader
            TMdb tmdb = TMdb.getInstance();
            List<TvSearch.Result> results = tmdb.searchTv(searchField.getText()).getResults();//Search for the tvShow
            if (results.isEmpty()) {
                logger.info("No results found...");
                AlertUtils.showMsg("No results found", "Find TV Show");
                return;
            }
            //Convert the results to a list of TmdbBeans
            List<TmdbBean> searchResults = results.stream()
                    .map(x -> tmdb.getTvShow(x.getId()))
                    .map(TmdbBean::new).toList();

            //Set the items of the table to the converted TmdbBeans list
            resultTable.setItems(FXCollections.observableList(searchResults));
            preLoaderAnimation.hideLoader();//Hide the loader
        }).start();
    }

    /**
     * Creates a new scene, based on this class
     *
     * @param controller The controller where we need to pass the data through
     */
    public static void startNewStage(IHasData<TMdbDataRecord> controller) {
        //Create the SceneBuilder object with the given properties
        SceneBuilder builder = new SceneBuilder("tmdbFinder.fxml")
                .setSubTitle("Find Tv Show")
                .setAlwaysOnTop()
                .setCloseOnKeyPressed(KeyCode.ESCAPE);
        //Get the controller from the fxmlLoader
        TMdbFinderController fxmlController = builder.getLoader().getController();
        fxmlController.setController(controller);//Set the controller where we pass the data through
        Stage stage = builder.build();//Build the stage
        fxmlController.setStage(stage);//Set the stage in the TMdbFinderController, this way we can close it when we need to (properly)k
        stage.show();
    }


    /**
     * Sets the stage we use this, so we can close the scene/stage when we double-click on a row in the table
     *
     * @param stage The stage object from 'startNewStage()'
     */
    public void setStage(Stage stage){
        this.stage = stage;
    }

    /**
     * Sets the controller, we need this to pass data through to another scene/stage
     *
     * @param controller The fxml controller class, like MainController, TMdbFinderController, etc. Keep in mind that
     */
    @Override
    public void setController(IHasData<TMdbDataRecord> controller) {
        this.controller = controller;
    }
}