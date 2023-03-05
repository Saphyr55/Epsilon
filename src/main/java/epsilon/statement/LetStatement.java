package epsilon.statement;

import epsilon.syntax.Token;
import epsilon.expression.Expression;

public record LetStatement(Token name, Token type, Expression initializer, boolean mutable) implements Statement {

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitLetStatement(this);
    }

}
