package epsilonc.expression;

import epsilonc.syntax.Token;
import epsilonc.type.Type;

public record AssignExpression(Token name, Expression value) implements Expression {


    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitAssignExpression(this);
    }

}
