package epsilon.statement;

import epsilon.expression.Expression;

@Deprecated
public record ForStatement(Statement statement, Expression midleExpression, Expression leftExpression,
                           Statement block) implements Statement {

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return null;
    }
}
