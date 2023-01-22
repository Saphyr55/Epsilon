package org.epsilon.expression;

public final class LiteralExpression implements Expression {

    private Object value;

    public LiteralExpression(Object value) {
        this.value = value;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitLiteralExpr(this);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Literal{" +
                "value=" + value +
                '}';
    }
}
