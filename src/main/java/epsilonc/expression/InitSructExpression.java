package epsilonc.expression;

import epsilonc.syntax.Token;
import epsilonc.statement.Statement;

import java.util.List;

public class InitSructExpression implements Expression {

    private final List<Statement> statements;
    private Token type;

    public InitSructExpression(Token type, List<Statement> statements) {
        this.statements = statements;
        this.type = type;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitInitStructExpression(this);
    }

    public Token getType() {
        return type;
    }

    public void setType(Token type) {
        this.type = type;
    }
}
