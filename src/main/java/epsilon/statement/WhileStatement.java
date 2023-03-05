package epsilon.statement;

import epsilon.expression.Expression;

public record WhileStatement(Expression condition, Statement body) implements Statement {

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitWhileStatement(this);
    }
}
