package epsilonc.object;

import epsilonc.resolver.Interpreter;
import epsilonc.type.Type;

import java.util.List;
import java.util.Map;

public interface Callable {

    Value call(Interpreter inter, List<Value> args);

    default int arity() { return 0; }

}
