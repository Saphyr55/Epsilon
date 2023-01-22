package org.epsilon.expression;

public final class GroupingExpression implements Expression {

    public Expression expression;

    public GroupingExpression(Expression expression) {
        this.expression = expression;
    }


    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "Grouping{" +
                "expr=" + expression +
                '}';
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitGroupingExpr(this);
    }

}
