package epsilon.expression;

import epsilon.syntax.Token;

public record AssignExpression(Token name, Expression value) implements Expression {


    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitAssignExpression(this);
    }

}
