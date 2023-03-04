package epsilonc.object;

import epsilonc.syntax.Token;
import epsilonc.core.InterpretRuntimeException;

import java.util.HashMap;
import java.util.Map;

public class InstanceClass implements Instance {

    private final EpsilonClass eClass;
    private final Map<String, Let> properties;
    private final Map<String, FuncCallable> methods;

    public InstanceClass(EpsilonClass eClass) {
        this.eClass = eClass;
        this.properties = new HashMap<>();
        eClass.getFields().forEach((s, let) -> properties.put(s, new Let(let.getValue(), let.getType(), let.isMutable())));
        this.methods = new HashMap<>(eClass.getMethods());
    }

    public EpsilonClass getEClass() {
        return eClass;
    }

    public void set(Token name, Object value) {

        Let let = properties.get(name.text());
        if (let == null)
            throw new InterpretRuntimeException(name, "Undefined property '"+name.text()+"'.");

        if (!let.isMutable() && let.getValue() != null)
            throw new InterpretRuntimeException(name, "'"+name.text()+"' isn't mutable.");

        let.setValue(value);
    }

    public Object get(Token name) {

        Let field = properties.get(name.text());
        if (field != null) return field.getValue();

        FuncCallable method = methods.get(name.text());
        if (method != null) return method;

        throw new InterpretRuntimeException(name, "Undefined property '"+name.text()+"'.");
    }
}
