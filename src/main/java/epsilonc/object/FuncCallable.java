package epsilonc.object;

import epsilonc.Environment;
import epsilonc.resolver.Interpreter;
import epsilonc.core.ReturnRuntimeException;
import epsilonc.statement.FunctionStatement;
import epsilonc.syntax.Token;
import epsilonc.type.Type;

import java.util.List;

public class FuncCallable implements Callable {

    private final FunctionStatement declaration;
    private final Environment closure;
    private final Type returnType;

    public FuncCallable(Environment closure, FunctionStatement declaration, Type returnType) {
        this.closure = closure;
        this.declaration = declaration;
        this.returnType = returnType;
    }

    @Override
    public Object call(Interpreter inter, List<Object> args) {
        Environment environment = new Environment(closure);
        List<Token> listTokenArgs = declaration.getParams().keySet().stream().toList();
        List<Token> listTokenType = declaration.getParams().values().stream().toList();
        for (int i = 0; i < declaration.getParams().size(); i++) {
            environment.define(listTokenArgs.get(i).text(), listTokenType.get(i).text(), args.get(i));
        }
        try {
            inter.executeBlock(declaration.getBody(), environment);
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

    public Type getReturnType() {
        return returnType;
    }

}
