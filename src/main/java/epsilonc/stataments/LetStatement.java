package epsilonc.stataments;

import epsilonc.Token;
import epsilonc.expression.Expression;

public class LetStatement implements Statement {

    private Token name;
    private Expression initializer;

    public LetStatement(Token name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
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
