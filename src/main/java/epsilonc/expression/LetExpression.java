package epsilonc.expression;

import epsilonc.syntax.Kind;
import epsilonc.syntax.Token;

public final class LetExpression implements Expression {

    private final Token name;

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
