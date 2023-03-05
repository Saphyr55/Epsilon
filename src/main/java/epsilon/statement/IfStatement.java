package epsilon.statement;

import epsilon.expression.Expression;

public record IfStatement(Expression condition, Statement thenBranch, Statement elseBranch) implements Statement {

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitIfStatement(this);
    }
}
