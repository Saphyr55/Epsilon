package epsilonc.statement;

import epsilonc.expression.Expression;

public class IfStatement implements Statement {

    private Expression condition;
    private Statement thenBranch;
    private Statement elseBranch;


    public IfStatement(Expression condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitIfStatement(this);
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getThenBranch() {
        return thenBranch;
    }

    public Statement getElseBranch() {
        return elseBranch;
    }
}
