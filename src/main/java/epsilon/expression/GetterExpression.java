package epsilon.expression;

import epsilon.syntax.Token;

public class GetterExpression implements Expression {

    private final Expression value;
    private final Token name;

    public GetterExpression(Expression value, Token name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitGetterExpression(this);
    }

    public Token getName() {
        return name;
    }

    public Expression getObject() {
        return value;
    }

}
