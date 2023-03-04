package epsilonc.object;

import epsilonc.Environment;
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
        Environment environment = new Environment(closure);
        List<Token> listTokenArgs = declaration.getParams().keySet().stream().toList();
        List<Token> listTokenType = declaration.getParams().values().stream().toList();
        for (int i = 0; i < declaration.getParams().size(); i++) {
            environment.define(listTokenArgs.get(i).text(), Value.of(
                            closure.getValue(listTokenType.get(i)).getType(),
                            closure.getValue(listTokenType.get(i)).get()));
        }
        try {
            inter.executeBlock(declaration.getBody(), environment);
        } catch (ReturnRuntimeException returnValue) {
            return Value.of(returnType, returnValue.getValue());
        }
        return null;
    }

    @Override
    public int arity() {
        return declaration.getParams().size();
    }

    @Override
    public String toString() {
        return "<func " + declaration.getName() + ">";
    }

    public Environment getClosure() {
        return closure;
    }

    public Type getReturnType() {
        return returnType;
    }

}
