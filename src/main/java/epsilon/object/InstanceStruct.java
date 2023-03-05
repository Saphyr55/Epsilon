package epsilon.object;

import epsilon.syntax.Token;
import epsilon.core.InterpretRuntimeException;
import epsilon.utils.PrettyPrintingMap;

import java.util.HashMap;
import java.util.Map;

public class InstanceStruct implements Instance {

    private final Struct struct;
    private final Map<String, Let> properties;

    public InstanceStruct(Struct struct, Map<String, Value> properties) {
        this.struct = struct;
        this.properties = new HashMap<>();
        struct.getProperties().forEach((s, let) -> this.properties.put(s,
                new Let(properties.get(s), let.isMutable())));
    }

    @Override
    public void set(Token name, Value value) {
        Let let = properties.get(name.text());

        if (let == null)
            throw new InterpretRuntimeException(name, "Undefined property '"+name.text()+"'.");

        if (!let.isMutable() && let.getValue() != null)
            throw new InterpretRuntimeException(name, "'"+name.text()+"' isn't mutable.");

        let.setValue(value);
    }

    @Override
    public Value get(Token name) {
        Let property = properties.get(name.text());
        if (property != null) return property.getValue();
        throw new InterpretRuntimeException(name, "Undefined property '"+name.text()+"'.");
    }

    public Map<String, Let> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return super.toString()+" {" +
                "type=" + struct.getName() +
                ",properties={" + PrettyPrintingMap.pretty(properties) +
                "}}";
    }
}
