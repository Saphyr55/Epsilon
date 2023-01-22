package org.epsilon.stataments;

import org.epsilon.expression.Expression;

public class ExpressionStatement implements Statement {

    private Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitExpressionStatement(this);
    }

    public Expression getExpression() {
        return expression;
    }



}
