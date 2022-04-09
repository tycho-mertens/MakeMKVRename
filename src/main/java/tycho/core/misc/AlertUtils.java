package tycho.core.misc;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import tycho.core.config.Config;

public class AlertUtils {

    /**
     * Creates a new alert object with styling and returns it
     *
     * @param type The type of alert box
     * @param subTitle The subTitle for the alert box
     * @return Returns an alert box with a styling
     */
    public static Alert createAlertWithStyling(Alert.AlertType type, String subTitle){
        Alert alert = new Alert(type);
        (alert.getDialogPane()).getStylesheets().add(Config.getInstance().asString(Config.CSS_STYLE));
        alert.setTitle(Config.getInstance().getProgramName() + " - " + subTitle);
        return alert;
    }

    /**
     * Creates a generic alert box that simply show "All Done!"
     *
     * @param subTitle The subTitle for the alert box
     */
    public static void showAllDone(String subTitle){
        showMsg("All Done!", subTitle);
    }

    public static void showMsg(String msg, String subTitle){
        Platform.runLater(() -> {
            Alert alert = createAlertWithStyling(Alert.AlertType.INFORMATION, subTitle);
            alert.setHeaderText(msg);
            alert.show();
        });
    }
}
