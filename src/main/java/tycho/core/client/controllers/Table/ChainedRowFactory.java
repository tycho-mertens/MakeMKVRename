package tycho.core.client.controllers.Table;

import javafx.scene.control.IndexedCell;
import javafx.scene.control.TableRow;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChainedRowFactory<T> extends EventBasedRowFactory<T>{

    private final Set<TableRow<T>> rows;
    private ChainedTableHandler<?, ?> handler;

    public ChainedRowFactory(){
        super();
        rows = new HashSet<>();
        handler = null;

        setOnIndexChange((row, table) -> {
            handler.setSelectedTable(table);//Setting the current table as the selected table. We need to know this, so we can clear the selection in the handler without causing errors (without this, we will get a lot of errors)

            handler.colorAllLowSimilarityRows();
            handler.selectFullRow(row.getIndex());//Color the selected row (the row we just clicked on)
        });

        setOnRowCreation((row, table) -> rows.add(row));
    }

    /**
     * Sets the handler, so we can access and use the handler from this class
     *
     * @param handler The main table handler
     */
    public void setHandler(ChainedTableHandler<?, ?> handler){
        this.handler = handler;
    }

    /**
     * Gets the first row of the row list
     *
     * @return Returns the first object in the rows list
     */
    private TableRow<T> firstRow(){
        return getRows().get(0);
    }

    /**
     * Retrieves the rows set and converts it into a sorted List<TableRow<T>>
     *
     * @return Returns the rows set as a list
     */
    public List<TableRow<T>> getRows(){
        List<TableRow<T>> ret = rows.stream().sorted(Comparator.comparingInt(IndexedCell::getIndex)).toList();
        return ret.size() > 1 ? ret.subList(1, ret.size()) : ret;//Preventing out an 'index of bounds' error with this 'if else' statement
    }

    /**
     * Sets the background color of the given row
     *
     * @param row The row you want to change the background from
     * @param colorCode The color you want the background to be
     */
    public void setRowColorFromCode(TableRow<T> row, String colorCode) {
        row.setStyle("-fx-background-color: " + colorCode + ";");
    }
}
