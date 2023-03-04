package epsilonc.statement;

import epsilonc.Environment;
import epsilonc.syntax.Token;
import epsilonc.object.FuncCallable;
import epsilonc.type.Type;

import java.util.List;
import java.util.Map;

public class FunctionStatement implements Statement {

    private Token name;
    private Map<Token, Token> params;
    private List<Statement> body;
    private Token returnType;

    public FunctionStatement(Token name, Map<Token, Token> params, List<Statement> body, Token returnType) {
        this.name = name;
        this.params = params;
        this.body = body;
        this.returnType = returnType;
    }

    public FuncCallable createCallable(Environment environment, Type returnTypeDeclaration) {
        return new FuncCallable(environment, this, returnTypeDeclaration);
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitFunctionStatement(this);
    }

    public Token getName() {
        return name;
    }

    public Map<Token, Token> getParams() {
        return params;
    }

    public List<Statement> getBody() {
        return body;
    }

    public Token getReturnType() {
        return returnType;
    }
}
