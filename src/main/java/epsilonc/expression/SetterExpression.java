package epsilonc.expression;

import epsilonc.syntax.Token;

public class SetterExpression implements Expression {

    private Expression object;
    private Token name;
    private Expression value;

    public SetterExpression(Expression object, Token name, Expression value) {
        this.object = object;
        this.name = name;
        this.value = value;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitSetterExpression(this);
    }

    public Token getName() {
        return name;
    }

    public Expression getObject() {
        return object;
    }

    public Expression getValue() {
        return value;
    }
}
