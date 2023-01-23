package epsilonc.expression;

import epsilonc.Token;

public final class LetExpression implements Expression {

    private Token name;

    public LetExpression(Token name) {
        this.name = name;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitLetExpression(this);
    }

    public Token getName() {
        return name;
    }
}
