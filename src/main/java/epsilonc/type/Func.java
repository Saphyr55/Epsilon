package epsilonc.type;

import epsilonc.Environment;
import epsilonc.Interpreter;
import epsilonc.core.ReturnRuntimeException;
import epsilonc.statement.FunctionStatement;

import java.util.List;

public class Func implements FunctionCallable {

    private final FunctionStatement declaration;
    private final Environment closure;

    public Func(Environment closure, FunctionStatement declaration) {
        this.closure = closure;
        this.declaration = declaration;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> args) {
        Environment environment = new Environment(closure);
        for (int i = 0; i < declaration.getParams().size(); i++) {
            environment.define(declaration.getParams().get(i).text(), args.get(i));
        }
        try {
            interpreter.executeBlock(declaration.getBody(), environment);
        } catch (ReturnRuntimeException returnValue) {
            return returnValue.getValue();
        }
        return null;
    }

    @Override
    public int arity() {
        return declaration.getParams().size();
    }

    @Override
    public String toString() {
        return "<fn " + declaration.getName().text() + ">";
    }

    public Environment getClosure() {
        return closure;
    }
}
