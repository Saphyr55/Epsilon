package epsilonc.expression;

import epsilonc.Token;
import epsilonc.statement.Statement;

import java.util.List;

public class BlockExpression implements Expression{

    private final List<Statement> statements;
    private Token type;

    public BlockExpression(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitBlockExpression(this);
    }

    public Token getType() {
        return type;
    }

    public void setType(Token type) {
        this.type = type;
    }
}
