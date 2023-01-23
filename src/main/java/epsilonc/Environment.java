package epsilonc;

import epsilonc.core.DeclarationException;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private final Map<String, Object> values;

    public Environment() {
        values = new HashMap<>();
    }

    public Object get(Token name) {
        if (values.containsKey(name.text())) {
            return values.get(name.text());
        }

        throw new DeclarationException(name, "Undefined variable '" + name.text() + "'.");
    }

    public void define(String name, Object value) {
        values.put(name, value);
    }

}
