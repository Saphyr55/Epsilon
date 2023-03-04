package epsilonc.object;

import epsilonc.syntax.Token;
import epsilonc.core.InterpretRuntimeException;
import epsilonc.utils.PrettyPrintingMap;

import java.util.HashMap;
import java.util.Map;

public class InstanceStruct implements Instance {

    private Struct struct;
    private final Map<String, Let> properties;

    public InstanceStruct(Struct struct, Map<String, Value> properties) {
        this.struct = struct;
        this.properties = new HashMap<>();
        struct.getProperties().forEach((s, let) -> this.properties.put(s,
                        new Let(Value.of(struct, properties.get(s)), let.isMutable())));
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
        Let field = properties.get(name.text());
        if (field != null) return field.getValue();
        throw new InterpretRuntimeException(name, "Undefined property '"+name.text()+"'.");
    }

    public Map<String, Let> getProperties() {
        return properties;
    }

    public Struct getStruct() {
        return struct;
    }

    public void getStruct(Struct struct) {
        this.struct = struct;
    }

    @Override
    public String toString() {
        return super.toString()+" {\n" +
                "\ttype=" + struct.getName() +
                ",\n\tproperties={" + PrettyPrintingMap.pretty(properties) +
                "}\n}";
    }
}
