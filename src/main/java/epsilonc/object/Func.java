package epsilonc.object;

import epsilonc.Environment;
import epsilonc.resolver.Interpreter;
import epsilonc.core.ReturnRuntimeException;
import epsilonc.statement.FunctionStatement;

import java.util.List;

public class Func implements Callable {

    private final FunctionStatement declaration;
    private final Environment closure;
    private final TypeDeclaration returnTypeDeclaration;

    public Func(Environment closure, FunctionStatement declaration, TypeDeclaration returnTypeDeclaration) {
        this.closure = closure;
        this.declaration = declaration;
        this.returnTypeDeclaration = returnTypeDeclaration;
    }

    @Override
    public Object call(Interpreter inter, List<Object> args) {
        Environment environment = new Environment(closure);
        for (int i = 0; i < declaration.getParams().size(); i++) {
            environment.define(
                    declaration.getParams().get(i).text(),
                    null,
                    args.get(i)
            );
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

    public TypeDeclaration getReturnType() {
        return returnTypeDeclaration;
    }

}
