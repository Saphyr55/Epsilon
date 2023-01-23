package epsilonc.type;

import epsilonc.Interpreter;

import java.util.List;

public interface FunctionCallable extends Callable {

    default int arity() { return 0; }

}
