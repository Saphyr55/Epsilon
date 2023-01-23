package epsilonc.core;

import epsilonc.Interpreter;

import java.util.List;

public interface Callable {

    Object call(Interpreter interpreter, List<Object> arguments);

}
