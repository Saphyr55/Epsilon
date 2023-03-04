package epsilonc.object;

import epsilonc.resolver.Interpreter;
import epsilonc.type.NativeType;

import java.util.List;

public class FuncNative implements Callable {

    private final int arity;
    private final Callable callable;

    public FuncNative(int arity, Callable callable) {
        this.arity = arity;
        this.callable = callable;
    }

    public static Value createValue(int arity, Callable callable) {
        return Value.ofFunc(new FuncNative(arity, callable));
    }

    public static Value createValue(Callable callable) {
        return Value.ofFunc(new FuncNative(0, callable));
    }

    @Override
    public Value call(Interpreter inter, List<Value> args) {
        return callable.call(inter, args);
    }

    @Override
    public int arity() {
        return arity;
    }

    @Override
    public String toString() {
        return "native function";
    }


}
