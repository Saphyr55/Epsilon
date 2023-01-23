package epsilonc.expression;

import epsilonc.Token;

public final class UnaryExpression implements Expression {

    private Token op;
    private Expression right;

    public UnaryExpression(Token op, Expression right) {
        this.right = right;
        this.op = op;
    }

    public Expression getRight() {
        return right;
    }

    public Token getOp() {
        return op;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitUnaryExpr(this);
    }

    @Override
    public String toString() {
        return "Unary{" +
                "op=" + op +
                ", right=" + right +
                '}';
    }
}
