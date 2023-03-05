package epsilon.statement;

import epsilon.syntax.Token;
import epsilon.expression.Expression;

public class ReturnStatement implements Statement {


    private final Token keyword;
    private final Expression value;
    private FunctionStatement functionStatement;

    public ReturnStatement(Token keyword, Expression value) {
        this.value = value;
        this.keyword = keyword;
    }

    public Expression getValue() {
        return value;
    }

    public FunctionStatement getFunctionStatement() {
        return functionStatement;
    }

    public void setFunctionStatement(FunctionStatement functionStatement) {
        this.functionStatement = functionStatement;
    }

    public Token getKeyword() {
        return keyword;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitReturnStatement(this);
    }


}
