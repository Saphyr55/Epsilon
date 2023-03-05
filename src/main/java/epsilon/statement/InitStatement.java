package epsilon.statement;

import epsilon.syntax.Token;
import epsilon.expression.Expression;

public class InitStatement implements Statement {

    private Token name;
    private Expression value;

    public InitStatement(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }

    public Expression getValue() {
        return value;
    }

    public Token getName() {
        return name;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitInitStatement(this);
    }

}
