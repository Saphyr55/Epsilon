package epsilon.expression;

import epsilon.syntax.Token;

import java.util.List;

public final class CallExpression implements Expression {

    private final Expression callee;
    private final Token paren;
    private final List<Expression> arguments;

    public CallExpression(final Expression callee,
                          final Token paren,
                          final List<Expression> arguments) {
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
