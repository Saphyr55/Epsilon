package epsilonc.object;

import java.util.Map;

public class Type {

    private final String name;
    private final Map<String, Let> properties;

    public Type(String name, Map<String, Let> properties) {
        this.name = name;
        this.properties = properties;
    }

}
