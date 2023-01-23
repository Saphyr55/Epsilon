package epsilonc.expression;

import epsilonc.Token;
import epsilonc.statement.FunctionStatement;
import epsilonc.statement.Statement;

import java.util.List;

public class AnonymousFuncExpression implements Expression {

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
