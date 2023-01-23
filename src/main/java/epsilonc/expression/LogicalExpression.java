package epsilonc.expression;

import epsilonc.Token;

public class LogicalExpression implements Expression {

    private Expression left;
    private Token op;
    private Expression right;

    public LogicalExpression(Expression left, Token op, Expression right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitLogicalExpression(this);
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public Token getOp() {
        return op;
    }
}
