package epsilon.object;

import epsilon.syntax.Token;
import epsilon.core.InterpretRuntimeException;

import java.util.HashMap;
import java.util.Map;

public class InstanceClass implements Instance {

    private final EClass eClass;
    private final Map<String, Let> attributes;
    private final Map<String, FuncCallable> methods;

    public InstanceClass(EClass eClass) {
        this.eClass = eClass;
        this.attributes = new HashMap<>();
        eClass.getFields().forEach((s, let) -> attributes.put(s, new Let(let.getValue(), let.isMutable())));
        this.methods = new HashMap<>(eClass.getMethods());
    }

    public void set(Token name, Value value) {

        Let let = attributes.get(name.text());
        if (let == null)
            throw new InterpretRuntimeException(name, "Undefined attribute '"+name.text()+"'.");

        if (!let.isMutable() && let.getValue() != null)
            throw new InterpretRuntimeException(name, "'"+name.text()+"' isn't mutable.");

        let.setValue(value);
    }

    public Value get(Token name) {

        Let field = attributes.get(name.text());
        if (field != null) return field.getValue();

        FuncCallable method = eClass.findMethod(name.text());
        if (method != null) return Value.ofFunc(method.bind(Value.of(eClass, this)));

        throw new InterpretRuntimeException(name, "Undefined attribute '"+name.text()+"'.");
    }

    public Value getMethod(String name) {
        FuncCallable method = eClass.findMethod(name);
        if (method != null) return Value.ofFunc(method.bind(Value.of(eClass, this)));
        return Value.ofVoid();
    }

    public Map<String, FuncCallable> getMethods() {
        return methods;
    }

    public Map<String, Let> getAttributes() {
        return attributes;
    }
}
