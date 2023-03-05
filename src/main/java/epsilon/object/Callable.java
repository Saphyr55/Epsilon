package epsilon.object;

import epsilon.resolver.Interpreter;

import java.util.List;

public interface Callable {

    Value call(Interpreter inter, List<Value> args);

    default int arity() { return 0; }

}
