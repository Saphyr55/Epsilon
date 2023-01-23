package epsilonc.expression;

import epsilonc.Token;

public final class AssignExpression implements Expression {


    private Token name;
    private Expression value;

    public AssignExpression(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitAssignExpression(this);
    }

    public Token getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }
}
