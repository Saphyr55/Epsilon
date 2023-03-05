package epsilon.object;

import epsilon.Environment;
import epsilon.core.InterpretRuntimeException;
import epsilon.resolver.Interpreter;
import epsilon.core.ReturnRuntimeException;
import epsilon.statement.FunctionStatement;
import epsilon.syntax.Syntax;
import epsilon.syntax.Token;
import epsilon.type.Type;

import java.util.List;

public class FuncCallable implements Callable {

    private final FunctionStatement declaration;
    private final Environment closure;
    private final Type returnType;
    private final Prototype prototype;

    public FuncCallable(Environment closure, FunctionStatement declaration, Prototype prototype, Type returnType) {
        this.closure = closure;
        this.declaration = declaration;
        this.returnType = returnType;
        this.prototype = prototype;
    }

    public FuncCallable bind(Value value) {
        if (value.getType() instanceof EClass) {
            InstanceClass instance = (InstanceClass) value.get();
            Environment environment = new Environment(closure);
            List<String> ids = declaration.paramsId().stream().map(Token::text).toList();

            environment.define(Syntax.Word.This, value);
            
            instance.getAttributes().forEach((s, let) -> {
                if (!ids.contains(s))
                    environment.define(s, let.getValue());
            });

            return new FuncCallable(environment, declaration, prototype, returnType);
        }
        return this;
    }

    @Override
    public Value call(Interpreter inter, List<Value> args) {
        var environment = new Environment(closure);
        var listTokenArgs = declaration.paramsId();
        var listTokenType = declaration.paramsType();
        for (int i = 0; i < declaration.paramsId().size(); i++) {

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
            inter.executeBlock(declaration.body(), environment);
        } catch (ReturnRuntimeException returnValue) {
            return Value.of(returnValue.getValue().getType(), returnValue.getValue().get());
        }
        return Value.ofVoid();
    }

    @Override
    public Prototype prototype() {
        return prototype;
    }

    @Override
    public String toString() {
        return "<func " + (declaration.name() == null ? "anonymous function" : declaration.name().text()) + ">";
    }

    public Environment getClosure() {
        return closure;
    }

    public Type getReturnType() {
        return returnType;
    }

}
