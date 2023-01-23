package epsilonc.expression;

import epsilonc.Token;

public final class BinaryExpression implements Expression {

    private Expression left, right;
    private Token op;

    public BinaryExpression(Expression left, Token op, Expression right) {
        this.left = left;
        this.right = right;
        this.op = op;
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

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitBinaryExpression(this);
    }

    @Override
    public String toString() {
        return "Binary{" +
                "left=" + left +
                ", right=" + right +
                ", op=" + op +
                '}';
    }
}
