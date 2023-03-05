package epsilon.statement;

import epsilon.syntax.Token;

import java.util.List;

public record StructStatement(Token name, List<LetStatement> properties) implements Statement {

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitStructStatement(this);
    }

}
