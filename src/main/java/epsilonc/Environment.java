package epsilonc;

import epsilonc.core.AssignException;
import epsilonc.core.DeclarationException;

import javax.management.openmbean.OpenMBeanAttributeInfo;
import java.util.HashMap;
import java.util.Map;

public class Environment {

    private final Map<String, Object> values;
    private Environment enclosing;

    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
        values = new HashMap<>();
    }

    public Environment() {
        this(null);
    }

    public Object get(Token name) {
        if (values.containsKey(name.text())) {
            return values.get(name.text());
        }

        if (enclosing != null) return enclosing.get(name);

        throw new DeclarationException(name, "Undefined variable '" + name.text() + "'.");
    }

    public void define(String name, Object value) {
        values.put(name, value);
    }

    public void assign(Token name, Object value) {

        if (values.containsKey(name.text())) {
            define(name.text(), value);
            return;
        }

        if (enclosing != null) {
            enclosing.values.put(name.text(), value);
            return;
        }

        throw new AssignException(name, "Undefined variable '" + name.text() + "'.");
    };

}
