package epsilonc.statement;

import java.util.List;

public class BlockStatement implements Statement {

    private List<Statement> statements;

    public BlockStatement(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitBlockStatement(this);
    }

    public List<Statement> getStatements() {
        return statements;
    }
}