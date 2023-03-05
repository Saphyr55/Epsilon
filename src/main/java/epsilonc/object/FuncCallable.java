package epsilonc.object;

import epsilonc.Environment;
import epsilonc.core.InterpretRuntimeException;
import epsilonc.resolver.Interpreter;
import epsilonc.core.ReturnRuntimeException;
import epsilonc.statement.FunctionStatement;
import epsilonc.syntax.Token;
import epsilonc.type.Type;

import java.util.List;
import java.util.Map;

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
    public Value call(Interpreter inter, List<Value> args) {
        var environment = new Environment(closure);
        var listTokenArgs = declaration.getParams().keySet().stream().toList();
        var listTokenType = declaration.getParams().values().stream().toList();
        for (int i = 0; i < declaration.getParams().size(); i++) {

            var tokenType = listTokenType.get(i);
            var tokenArg = listTokenArgs.get(i);
            var currentArg = args.get(declaration.getParams().size() - 1 - i);

            if (!currentArg.getType().getClass().isInstance(closure.getType(tokenType)))
                throw new InterpretRuntimeException(tokenArg, "Wrong type arguments for '" +
                        tokenArg.text() + "', the expected type is '"+ tokenType.text() +
                        "'. We actually got a '"+ currentArg.getType().name()+"'");

            environment.define(listTokenArgs.get(i).text(), currentArg);
        }
        try {
            inter.executeBlock(declaration.getBody(), environment);
        } catch (ReturnRuntimeException returnValue) {
            return Value.of(returnType, returnValue.getValue().get());
        }
        return Value.of(returnType);
    }

    @Override
    public int arity() {
        return declaration.getParams().size();
    }

    @Override
    public String toString() {
        return "<func " + (declaration.getName() == null ? "anonymous function" : declaration.getName().text()) + ">";
    }

    public Environment getClosure() {
        return closure;
    }

    public Type getReturnType() {
        return returnType;
    }

}
