package org.epsilon.stataments;

public interface Statement {

    <R> void accept(StatementVisitor<R> expressionVisitor);

}
