package epsilonc.statement;

import epsilonc.Token;
import epsilonc.core.FuncCallable;

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

    public FuncCallable createCallable() {
        return new FuncCallable(this);
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
