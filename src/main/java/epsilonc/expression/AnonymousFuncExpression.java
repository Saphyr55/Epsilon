package epsilonc.expression;

import epsilonc.statement.FunctionStatement;
import epsilonc.type.Type;

public final class AnonymousFuncExpression implements Expression {

    private final FunctionStatement statement;

    public AnonymousFuncExpression(FunctionStatement statement) {
        this.statement = statement;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitAnonymousFuncExpression(this);
    }

    public FunctionStatement getStatement() {
        return statement;
    }

}
