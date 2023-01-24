package epsilonc.statement;

import epsilonc.Environment;
import epsilonc.Token;
import epsilonc.object.Func;

import java.util.List;

public class FunctionStatement implements Statement {

    private Token name;
    private List<Token> params;
    private List<Statement> body;

    public FunctionStatement(Token name, List<Token> params, List<Statement> body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

    public Func createCallable(Environment environment) {
        return new Func(environment, this);
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitFunctionStatement(this);
    }

    public Token getName() {
        return name;
    }

    public List<Token> getParams() {
        return params;
    }

    public List<Statement> getBody() {
        return body;
    }
}
