package epsilonc.statement;

import epsilonc.expression.Expression;

public class PrintStatement implements Statement {

    private Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitPrintStatement(this);
    }

    public Expression getExpression() {
        return expression;
    }
}
