package epsilonc.statement;

import epsilonc.Parser;
import epsilonc.expression.Expression;

@Deprecated
public final class ForStatement implements Statement {

    private Statement statement;
    private Expression midleExpression;
    private Expression leftExpression;
    private Statement block;

    public ForStatement(Statement statement, Expression midleExpression, Expression leftExpression, Statement block) {
        this.statement = statement;
        this.midleExpression = midleExpression;
        this.leftExpression = leftExpression;
        this.block = block;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return null;
    }

    public Statement getStatement() {
        return statement;
    }

    public Expression getMidleExpression() {
        return midleExpression;
    }

    public Expression getLeftExpression() {
        return leftExpression;
    }

    public Statement getBlock() {
        return block;
    }
}
