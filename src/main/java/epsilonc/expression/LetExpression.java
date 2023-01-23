package epsilonc.expression;

import epsilonc.Token;

public final class LetExpression implements Expression {

    private Token name;
    private boolean isMutable;

    public LetExpression(Token name, boolean isMutable) {
        this.name = name;
        this.isMutable = isMutable;
    }

    public LetExpression(Token name) {
        this(name, false);
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitLetExpression(this);
    }

    public Token getName() {
        return name;
    }
}
