package epsilon.statement;

import epsilon.expression.Expression;

public record ExpressionStatement(Expression expression) implements Statement {

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitExpressionStatement(this);
    }

}
