package epsilon.object;

import epsilon.Environment;
import epsilon.core.InterpretRuntimeException;
import epsilon.resolver.Interpreter;
import epsilon.core.ReturnRuntimeException;
import epsilon.statement.FunctionStatement;
import epsilon.type.Type;

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
    public Value call(Interpreter inter, List<Value> args) {
        var environment = new Environment(closure);
        var listTokenArgs = declaration.getParamsId();
        var listTokenType = declaration.getParamsType();
        for (int i = 0; i < declaration.getParamsId().size(); i++) {

            var tokenType = listTokenType.get(i);
            var tokenArg = listTokenArgs.get(i);
            var currentArg = args.get(i);
            if (!currentArg.getType().isInstance(closure.getType(tokenType)))
                throw new InterpretRuntimeException(tokenArg, "Wrong type arguments for '" +
                        tokenArg.text() + "', the expected type is '"+ tokenType.text() +
                        "'. We actually got a '"+ currentArg.getType().name()+"'");
            environment.define(listTokenArgs.get(i).text(), currentArg);
        }
        try {
            inter.executeBlock(declaration.getBody(), environment);
        } catch (ReturnRuntimeException returnValue) {
            return Value.of(returnValue.getValue().getType(), returnValue.getValue().get());
        }
        return Value.ofVoid();
    }

    @Override
    public int arity() {
        return declaration.getParamsId().size();
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
