package epsilonc.resolver;

import epsilonc.expression.Expression;
import epsilonc.expression.ExpressionVisitor;
import epsilonc.statement.Statement;
import epsilonc.statement.StatementVisitor;

import java.util.List;

public interface Resolver<R,U> extends ExpressionVisitor<R>, StatementVisitor<U> {

    default void resolve(Expression expression) {
        expression.accept(this);
    }

    default void resolve(Statement statement) {
        statement.accept(this);
    }

    default void resolve(List<? extends Statement> statements) {
        statements.forEach(this::resolve);
    }


}
