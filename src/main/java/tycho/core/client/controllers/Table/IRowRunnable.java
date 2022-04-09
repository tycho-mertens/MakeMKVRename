package tycho.core.client.controllers.Table;

import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

public interface IRowRunnable<T> {
    void run(TableRow<T> row, TableView<T> tableView);
}
