package epsilon.object;

import epsilon.syntax.Token;
import epsilon.core.InterpretRuntimeException;
import epsilon.type.NativeType;

import java.util.*;

public class InstanceClass implements Instance {

    private final EClass eClass;
    private final Map<String, Let> attributes;
    private final Map<Prototype, FuncCallable> methods;

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

        var methods = eClass.findMethods(name.text());
        List<Value> values = new LinkedList<>();
        if (!methods.isEmpty()) {
            for (var entry : methods.entrySet()) {
                values.add(Value.ofFunc(entry.getValue().bind(Value.of(eClass, this))));
            }
            return Value.of(NativeType.Void, values);
        }

        throw new InterpretRuntimeException(name, "Undefined attribute '"+name.text()+"'.");
    }

    public Value getMethod(Token name, List<Token> types) {
        throw new InterpretRuntimeException(name, "Undefined function '"+name.text()+"'.");
    }

    public Map<Prototype, FuncCallable> getMethods() {
        return methods;
    }

    public Map<String, Let> getAttributes() {
        return attributes;
    }
}
