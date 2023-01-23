package epsilonc.statement;

import epsilonc.Token;
import epsilonc.expression.Expression;

public class LetStatement implements Statement {

    private final Token name;
    private final Expression initializer;
    private final boolean mutable;

    public LetStatement(Token name, Expression initializer, boolean mutable) {
        this.name = name;
        this.initializer = initializer;
        this.mutable = mutable;
    }

    public boolean isMutable() {
        return mutable;
    }

    public Token getName() {
        return name;
    }

    public Expression getInitializer() {
        return initializer;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitLetStatement(this);
    }

}
