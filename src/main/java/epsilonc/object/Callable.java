package epsilonc.object;

import epsilonc.resolver.Interpreter;

import java.util.List;

public interface Callable {

    Object call(Interpreter inter, List<Object> args);

    default int arity() { return 0; }

}
