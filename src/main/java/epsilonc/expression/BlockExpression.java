package epsilonc.expression;

import epsilonc.statement.Statement;
import epsilonc.statement.StatementVisitor;

import java.util.List;

public class BlockExpression implements Expression{

    private List<Statement> statements;

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
}
