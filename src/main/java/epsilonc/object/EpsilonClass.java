package epsilonc.object;

import epsilonc.resolver.Interpreter;

import java.util.List;
import java.util.Map;

public class EpsilonClass implements Callable {

    private final String name;
    private final Map<String, Func> methods;
    private final Map<String, Func> staticFunctions;
    private final Map<String, Let> fields;

    public EpsilonClass(String name, Map<String, Func> methods, Map<String, Func> staticFunctions, Map<String, Let> fields) {
        this.name = name;
        this.methods = methods;
        this.staticFunctions = staticFunctions;
        this.fields = fields;
    }

    @Override
    public Object call(Interpreter inter, List<Object> args) {
        return new InstanceClass(this);
    }

    public Let findFields(String name) {
        return fields.get(name);
    }

    public Func findFunc(String name) {
        return staticFunctions.get(name);
    }

    public Func findMethod(String name) {
        return methods.get(name);
    }

    public Map<String, Func> getMethods() {
        return methods;
    }

    public Map<String, Func> getStaticFunctions() {
        return staticFunctions;
    }

    public Map<String, Let> getFields() {
        return fields;
    }

}
