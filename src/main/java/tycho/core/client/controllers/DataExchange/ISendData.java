package tycho.core.client.controllers.DataExchange;

public interface ISendData<T> {
    /**
     * Sets the 'Receiver' controller in the 'Sender' controller, this allows us to communicate between scenes
     *
     * @param controller The fxml controller class, like MainController, TMdbFinderController, etc. Keep in mind that
     *                   the controller needs to have IHasData<T> implemented
     */
    void setController(IHasData<T> controller);
}
