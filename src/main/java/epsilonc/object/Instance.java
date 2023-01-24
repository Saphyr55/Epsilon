package epsilonc.object;

import epsilonc.Token;
import epsilonc.core.InterpretRuntimeException;

public class Instance {

    private final EpsilonClass eClass;

    public Instance(EpsilonClass eClass) {
        this.eClass = eClass;
    }

    public EpsilonClass getEClass() {
        return eClass;
    }

    public void set(Token name, Object value) {

        Let let = eClass.findFields(name.text());
        if (let == null)
            throw new InterpretRuntimeException(name, "Undefined property '"+name.text()+"'.");

        if (!let.isMutable() && let.getValue() != null)
            throw new InterpretRuntimeException(name, "'"+name.text()+"' isn't mutable.");

        let.setValue(value);
    }

    public Object get(Token name) {

        Let field = eClass.findFields(name.text());
        if (field != null) return field.getValue();

        Func method = eClass.findMethod(name.text());
        if (method != null) return method;

        throw new InterpretRuntimeException(name, "Undefined property '"+name.text()+"'.");
    }
}
