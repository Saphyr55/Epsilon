package epsilonc.statement;

import epsilonc.syntax.Token;

import java.util.List;

public class StructStatement implements Statement {

    private final Token name;
    private final List<LetStatement> properties;

    public StructStatement(Token name, List<LetStatement> properties) {
        this.name = name;
        this.properties = properties;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitTypeStatement(this);
    }

    public Token getName() {
        return name;
    }

    public List<LetStatement> getProperties() {
        return properties;
    }

}
