package org.epsilon.stataments;

import org.epsilon.expression.ExpressionVisitor;

public interface Statement {

    <R> void accept(StatementVisitor<R> expressionVisitor);

}
