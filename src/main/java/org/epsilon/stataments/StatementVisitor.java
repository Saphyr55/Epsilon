package org.epsilon.stataments;

public interface StatementVisitor<R> {


    R visitExpressionStatement(ExpressionStatement statement);
    R visitPrintStatement(PrintStatement statement);

}
