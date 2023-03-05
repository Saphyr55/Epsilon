package epsilon.object;

import epsilon.resolver.Interpreter;

import java.util.List;

public class NativeFunc implements Callable {

    private final Prototype prototype;
    private final Callable callable;

    public NativeFunc(Callable callable) {
        this.prototype = callable.prototype();
        this.callable = callable;
    }

    public static Value createValue(Callable callable) {
        return Value.ofFunc(new NativeFunc(callable));
    }

    @Override
    public Value call(Interpreter inter, List<Value> args) {
        return callable.call(inter, args);
    }

    @Override
    public Prototype prototype() {
        return prototype;
    }


}
