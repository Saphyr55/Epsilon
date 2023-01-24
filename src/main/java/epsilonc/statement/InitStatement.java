package epsilonc.statement;

import epsilonc.Token;
import epsilonc.expression.Expression;

public class InitStatement implements Statement {

    private Expression parent;
    private Token name;
    private Expression value;

    public InitStatement(Expression parent, Token name, Expression value) {
        this.parent = parent;
        this.name = name;
        this.value = value;
    }

    public Expression getValue() {
        return value;
    }

    public Token getName() {
        return name;
    }

    public Expression getParent() {
        return parent;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitInitStatement(this);
    }

}
