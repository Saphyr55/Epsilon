package epsilon.object;

import epsilon.resolver.Interpreter;
import epsilon.type.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class EClass implements Callable, Type {

    private final String name;
    private final Map<Prototype, FuncCallable> methods;
    private final Map<Prototype, FuncCallable> constructors;
    private final Map<String, Let> fields;

    public EClass(String name,
                  Map<Prototype, FuncCallable> constructors,
                  Map<Prototype, FuncCallable> methods,
                  Map<String, Let> fields) {
        this.name = name;
        this.methods = methods;
        this.fields = fields;
        this.constructors = constructors;
    }

    @Override
    public Prototype prototype() {
        return (Prototype) constructors.keySet().toArray()[0];
    }

    @Override
    public Value call(Interpreter inter, List<Value> args) {
        InstanceClass instance = new InstanceClass(this);
        var typesArgs = args.stream().map(Value::getType).toList();

        return Value.of(this, instance);
    }

    public Let findFields(String name) {
        return fields.get(name);
    }

    public Map<Prototype, FuncCallable> findMethods(String name) {
        Map<Prototype, FuncCallable> map = new HashMap<>();
        methods.forEach((prototype, callable) -> {
            if (prototype.sameName(name)) {
                map.put(prototype, callable);
            }
        });
        return map;
    }

    public Map<Prototype, FuncCallable> getMethods() {
        return methods;
    }

    public Map<String, Let> getFields() {
        return fields;
    }

    public Map<Prototype, FuncCallable> getConstructors() {
        return constructors;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "EClass{" + "name='" + name + '\'' + '}';
    }

}
