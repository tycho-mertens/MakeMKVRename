package tycho.core.client.controllers.Table;

import tycho.core.client.beans.RenameToBean;
import tycho.core.misc.ColorUtils;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import tycho.core.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChainedTableHandler<T, S> {

    private static final Logger logger = Logger.getLogger(ChainedTableHandler.class.getName());

    private final TableView<T> tableOne;
    private final TableView<S> tableTwo;

    private TableView<?> selectedTable;

    private final ChainedRowFactory<T> tableOneRowFactory;
    private final ChainedRowFactory<S> tableTwoRowFactory;

    private final String highlightColor;
    private final String lowSimilarityColor;

    private final float lowSimilarityThreshold;
    private final float darkenPercentageOnSelection;

    /**
     * Default constructor
     *
     * @param tableOne The first table
     * @param tableTwo The second table
     * @param highlightColor The background color for the selected row
     * @param lowSimilarityColor What color are the rows with a low similarity score
     * @param darkenPercentage How much should we darken a background of a row that is already colored
     */
    public ChainedTableHandler(TableView<T> tableOne, TableView<S> tableTwo, String highlightColor,
                               String lowSimilarityColor, float darkenPercentage) {
        //Set the tables
        this.tableOne = tableOne;
        this.tableTwo = tableTwo;

        //Retrieve and set the ChainedRowFactories from the tables
        this.tableOneRowFactory = (ChainedRowFactory<T>) tableOne.getRowFactory();
        this.tableTwoRowFactory = (ChainedRowFactory<S>) tableTwo.getRowFactory();

        Config c = Config.getInstance();

        //Set the default colors
        this.highlightColor = highlightColor == null
                ? c.asString(Config.HIGHLIGHTED_ROW_COLOR)
                : highlightColor;
        this.lowSimilarityColor = lowSimilarityColor == null
                ? c.asString(Config.LOW_SIMILARITY_COLOR)
                : lowSimilarityColor;
        darkenPercentageOnSelection = darkenPercentage <= 0
                ? c.asFloat(Config.DARKEN_SELECTED_ROW_PERCENTAGE)
                : darkenPercentage;

        //Start with table one as the selected table
        selectedTable = tableOne;
        lowSimilarityThreshold = c.asFloat(Config.LOW_SIMILARITY_THRESHOLD);
    }

    /**
     * Retrieves the selected indices of both tables and returns the correct index
     *
     * @return Returns the index of the currently selected row
     */
    public int getSelectedIndex(){
        //Get the indices from both the tables, one of them will be -1, the other will be a positive number
        int firstIndex = tableOne.getSelectionModel().getSelectedIndex();
        int secondIndex = tableTwo.getSelectionModel().getSelectedIndex();

        return Math.max(firstIndex, secondIndex);//With Math.max, we can select the positive number and return that
        //return firstIndex == -1 ? secondIndex : firstIndex;
    }

    /**
     * Get the index from the selected row and get both rows from the tables according to the index and remove them
     *
     */
    public void removeSelectedFullRow(){
        //We need a try catch here to prevent errors. Sometimes we click on an empty row, this would cause errors.
        try {
            int index = getSelectedIndex();
            removeTableRow(tableOne, index);
            removeTableRow(tableTwo, index);

            //Update the colored rows
            clearAllCssRowStyles();
            colorAllLowSimilarityRows();
        }catch(Exception e){
            logger.warning("Failed to remove on of the two rows");
        }finally {
            //We failed at table one, so we now try table two
            try{
                removeTableRow(tableTwo, getSelectedIndex());
            }catch(Exception ignored){}
        }
    }

    //TODO maybe create updateColoredRows method?

    /**
     * Removes a row for the given table
     *
     * @param table The table you want to remove a row from
     * @param index The index of the row
     * @param <E> The type of the table/row
     */
    private <E> void removeTableRow(TableView<E> table, int index){
        List<E> list = new ArrayList<>(table.getItems());
        list.remove(index);
        table.setItems(FXCollections.observableList(list));
    }

    /**
     * Sets the selected table
     *
     * @param table The table we want to be the selected one
     */
    public void setSelectedTable(TableView<?> table){
        selectedTable = table;
    }

    /**
     * Default getter
     *
     * @return Returns the code for highlighted rows
     */
    public String getHighlightColor() {
        return highlightColor;
    }

    /**
     * Default getter
     *
     * @return Returns the code for low similarity rows
     */
    public String getLowSimilarityColor() {
        return lowSimilarityColor;
    }

    /**
     * Default getter
     *
     * @return Returns the percentage we have to darken colored rows with when selected
     */
    public float getDarkenPercentageOnSelection() {
        return darkenPercentageOnSelection;
    }

    /**
     * Checks whether if the row has a background color or not
     *
     * @param index The index of the row
     * @return Returns true if the row has a background color, returns false if the row has no background color
     */
    private boolean isRowColored(int index){
        return !tableOneRowFactory.getRows().get(index).getStyle().isEmpty();
    }

    /**
     * Select a full row, aka select the row on both tables (color them)
     *
     * @param index The index of the row to be selected
     */
    public void selectFullRow(int index){
        try {
            getOtherTable(selectedTable).getSelectionModel().clearSelection();//Clear the selection in the correct table
            if (isRowColored(index)) {
                ColorUtils.RGB first = ColorUtils.hexToRgb(getLowSimilarityColor());//Create a rgb object from the low similarity color

                //Lower the brightness in the 'first' object and color the row
                //Note: you can't actually lower the brightness on a simple rgb, but I couldn't come up with a better name
                colorFullRow(index, ColorUtils.rgbToHex(ColorUtils.lowerBrightnessOfColor(first,
                        getDarkenPercentageOnSelection()), true));
            } else {
                colorFullRow(index, highlightColor);
            }
        }catch(Exception ignored){}
    }

    /**
     * Set the background color for a full row, aka for the rows on both tables
     *
     * @param index The index of the row
     * @param hexColor The color the background has to be for the rows
     */
    public void colorFullRow(int index, String hexColor) {
        String css = "-fx-background-color: " + hexColor + ";";
        try {
            tableOneRowFactory.getRows().get(index).setStyle(css);
            tableTwoRowFactory.getRows().get(index).setStyle(css);
        }catch(Exception ignored){
            logger.info("Failed to color one of the two rows of the tables");
        }
    }

    /**
     * Removes all styling from all the rows on both tables
     *
     */
    public void clearAllCssRowStyles() {
        tableOneRowFactory.getRows().forEach(row -> row.setStyle(""));
        tableTwoRowFactory.getRows().forEach(row -> row.setStyle(""));
    }

    /**
     * Sets the background color for all the full rows that have a low similarity score
     *
     */
    public void colorAllLowSimilarityRows() {
        clearAllCssRowStyles();
        List<S> rows = tableTwo.getItems();//Need to use the TableView instance of the table and not the rowFactory. The rowFactory will update the list too late, there is a delay, this delay causes problems, that's why I'm using the tableview here
        for (int i = 0; i < rows.size(); i++) {
            if (!(rows.get(i) instanceof RenameToBean item)) return;//Just stop if the items are not an instanceof the RenameToBean class, otherwise create a new object called item
            if (item.getSimilarity() <= lowSimilarityThreshold) colorFullRow(i, lowSimilarityColor);//Color the row if the similarity is too low
        }
    }

    /**
     * Retrieves the other table
     *
     * @param currentTable The current table
     * @return Returns the other table
     */
    private TableView<?> getOtherTable(TableView<?> currentTable){
        return currentTable.equals(tableOne)
                ? tableTwo
                : tableOne;
    }

    /**
     * Retrieves the other rowFactory
     *
     * @param currentInstance The current rowFactory instance
     * @return Returns the other rowFactory
     */
    public ChainedRowFactory<?> getOtherFactory(ChainedRowFactory<?> currentInstance) {
        return currentInstance.equals(tableOneRowFactory)
                ? tableTwoRowFactory
                : tableOneRowFactory;
    }
}
