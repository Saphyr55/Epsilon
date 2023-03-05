package epsilon.object;

import epsilon.resolver.Interpreter;

import java.util.List;

public interface Callable {

    Prototype prototype();

    Value call(Interpreter inter, List<Value> args);

}
