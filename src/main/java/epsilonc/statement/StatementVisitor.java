package epsilonc.statement;

public interface StatementVisitor<R> {


    R visitExpressionStatement(ExpressionStatement statement);
    R visitPrintStatement(PrintStatement statement);
    R visitLetStatement(LetStatement statement);
    R visitBlockStatement(BlockStatement statement);

}
