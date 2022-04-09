package tycho.core.client.controllers.Table;

import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class EventBasedRowFactory<T> implements
        Callback<TableView<T>, TableRow<T>> {

    private IRowRunnable<T> execOnIndexChange, execOnRowCreation;

    public EventBasedRowFactory() {
        execOnIndexChange = (row, table) -> {};
        execOnRowCreation = (row, table) -> {};
    }

    /**
     * The runnable will be executed when the index of the table changes
     *
     * @param runnable The runnable which we will execute
     */
    public void setOnIndexChange(IRowRunnable<T> runnable){
        execOnIndexChange = runnable;
    }

    /**
     * The runnable will be executed when a row is created
     * NOTE: rows are created when a table is initialized, not when you set items for the table, etc...
     *
     * @param runnable The runnable which we will execute
     */
    public void setOnRowCreation(IRowRunnable<T> runnable){
        execOnRowCreation = runnable;
    }

    @Override
    public TableRow<T> call(TableView<T> tTableView) {
        TableRow<T> row = new TableRow<>();

        row.indexProperty().addListener((observableValue, number, t1) -> execOnRowCreation.run(row, tTableView));

        row.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if(!t1 && aBoolean) return;//Not sure what 't1' and 'aBoolean' is or does, but this way we prevent this listener from executing multiple times in a row
            execOnIndexChange.run(row, tTableView);
        });
        return row;
    }


}
