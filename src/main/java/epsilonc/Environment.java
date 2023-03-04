package epsilonc;

import epsilonc.core.AssignException;
import epsilonc.core.DeclarationException;
import epsilonc.core.InterpretRuntimeException;
import epsilonc.object.EpsilonClass;
import epsilonc.object.Let;
import epsilonc.syntax.Token;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private final Map<String, Let> values;
    private final Environment enclosing;

    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
        values = new HashMap<>();
    }

    public Environment() {
        this(null);
    }

    public Object getValue(Token name) {

        if (values.containsKey(name.text()))
            return values.get(name.text()).getValue();

        if (enclosing != null) return enclosing.getValue(name);

        throw new DeclarationException(name, "Undefined variable '" + name.text() + "'.");
    }

    public void define(String name, String type, Object value, boolean isMutable) {
        values.put(name, new Let(value, type, isMutable));
    }

    public void define(String name, String type, Object value) {
        values.put(name, new Let(value, type, false));
    }

    public void assignAt(Integer distance, Token name, Object value) {
        if (ancestor(distance).values.containsKey(name.text())) {
            Let let = ancestor(distance).values.get(name.text());
            if (let.isMutable()) {
                let.setValue(value);
                return;
            }
            throw new InterpretRuntimeException(name, "Cannot change the value of " + name.text() + ", please write 'let mut " + name.text() + ";'");
        }
    }

    public void assign(Token name, Object value) {

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

        throw new AssignException(name, "Undefined variable '" + name.text() + "'.");
    };

    public Object getAt(Integer distance, String text) {
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
