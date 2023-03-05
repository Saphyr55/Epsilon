package epsilon.expression;

import epsilon.object.Value;

public final class LiteralExpression implements Expression {

    private Value value;

    public LiteralExpression(Value value) {
        this.value = value;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitLiteralExpression(this);
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Literal{" +
                "value=" + value +
                '}';
    }

}
