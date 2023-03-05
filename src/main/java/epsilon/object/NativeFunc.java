package epsilon.object;

import epsilon.resolver.Interpreter;

import java.util.List;

public abstract class NativeFunc implements Callable {


    public static Value createValue(NativeFunc func) {
        return Value.ofFunc(func);
    }

    @Override
    public String toString() {
        return "NativeFunc{" +
                "prototype=" + prototype() +
                '}';
    }
}
