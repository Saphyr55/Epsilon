package epsilonc.core;

import epsilonc.FunctionCallable;
import epsilonc.Environment;
import epsilonc.Interpreter;
import epsilonc.statement.FunctionStatement;

import java.util.List;

public class FuncCallable implements FunctionCallable {

    private final FunctionStatement declaration;

    public FuncCallable(FunctionStatement declaration) {
        this.declaration = declaration;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> args) {
        Environment environment = new Environment(interpreter.getGlobals());
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

}
