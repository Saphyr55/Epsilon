package epsilonc.type;

import epsilonc.Interpreter;

import java.util.List;

public class FuncNative implements FunctionCallable {

    private final int arity;
    private final Callable callable;

    public FuncNative(int arity, Callable callable) {
        this.arity = arity;
        this.callable = callable;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        return callable.call(interpreter, arguments);
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
