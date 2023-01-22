package org.epsilon.expression;

import org.epsilon.Token;

public class LetExpression implements Expression {

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
