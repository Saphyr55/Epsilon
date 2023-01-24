package epsilonc.expression;

import epsilonc.Token;

import java.util.List;

public class CallExpression implements Expression {

    private Expression callee;
    private Token paren;
    private List<Expression> arguments;

    public CallExpression(Expression callee, Token paren, List<Expression> arguments) {
        this.callee = callee;
        this.paren = paren;
        this.arguments = arguments;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitCallExpression(this);
    }

    public Expression getCallable() {
        return callee;
    }

    public Token getParen() {
        return paren;
    }

    public List<Expression> getArguments() {
        return arguments;
    }
}
