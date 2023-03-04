package epsilonc;

import epsilonc.core.AssignException;
import epsilonc.core.DeclarationException;
import epsilonc.core.InterpretRuntimeException;
import epsilonc.object.Let;
import epsilonc.object.Value;
import epsilonc.syntax.Token;
import epsilonc.type.Type;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private final Map<String, Type> types;
    private final Map<String, Let> values;
    private final Environment enclosing;

    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
        this.values = new HashMap<>();
        this.types = new HashMap<>();
    }

    public Environment() {
        this(null);
    }

    public Type getType(Token name) {
        return getValue(name).getType();
    }

    public Value getValue(Token name) {

        if (values.containsKey(name.text()))
            return values.get(name.text()).getValue();

        if (enclosing != null) return enclosing.getValue(name);

        throw new DeclarationException(name, "Undefined variable '" + name.text() + "' on get value.");
    }

    public void define(String name, Value value, boolean isMutable) {
        values.put(name, new Let(value, isMutable));
    }

    public void define(String name, Type type) {
        types.put(name, type);
    }

    public void define(String name, Value value) {
        values.put(name, new Let(value, false));
    }

    public void assignAt(Integer distance, Token name, Value value) {
        if (ancestor(distance).values.containsKey(name.text())) {
            Let let = ancestor(distance).values.get(name.text());
            if (let.isMutable()) {
                let.setValue(value);
                return;
            }
            throw new InterpretRuntimeException(name, "Cannot change the value of " + name.text() + ", please write 'let mut " + name.text() + ";'");
        }
    }

    public void assign(Token name, Value value) {

        if (values.containsKey(name.text())) {
            Let let = values.get(name.text());
            if (let.isMutable()) {
                let.setValue(value);
                return;
            }
            throw new InterpretRuntimeException(name,
                    "Cannot change the value of " + name.text() +
                            ", please write 'let mut " + name.text() + ";'");
        }

        if (enclosing != null) {
            Let let = enclosing.values.get(name.text());
            if (let.isMutable()) {
                let.setValue(value);
                return;
            }
        }


        throw new AssignException(name, "Undefined variable '" + name.text() + "' on assign.");
    };

    public Value getAt(Integer distance, String text) {
        return ancestor(distance).values.get(text).getValue();
    }

    Environment ancestor(int distance) {
        Environment environment = this;
        for (int i = 0; i < distance; i++) {
            environment = environment.enclosing;
        }
        return environment;
    }

}
