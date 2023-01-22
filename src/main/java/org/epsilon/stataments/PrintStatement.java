package org.epsilon.stataments;

import org.epsilon.expression.Expression;

public class PrintStatement implements Statement {

    private Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <R> void accept(StatementVisitor<R> visitor) {
        visitor.visitPrintStatement(this);
    }

    public Expression getExpression() {
        return expression;
    }
}
