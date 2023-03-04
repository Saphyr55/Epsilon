package epsilonc.object;

import epsilonc.resolver.Interpreter;
import epsilonc.type.Type;

import java.util.List;
import java.util.Map;

public class EClass implements Callable, Type {

    private final String name;
    private final Map<String, FuncCallable> methods;
    private final Map<String, FuncCallable> staticFunctions;
    private final Map<String, Let> fields;

    public EClass(String name, Map<String, FuncCallable> methods, Map<String, FuncCallable> staticFunctions, Map<String, Let> fields) {
        this.name = name;
        this.methods = methods;
        this.staticFunctions = staticFunctions;
        this.fields = fields;
    }

    @Override
    public Value call(Interpreter inter, List<Value> args) {
        return Value.of(this, new InstanceClass(this));
    }

    public Let findFields(String name) {
        return fields.get(name);
    }

    public FuncCallable findFunc(String name) {
        return staticFunctions.get(name);
    }

    public FuncCallable findMethod(String name) {
        return methods.get(name);
    }

    public Map<String, FuncCallable> getMethods() {
        return methods;
    }

    public Map<String, FuncCallable> getStaticFunctions() {
        return staticFunctions;
    }

    public Map<String, Let> getFields() {
        return fields;
    }

    @Override
    public String name() {
        return name;
    }

}
