package epsilonc.expression;

import epsilonc.Token;

public class GetterExpression implements Expression {

    private final Expression object;
    private final Token name;

    public GetterExpression(Expression object, Token name) {
        this.object = object;
        this.name = name;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitGetterExpression(this);
    }

    public Token getName() {
        return name;
    }

    public Expression getObjet() {
        return object;
    }

}
