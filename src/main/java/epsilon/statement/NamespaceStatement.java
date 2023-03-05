package epsilon.statement;

import epsilon.syntax.Token;

import java.util.List;

public record NamespaceStatement(Token name, List<Statement> statements) implements Statement {

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return null;
    }
}
