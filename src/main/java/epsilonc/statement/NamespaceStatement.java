package epsilonc.statement;

import epsilonc.syntax.Token;

import java.util.List;

public class NamespaceStatement implements Statement {

    private final Token name;
    private final List<Statement> statements;

    public NamespaceStatement(Token name, List<Statement> statements) {
        this.name = name;
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public Token getName() {
        return name;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return null;
    }
}
