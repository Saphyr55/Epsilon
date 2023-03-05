package epsilon.statement;

import epsilon.syntax.Token;
import epsilon.expression.Expression;

public record InitStatement(Token name, Expression value) implements Statement {

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitInitStatement(this);
    }

}
