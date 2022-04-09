package tycho.core.config;

import java.util.HashMap;

public class Configuration {

    private HashMap<String, Object> properties;

    public Configuration() {
        this.properties = new HashMap<>();
    }

    /**
     * Default getter
     *
     * @return Returns the list of all the properties
     */
    public HashMap<String, Object> getProperties() {
        return properties;
    }

    /**
     * Default setter
     *
     * @param properties The list of all the properties
     */
    public void setProperties(HashMap<String, Object> properties){
        this.properties = properties;
    }


}
