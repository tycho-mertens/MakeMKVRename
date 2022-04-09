package tycho.core.client.controllers.DataExchange;
public interface IHasData<T>{
    /**
     * This method/interface allows two scenes to communicate with each other, and pass data to each other
     *
     * @param data The data to pass through to another scene
     */
    void setObjectData(T data);
}
