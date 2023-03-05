package epsilon;

import epsilon.core.AssignException;
import epsilon.core.DeclarationException;
import epsilon.core.InterpretRuntimeException;
import epsilon.object.Let;
import epsilon.object.Value;
import epsilon.syntax.Token;
import epsilon.type.NativeType;
import epsilon.type.Type;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private final Map<String, Let> declarations;
    private final Environment enclosing;

    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
        this.declarations = new HashMap<>();
    }

    public Environment() {
        this(null);
    }

    public Type getType(Token name) {
        return name == null ? NativeType.Undefined : getValue(name).getType();
    }

    public Value getValue(Token name) {

        if (declarations.containsKey(name.text())) {
            return declarations.get(name.text()).getValue();
        }

        if (enclosing != null) return enclosing.getValue(name);

        throw new DeclarationException(name, "Undefined declaration '" + name.text() + "'.");
    }

    public void define(String name, Value value, boolean isMutable) {
        declarations.put(name, new Let(value, isMutable));
    }

    public void define(String name, Value value) {
        declarations.put(name, new Let(value, false));
    }

    public void assignAt(Integer distance, Token name, Value value) {
        if (ancestor(distance).declarations.containsKey(name.text())) {
            Let let = ancestor(distance).declarations.get(name.text());
            if (let.isMutable()) {
                let.setValue(value);
                return;
            }
            throw new InterpretRuntimeException(name, "Cannot change the value of " + name.text() + ", please write 'let mut " + name.text() + ";'");
        }
    }

    public void assign(Token name, Value value) {

        if (declarations.containsKey(name.text())) {
            Let let = declarations.get(name.text());
            if (let.isMutable()) {
                let.setValue(value);
                return;
            }
            throw new InterpretRuntimeException(name,
                    "Cannot change the value of " + name.text() +
                            ", please write 'let mut " + name.text() + ";'");
        }

        if (enclosing != null) {
            Let let = enclosing.declarations.get(name.text());
            if (let.isMutable()) {
                let.setValue(value);
                return;
            }
        }

        throw new AssignException(name, "Undefined variable '" + name.text() + "' on assign.");
    };

    public Value getAt(Integer distance, String text) {
        var declaration = ancestor(distance).declarations.get(text);
        if (declaration == null) throw new NullPointerException();
        return declaration.getValue();
    }

    Environment ancestor(int distance) {
        Environment environment = this;
        for (int i = 0; i < distance; i++) {
            environment = environment.enclosing;
        }
        return environment;
    }

    public Map<String, Let> getDeclarations() {
        return declarations;
    }

    public Environment getEnclosing() {
        return enclosing;
    }
}
