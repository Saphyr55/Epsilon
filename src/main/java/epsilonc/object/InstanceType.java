package epsilonc.object;

import epsilonc.Token;
import epsilonc.core.InterpretRuntimeException;
import epsilonc.utils.PrettyPrintingMap;

import java.util.HashMap;
import java.util.Map;

public class InstanceType implements Instance {

    private Type type;
    private final Map<String, Let> properties;

    public InstanceType(Type type, Map<String, Object> properties) {
        this.type = type;
        this.properties = new HashMap<>();
        type.getProperties().forEach((s, let) -> this.properties.put(s, new Let(properties.get(s), let.isMutable())));
    }

    @Override
    public void set(Token name, Object value) {
        Let let = properties.get(name.text());

        if (let == null)
            throw new InterpretRuntimeException(name, "Undefined property '"+name.text()+"'.");

        if (!let.isMutable() && let.getValue() != null)
            throw new InterpretRuntimeException(name, "'"+name.text()+"' isn't mutable.");

        let.setValue(value);
    }

    @Override
    public Object get(Token name) {
        Let field = properties.get(name.text());
        if (field != null) return field.getValue();
        throw new InterpretRuntimeException(name, "Undefined property '"+name.text()+"'.");
    }

    public Map<String, Let> getProperties() {
        return properties;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return super.toString()+" {\n" +
                "\ttype=" + type.getName() +
                ",\n\tproperties={" + PrettyPrintingMap.pretty(properties) +
                "}\n}";
    }
}
