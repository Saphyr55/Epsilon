package epsilonc.statement;

import epsilonc.syntax.Token;
import epsilonc.expression.Expression;

public class ReturnStatement implements Statement {


    private final Token keyword;
    private final Expression value;

    public ReturnStatement(Token keyword, Expression value) {
        this.value = value;
        this.keyword = keyword;
    }

    public Expression getValue() {
        return value;
    }

    public Token getKeyword() {
        return keyword;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitReturnStatement(this);
    }


}
