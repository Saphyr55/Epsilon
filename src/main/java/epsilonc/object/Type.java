package epsilonc.object;

import epsilonc.utils.PrettyPrintingMap;

import java.util.Map;

public class Type {

    private final String name;
    private final Map<String, Let> properties;

    public Type(String name, Map<String, Let> properties) {
        this.name = name;
        this.properties = properties;
    }

    public Map<String, Let> getProperties() {
        return properties;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return super.toString()+"{\n" +
                "\tname='" + name + "'" +
                ",\n\tproperties=" + PrettyPrintingMap.pretty(properties) +
                "\t}";
    }
}
